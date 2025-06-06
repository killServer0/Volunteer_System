<template>
    <div class="main-content">
        <!-- 列表页 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content"
                :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
                <el-row :gutter="20" class="slt"
                    :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item :label="contents.inputTitle == 1 ? '论坛状态' : ''">
                        <el-input v-model="searchForm.indexNameSearch" placeholder="论坛状态" clearable
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="success" @click="search"
                            :style="{ backgroundColor: contents.searchBtnBgColor, color: contents.searchBtnFontColor, fontSize: contents.searchBtnFontSize, borderWidth: contents.searchBtnBorderWidth, borderStyle: contents.searchBtnBorderStyle, borderColor: contents.searchBtnBorderColor, borderRadius: contents.searchBtnBorderRadius }">
                            <el-icon>
                                <Search />
                            </el-icon>查询
                        </el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad"
                    :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item>
                        <el-button v-if="isAuth('dictionaryForumState', '新增')" type="success"
                            @click="addOrUpdateHandler()"
                            :style="{ backgroundColor: contents.btnAdAllAddBgColor, color: contents.btnAdAllAddFontColor, fontSize: contents.btnAdAllAddFontSize, borderWidth: contents.btnAdAllAddBorderWidth, borderStyle: contents.btnAdAllAddBorderStyle, borderColor: contents.btnAdAllAddBorderColor, borderRadius: contents.btnAdAllAddBorderRadius }">
                            <el-icon>
                                <Plus />
                            </el-icon>新增
                        </el-button>
                        <el-button v-if="isAuth('dictionaryForumState', '删除')"
                            :disabled="dataListSelections.length <= 0" type="danger" @click="deleteHandler()"
                            :style="{ backgroundColor: contents.btnAdAllDelBgColor, color: contents.btnAdAllDelFontColor, fontSize: contents.btnAdAllDelFontSize, borderWidth: contents.btnAdAllDelBorderWidth, borderStyle: contents.btnAdAllDelBorderStyle, borderColor: contents.btnAdAllDelBorderColor, borderRadius: contents.btnAdAllDelBorderRadius }">
                            <el-icon>
                                <Delete />
                            </el-icon>删除
                        </el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandler"
                    :style="{ width: '100%', fontSize: contents.tableContentFontSize, color: contents.tableContentFontColor }"
                    :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :row-style="rowStyle"
                    :cell-style="cellStyle">
                    <el-table-column type="selection" header-align="center" align="center" width="50"
                        v-if="contents.tableSelection">
                    </el-table-column>
                    <el-table-column label="索引" type="index" width="50" v-if="contents.tableIndex" />
                    <el-table-column label="论坛状态编码" prop="codeIndex" header-align="center" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                    </el-table-column>
                    <el-table-column label="论坛状态名称" prop="indexName" header-align="center" align="center"
                        :sortable="contents.tableSortable" :align="contents.tableAlign">
                    </el-table-column>
                    <el-table-column fixed="right" label="操作" width="300" header-align="center" align="center">
                        <template #default="scope">
                            <el-button v-if="isAuth('dictionaryForumState', '查看')" type="success" size="small"
                                @click="addOrUpdateHandler(scope.row.id, 'info')"
                                :style="{ backgroundColor: contents.btnInfoBgColor, color: contents.btnInfoFontColor, fontSize: contents.btnInfoFontSize, borderWidth: contents.btnInfoBorderWidth, borderStyle: contents.btnInfoBorderStyle, borderColor: contents.btnInfoBorderColor, borderRadius: contents.btnInfoBorderRadius }">
                                <el-icon>
                                    <View />
                                </el-icon>详情
                            </el-button>
                            <el-button v-if="isAuth('dictionaryForumState', '修改')" type="primary" size="small"
                                @click="addOrUpdateHandler(scope.row.id)"
                                :style="{ backgroundColor: contents.btnEditBgColor, color: contents.btnEditFontColor, fontSize: contents.btnEditFontSize, borderWidth: contents.btnEditBorderWidth, borderStyle: contents.btnEditBorderStyle, borderColor: contents.btnEditBorderColor, borderRadius: contents.btnEditBorderRadius }">
                                <el-icon>
                                    <Edit />
                                </el-icon>修改
                            </el-button>
                            <el-button v-if="isAuth('dictionaryForumState', '删除')" type="danger" size="small"
                                @click="deleteHandler(scope.row.id)"
                                :style="{ backgroundColor: contents.btnDelBgColor, color: contents.btnDelFontColor, fontSize: contents.btnDelFontSize, borderWidth: contents.btnDelBorderWidth, borderStyle: contents.btnDelBorderStyle, borderColor: contents.btnDelBorderColor, borderRadius: contents.btnDelBorderRadius }">
                                <el-icon>
                                    <Delete />
                                </el-icon>删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                    :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                    :layout="layouts" class="pagination-content"
                    :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }"
                    :background="contents.pageBtnBG">
                </el-pagination>
            </div>
        </div>
        <!-- 添加/修改页面 -->
        <add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, View, Edit, Upload, Download, Printer } from '@element-plus/icons-vue'
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import { getDictionaryList, deleteDictionary } from '@/api/dictionary'
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
const contents = ref(null)
const layouts = ref('total, sizes, prev, pager, next, jumper')

// 获取数据列表
const getDataList = async () => {
    dataListLoading.value = true
    try {
        const params = {
            page: pageIndex.value,
            limit: pageSize.value,
            dicCode: 'forum_state_types'
        }
        if (searchForm.indexNameSearch) {
            params.indexName = searchForm.indexNameSearch
        }
        const { data } = await getDictionaryList(params)
        dataList.value = data.list
        totalPage.value = data.total
    } catch (error) {
        console.error('获取数据列表失败:', error)
        ElMessage.error('获取数据列表失败')
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

// 多选
const selectionChangeHandler = (val) => {
    dataListSelections.value = val
}

// 搜索
const search = () => {
    pageIndex.value = 1
    getDataList()
}

// 新增 / 修改
const addOrUpdateHandler = (id, type) => {
    addOrUpdateFlag.value = true
    nextTick(() => {
        const addOrUpdate = document.querySelector('#add-or-update')
        if (addOrUpdate) {
            addOrUpdate.init(id, type)
        }
    })
}

// 删除
const deleteHandler = async (id) => {
    const ids = id ? [id] : dataListSelections.value.map(item => item.id)
    try {
        await ElMessageBox.confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
        })
        await deleteDictionary(ids)
        ElMessage.success("删除成功")
        getDataList()
    } catch (error) {
        console.error('删除失败:', error)
    }
}

// 样式系统
const initStyle = () => {
    contents.value = styleJs.listStyle()
}

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
                text: '论坛状态统计'
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
const getActionUrl = ref('http://localhost:8081/zhiyuanzheguanlixitong/upload/forumStateMuBan.xls')

const forumStateUploadSuccess = (response) => {
    if (response.code === 0) {
        ElMessage.success('导入成功')
        getDataList()
    } else {
        ElMessage.error(response.msg)
    }
}

const forumStateUploadError = () => {
    ElMessage.error('导入失败')
}

const exportExcel = () => {
    // 实现导出Excel功能
}

onMounted(() => {
    initStyle()
    getDataList()
})
</script>

<style scoped>
.main-content {
    padding: 20px;
}

.form-content {
    margin-bottom: 20px;
}

.slt {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.ad {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;
}

.table-content {
    margin-top: 20px;
}

.pagination-content {
    margin-top: 20px;
}
</style>