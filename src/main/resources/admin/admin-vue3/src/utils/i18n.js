import { createI18n } from 'vue-i18n'

// 中文语言包
const zh = {
  common: {
    add: '新增',
    edit: '编辑',
    delete: '删除',
    search: '搜索',
    reset: '重置',
    submit: '提交',
    cancel: '取消',
    confirm: '确认',
    back: '返回',
    export: '导出',
    import: '导入',
    upload: '上传',
    download: '下载',
    more: '更多',
    loading: '加载中...',
    success: '操作成功',
    error: '操作失败',
    warning: '警告',
    info: '提示'
  },
  auth: {
    login: '登录',
    logout: '退出登录',
    username: '用户名',
    password: '密码',
    captcha: '验证码',
    remember: '记住密码',
    forgot: '忘记密码'
  },
  menu: {
    dashboard: '首页',
    system: '系统管理',
    user: '用户管理',
    role: '角色管理',
    menu: '菜单管理',
    activity: '活动管理',
    reservation: '预约管理',
    dictionary: '字典管理'
  }
}

// 英文语言包
const en = {
  common: {
    add: 'Add',
    edit: 'Edit',
    delete: 'Delete',
    search: 'Search',
    reset: 'Reset',
    submit: 'Submit',
    cancel: 'Cancel',
    confirm: 'Confirm',
    back: 'Back',
    export: 'Export',
    import: 'Import',
    upload: 'Upload',
    download: 'Download',
    more: 'More',
    loading: 'Loading...',
    success: 'Operation successful',
    error: 'Operation failed',
    warning: 'Warning',
    info: 'Info'
  },
  auth: {
    login: 'Login',
    logout: 'Logout',
    username: 'Username',
    password: 'Password',
    captcha: 'Captcha',
    remember: 'Remember me',
    forgot: 'Forgot password'
  },
  menu: {
    dashboard: 'Dashboard',
    system: 'System',
    user: 'User',
    role: 'Role',
    menu: 'Menu',
    activity: 'Activity',
    reservation: 'Reservation',
    dictionary: 'Dictionary'
  }
}

// 创建i18n实例
const i18n = createI18n({
  legacy: false,
  locale: 'zh',
  fallbackLocale: 'en',
  messages: {
    zh,
    en
  }
})

export default i18n 