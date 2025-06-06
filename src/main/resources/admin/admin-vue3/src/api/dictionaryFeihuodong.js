import request from '@/utils/request'

// 获取社区活动类型字典列表
export function getDictionaryFeihuodongList(params) {
    return request({
        url: '/dictionaryFeihuodong/list',
        method: 'get',
        params
    })
}

// 获取社区活动类型字典详情
export function getDictionaryFeihuodongInfo(id) {
    return request({
        url: `/dictionaryFeihuodong/info/${id}`,
        method: 'get'
    })
}

// 新增社区活动类型字典
export function saveDictionaryFeihuodong(data) {
    return request({
        url: '/dictionaryFeihuodong/save',
        method: 'post',
        data
    })
}

// 修改社区活动类型字典
export function updateDictionaryFeihuodong(data) {
    return request({
        url: '/dictionaryFeihuodong/update',
        method: 'post',
        data
    })
}

// 删除社区活动类型字典
export function deleteDictionaryFeihuodong(ids) {
    return request({
        url: '/dictionaryFeihuodong/delete',
        method: 'post',
        data: ids
    })
} 