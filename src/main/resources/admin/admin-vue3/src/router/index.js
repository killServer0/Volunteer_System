import { createRouter, createWebHistory } from 'vue-router'
import { getToken, isTokenValid } from '@/utils/auth'
import Layout from '../layout/index.vue'
import http from '@/utils/http'
import { ElMessage } from 'element-plus'

// 预加载组件
const ZhihuodongAddOrUpdate = () => import('@/views/modules/zhihuodong/add-or-update.vue')
const ZhihuodongInfo = () => import('@/views/modules/zhihuodong/info.vue')
const FeihuodongAddOrUpdate = () => import('@/views/modules/feihuodong/add-or-update.vue')
const FeihuodongInfo = () => import('@/views/modules/feihuodong/info.vue')
const ZhihuodongYuyueAddOrUpdate = () => import('@/views/modules/zhihuodongYuyue/add-or-update.vue')
const ZhihuodongYuyueInfo = () => import('@/views/modules/zhihuodongYuyue/info.vue')
const ZhihuodongYuyueList = () => import('@/views/modules/zhihuodongYuyue/list.vue')
const GonggaoList = () => import('@/views/modules/gonggao/list.vue')
const ConfigList = () => import('@/views/modules/config/list.vue')
const ConfigAddOrUpdate = () => import('@/views/modules/config/add-or-update.vue')
const DictionaryList = () => import('@/views/modules/dictionary/list.vue')
const DictionaryAddOrUpdate = () => import('@/views/modules/dictionary/add-or-update.vue')
const HuodongShenhe = () => import('@/views/modules/huodong/shenhe.vue')

// 常量路由
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/notFound',
    name: 'notFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '404' }
  },
  {
    path: '/:pathMatch(.*)',
    redirect: '/notFound',
  }
]

