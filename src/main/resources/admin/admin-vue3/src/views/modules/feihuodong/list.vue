<template>
    <div class="main-content">
        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-card class="search-card" shadow="hover">
                <el-form :inline="true" :model="searchForm" class="form-content">
                    <!-- 第一行：查询筛选区域 -->
                    <el-row :gutter="20" class="search-row">
                        <el-col :xs="24" :sm="8" :md="6" :lg="6">
                            <el-form-item label="活动名称" v-if="isAuth('feihuodong:feihuodongName')">
                                <el-input v-model="searchForm.feihuodongName" placeholder="请输入活动名称" clearable
                                    class="standard-input">
                                    <template #prefix>
                                        <el-icon><Search /></el-icon>
                                    </template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="8" :md="6" :lg="6">
                            <el-form-item label="活动类型" v-if="isAuth('feihuodong:feihuodongTypes')">
                                <el-select v-model="searchForm.feihuodongTypes" placeholder="请选择活动类型" clearable
                                    class="standard-input wider-select">
                                    <el-option label="全部活动" value="" />
                                    <el-option v-for="(item, index) in feihuodongTypesSelectSearch" :key="index"
                                        :label="item.indexName" :value="item.codeIndex" />
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="8" :md="6" :lg="6">
                            <el-form-item label="游客姓名">
                                <el-input v-model="searchForm.feizhiyuanName" placeholder="请输入游客姓名" clearable
                                    class="standard-input">
                                    <template #prefix>
                                        <el-icon><Search /></el-icon>
                                    </template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="24" :md="6" :lg="6" class="search-buttons">
                            <el-form-item>
                                <el-button type="primary" @click="search" class="action-button">
                                    <el-icon><Search /></el-icon>查询
                                </el-button>
                                <el-button @click="resetSearch" class="action-button">
                                    <el-icon><Refresh /></el-icon>重置
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    
                    <!-- 第二行：操作按钮区域 -->
                    <el-row :gutter="20" class="action-row">
                        <el-col :span="24" class="action-col">
                            <el-form-item>
                                <el-button v-if="isAuth('feihuodong', '新增')" type="success" @click="addOrUpdateHandler()" class="action-button">
                                    <el-icon><Plus /></el-icon>新增
                                </el-button>
                                <el-button v-if="isAuth('feihuodong', '删除')" type="danger" :disabled="dataListSelections.length <= 0" 
                                    @click="deleteHandler()" class="action-button">
                                    <el-icon><Delete /></el-icon>批量删除
                                </el-button>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </el-card>

            <!-- 表格 -->
            <el-card class="table-card" shadow="hover">
                <div class="table-content">
                    <el-table v-loading="dataListLoading" :data="dataList" border
                        stripe highlight-current-row
                        @selection-change="selectionChangeHandler">
                        <el-table-column type="selection" width="55" align="center" />
                        <el-table-column type="index" label="序号" width="60" align="center" v-if="contents.tableIndex" />
                        <el-table-column prop="feihuodongName" header-align="center" align="center" label="活动名称" min-width="150" show-overflow-tooltip />
                        <el-table-column prop="feihuodongUuidNumber" header-align="center" align="center" label="活动编号" min-width="100" show-overflow-tooltip />
                        <el-table-column prop="feihuodongPhoto" header-align="center" align="center" label="活动照片" width="120">
                            <template #default="scope">
                                <el-image 
                                    :src="getImageUrl(scope.row.feihuodongPhoto)"
                                    style="width: 80px; height: 80px; object-fit: cover; border-radius: 4px;"
                                    :preview-src-list="[getImageUrl(scope.row.feihuodongPhoto)]"
                                    fit="cover"
                                    @error="handleImageError">
                                </el-image>
                            </template>
                        </el-table-column>
                        <el-table-column prop="feihuodongAddress" header-align="center" align="center" label="活动地点" min-width="120" show-overflow-tooltip />
                        <el-table-column prop="feihuodongTypes" header-align="center" align="center" label="活动类型" width="100">
                            <template #default="scope">
                                <el-tag :type="getActivityTypeStyle(scope.row.feihuodongTypes)">
                                    {{ getActivityType(scope.row.feihuodongTypes) }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="feihuodongKucunNumber" header-align="center" align="center" label="活动人数" width="90" />
                        <el-table-column prop="feihuodongTiaojian" header-align="center" align="center" label="参加条件" min-width="120" show-overflow-tooltip />
                        <el-table-column prop="huodong_status" header-align="center" align="center" label="活动状态" width="100">
                            <template #default="scope">
                                <el-tag :type="getStatusType(scope.row.huodong_status)">
                                    {{ getStatusText(scope.row.huodong_status) }}
                                </el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="180" fixed="right" align="center">
                            <template #default="scope">
                                <div class="operation-buttons">
                                    <el-button link type="primary" size="small"
                                        @click="addOrUpdateHandler(scope.row.id, 'info')">
                                        <el-icon><View /></el-icon>详情
                                    </el-button>
                                    <el-button link type="primary" size="small"
                                        @click="addOrUpdateHandler(scope.row.id)">
                                        <el-icon><Edit /></el-icon>修改
                                    </el-button>
                                    <el-button link type="danger" size="small" @click="deleteHandler(scope.row.id)">
                                        <el-icon><Delete /></el-icon>删除
                                    </el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>

                <!-- 分页 -->
                <div class="pagination-container">
                    <el-pagination v-model:current-page="pageIndex" v-model:page-size="pageSize" 
                        :page-sizes="[10, 20, 50, 100]"
                        :total="totalPage" 
                        layout="total, sizes, prev, pager, next, jumper" 
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        background />
                </div>
            </el-card>
        </div>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList" />
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
    Delete,
    Plus,
    Search,
    Upload,
    Download,
    View,
    Edit,
    PieChart,
    Refresh
} from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import AddOrUpdate from './add-or-update.vue'
import styleJs from '@/utils/style.js'
import { getImageUrl, handleImageError } from '@/utils/imageUtils'

