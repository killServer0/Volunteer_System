import request from '@/utils/request'

// 获取公告列表
export function getGonggaoList(params) {
  return request({
    url: '/gonggao/page',
    method: 'get',
    params
  })
}

// 添加公告
export function addGonggao(data) {
  return request({
    url: '/gonggao/add',
    method: 'post',
    data
  })
}

// 更新公告
export function updateGonggao(data) {
  return request({
    url: '/gonggao/update',
    method: 'post',
    data
  })
}

// 删除公告
export function deleteGonggao(id) {
  return request({
    url: `/gonggao/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除公告
export function batchDeleteGonggao(ids) {
  return request({
    url: '/gonggao/batchDelete',
    method: 'post',
    data: { ids }
  })
}

// 获取公告详情
export function getGonggaoInfo(id) {
  return request({
    url: `/gonggao/info/${id}`,
    method: 'get'
  })
} 