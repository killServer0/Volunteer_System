<template>
    <div class="main-content">
        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content"
                :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
                <el-row :gutter="20" class="slt"
                    :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item :label="contents.inputTitle == 1 ? '游客姓名' : ''">
                        <el-input v-model="searchForm.feizhiyuanName" placeholder="游客姓名" clearable
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item :label="contents.inputTitle == 1 ? '手机号码' : ''">
                        <el-input v-model="searchForm.feizhiyuanPhone" placeholder="手机号码" clearable
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item :label="contents.inputTitle == 1 ? '身份证号' : ''">
                        <el-input v-model="searchForm.feizhiyuanIdNumber" placeholder="身份证号" clearable
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="search"
                            :style="{ backgroundColor: contents.searchBtnBgColor, color: contents.searchBtnFontColor, fontSize: contents.searchBtnFontSize, borderWidth: contents.searchBtnBorderWidth, borderStyle: contents.searchBtnBorderStyle, borderColor: contents.searchBtnBorderColor, borderRadius: contents.searchBtnBorderRadius }">
                            查询<el-icon class="el-icon--right">
                                <Search />
                            </el-icon>
                        </el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad"
                    :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item>
                        <el-button v-if="isAuth('feizhiyuan', '新增')" type="primary" :icon="Plus"
                            @click="addOrUpdateHandler()"
                            :style="{ backgroundColor: contents.btnAdAllAddBgColor, color: contents.btnAdAllAddFontColor, fontSize: contents.btnAdAllAddFontSize, borderWidth: contents.btnAdAllAddBorderWidth, borderStyle: contents.btnAdAllAddBorderStyle, borderColor: contents.btnAdAllAddBorderColor, borderRadius: contents.btnAdAllAddBorderRadius }">新增</el-button>
                        <el-button v-if="isAuth('feizhiyuan', '删除')" type="danger" :icon="Delete"
                            :disabled="dataListSelections.length <= 0" @click="deleteHandler()"
                            :style="{ backgroundColor: contents.btnAdAllDelBgColor, color: contents.btnAdAllDelFontColor, fontSize: contents.btnAdAllDelFontSize, borderWidth: contents.btnAdAllDelBorderWidth, borderStyle: contents.btnAdAllDelBorderStyle, borderColor: contents.btnAdAllDelBorderColor, borderRadius: contents.btnAdAllDelBorderRadius }">删除</el-button>
                        <el-button v-if="isAuth('feizhiyuan', '报表')" type="success" :icon="PieChart"
                            @click="chartDialog()"
                            :style="{ backgroundColor: contents.btnAdAllReportBgColor, color: contents.btnAdAllReportFontColor, fontSize: contents.btnAdAllReportFontSize, borderWidth: contents.btnAdAllReportBorderWidth, borderStyle: contents.btnAdAllReportBorderStyle, borderColor: contents.btnAdAllReportBorderColor, borderRadius: contents.btnAdAllReportBorderRadius }">报表</el-button>
                        <a v-if="isAuth('feizhiyuan', '导入导出')" class="el-button el-button--success"
                            style="text-decoration: none"
                            :style="{ backgroundColor: contents.btnAdAllImportBgColor, color: contents.btnAdAllImportFontColor, fontSize: contents.btnAdAllImportFontSize, borderWidth: contents.btnAdAllImportBorderWidth, borderStyle: contents.btnAdAllImportBorderStyle, borderColor: contents.btnAdAllImportBorderColor, borderRadius: contents.btnAdAllImportBorderRadius }"
                            :href="`${baseUrl}upload/feizhiyuanMuBan.xls`">批量导入游客数据模板</a>
                        <el-upload v-if="isAuth('feizhiyuan', '导入导出')" style="display: inline-block"
                            :action="`${baseUrl}feizhiyuan/import`" :on-success="feizhiyuanUploadSuccess"
                            :on-error="feizhiyuanUploadError" :show-file-list="false">
                            <el-button type="success" :icon="Upload"
                                :style="{ backgroundColor: contents.btnAdAllImportBgColor, color: contents.btnAdAllImportFontColor, fontSize: contents.btnAdAllImportFontSize, borderWidth: contents.btnAdAllImportBorderWidth, borderStyle: contents.btnAdAllImportBorderStyle, borderColor: contents.btnAdAllImportBorderColor, borderRadius: contents.btnAdAllImportBorderRadius }">批量导入游客数据</el-button>
                        </el-upload>
                        <el-button v-if="isAuth('feizhiyuan', '导入导出')" type="success" :icon="Download"
                            @click="exportExcel"
                            :style="{ backgroundColor: contents.btnAdAllExportBgColor, color: contents.btnAdAllExportFontColor, fontSize: contents.btnAdAllExportFontSize, borderWidth: contents.btnAdAllExportBorderWidth, borderStyle: contents.btnAdAllExportBorderStyle, borderColor: contents.btnAdAllExportBorderColor, borderRadius: contents.btnAdAllExportBorderRadius }">导出</el-button>
                    </el-form-item>
                </el-row>
            </el-form>

            <!-- 表格 -->
            <div class="table-content">
                <el-table v-loading="dataListLoading" :data="dataList" border
                    :style="{ width: '100%', fontSize: contents.tableContentFontSize, color: contents.tableContentFontColor }"
                    :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :row-style="rowStyle"
                    :cell-style="cellStyle" @selection-change="selectionChangeHandler">
                    <el-table-column type="selection" width="55" align="center" v-if="contents.tableSelection" />
                    <el-table-column type="index" label="索引" width="50" v-if="contents.tableIndex" />
                    <el-table-column prop="username" label="账户" align="center" :sortable="contents.tableSortable"
                        :align="contents.tableAlign" />
                    <el-table-column prop="feizhiyuanName" label="游客姓名" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign" />
                    <el-table-column prop="feizhiyuanPhone" label="游客手机号" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign" />
                    <el-table-column prop="feizhiyuanIdNumber" label="游客身份证号" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign" />
                    <el-table-column prop="feizhiyuanPhoto" label="游客头像" align="center" width="100"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                        <template #default="{ row }">
                            <div v-if="row.feizhiyuanPhoto">
                                <img :src="baseUrl + row.feizhiyuanPhoto" width="50" height="50">
                            </div>
                            <div v-else>无照片</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="sexValue" label="性别" align="center" :sortable="contents.tableSortable"
                        :align="contents.tableAlign" />
                    <el-table-column prop="feizhiyuanEmail" label="游客邮箱" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign" />
                    <el-table-column label="操作" align="center" width="300">
                        <template #default="{ row }">
                            <el-button type="success" :icon="View" size="small"
                                @click="addOrUpdateHandler(row.id, 'info')"
                                :style="{ backgroundColor: contents.btnTableInfoBgColor, color: contents.btnTableInfoFontColor, fontSize: contents.btnTableInfoFontSize, borderWidth: contents.btnTableInfoBorderWidth, borderStyle: contents.btnTableInfoBorderStyle, borderColor: contents.btnTableInfoBorderColor, borderRadius: contents.btnTableInfoBorderRadius }">详情</el-button>
                            <el-button v-if="isAuth('feizhiyuan', '修改')" type="primary" :icon="Edit" size="small"
                                @click="addOrUpdateHandler(row.id)"
                                :style="{ backgroundColor: contents.btnTableUpdateBgColor, color: contents.btnTableUpdateFontColor, fontSize: contents.btnTableUpdateFontSize, borderWidth: contents.btnTableUpdateBorderWidth, borderStyle: contents.btnTableUpdateBorderStyle, borderColor: contents.btnTableUpdateBorderColor, borderRadius: contents.btnTableUpdateBorderRadius }">修改</el-button>
                            <el-button v-if="isAuth('feizhiyuan', '删除')" type="danger" :icon="Delete" size="small"
                                @click="deleteHandler(row.id)"
                                :style="{ backgroundColor: contents.btnTableDelBgColor, color: contents.btnTableDelFontColor, fontSize: contents.btnTableDelFontSize, borderWidth: contents.btnTableDelBorderWidth, borderStyle: contents.btnTableDelBorderStyle, borderColor: contents.btnTableDelBorderColor, borderRadius: contents.btnTableDelBorderRadius }">删除</el-button>
                            <el-button v-if="isAuth('feizhiyuan', '修改')" type="success" :icon="Key" size="small"
                                @click="resetPassword(row.id)"
                                :style="{ backgroundColor: contents.btnTableResetBgColor, color: contents.btnTableResetFontColor, fontSize: contents.btnTableResetFontSize, borderWidth: contents.btnTableResetBorderWidth, borderStyle: contents.btnTableResetBorderStyle, borderColor: contents.btnTableResetBorderColor, borderRadius: contents.btnTableResetBorderRadius }">重置密码</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分页 -->
            <div class="pagination-content">
                <el-pagination v-model:current-page="pageIndex" v-model:page-size="pageSize"
                    :page-sizes="[10, 20, 50, 100]" :total="totalPage" :layout="layouts" @size-change="sizeChangeHandle"
                    @current-change="currentChangeHandle"
                    :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }"
                    :background="contents.pageBtnBG" />
            </div>
        </div>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList" />

        <!-- 报表弹窗 -->
        <el-dialog v-model="chartVisiable" title="报表" width="800px">
            <div id="chartRef" style="width: 100%; height: 400px;"></div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
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
    Key
} from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import AddOrUpdate from './add-or-update.vue'
import styleJs from '@/utils/style.js'
import * as echarts from 'echarts'

