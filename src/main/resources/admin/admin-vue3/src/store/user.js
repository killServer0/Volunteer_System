import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, logout, getUserInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(getToken() || '')
  const userInfo = ref({})
  const roles = ref([])
  const permissions = ref([])

  // 计算属性
  const isLogin = computed(() => !!token.value)
  const username = computed(() => userInfo.value.username || '')
  const avatar = computed(() => userInfo.value.avatar || '')

  // 方法
  const loginAction = async (loginForm) => {
    try {
      const { data } = await login(loginForm)
      token.value = data.token
      setToken(data.token)
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  const getUserInfoAction = async () => {
    try {
      const { data } = await getUserInfo()
      userInfo.value = data
      roles.value = data.roles || []
      permissions.value = data.permissions || []
      return Promise.resolve(data)
    } catch (error) {
      return Promise.reject(error)
    }
  }

  const logoutAction = async () => {
    try {
      await logout()
      resetState()
      return Promise.resolve()
    } catch (error) {
      return Promise.reject(error)
    }
  }

  const resetState = () => {
    token.value = ''
    userInfo.value = {}
    roles.value = []
    permissions.value = []
    removeToken()
  }

  return {
    token,
    userInfo,
    roles,
    permissions,
    isLogin,
    username,
    avatar,
    loginAction,
    getUserInfoAction,
    logoutAction,
    resetState
  }
}) 