/**
 * 获取本地存储
 * @param {string} key 键名
 * @returns {any} 存储的值
 */
export function getStorage(key) {
  const value = localStorage.getItem(key)
  try {
    return JSON.parse(value)
  } catch (e) {
    return value
  }
}

/**
 * 设置本地存储
 * @param {string} key 键名
 * @param {any} value 要存储的值
 */
export function setStorage(key, value) {
  if (typeof value === 'object') {
    value = JSON.stringify(value)
  }
  localStorage.setItem(key, value)
}

/**
 * 移除本地存储
 * @param {string} key 键名
 */
export function removeStorage(key) {
  localStorage.removeItem(key)
}

/**
 * 清空本地存储
 */
export function clearStorage() {
  localStorage.clear()
}

/**
 * 获取本地存储的快捷方法
 * @param {string} key 键名
 * @returns {any} 存储的值
 */
export function get(key) {
  return getStorage(key)
}

/**
 * 设置本地存储的快捷方法
 * @param {string} key 键名
 * @param {any} value 要存储的值
 */
export function set(key, value) {
  setStorage(key, value)
}

/**
 * 移除本地存储的快捷方法
 * @param {string} key 键名
 */
export function remove(key) {
  removeStorage(key)
}

// 创建storage对象
export const useStorage = () => {
  return {
    get,
    set,
    remove,
    clear: clearStorage
  }
}

// 添加默认导出
export default {
  getStorage,
  setStorage,
  removeStorage,
  clearStorage,
  get,
  set,
  remove,
  useStorage
} 