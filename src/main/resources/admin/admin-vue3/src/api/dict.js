import request from '@/utils/request'

// 获取字典列表
export function getDictList(params) {
  return request({
    url: '/dict/list',
    method: 'get',
    params
  })
}

// 添加字典
export function addDict(data) {
  return request({
    url: '/dict/add',
    method: 'post',
    data
  })
}

// 更新字典
export function updateDict(data) {
  return request({
    url: '/dict/update',
    method: 'post',
    data
  })
}

// 删除字典
export function deleteDict(id) {
  return request({
    url: `/dict/delete/${id}`,
    method: 'delete'
  })
}

// 获取字典类型列表
export function getDictTypeList(params) {
  return request({
    url: '/dict/type/list',
    method: 'get',
    params
  })
}

// 添加字典类型
export function addDictType(data) {
  return request({
    url: '/dict/type/add',
    method: 'post',
    data
  })
}

// 更新字典类型
export function updateDictType(data) {
  return request({
    url: '/dict/type/update',
    method: 'post',
    data
  })
}

// 删除字典类型
export function deleteDictType(id) {
  return request({
    url: `/dict/type/delete/${id}`,
    method: 'delete'
  })
} 