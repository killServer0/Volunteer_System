import request from '@/utils/request'

// 获取菜单列表
export function getMenuList(params) {
  return request({
    url: '/menu/list',
    method: 'get',
    params
  })
}

// 添加菜单
export function addMenu(data) {
  return request({
    url: '/menu/save',
    method: 'post',
    data
  })
}

// 更新菜单
export function updateMenu(data) {
  return request({
    url: '/menu/update',
    method: 'post',
    data
  })
}

// 删除菜单
export function deleteMenu(ids) {
  return request({
    url: '/menu/delete',
    method: 'post',
    data: ids
  })
}

// 获取菜单树形结构
export function getMenuTree() {
  return request({
    url: '/menu/tree',
    method: 'get'
  })
}

// 获取用户菜单权限
export function getUserMenus() {
  return request({
    url: '/menu/user',
    method: 'get'
  })
} 