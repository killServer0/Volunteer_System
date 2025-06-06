const TokenKey = 'token'

export function getToken() {
  const token = localStorage.getItem(TokenKey)
  console.log('获取token:', token)
  return token
}

export function setToken(token) {
  console.log('设置token:', token)
  if (token) {
    localStorage.setItem(TokenKey, token)
  } else {
    localStorage.removeItem(TokenKey)
  }
}

export function removeToken() {
  console.log('移除token')
  localStorage.removeItem(TokenKey)
}

export function isTokenValid() {
  const token = getToken()
  const isValid = token && token.length > 0
  console.log('token是否有效:', isValid)
  return isValid
} 

// 检查用户权限
export function isAuth(tableName, type) {
  // 从localStorage获取用户权限
  const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
  // 构建权限标识
  const permission = `${tableName}:${type}`
  // 检查是否包含该权限
  return permissions.includes(permission)
} 