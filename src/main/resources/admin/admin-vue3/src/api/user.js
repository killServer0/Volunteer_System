import request from '@/utils/request'

// 用户登录
export function login(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getInfo(token) {
  return request({
    url: '/admin/info',
    method: 'get',
    params: { token }
  })
}

// 导出getUserInfo作为getInfo的别名
export const getUserInfo = getInfo

// 用户登出
export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/admin/user/add',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/admin/user/update',
    method: 'post',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/admin/user/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除用户
export function batchDeleteUser(ids) {
  return request({
    url: '/user/batchDelete',
    method: 'post',
    data: { ids }
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/admin/user/changePassword',
    method: 'post',
    data
  })
} 