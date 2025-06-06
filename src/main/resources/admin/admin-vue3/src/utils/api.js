import request from './http'

// 用户相关接口
export const userApi = {
  login: (data) => request.post('/user/login', data),
  logout: () => request.post('/user/logout'),
  info: () => request.get('/user/info'),
  list: (params) => request.get('/user/list', { params }),
  add: (data) => request.post('/user/add', data),
  update: (data) => request.post('/user/update', data),
  delete: (id) => request.post('/user/delete', { id })
}

// 角色相关接口
export const roleApi = {
  list: (params) => request.get('/role/list', { params }),
  add: (data) => request.post('/role/add', data),
  update: (data) => request.post('/role/update', data),
  delete: (id) => request.post('/role/delete', { id }),
  permissions: (roleId) => request.get('/role/permissions', { params: { roleId } }),
  assignPermissions: (data) => request.post('/role/assignPermissions', data)
}

// 菜单相关接口
export const menuApi = {
  list: (params) => request.get('/menu/list', { params }),
  add: (data) => request.post('/menu/add', data),
  update: (data) => request.post('/menu/update', data),
  delete: (id) => request.post('/menu/delete', { id })
}

// 活动相关接口
export const activityApi = {
  list: (params) => request.get('/activity/list', { params }),
  add: (data) => request.post('/activity/add', data),
  update: (data) => request.post('/activity/update', data),
  delete: (id) => request.post('/activity/delete', { id }),
  detail: (id) => request.get('/activity/detail', { params: { id } })
}

// 活动预约相关接口
export const activityReservationApi = {
  list: (params) => request.get('/activityReservation/list', { params }),
  add: (data) => request.post('/activityReservation/add', data),
  update: (data) => request.post('/activityReservation/update', data),
  delete: (id) => request.post('/activityReservation/delete', { id }),
  audit: (data) => request.post('/activityReservation/audit', data)
}

// 文件上传相关接口
export const fileApi = {
  upload: (data) => request.post('/file/upload', data),
  download: (id) => request.get('/file/download', { params: { id } })
}

// 数据字典相关接口
export const dictionaryApi = {
  list: (params) => request.get('/dictionary/list', { params }),
  add: (data) => request.post('/dictionary/add', data),
  update: (data) => request.post('/dictionary/update', data),
  delete: (id) => request.post('/dictionary/delete', { id })
} 