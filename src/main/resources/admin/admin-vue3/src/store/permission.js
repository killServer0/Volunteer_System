import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getMenuList } from '@/api/menu'
import { useUserStore } from './user'
import { asyncRoutes, constantRoutes } from '@/router'

/**
 * 使用meta.role判断当前用户是否有权限
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * 通过递归过滤异步路由表
 * @param routes asyncRoutes
 * @param roles
 */
function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

export const usePermissionStore = defineStore('permission', () => {
  // 状态
  const routes = ref([])
  const addRoutes = ref([])
  const defaultRoutes = ref([])
  const topbarRoutes = ref([])
  const sidebarRoutes = ref([])

  // 计算属性
  const permissionRoutes = computed(() => routes.value)

  // 方法
  const setRoutes = (newRoutes) => {
    addRoutes.value = newRoutes
    routes.value = constantRoutes.concat(newRoutes)
  }

  const generateRoutes = async () => {
    const userStore = useUserStore()
    const { roles } = userStore

    let accessedRoutes
    if (roles.includes('admin')) {
      accessedRoutes = asyncRoutes || []
    } else {
      accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    }

    setRoutes(accessedRoutes)
    return accessedRoutes
  }

  const resetRoutes = () => {
    routes.value = []
    addRoutes.value = []
    defaultRoutes.value = []
    topbarRoutes.value = []
    sidebarRoutes.value = []
  }

  return {
    routes,
    addRoutes,
    defaultRoutes,
    topbarRoutes,
    sidebarRoutes,
    permissionRoutes,
    setRoutes,
    generateRoutes,
    resetRoutes
  }
}) 