<template>
    <div class="main-content">
        <!-- 搜索表单 -->
        <el-form :inline="true" :model="searchForm" class="form-content"
            :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
            <el-row :gutter="20" class="slt">
                <el-form-item label="状态">
                    <el-input v-model="searchForm.indexNameSearch" placeholder="状态" clearable
                        :style="{ width: contents.inputWidth }"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search"
                        :style="{ backgroundColor: contents.searchBtnBgColor, color: contents.searchBtnFontColor, borderColor: contents.searchBtnBorderColor }">查询</el-button>
                </el-form-item>
            </el-row>
            <el-row class="ad"
                :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
                <el-form-item>
                    <el-button v-if="isAuth('dictionaryZhuangtai', '新增')" type="primary" @click="addOrUpdateHandler()"
                        :style="{ backgroundColor: contents.btnAdAllAddBgColor, color: contents.btnAdAllAddFontColor, borderColor: contents.btnAdAllBorderColor }">新增</el-button>
                    <el-button v-if="isAuth('dictionaryZhuangtai', '删除')" :disabled="dataListSelections.length <= 0"
                        type="danger" @click="deleteHandler()"
                        :style="{ backgroundColor: contents.btnAdAllDelBgColor, color: contents.btnAdAllDelFontColor, borderColor: contents.btnAdAllBorderColor }">删除</el-button>
                </el-form-item>
            </el-row>
        </el-form>

        <!-- 数据表格 -->
        <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%"
            @selection-change="selectionChangeHandler" :header-row-style="headerRowStyle"
            :header-cell-style="headerCellStyle" :row-style="rowStyle" :cell-style="cellStyle"
            :size="contents.tableSize" :show-header="contents.tableShowHeader" :border="contents.tableBorder"
            :fit="contents.tableFit" :stripe="contents.tableStripe">
            <el-table-column v-if="contents.tableSelection" type="selection" width="55"></el-table-column>
            <el-table-column v-if="contents.tableIndex" type="index" label="序号" width="50"></el-table-column>
            <el-table-column prop="codeIndex" label="状态编码" :sortable="contents.tableSortable"
                :align="contents.tableAlign"></el-table-column>
            <el-table-column prop="indexName" label="状态名称" :sortable="contents.tableSortable"
                :align="contents.tableAlign"></el-table-column>
            <el-table-column label="操作" width="200" :align="contents.tableAlign">
                <template #default="scope">
                    <el-button v-if="isAuth('dictionaryZhuangtai', '查看')" type="primary" size="small"
                        @click="addOrUpdateHandler(scope.row.id, 'info')"
                        :style="{ backgroundColor: contents.btnTableInfoBgColor, color: contents.btnTableInfoFontColor, borderColor: contents.btnTableInfoBorderColor }">详情</el-button>
                    <el-button v-if="isAuth('dictionaryZhuangtai', '修改')" type="success" size="small"
                        @click="addOrUpdateHandler(scope.row.id)"
                        :style="{ backgroundColor: contents.btnTableUpdateBgColor, color: contents.btnTableUpdateFontColor, borderColor: contents.btnTableUpdateBorderColor }">修改</el-button>
                    <el-button v-if="isAuth('dictionaryZhuangtai', '删除')" type="danger" size="small"
                        @click="deleteHandler(scope.row.id)"
                        :style="{ backgroundColor: contents.btnTableDelBgColor, color: contents.btnTableDelFontColor, borderColor: contents.btnTableDelBorderColor }">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination v-model:current-page="pageIndex" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
            :total="totalPage" :layout="layouts" @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
            :small="contents.pageStyle" :background="contents.pageBtnBG"
            :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }"></el-pagination>

        <!-- 添加/修改弹窗 -->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, View, Edit, Upload, Download, Printer } from '@element-plus/icons-vue'
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import { getList, deleteData } from '@/api/dictionaryZhuangtai'
import styleJs from '@/utils/style.js'

const searchForm = reactive({
    indexNameSearch: ''
})

const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const dataListSelections = ref([])
const showFlag = ref(true)
const addOrUpdateFlag = ref(false)
const addOrUpdate = ref(null)

// 样式配置
const contents = ref(styleJs.listStyle())
const layouts = ref('')

// 表格样式
const headerRowStyle = ({ row, rowIndex }) => {
    return { color: contents.value.tableHeaderFontColor }
}

