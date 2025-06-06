import request from '@/utils/request'

// 获取志愿者列表
export function getZhiyuanzheList(params) {
  return request({
    url: '/zhiyuanzhe/list',
    method: 'get',
    params
  })
}

// 添加志愿者
export function addZhiyuanzhe(data) {
  return request({
    url: '/zhiyuanzhe/add',
    method: 'post',
    data
  })
}

// 更新志愿者
export function updateZhiyuanzhe(data) {
  return request({
    url: '/zhiyuanzhe/update',
    method: 'post',
    data
  })
}

// 删除志愿者
export function deleteZhiyuanzhe(id) {
  return request({
    url: `/zhiyuanzhe/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除志愿者
export function batchDeleteZhiyuanzhe(ids) {
  return request({
    url: '/zhiyuanzhe/batchDelete',
    method: 'post',
    data: { ids }
  })
}

// 获取志愿者详情
export function getZhiyuanzheInfo(id) {
  return request({
    url: `/zhiyuanzhe/info/${id}`,
    method: 'get'
  })
}

// 获取志愿者留言列表
export function getZhiyuanzheMessageList(params) {
  return request({
    url: '/zhiyuanzhe/message/list',
    method: 'get',
    params
  })
}

// 添加志愿者留言
export function addZhiyuanzheMessage(data) {
  return request({
    url: '/zhiyuanzhe/message/add',
    method: 'post',
    data
  })
}

// 删除志愿者留言
export function deleteZhiyuanzheMessage(id) {
  return request({
    url: `/zhiyuanzhe/message/delete/${id}`,
    method: 'delete'
  })
} 