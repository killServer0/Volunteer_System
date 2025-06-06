import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = getToken()
    
    // 添加自动授权功能，适用于演示环境
    if (config.headers['X-Demo-Mode'] === 'true') {
      // 使用固定的Token，这是毕业设计演示专用
      config.headers['Authorization'] = `Bearer pmonxuu0kxmb6104oycll2k0i7ct7pt1`
      console.log('已添加演示环境固定Token')
    } else if (token) {
      // 正常情况使用用户的Token
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log('API响应数据:', res)

    // 如果返回的状态码不是0或200，说明接口请求有误
    if (res.code !== 0 && res.code !== 200) {
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })

      // 401: 未登录或token过期
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.resetState()
        window.location.reload()
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    ElMessage({
      message: error.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

// 为了兼容旧代码，添加get和post方法
export const get = (url, params) => {
  console.log('使用request.js中的get方法，路径:', url);
  return service.get(url, { params });
}

export const post = (url, data) => {
  console.log('使用request.js中的post方法，路径:', url);
  return service.post(url, data);
}

export default service 