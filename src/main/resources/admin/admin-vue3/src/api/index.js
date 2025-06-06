// API接口配置
export const api = {
  // 用户相关
  user: {
    login: '/user/login',
    register: '/user/register',
    info: '/user/info',
    updatePassword: '/user/updatePassword'
  },
  // 活动相关
  activity: {
    list: '/activity/list',
    add: '/activity/add',
    update: '/activity/update',
    delete: '/activity/delete',
    detail: '/activity/detail'
  },
  // 志愿者相关
  volunteer: {
    list: '/volunteer/list',
    add: '/volunteer/add',
    update: '/volunteer/update',
    delete: '/volunteer/delete',
    detail: '/volunteer/detail'
  },
  // 公告相关
  notice: {
    list: '/notice/list',
    add: '/notice/add',
    update: '/notice/update',
    delete: '/notice/delete',
    detail: '/notice/detail'
  },
  // 论坛相关
  forum: {
    list: '/forum/list',
    add: '/forum/add',
    update: '/forum/update',
    delete: '/forum/delete',
    detail: '/forum/detail'
  },
  // 轮播图配置
  config: {
    list: '/config/list',
    update: '/config/update'
  }
}

export default api

// 导出所有API接口
export * from './user'
export * from './menu'
export * from './dict'
export * from './role'
export * from './permission'
export * from './huodong'
export * from './gonggao'
export * from './zhiyuanzhe'
export * from './news' 