// 样式配置
const contents = ref(styleJs.listStyle())
const layouts = ref('total, sizes, prev, pager, next, jumper')

// 数据列表
const dataList = ref([])
const dataListLoading = ref(false)
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const showFlag = ref(true)
const dataListSelections = ref([])
const baseUrl = import.meta.env.VITE_API_URL || ''

// 查询条件
const searchForm = reactive({
    feizhiyuanName: '',
    feizhiyuanPhone: '',
    feizhiyuanIdNumber: ''
})

// 新增/修改弹窗
const addOrUpdateVisible = ref(false)
const addOrUpdate = ref(null)

// 报表相关
const chartVisiable = ref(false)
const chartRef = ref(null)

// 表格样式
const headerRowStyle = () => {
    return {
        color: contents.value.tableHeaderFontColor,
        fontSize: contents.value.tableHeaderFontSize,
        backgroundColor: contents.value.tableHeaderBgColor
    }
}

const headerCellStyle = () => {
    return {
        color: contents.value.tableHeaderFontColor,
        fontSize: contents.value.tableHeaderFontSize,
        backgroundColor: contents.value.tableHeaderBgColor
    }
}

const rowStyle = () => {
    return {
        color: contents.value.tableContentFontColor,
        fontSize: contents.value.tableContentFontSize,
        backgroundColor: contents.value.tableContentBgColor
    }
}

