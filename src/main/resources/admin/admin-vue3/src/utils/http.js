import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from './auth'
import router from '@/router'

const service = axios.create({
    baseURL: '/api',
    timeout: 5000,
    withCredentials: true
})

service.interceptors.request.use(
    config => {
        const token = getToken()
        console.log('请求拦截器 - token:', token)
        if (token) {
            config.headers['token'] = token
            console.log('请求拦截器 - 添加token到请求头')
        }
        return config
    },
    error => {
        console.error('请求拦截器 - 错误:', error)
        return Promise.reject(error)
    }
)

service.interceptors.response.use(
    response => {
        console.log('响应拦截器 - 响应数据:', response.data)
        const res = response.data
        
        // 如果返回的是分页数据，直接返回
        if (res.list && typeof res.total !== 'undefined') {
            return res
        }
        
        // 否则按原来的逻辑处理
        if (res.code !== 0) {
            console.error('响应拦截器 - 业务错误:', res)
            ElMessage.error(res.msg || '请求失败')
            if (res.code === 401 || res.code === 403) {
                removeToken()
                router.push('/login')
            }
            return Promise.reject(new Error(res.msg || '请求失败'))
        }
        return res
    },
    error => {
        console.error('响应拦截器 - 错误:', error)
        if (error.response) {
            console.error('响应拦截器 - 错误状态码:', error.response.status)
            if (error.response.status === 401 || error.response.status === 403) {
                ElMessage.error('登录已过期，请重新登录')
                removeToken()
                router.push('/login')
            } else {
                ElMessage.error(error.response.data?.msg || '请求失败')
            }
        } else {
            ElMessage.error('网络错误，请稍后重试')
        }
        return Promise.reject(error)
    }
)

// 导出http实例
export const http = service

// 导出默认实例
export default service

// 封装GET请求
export function get(url, params) {
    return service.get(url, { params })
}

// 封装POST请求
export function post(url, data) {
    return service.post(url, data)
}

// 封装PUT请求
export function put(url, data) {
    return service.put(url, data)
}

// 封装DELETE请求
export function del(url) {
    return service.delete(url)
} 