const router = useRouter()
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dataForm = ref({
    id: 0,
    feihuodongName: '',
    feihuodongUuidNumber: '',
    feihuodongPhoto: '',
    feihuodongAddress: '',
    feihuodongTypes: 1,
    feihuodongKucunNumber: 0,
    feihuodongTiaojian: '',
    feihuodongContent: '',
    feihuodongDelete: 0
})

// 权限判断函数
const isAuth = (key) => {
    const token = localStorage.getItem('token')
    return !!token
}

// 样式配置
const contents = reactive(styleJs)
const layouts = ref('')

// 数据列表
const dataList = ref([])
const dataListLoading = ref(false)
const pageIndex = ref(1)
const totalPage = ref(0)
const showFlag = ref(true)
const dataListSelections = ref([])
const feihuodongTypesSelectSearch = ref([])

// 查询条件
const searchForm = reactive({
    feihuodongName: '',
    feihuodongTypes: '',
    feizhiyuanName: ''
})

// 新增/修改弹窗
const addOrUpdateVisible = ref(false)
const addOrUpdate = ref(null)

// 表格样式
const headerRowStyle = () => {
    return {
        color: contents.tableHeaderFontColor,
        fontSize: contents.tableHeaderFontSize,
        backgroundColor: contents.tableHeaderBgColor
    }
}

const headerCellStyle = () => {
    return {
        color: contents.tableHeaderFontColor,
        fontSize: contents.tableHeaderFontSize,
        backgroundColor: contents.tableHeaderBgColor
    }
}

const rowStyle = () => {
    return {
        color: contents.tableContentFontColor,
        fontSize: contents.tableContentFontSize,
        backgroundColor: contents.tableContentBgColor
    }
}

