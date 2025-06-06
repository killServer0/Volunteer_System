import request from '@/utils/request'

// 获取活动列表
export function getHuodongList(params) {
  return request({
    url: '/huodong/list',
    method: 'get',
    params
  })
}

// 添加活动
export function addHuodong(data) {
  return request({
    url: '/huodong/add',
    method: 'post',
    data
  })
}

// 更新活动
export function updateHuodong(data) {
  return request({
    url: '/huodong/update',
    method: 'post',
    data
  })
}

// 删除活动
export function deleteHuodong(id) {
  return request({
    url: `/huodong/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除活动
export function batchDeleteHuodong(ids) {
  return request({
    url: '/huodong/batchDelete',
    method: 'post',
    data: { ids }
  })
}

// 获取活动详情
export function getHuodongInfo(id) {
  return request({
    url: `/huodong/info/${id}`,
    method: 'get'
  })
}

// 获取活动报名列表
export function getHuodongSignupList(params) {
  return request({
    url: '/huodong/signup/list',
    method: 'get',
    params
  })
}

// 审核活动报名
export function reviewHuodongSignup(data) {
  return request({
    url: '/huodong/signup/review',
    method: 'post',
    data
  })
} 