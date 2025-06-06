<template>
    <div class="app-container">
        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="search-form" :style="contents.formStyle">
                <el-row :gutter="20" class="slt">
                    <el-form-item label="公告名称" :style="contents.formItemStyle">
                        <el-input v-model="searchForm.gonggaoName" placeholder="请输入公告名称"
                            :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <el-form-item label="公告类型" :style="contents.formItemStyle">
                        <el-select v-model="searchForm.gonggaoTypes" placeholder="请选择公告类型"
                            :style="contents.selectStyle">
                            <el-option label="全部" value=""></el-option>
                            <el-option v-for="(item, index) in gonggaoTypesOptions" :key="index" :label="item.label"
                                :value="item.value"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="search()" :style="contents.buttonStyle">查询</el-button>
                        <el-button type="primary" @click="reset()" :style="contents.buttonStyle">重置</el-button>
                        <el-button type="primary" @click="add()" :style="contents.buttonStyle">新增</el-button>
                        <el-button type="primary" @click="batchDelete()" :style="contents.buttonStyle">批量删除</el-button>
                        <el-button type="primary" @click="exportExcel()" :style="contents.buttonStyle">导出</el-button>
                        <el-button type="primary" @click="print()" :style="contents.buttonStyle">打印</el-button>
                    </el-form-item>
                </el-row>
            </el-form>

            <!-- 表格 -->
            <div class="table-content">
                <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandler"
                    :style="contents.tableStyle">
                    <el-table-column type="selection" header-align="center" align="center" width="50"
                        v-if="contents.tableSelection">
                    </el-table-column>
                    <el-table-column label="索引" type="index" width="50" v-if="contents.tableIndex" />
                    <el-table-column prop="gonggaoName" label="公告名称" header-align="center" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                    </el-table-column>
                    <el-table-column label="公告图片" align="center" width="100">
                        <template #default="scope">
                            <el-image 
                                style="width: 100px; height: 100px"
                                :src="getImageUrl(scope.row.gonggaoPhoto)"
                                :preview-src-list="[getImageUrl(scope.row.gonggaoPhoto)]"
                                fit="cover"
                                @error="handleImageError">
                            </el-image>
                        </template>
                    </el-table-column>
                    <el-table-column prop="gonggaoTypes" label="公告类型" header-align="center" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                        <template #default="scope">
                            {{ scope.row.gonggaoValue }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="insertTime" label="公告发布时间" header-align="center" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="300" header-align="center" align="center">
                        <template #default="scope">
                            <el-button type="success" size="small"
                                @click="addOrUpdateHandler(scope.row.id, 'info')" :style="contents.buttonStyle">
                                <el-icon>
                                    <View />
                                </el-icon>详情
                            </el-button>
                            <el-button type="primary" size="small"
                                @click="addOrUpdateHandler(scope.row.id)" :style="contents.buttonStyle">
                                <el-icon>
                                    <Edit />
                                </el-icon>修改
                            </el-button>
                            <el-button type="danger" size="small"
                                @click="deleteHandler(scope.row.id)" :style="contents.buttonStyle">
                                <el-icon>
                                    <Delete />
                                </el-icon>删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination 
                    @size-change="sizeChangeHandle" 
                    @current-change="currentChangeHandle"
                    :current-page="pageIndex" 
                    :page-sizes="[10, 20, 50, 100]" 
                    :page-size="pageSize" 
                    :total="totalPage"
                    :layout="layouts" 
                    class="pagination-content"
                    :style="{
                        textAlign: contents.pagePosition === 1 ? 'left' : contents.pagePosition === 2 ? 'center' : 'right'
                    }"
                    :background="contents.pageBtnBG"
                />
            </div>
        </div>
        <!-- 添加/修改页面 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

        <!-- 打印对话框 -->
        <el-dialog v-model="printDialogVisible" title="打印" width="500px">
            <el-form :model="printForm" ref="printFormRef" :rules="printRules">
                <el-form-item label="公告名称" prop="gonggaoName">
                    <el-input v-model="printForm.gonggaoName" placeholder="请输入公告名称"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="printDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="printSubmit">打印</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, View, Edit, Upload, Download, Printer } from '@element-plus/icons-vue'
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import { get, post, put, del } from '@/utils/http'
import styleJs from '@/utils/style.js'
import { json2excel } from '@/utils/excel'
import { useRouter, useRoute } from 'vue-router'
import { getImageUrl, handleImageError } from '@/utils/imageUtils'

// 添加路由相关
const router = useRouter()
const route = useRoute()

// 添加调试日志
console.log('Gonggao List Component Mounted')
console.log('Current Route:', route.path)
console.log('Router:', router)

// 样式配置
const contents = reactive(styleJs)
const layouts = ref('total, sizes, prev, pager, next, jumper')
console.log('Layout:', layouts.value)

// 数据列表
const dataList = ref([])
const dataListLoading = ref(false)
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const showFlag = ref(true)
const addOrUpdateVisible = ref(false)
const dataListSelections = ref([])
const gonggaoTypesSelectSearch = ref([])
const gonggaoTypesOptions = ref([])

// 查询条件
const searchForm = reactive({
    gonggaoName: '',
    gonggaoTypes: ''
})

// 打印相关
const printDialogVisible = ref(false)
const printForm = reactive({
    gonggaoName: ''
})
const printRules = {
    gonggaoName: [{ required: true, message: '请输入公告名称', trigger: 'blur' }]
}