const cellStyle = () => {
    return {
        color: contents.tableContentFontColor,
        fontSize: contents.tableContentFontSize,
        backgroundColor: contents.tableContentBgColor
    }
}

// 分页样式
const contentPageStyleChange = () => {
    let arr = []
    
    // 确保以下项目都添加到布局中
    arr.push('total')
    arr.push('sizes')
    arr.push('prev')
    arr.push('pager')
    arr.push('next') 
    arr.push('jumper')
    
    // 设置布局字符串
    layouts.value = arr.join(', ')
    
    // 设置每页默认显示数量
    contents.pageEachNum = 10
}

// 获取数据列表
const getDataList = async () => {
    try {
        dataListLoading.value = true
        const res = await http.get('/feihuodong/page', {
            params: {
                page: pageIndex.value,
                limit: pageSize.value,
                feihuodongName: searchForm.feihuodongName,
                feihuodongTypes: searchForm.feihuodongTypes,
                feizhiyuanName: searchForm.feizhiyuanName
            }
        })
        if (res.code === 0) {
            console.log('获取到的数据列表:', res.data.list)
            // 处理图片路径
            res.data.list.forEach(item => {
                if (item.feihuodongPhoto) {
                    console.log('处理前的图片路径:', item.feihuodongPhoto)
                    // 移除upload/前缀
                    if (item.feihuodongPhoto.startsWith('upload/')) {
                        item.feihuodongPhoto = item.feihuodongPhoto.substring(7)
                    }
                    console.log('处理后的图片路径:', item.feihuodongPhoto)
                }
            })
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

// 获取活动类型选项
const getFeihuodongTypes = async () => {
    try {
        const res = await http.get('/dictionary/page', {
            params: {
                page: 1,
                limit: 100,
                dicCode: 'feihuodong_types'
            }
        })
        if (res.code === 0) {
            feihuodongTypesSelectSearch.value = res.data.list
        }
    } catch (error) {
        console.error('获取活动类型失败:', error)
    }
}

// 每页数
const sizeChangeHandle = (val) => {
    pageSize.value = val
    pageIndex.value = 1
    getDataList()
}

// 当前页
const currentChangeHandle = (val) => {
    pageIndex.value = val
    getDataList()
}

// 多选
const selectionChangeHandler = (val) => {
    dataListSelections.value = val
}

// 查询
const search = () => {
    pageIndex.value = 1
    getDataList()
}

// 新增/修改
const addOrUpdateHandler = (id, type) => {
    console.log('addOrUpdateHandler called with:', { id, type })
    
    if (type === 'info') {
        router.push({
            name: 'FeihuodongInfo',
            params: { id }
        })
    } else {
        router.push({
            name: 'FeihuodongAddOrUpdate',
            query: id ? { id } : undefined
        })
    }
}

// 删除
const deleteHandler = async (id) => {
    try {
        const ids = id ? [id] : dataListSelections.value.map(item => item.id)
        if (ids.length === 0) {
            ElMessage.warning('请选择要删除的记录')
            return
        }
        await ElMessageBox.confirm('确定要删除选中的记录吗？', '提示', {
            type: 'warning'
        })
        const res = await http.post('/feihuodong/delete', ids)
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
}

// 导出
const exportExcel = () => {
    window.open(`http://localhost:8081/zhiyuanzheguanlixitong/feihuodong/export`)
}

// 上传文件
const getActionUrl = ref('http://localhost:8081/zhiyuanzheguanlixitong/file/upload')
const feihuodongUploadSuccess = (res) => {
    if (res.code === 0) {
        ElMessage.success('上传成功')
        getDataList()
    } else {
        ElMessage.error(res.msg)
    }
}
const feihuodongUploadError = () => {
    ElMessage.error('上传失败')
}

// 报表弹窗
const chartDialog = () => {
    // TODO: 实现报表功能
}

// 重置搜索
const resetSearch = () => {
    searchForm.feihuodongName = ''
    searchForm.feihuodongTypes = ''
    searchForm.feizhiyuanName = ''
    pageIndex.value = 1
    getDataList()
}

// 在 script setup 部分添加状态相关方法
const getStatusType = (status) => {
    const types = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
    }
    return types[status] || 'info'
}

const getStatusText = (status) => {
    const texts = {
        1: '待审核',
        2: '未开始',
        3: '进行中',
        4: '已结束'
    }
    return texts[status] || '未知'
}

// 在 script setup 部分添加活动类型方法
const getActivityType = (type) => {
    // 为feihuodongTypes提供映射
    if (!type) return '未知类型';
    
    const types = {
        1: '公益活动',
        2: '社会实践',
        3: '志愿服务',
        4: '文化活动',
        5: '培训活动',
        6: '其他活动'
    }
    return types[type] || '其他类型'
}

// 获取活动类型的样式
const getActivityTypeStyle = (type) => {
    // 根据活动类型设置不同的标签样式
    const styles = {
        1: 'success',  // 公益活动 - 绿色
        2: 'warning',  // 社会实践 - 黄色
        3: 'primary',  // 志愿服务 - 蓝色
        4: 'info',     // 文化活动 - 灰色
        5: 'danger',   // 培训活动 - 红色
        6: ''          // 其他活动 - 默认
    }
    return styles[type] || ''
}

onMounted(() => {
    contentPageStyleChange()
    getDataList()
    getFeihuodongTypes()
})
</script>

<style lang="scss" scoped>
.main-content {
    padding: 20px;
    background-color: #f5f7fa;
}

.search-card {
    margin-bottom: 20px;
    border-radius: 8px;
    
    .search-row {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        margin-bottom: 10px;
    }
    
    .action-row {
        display: flex;
        margin-top: 6px;
    }
    
    .action-col {
        display: flex;
        align-items: center;
        
        .el-form-item {
            margin-bottom: 0;
        }
    }
    
    .search-buttons {
        display: flex;
        justify-content: flex-start;
        align-items: center;
        
        .el-form-item {
            margin-bottom: 0;
        }
    }
}

.standard-input {
    width: 100%;
}

.wider-select {
    min-width: 180px;
    
    :deep(.el-select__tags) {
        max-width: 90%;
        overflow: hidden;
    }
    
    :deep(.el-input__wrapper) {
        padding-right: 30px;
    }
}

.action-button {
    display: inline-flex;
    align-items: center;
    margin-right: 10px;
    
    .el-icon {
        margin-right: 4px;
    }
}

.table-card {
    border-radius: 8px;
    
    .table-content {
        margin-bottom: 20px;
    }
}

.operation-buttons {
    display: flex;
    justify-content: center;
    gap: 5px;
}

.pagination-container {
    margin-top: 20px;
    padding: 10px 0;
    display: flex;
    justify-content: flex-end;
    
    :deep(.el-pagination) {
        padding: 0;
        margin: 0;
        
        .el-pagination__total, 
        .el-pagination__sizes, 
        .el-pagination__jump {
            margin-right: 10px;
        }
        
        .el-pager li {
            min-width: 32px;
            height: 32px;
            line-height: 32px;
            font-size: 14px;
        }
        
        .btn-prev, .btn-next {
            min-width: 32px;
            height: 32px;
            line-height: 32px;
        }
    }
}

@media (max-width: 768px) {
    .el-form-item {
        margin-bottom: 15px;
    }
    
    .search-card {
        .search-row {
            margin-bottom: 0;
        }
        
        .action-row {
            margin-top: 0;
        }
    }
    
    .operation-buttons {
        flex-direction: column;
        align-items: center;
    }
    
    .search-buttons, .action-col {
        flex-wrap: wrap;
        
        .action-button {
            margin-bottom: 8px;
        }
    }
}
</style>