// 异步加载路由
export const asyncRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: '首页', icon: 'home' }
      },
      {
        path: 'center',
        name: 'Center',
        component: () => import('@/views/Center.vue'),
        meta: { title: '个人中心', icon: 'user' }
      },
      {
        path: 'updatePassword',
        name: 'UpdatePassword',
        component: () => import('@/views/UpdatePassword.vue'),
        meta: { title: '修改密码', icon: 'lock' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/modules/users/list.vue'),
        meta: { title: '用户管理', icon: 'user' }
      },
      {
        path: 'feihuodong',
        name: 'Feihuodong',
        component: () => import('@/views/modules/feihuodong/list.vue'),
        meta: { title: '社区活动', icon: 'activity' }
      },
      {
        path: 'feihuodongYuyue',
        name: 'FeihuodongYuyue',
        component: () => import('@/views/modules/feihuodongYuyue/list.vue'),
        meta: { title: '社区活动预约', icon: 'calendar' }
      },
      {
        path: 'zhihuodong',
        name: 'Zhihuodong',
        component: () => import('@/views/modules/zhihuodong/list.vue'),
        meta: { title: '志愿者活动', icon: 'activity' }
      },
      {
        path: 'zhihuodong/add-or-update',
        name: 'ZhihuodongAddOrUpdate',
        component: ZhihuodongAddOrUpdate,
        meta: { title: '添加/修改活动' }
      },
      {
        path: 'zhihuodong/info/:id',
        name: 'ZhihuodongInfo',
        component: ZhihuodongInfo,
        meta: { title: '活动详情' }
      },
      {
        path: 'zhihuodongYuyue',
        name: 'ZhihuodongYuyue',
        component: ZhihuodongYuyueList,
        meta: { title: '志愿者活动预约', icon: 'calendar' }
      },
      {
        path: 'zhihuodongYuyue/add-or-update',
        name: 'ZhihuodongYuyueAddOrUpdate',
        component: ZhihuodongYuyueAddOrUpdate,
        meta: { title: '添加/修改预约' }
      },
      {
        path: 'zhihuodongYuyue/info/:id',
        name: 'ZhihuodongYuyueInfo',
        component: ZhihuodongYuyueInfo,
        meta: { title: '预约详情' }
      },
      {
        path: 'zhiyuanzhe',
        name: 'Zhiyuanzhe',
        component: () => import('@/views/modules/zhiyuanzhe/list.vue'),
        meta: { title: '志愿者管理', icon: 'user' }
      },
      {
        path: 'zhiyuanzheLiuyan',
        name: 'ZhiyuanzheLiuyan',
        component: () => import('@/views/modules/zhiyuanzheLiuyan/list.vue'),
        meta: { title: '志愿者留言', icon: 'message' }
      },
      {
        path: 'feizhiyuan',
        name: 'Feizhiyuan',
        component: () => import('@/views/modules/feizhiyuan/list.vue'),
        meta: { title: '游客管理', icon: 'user' }
      },
      {
        path: 'forum',
        name: 'Forum',
        component: () => import('@/views/modules/forum/list.vue'),
        meta: { 
            title: '论坛管理', 
            icon: 'chat',
            permission: 'forum:view'
        }
      },
      {
        path: 'gonggao',
        name: 'Gonggao',
        component: GonggaoList,
        meta: { title: '公告管理', icon: 'bell' }
      },
      {
        path: 'chat',
        name: 'Chat',
        component: () => import('@/views/modules/chat/list.vue'),
        meta: { title: '聊天管理', icon: 'chat' }
      },
      {
        path: 'config',
        name: 'Config',
        component: ConfigList,
        meta: { 
          title: '系统配置', 
          icon: 'setting',
          permission: 'config:view'
        }
      },
      {
        path: 'config/add-or-update',
        name: 'ConfigAddOrUpdate',
        component: ConfigAddOrUpdate,
        meta: { title: '添加/修改配置' }
      },
      {
        path: 'databaseBackup',
        name: 'DatabaseBackup',
        component: () => import('@/views/modules/databaseBackup/list.vue'),
        meta: { 
          title: '数据库备份', 
          icon: 'database',
          permission: 'databaseBackup:view'
        }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => {
          console.log('正在加载菜单管理组件...');
          const comp = import('@/views/modules/menu/list.vue');
          console.log('菜单管理组件加载结果:', comp);
          return comp;
        },
        meta: { 
          title: '菜单管理', 
          icon: 'menu',
          permission: 'menu:view'
        }
      },
      {
        path: 'dictionary',
        name: 'Dictionary',
        component: DictionaryList,
        meta: { title: '基础字典', icon: 'dict' }
      },
      {
        path: 'feihuodong/add-or-update',
        name: 'FeihuodongAddOrUpdate',
        component: FeihuodongAddOrUpdate,
        meta: { title: '添加/修改活动' }
      },
      {
        path: 'feihuodong/info/:id',
        name: 'FeihuodongInfo',
        component: FeihuodongInfo,
        meta: { title: '活动详情' }
      },
      {
        path: 'huodongshenhe',
        name: 'HuodongShenhe',
        component: HuodongShenhe,
        meta: { 
          title: '活动审核', 
          icon: 'check',
          permission: 'huodong:audit'
        }
      }
    ]
  }
]

// 合并路由
const routes = [...constantRoutes, ...asyncRoutes]

// 增加单独的个人中心和修改密码路由
routes.push(
  {
    path: '/center',
    name: 'CenterPage',
    component: () => import('@/views/Center.vue'),
    meta: { title: '个人中心' }
  },
  {
    path: '/updatePassword',
    name: 'UpdatePasswordPage',
    component: () => import('@/views/UpdatePassword.vue'),
    meta: { title: '修改密码' }
  }
)

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  console.log('路由守卫触发')
  console.log('当前路径:', to.path)
  console.log('来源路径:', from.path)
  
  const token = getToken()
  console.log('获取到的token:', token)
  
  // 白名单路径
  const whiteList = ['/login', '/register']
  
  if (whiteList.includes(to.path)) {
    next()
    return
  }
  
  if (!token) {
    console.log('未登录，跳转到登录页')
    next('/login')
    return
  }
  
  next()
})

export default router 