const cellStyle = () => {
    return {
        color: contents.value.tableContentFontColor,
        fontSize: contents.value.tableContentFontSize,
        backgroundColor: contents.value.tableContentBgColor
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
const getDataList = () => {
    dataListLoading.value = true
    http.get('feizhiyuan/page', {
        params: {
            page: pageIndex.value,
            limit: pageSize.value,
            feizhiyuanName: searchForm.feizhiyuanName,
            feizhiyuanPhone: searchForm.feizhiyuanPhone,
            feizhiyuanIdNumber: searchForm.feizhiyuanIdNumber
        }
    }).then(({ data }) => {
        if (data && data.code === 0) {
            dataList.value = data.data.list
            totalPage.value = data.data.total
        } else {
            dataList.value = []
            totalPage.value = 0
        }
        dataListLoading.value = false
    })
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
    showFlag.value = false
    addOrUpdateVisible.value = true
    nextTick(() => {
        addOrUpdate.value.init(id, type)
    })
}

// 删除
const deleteHandler = (id) => {
    const ids = id ? [id] : dataListSelections.value.map(item => item.id)
    if (!ids.length) {
        ElMessage.warning('请选择要删除的数据')
        return
    }
    ElMessageBox.confirm('确定要删除选中的数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        http.post('feizhiyuan/delete', ids).then(({ data }) => {
            if (data && data.code === 0) {
                ElMessage.success('删除成功')
                getDataList()
            } else {
                ElMessage.error(data.msg)
            }
        })
    })
}

