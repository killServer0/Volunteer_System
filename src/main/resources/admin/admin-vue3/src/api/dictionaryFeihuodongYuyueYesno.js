import request from '@/utils/request'

// 获取社区活动预约状态字典列表
export function getDictionaryFeihuodongYuyueYesnoList(params) {
    return request({
        url: '/dictionaryFeihuodongYuyueYesno/list',
        method: 'get',
        params
    })
}

// 获取社区活动预约状态字典详情
export function getDictionaryFeihuodongYuyueYesnoInfo(id) {
    return request({
        url: `/dictionaryFeihuodongYuyueYesno/info/${id}`,
        method: 'get'
    })
}

// 新增社区活动预约状态字典
export function saveDictionaryFeihuodongYuyueYesno(data) {
    return request({
        url: '/dictionaryFeihuodongYuyueYesno/save',
        method: 'post',
        data
    })
}

// 修改社区活动预约状态字典
export function updateDictionaryFeihuodongYuyueYesno(data) {
    return request({
        url: '/dictionaryFeihuodongYuyueYesno/update',
        method: 'post',
        data
    })
}

// 删除社区活动预约状态字典
export function deleteDictionaryFeihuodongYuyueYesno(ids) {
    return request({
        url: '/dictionaryFeihuodongYuyueYesno/delete',
        method: 'post',
        data: ids
    })
} 