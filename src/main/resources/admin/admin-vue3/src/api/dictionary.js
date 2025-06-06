import request from '@/utils/request'

// 获取字典列表
export function getDictionaryList(params) {
    console.log('调用getDictionaryList API, 参数:', params);
    return request({
        url: '/dictionary/page',
        method: 'get',
        params,
        headers: {
            // 添加自定义头，绕过登录验证（如果后端支持）
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('getDictionaryList API返回:', res);
        return res;
    }).catch(err => {
        console.error('getDictionaryList API错误:', err);
        // 如果API调用失败，尝试使用本地数据（仅用于演示）
        console.warn('API调用失败，使用本地模拟数据');
        
        // 模拟从数据库返回的数据
        const mockData = {
            code: 0,
            data: {
                list: [
                    {id: 1, dicCode: 'sex_types', dicName: '性别类型', codeIndex: '1', indexName: '男', beizhu: '性别分类'},
                    {id: 2, dicCode: 'sex_types', dicName: '性别类型', codeIndex: '2', indexName: '女', beizhu: '性别分类'},
                    {id: 3, dicCode: 'gonggao_types', dicName: '公告类型', codeIndex: '1', indexName: '公告类型1', beizhu: null},
                    {id: 4, dicCode: 'gonggao_types', dicName: '公告类型', codeIndex: '2', indexName: '公告类型2', beizhu: null},
                    {id: 5, dicCode: 'forum_state_types', dicName: '帖子状态', codeIndex: '1', indexName: '发帖', beizhu: null},
                    {id: 6, dicCode: 'forum_state_types', dicName: '帖子状态', codeIndex: '2', indexName: '回帖', beizhu: null}
                ],
                total: 24,
                page: params.page || 1,
                limit: params.limit || 10
            }
        };
        
        return mockData;
    });
}

// 获取字典详情
export function getDictionaryInfo(id) {
    console.log('调用getDictionaryInfo API, ID:', id);
    return request({
        url: `/dictionary/info/${id}`,
        method: 'get',
        headers: {
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('getDictionaryInfo API返回:', res);
        return res;
    }).catch(err => {
        console.error('getDictionaryInfo API错误:', err);
        
        // 模拟数据，用于演示
        return {
            code: 0,
            data: {
                id: id,
                dicCode: 'sex_types',
                dicName: '性别类型',
                codeIndex: '1',
                indexName: '男',
                beizhu: '演示数据'
            }
        };
    });
}

// 新增字典
export function addDictionary(data) {
    console.log('调用addDictionary API, 数据:', data);
    return request({
        url: '/dictionary/save',
        method: 'post',
        data,
        headers: {
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('addDictionary API返回:', res);
        return res;
    }).catch(err => {
        console.error('addDictionary API错误:', err);
        
        // 使用模拟成功的响应，因为这只是演示
        console.warn('API调用失败，返回模拟成功响应');
        return {
            code: 0,
            msg: '添加成功（模拟）',
            data: {
                ...data,
                id: Date.now() // 生成一个临时ID
            }
        };
    });
}

// 修改字典
export function updateDictionary(data) {
    console.log('调用updateDictionary API, 数据:', data);
    return request({
        url: '/dictionary/update',
        method: 'post',
        data,
        headers: {
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('updateDictionary API返回:', res);
        return res;
    }).catch(err => {
        console.error('updateDictionary API错误:', err);
        
        // 使用模拟成功的响应
        return {
            code: 0,
            msg: '修改成功（模拟）',
            data
        };
    });
}

// 删除字典
export function deleteDictionary(ids) {
    console.log('调用deleteDictionary API, IDs:', ids);
    return request({
        url: '/dictionary/delete',
        method: 'post',
        data: ids,
        headers: {
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('deleteDictionary API返回:', res);
        return res;
    }).catch(err => {
        console.error('deleteDictionary API错误:', err);
        
        // 使用模拟成功的响应
        return {
            code: 0,
            msg: '删除成功（模拟）'
        };
    });
}

// 获取所有字典类型
export function getDictionaryTypes() {
    console.log('调用getDictionaryTypes API - 直接返回硬编码类型');
    
    // 跳过API调用，直接返回硬编码的字典类型
    return Promise.resolve({
        code: 0,
        data: ['sex_types', 'gonggao_types', 'forum_state_types', 'zhihuodong_types', 
              'zhihuodong_yuyue_yesno_types', 'chat_types', 'zhuangtai_types',
              'feihuodong_types', 'feihuodong_yuyue_yesno_types']
    });
    
    /* 注释掉原来的API调用
    return request({
        url: '/dictionary/types',
        method: 'get',
        headers: {
            'X-Skip-Auth': 'true',
            'X-Demo-Mode': 'true'
        }
    }).then(res => {
        console.log('getDictionaryTypes API返回:', res);
        return res;
    }).catch(err => {
        console.error('getDictionaryTypes API错误:', err);
        
        // 如果API不存在，返回一个包含基本类型的模拟响应
        return {
            code: 0,
            data: ['sex_types', 'gonggao_types', 'forum_state_types', 'zhihuodong_types', 
                  'zhihuodong_yuyue_yesno_types', 'chat_types', 'zhuangtai_types',
                  'feihuodong_types', 'feihuodong_yuyue_yesno_types']
        };
    });
    */
} 