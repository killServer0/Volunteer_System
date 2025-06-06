<template>
    <div class="main-content">
        <!-- 审核弹窗 -->
        <el-dialog title="审核" v-model="zhihuodongYuyueYesnoTypesVisible" width="50%" 
            :close-on-click-modal="false" :close-on-press-escape="false">
            <el-form :model="form" :rules="dataRule" ref="formRef" label-width="100px">
                <el-form-item label="审核状态" prop="zhihuodongYuyueYesnoTypes">
                    <el-select v-model="form.zhihuodongYuyueYesnoTypes" placeholder="请选择审核状态" style="width: 100%">
                        <el-option v-for="item in zhihuodongYuyueYesnoTypesSelectSearch" :key="item.codeIndex" 
                            :label="item.indexName" :value="item.codeIndex">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="审核回复" prop="zhihuodongYuyueYesnoText">
                    <el-input type="textarea" v-model="form.zhihuodongYuyueYesnoText" 
                        placeholder="请输入审核回复" :rows="4"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="zhihuodongYuyueYesnoTypesVisible = false">取 消</el-button>
                    <el-button type="primary" @click="zhihuodongYuyueYesnoTypesShenhe">确 定</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 条件查询 -->
        <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="活动名称">
                <el-input v-model="searchForm.zhihuodongName" placeholder="请输入活动名称" clearable></el-input>
                </el-form-item>
            <el-form-item label="活动类型">
                <el-select v-model="searchForm.zhihuodongTypes" placeholder="请选择活动类型" clearable style="width: 200px">
                    <el-option v-for="item in zhihuodongTypesSelectSearch" :key="item.codeIndex" 
                            :label="item.indexName" :value="item.codeIndex">
                        </el-option>
                    </el-select>
                </el-form-item>
            <el-form-item label="志愿者姓名">
                <el-input v-model="searchForm.zhiyuanzheName" placeholder="请输入志愿者姓名" clearable></el-input>
                </el-form-item>
            <el-form-item label="报名状态">
                <el-select v-model="searchForm.zhihuodongYuyueYesnoTypes" placeholder="请选择报名状态" clearable style="width: 200px">
                    <el-option v-for="item in zhihuodongYuyueYesnoTypesSelectSearch" :key="item.codeIndex" 
                            :label="item.indexName" :value="item.codeIndex">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                <el-button type="primary" @click="getDataList">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
                </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <div class="operation-buttons">
            <el-button v-if="isAuth('zhihuodongYuyue', '新增')" type="primary" @click="addOrUpdateHandler()">
                <el-icon><Plus /></el-icon>新增
            </el-button>
            <el-button v-if="isAuth('zhihuodongYuyue', '删除')" type="danger" :disabled="dataListSelections.length <= 0"
                @click="deleteHandler()">
                <el-icon><Delete /></el-icon>删除
            </el-button>
            <el-button v-if="isAuth('zhihuodongYuyue', '导入导出')" type="success" @click="exportExcel()">
                <el-icon><Download /></el-icon>导出
            </el-button>
            <el-upload v-if="isAuth('zhihuodongYuyue', '导入导出')" :action="getActionUrl" 
                :on-success="zhihuodongYuyueUploadSuccess" :on-error="zhihuodongYuyueUploadError" 
                :show-file-list="false" style="display: inline-block">
                <el-button type="success">
                    <el-icon><Upload /></el-icon>批量导入
                </el-button>
            </el-upload>
        </div>

        <!-- 数据表格 -->
        <el-table :data="dataList" v-loading="dataListLoading" @selection-change="selectionChangeHandler" 
            border stripe style="width: 100%">
            <el-table-column type="selection" width="55" align="center"></el-table-column>
            <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
            <el-table-column prop="zhihuodongName" label="活动名称" min-width="120"></el-table-column>
            <el-table-column prop="zhihuodongValue" label="活动类型" width="100"></el-table-column>
            <el-table-column prop="zhiyuanzheName" label="志愿者姓名" width="100"></el-table-column>
            <el-table-column prop="zhiyuanzhePhone" label="志愿者手机号" width="120"></el-table-column>
            <el-table-column prop="zhihuodongYuyueUuidNumber" label="报名编号" width="120"></el-table-column>
            <el-table-column prop="zhihuodongYuyueText" label="报名理由" min-width="150"></el-table-column>
            <el-table-column label="报名状态" width="100">
                    <template #default="scope">
                    <el-tag :type="getStatusType(scope.row.zhihuodongYuyueYesnoTypes)">
                        {{getStatusText(scope.row.zhihuodongYuyueYesnoTypes)}}
                    </el-tag>
                    </template>
                </el-table-column>
            <el-table-column prop="zhihuodongYuyueYesnoText" label="审核回复" min-width="150">
                    <template #default="scope">
                    <span>{{scope.row.zhihuodongYuyueYesnoText || '-'}}</span>
                    </template>
                </el-table-column>
            <el-table-column prop="insertTime" label="报名时间" width="160"></el-table-column>
            <el-table-column label="操作" width="200" fixed="right">
                    <template #default="scope">
                    <el-button-group>
                        <template v-for="button in getOperationButtons(scope.row)" :key="button.text">
                            <el-button 
                                :type="button.type" 
                                size="small" 
                                @click="button.click">
                                {{ button.text }}
                            </el-button>
                        </template>
                    </el-button-group>
                    </template>
                </el-table-column>
            </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
            <el-pagination
                @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle"
                :current-page="pageIndex"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                :total="totalPage"
                layout="total, sizes, prev, pager, next, jumper">
            </el-pagination>
        </div>

        <!-- 添加/修改弹窗 -->
        <add-or-update 
            v-if="addOrUpdateVisible" 
            ref="addOrUpdate" 
            :visible="addOrUpdateVisible" 
            @update:visible="addOrUpdateVisible = $event"
            @refreshDataList="getDataList">
        </add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeMount, onBeforeUnmount, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { Plus, Delete, Download, Upload, Picture } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import config from '@/config'