const headerCellStyle = ({ row, rowIndex }) => {
    return { backgroundColor: contents.value.tableHeaderBgColor }
}

const rowStyle = ({ row, rowIndex }) => {
    if (rowIndex % 2 == 1) {
        if (contents.value.tableStripe) {
            return { color: contents.value.tableStripeFontColor }
        }
    } else {
        return ''
    }
}

const cellStyle = ({ row, rowIndex }) => {
    if (rowIndex % 2 == 1) {
        if (contents.value.tableStripe) {
            return { backgroundColor: contents.value.tableStripeBgColor }
        }
    } else {
        return ''
    }
}

// 分页样式
const contentPageStyleChange = () => {
    let arr = []
    if (contents.value.pageTotal) arr.push('total')
    if (contents.value.pageSizes) arr.push('sizes')
    if (contents.value.pagePrevNext) {
        arr.push('prev')
        if (contents.value.pagePager) arr.push('pager')
        arr.push('next')
    }
    if (contents.value.pageJumper) arr.push('jumper')
    layouts.value = arr.join()
    contents.value.pageEachNum = 10
}

// 获取数据列表
const getDataList = async () => {
    dataListLoading.value = true
    try {
        const { data } = await getList({
            page: pageIndex.value,
            limit: pageSize.value,
            ...searchForm
        })
        dataList.value = data.list
        totalPage.value = data.total
    } catch (error) {
        console.error('获取数据列表失败:', error)
        ElMessage.error('获取数据列表失败')
    } finally {
        dataListLoading.value = false
    }
}

// 查询
const search = () => {
    pageIndex.value = 1
    getDataList()
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

// 新增 / 修改
const addOrUpdateHandler = (id, type) => {
    showFlag.value = false
    addOrUpdateFlag.value = true
    nextTick(() => {
        addOrUpdate.value.init(id, type)
    })
}

// 删除
const deleteHandler = (id) => {
    const ids = id ? [id] : dataListSelections.value.map(item => item.id)
    ElMessageBox.confirm('确定要删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            await deleteData(ids)
            ElMessage.success('删除成功')
            getDataList()
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    })
}

// 打印功能
const printFrom = ref({
    name1: ''
})
const printVisiable = ref(false)

const dayinOpen = (row) => {
    printFrom.value.name1 = row.indexName
    printVisiable.value = true
}

// 报表功能
const chartVisiable = ref(false)
const chartDialog = () => {
    chartVisiable.value = true
    nextTick(() => {
        const chart = echarts.init(document.getElementById('chartRef'))
        const option = {
            title: {
                text: '状态统计'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['状态数量']
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: '状态数量',
                    type: 'bar',
                    data: [10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65]
                }
            ]
        }
        chart.setOption(option)
    })
}

// 批量导入导出
const getActionUrl = ref('http://localhost:8081/zhiyuanzheguanlixitong/upload/zhuangtaiMuBan.xls')

const zhuangtaiUploadSuccess = (response) => {
    if (response.code === 0) {
        ElMessage.success('导入成功')
        getDataList()
    } else {
        ElMessage.error(response.msg)
    }
}

const zhuangtaiUploadError = () => {
    ElMessage.error('导入失败')
}

const exportExcel = () => {
    // 实现导出Excel功能
}

onMounted(() => {
    contentPageStyleChange()
    getDataList()
})
</script>

<style scoped>
.main-content {
    padding: 20px;
    background: transparent;
}

.form-content {
    margin-bottom: 20px;
    background: transparent;
}

.slt {
    margin-bottom: 20px;
    display: flex;
}

.ad {
    margin-bottom: 20px;
    display: flex;
}

.tables /deep/ .el-button--success {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables /deep/ .el-button--primary {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables /deep/ .el-button--danger {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables /deep/ .el-button {
    margin: 4px;
}

.tables /deep/ .el-table__body tr {
    background-color: #f5f5f5 !important;
    color: #606266 !important;
}

.tables /deep/ .el-table__body tr.el-table__row--striped td {
    background: transparent;
}

.tables /deep/ .el-table__body tr.el-table__row--striped {
    background-color: #F5F7FA !important;
    color: #606266 !important;
}

.tables /deep/ .el-table__body tr:hover>td {
    background-color: #f5f5f5 !important;
    color: #333 !important;
}
</style>