import request from '@/utils/request'

// 获取权限列表
export function getPermissionList(params) {
  return request({
    url: '/permission/list',
    method: 'get',
    params
  })
}

// 添加权限
export function addPermission(data) {
  return request({
    url: '/permission/add',
    method: 'post',
    data
  })
}

// 更新权限
export function updatePermission(data) {
  return request({
    url: '/permission/update',
    method: 'post',
    data
  })
}

// 删除权限
export function deletePermission(id) {
  return request({
    url: `/permission/delete/${id}`,
    method: 'delete'
  })
}

// 获取权限树形结构
export function getPermissionTree() {
  return request({
    url: '/permission/tree',
    method: 'get'
  })
}

// 获取用户权限
export function getUserPermissions() {
  return request({
    url: '/permission/user',
    method: 'get'
  })
} 