import { ref } from 'vue'
import { useRouter } from 'vue-router'

/**
 * 菜单工具类
 */
export default {
  /**
   * 生成菜单数据
   * @param {Array} menuList 菜单列表
   * @returns {Array} 处理后的菜单数据
   */
  generateMenu(menuList) {
    return menuList.map(item => {
      const menu = {
        path: item.path,
        name: item.name,
        meta: {
          title: item.title,
          icon: item.icon,
          roles: item.roles || []
        }
      }
      if (item.children && item.children.length > 0) {
        menu.children = this.generateMenu(item.children)
      }
      return menu
    })
  },

  /**
   * 根据权限过滤菜单
   * @param {Array} menuList 菜单列表
   * @param {Array} roles 用户角色列表
   * @returns {Array} 过滤后的菜单数据
   */
  filterMenuByRoles(menuList, roles) {
    return menuList.filter(menu => {
      if (menu.meta && menu.meta.roles) {
        return roles.some(role => menu.meta.roles.includes(role))
      }
      return true
    }).map(menu => {
      if (menu.children && menu.children.length > 0) {
        menu.children = this.filterMenuByRoles(menu.children, roles)
      }
      return menu
    })
  },

  /**
   * 获取面包屑数据
   * @param {string} currentPath 当前路径
   * @param {Array} menuList 菜单列表
   * @returns {Array} 面包屑数据
   */
  getBreadcrumb(currentPath, menuList) {
    const breadcrumb = []
    const findPath = (path, menus) => {
      for (const menu of menus) {
        if (menu.path === path) {
          breadcrumb.push({
            path: menu.path,
            title: menu.meta.title
          })
          return true
        }
        if (menu.children && menu.children.length > 0) {
          if (findPath(path, menu.children)) {
            breadcrumb.unshift({
              path: menu.path,
              title: menu.meta.title
            })
            return true
          }
        }
      }
      return false
    }
    findPath(currentPath, menuList)
    return breadcrumb
  },

  /**
   * 获取默认展开的菜单
   * @param {string} currentPath 当前路径
   * @param {Array} menuList 菜单列表
   * @returns {Array} 默认展开的菜单路径
   */
  getDefaultOpenMenu(currentPath, menuList) {
    const openMenu = []
    const findOpenMenu = (path, menus) => {
      for (const menu of menus) {
        if (menu.path === path) {
          return true
        }
        if (menu.children && menu.children.length > 0) {
          if (findOpenMenu(path, menu.children)) {
            openMenu.push(menu.path)
            return true
          }
        }
      }
      return false
    }
    findOpenMenu(currentPath, menuList)
    return openMenu
  }
} 