import http from '@/utils/http'

const router = useRouter()
const formRef = ref(null)

// 添加响应式变量
const dataListLoading = ref(false)
const dataList = ref([])
const dataListSelections = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const addOrUpdateVisible = ref(false)
const addOrUpdate = ref(null)
const zhihuodongYuyueYesnoTypesVisible = ref(false)
const form = reactive({
    id: '',
    zhihuodongYuyueYesnoTypes: '',
    zhihuodongYuyueYesnoText: ''
})

// 搜索表单
const searchForm = reactive({
    zhihuodongName: '',
    zhihuodongTypes: '',
    zhiyuanzheName: '',
    zhihuodongYuyueYesnoTypes: ''
})

// 选项数据
const zhihuodongTypesSelectSearch = ref([])
const zhihuodongYuyueYesnoTypesSelectSearch = ref([])

// 添加表单验证规则
const dataRule = {
    zhihuodongYuyueYesnoTypes: [
        { required: true, message: '请选择审核状态', trigger: 'change' }
    ],
    zhihuodongYuyueYesnoText: [
        { required: true, message: '请输入审核回复', trigger: 'blur' },
        { min: 2, message: '审核回复不能少于2个字符', trigger: 'blur' }
    ]
}

// 组件生命周期钩子
onMounted(() => {
        getDataList()
        getOptions()
})

// 获取数据列表
const getDataList = async () => {
    try {
        dataListLoading.value = true
        const res = await http.get('/zhihuodongYuyue/page', {
            params: {
            page: pageIndex.value,
            limit: pageSize.value,
                ...searchForm
            }
        })
        
        if (res.code === 0) {
            dataList.value = res.data.list
            totalPage.value = res.data.total
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取数据列表失败:', error)
        ElMessage.error('获取数据列表失败')
    } finally {
        dataListLoading.value = false
    }
}

// 获取选项数据
const getOptions = async () => {
    try {
        // 获取报名状态选项
        const res1 = await http.get('dictionary/page', {
            params: {
                dicCode: 'zhihuodong_yuyue_yesno_types'
            }
        })
        if (res1.code === 0) {
            zhihuodongYuyueYesnoTypesSelectSearch.value = res1.data.list
        }

        // 获取活动类型选项
        const res2 = await http.get('dictionary/page', {
            params: {
                dicCode: 'zhihuodong_types'
            }
        })
        if (res2.code === 0) {
            zhihuodongTypesSelectSearch.value = res2.data.list
        }
    } catch (error) {
        console.error('获取选项数据失败:', error)
        ElMessage.error('获取选项数据失败')
    }
}

// 搜索
const search = () => {
    pageIndex.value = 1
    getDataList()
}

// 重置搜索
const resetSearch = () => {
    Object.keys(searchForm).forEach(key => {
        searchForm[key] = ''
    })
    search()
}

// 多选
const selectionChangeHandler = (val) => {
    dataListSelections.value = val
}

// 新增/修改/查看
const addOrUpdateHandler = (id, type) => {
    if (type === 'info') {
        router.push({
            name: 'ZhihuodongYuyueInfo',
            params: { id }
        })
    } else if (type === 'update') {
        addOrUpdateVisible.value = true
        nextTick(() => {
            if (addOrUpdate.value) {
                addOrUpdate.value.init(id)
            }
        })
    } else {
    addOrUpdateVisible.value = true
    nextTick(() => {
            if (addOrUpdate.value) {
                addOrUpdate.value.init()
            }
    })
    }
}

// 删除
const deleteHandler = (id) => {
    const ids = id ? [id] : dataListSelections.value.map(item => item.id)
    if (ids.length === 0) {
        ElMessage.warning('请选择删除记录')
        return
    }

    ElMessageBox.confirm('确定要删除所选的记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post('zhihuodongYuyue/delete', ids)
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getDataList()
            } else {
                ElMessage.error(res.msg)
            }
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    })
}