// 报表相关
const reportDialogVisible = ref(false)
const reportChart = ref(null)
let chartInstance = null

// 新增/修改弹窗
const addOrUpdate = ref(null)

// 获取数据列表
const getDataList = async () => {
    dataListLoading.value = true
    try {
        const params = {
            page: pageIndex.value,
            limit: pageSize.value,
            sort: 'id'
        }

        if (searchForm.gonggaoName) {
            params.gonggaoName = '%' + searchForm.gonggaoName + '%'
        }
        if (searchForm.gonggaoTypes) {
            // 根据选中的值找到对应的索引值
            const selectedType = gonggaoTypesOptions.value.find(option => option.value === searchForm.gonggaoTypes)
            if (selectedType) {
                params.gonggaoTypes = selectedType.indexValue
            }
        }
        params.gonggaoDelete = 1

        console.log('Request params:', params)
        const res = await get('gonggao/page', params)
        console.log('Response data:', res)
        
        if (res.code === 0) {
            dataList.value = res.data.list
            totalPage.value = res.data.total
        } else {
            ElMessage.error(res.msg || '获取数据失败')
        }
    } catch (error) {
        console.error('获取数据失败:', error)
        ElMessage.error('获取数据失败')
    } finally {
        dataListLoading.value = false
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

// 搜索
const search = () => {
    pageIndex.value = 1
    getDataList()
}

// 重置
const reset = () => {
    searchForm.gonggaoName = ''
    searchForm.gonggaoTypes = ''
    pageIndex.value = 1
    getDataList()
}

// 新增
const add = () => {
    console.log('Adding new announcement')
    addOrUpdateVisible.value = true
    nextTick(() => {
        if (addOrUpdate.value) {
            addOrUpdate.value.init()
        } else {
            console.error('addOrUpdate is not available')
            ElMessage.error('组件加载失败，请刷新页面重试')
        }
    })
}

// 批量删除
const batchDelete = () => {
    if (dataListSelections.value.length === 0) {
        ElMessage.warning('请选择要删除的数据')
        return
    }
    
    ElMessageBox.confirm('确定进行批量删除操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const ids = dataListSelections.value.map(item => item.id)
            const res = await post('gonggao/delete', ids)
            if (res.code === 0) {
                ElMessage.success('批量删除成功')
                getDataList()
            } else {
                ElMessage.error(res.msg || '批量删除失败')
            }
        } catch (error) {
            console.error('批量删除失败:', error)
            ElMessage.error('批量删除失败')
        }
    }).catch(() => {})
}

// 导出Excel
const exportExcel = () => {
    const data = dataList.value.map(item => ({
        '公告名称': item.gonggaoName,
        '公告类型': item.gonggaoValue,
        '公告发布时间': item.insertTime
    }))
    json2excel(data, '公告列表')
}

// 打印
const print = () => {
    printDialogVisible.value = true
}

// 打印提交
const printSubmit = () => {
    // 实现打印逻辑
    printDialogVisible.value = false
}

// 表格选择变化
const selectionChangeHandler = (val) => {
    dataListSelections.value = val
}

// 添加/修改/查看
const addOrUpdateHandler = (id, type) => {
    console.log('Opening add/update dialog:', { id, type })
    addOrUpdateVisible.value = true
    nextTick(() => {
        if (addOrUpdate.value) {
            addOrUpdate.value.init(id, type)
        } else {
            console.error('addOrUpdate is not available')
            ElMessage.error('组件加载失败，请刷新页面重试')
        }
    })
}

// 删除
const deleteHandler = (id) => {
    ElMessageBox.confirm('确定进行删除操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await post('gonggao/delete', [id])
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getDataList()
            } else {
                ElMessage.error(res.msg || '删除失败')
            }
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    }).catch(() => {})
}

// 获取公告类型选项
const getGonggaoTypesOptions = async () => {
    try {
        const res = await get('dictionary/page', {
            dicCode: 'gonggao_types'
        })
        if (res.code === 0) {
            gonggaoTypesOptions.value = res.data.list.map(item => ({
                value: item.codeIndex,
                label: item.indexName,
                indexValue: item.codeIndex
            }))
            console.log('公告类型选项:', gonggaoTypesOptions.value)
        } else {
            ElMessage.error(res.msg || '获取公告类型失败')
        }
    } catch (error) {
        console.error('获取公告类型失败:', error)
        ElMessage.error('获取公告类型失败')
    }
}

// 检查权限
const checkPermissions = () => {
    console.log('Checking permissions...')
    const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
    console.log('User permissions:', permissions)
    // 临时返回所有权限为true，以便测试功能
    return {
        canView: true,
        canEdit: true,
        canDelete: true
    }
}

// 新增/修改/查看 成功回调
const addOrUpdateSuccess = () => {
    getDataList()
}

// 初始化
onMounted(() => {
    console.log('Component mounted, initializing...')
    console.log('Initial showFlag:', showFlag.value)
    const permissions = checkPermissions()
    console.log('User permissions:', permissions)
    getDataList()
    getGonggaoTypesOptions()
})
</script>

<style lang="scss" scoped>
.app-container {
    padding: 20px;
}

.search-form {
    margin-bottom: 20px;
}

.slt {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.table-content {
    margin-top: 20px;
}

.pagination-content {
    margin-top: 20px;
}

.dialog-footer {
    text-align: right;
    margin-top: 20px;
}
</style>