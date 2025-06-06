export default {
  // 开发环境配置
  development: {
    baseURL: 'http://localhost:8081'
  },
  // 生产环境配置
  production: {
    baseURL: 'http://localhost:8081'
  },
  // 其他配置
  title: '志愿者管理系统',
  tokenName: 'token',
  tokenExpires: 7 * 24 * 60 * 60 * 1000, // 7天
  // 分页配置
  pageSize: 10,
  pageSizes: [10, 20, 50, 100],
  // 添加baseUrl导出
  baseUrl: 'http://localhost:8081'
} 