// 重置密码
const resetPassword = (id) => {
    ElMessageBox.confirm('确定要重置密码吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        http.get(`feizhiyuan/resetPassword/${id}`).then(({ data }) => {
            if (data && data.code === 0) {
                ElMessage.success('重置密码成功')
            } else {
                ElMessage.error(data.msg)
            }
        })
    })
}

// 导出
const exportExcel = () => {
    window.location.href = `${baseUrl}feizhiyuan/export`
}

// 报表
const chartDialog = () => {
    chartVisiable.value = true
    nextTick(() => {
        const myChart = echarts.init(document.getElementById('chartRef'))
        http.get('feizhiyuan/chart').then(({ data }) => {
            if (data && data.code === 0) {
                const option = {
                    title: {
                        text: '游客性别统计',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男', '女']
                    },
                    series: [
                        {
                            name: '性别分布',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            data: [
                                { value: data.data.male, name: '男' },
                                { value: data.data.female, name: '女' }
                            ],
                            emphasis: {
                                itemStyle: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                }
                myChart.setOption(option)
            }
        })
    })
}

// 批量导入成功
const feizhiyuanUploadSuccess = (response) => {
    if (response && response.code === 0) {
        ElMessage.success('导入成功')
        getDataList()
    } else {
        ElMessage.error(response.msg)
    }
}

// 批量导入失败
const feizhiyuanUploadError = () => {
    ElMessage.error('导入失败')
}

// 判断权限
const isAuth = (tableName, buttonName) => {
    return true // 这里可以根据实际需求判断权限
}

onMounted(() => {
    contentPageStyleChange()
    getDataList()
})
</script>

<style lang="scss" scoped>
.main-content {
    padding: 20px;
}

.form-content {
    margin-bottom: 20px;
}

.table-content {
    margin-top: 20px;
}

.pagination-content {
    margin-top: 20px;
    text-align: right;
}

:deep(.el-pagination__sizes) {
    margin-right: 0;
}

.slt {
    margin-bottom: 20px;
    display: flex;
}

.ad {
    margin-bottom: 20px;
    display: flex;
}

.pages {
    & :deep(.el-pagination__sizes) {
        & :deep(.el-input__inner) {
            height: 22px;
            line-height: 22px;
        }
    }
}

.el-button+.el-button {
    margin: 0;
}

.tables :deep(.el-button--success) {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables :deep(.el-button--primary) {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables :deep(.el-button--danger) {
    height: 40px;
    color: var(--publicSubColor);
    font-size: 14px;
    border-width: 4px;
    border-style: solid;
    border-color: var(--publicSubColor);
    border-radius: 20px 20px 20px 0;
    background-color: rgba(255, 255, 255, 0);
}

.tables :deep(.el-button) {
    margin: 4px;
}

.tables :deep(.el-table__body tr) {
    background-color: #f5f5f5 !important;
    color: #606266 !important;
}

.tables :deep(.el-table__body tr.el-table__row--striped td) {
    background: transparent;
}

.tables :deep(.el-table__body tr.el-table__row--striped) {
    background-color: #F5F7FA !important;
    color: #606266 !important;
}

.tables :deep(.el-table__body tr:hover>td) {
    background-color: #f5f5f5 !important;
    color: #333 !important;
}

.app-container {
    padding: 20px;

    .el-pagination {
        margin-top: 20px;
        text-align: right;

        :deep(.el-pagination__sizes) {
            margin-right: 0;
        }
    }
}
</style>