// 审核
const yesnoHandler = (id) => {
    console.log('=== 开始审核操作 ===')
    console.log('审核记录ID:', id)
    form.id = id
    form.zhihuodongYuyueYesnoTypes = '1' // 默认设置为待审核
    form.zhihuodongYuyueYesnoText = ''
    zhihuodongYuyueYesnoTypesVisible.value = true
    console.log('审核表单初始值:', JSON.stringify(form))
}

// 审核提交
const zhihuodongYuyueYesnoTypesShenhe = async () => {
    console.log('=== 开始提交审核 ===')
    if (!formRef.value) {
        console.error('表单引用不存在')
        return
    }

    try {
        await formRef.value.validate(async (valid) => {
            if (valid) {
                console.log('表单验证通过，提交数据:', JSON.stringify(form))
                const res = await http.post('zhihuodongYuyue/update', form)
                console.log('审核提交响应:', res)
                
                if (res.code === 0) {
            ElMessage.success('审核成功')
            zhihuodongYuyueYesnoTypesVisible.value = false
            getDataList()
        } else {
                    ElMessage.error(res.msg || '审核失败')
        }
            } else {
                console.log('表单验证失败')
                return false
            }
        })
    } catch (error) {
        console.error('审核提交失败:', error)
        ElMessage.error('审核失败: ' + (error.message || '未知错误'))
    }
}

// 导出Excel
const exportExcel = async () => {
    try {
        const res = await http.get('zhihuodongYuyue/export', {
            params: searchForm,
            responseType: 'blob'
        })
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '志愿者活动预约.xlsx'
        link.click()
    } catch (error) {
        console.error('导出失败:', error)
        ElMessage.error('导出失败')
    }
}

// 获取上传地址
const getActionUrl = computed(() => {
    return `${baseUrl}zhihuodongYuyue/import`
})

// 上传成功
const zhihuodongYuyueUploadSuccess = (res) => {
    if (res.code === 0) {
        ElMessage.success('导入成功')
        getDataList()
    } else {
        ElMessage.error(res.msg)
    }
}

// 上传失败
const zhihuodongYuyueUploadError = () => {
    ElMessage.error('导入失败')
}

// 分页
const sizeChangeHandle = (val) => {
    pageSize.value = val
    getDataList()
}

const currentChangeHandle = (val) => {
    pageIndex.value = val
    getDataList()
}

// 修改操作列的实现
const getOperationButtons = (row) => {
    return [
        {
            type: 'primary',
            text: '查看',
            click: () => addOrUpdateHandler(row.id, 'info')
        },
        {
            type: 'success',
            text: '修改',
            click: () => addOrUpdateHandler(row.id, 'update')
        },
        {
            type: 'warning',
            text: '审核',
            click: () => yesnoHandler(row.id)
        }
    ]
}

// 获取状态文本
const getStatusText = (status) => {
    console.log('获取状态文本:', status)
    if (status === null || status === undefined) {
        return '待审核'
    }
    
    const statusStr = String(status).trim()
    console.log('处理后的状态值:', statusStr)
    
    switch (statusStr) {
        case '1': return '待审核'
        case '2': return '已通过'
        case '3': return '已拒绝'
        default: return '待审核'
    }
}

// 获取状态类型
const getStatusType = (status) => {
    console.log('获取状态类型:', status)
    if (status === null || status === undefined) {
        return 'info'
    }
    
    const statusStr = String(status).trim()
    console.log('处理后的状态值:', statusStr)
    
    switch (statusStr) {
        case '1': return 'info'
        case '2': return 'success'
        case '3': return 'danger'
        default: return 'info'
    }
}
</script>

<style scoped>
.main-content {
    padding: 20px;
}

.search-form {
    margin-bottom: 20px;
}

.search-form :deep(.el-form-item) {
    margin-right: 20px;
}

.search-form :deep(.el-select) {
    width: 200px;
}

.operation-buttons {
    margin-bottom: 20px;
}

.pagination-container {
    margin-top: 20px;
    text-align: right;
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 30px;
}

.el-image {
    border-radius: 4px;
}

.el-tag {
    margin-right: 10px;
}
</style>