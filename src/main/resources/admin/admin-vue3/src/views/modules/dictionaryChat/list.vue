<template>
    <div class="main-content">
        <!-- 列表页 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content"
                :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
                <el-row :gutter="20" class="slt"
                    :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item :label="contents.inputTitle == 1 ? '数据类型' : ''">
                        <el-input prefix-icon="el-icon-search" v-model="searchForm.indexNameSearch" placeholder="数据类型"
                            clearable></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="success" @click="search()">查询<i
                                class="el-icon-search el-icon--right" /></el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad"
                    :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item>
                        <el-button v-if="isAuth('dictionaryChat', '新增')" type="success" icon="el-icon-plus"
                            @click="addOrUpdateHandler()">新增</el-button>
                        <el-button v-if="isAuth('dictionaryChat', '删除')" :disabled="dataListSelections.length <= 0"
                            type="danger" icon="el-icon-delete" @click="deleteHandler()">删除</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
                    :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle"
                    :border="contents.tableBorder" :fit="contents.tableFit" :stripe="contents.tableStripe"
                    :row-style="rowStyle" :cell-style="cellStyle"
                    :style="{ width: '100%', fontSize: contents.tableContentFontSize, color: contents.tableContentFontColor }"
                    v-if="isAuth('dictionaryChat', '查看')" :data="dataList" v-loading="dataListLoading"
                    @selection-change="selectionChangeHandler">
                    <el-table-column v-if="contents.tableSelection" type="selection" header-align="center"
                        align="center" width="50">
                    </el-table-column>
                    <el-table-column label="索引" v-if="contents.tableIndex" type="index" width="50" />
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="codeIndex"
                        header-align="center" label="数据类型编码">
                        <template #default="scope">
                            {{ scope.row.codeIndex }}
                        </template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="indexName"
                        header-align="center" label="数据类型名称">
                        <template #default="scope">
                            {{ scope.row.indexName }}
                        </template>
                    </el-table-column>
                    <el-table-column width="300" :align="contents.tableAlign" header-align="center" label="操作">
                        <template #default="scope">
                            <el-button v-if="isAuth('dictionaryChat', '查看')" type="success" icon="el-icon-tickets"
                                size="mini" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
                            <el-button v-if="isAuth('dictionaryChat', '修改')" type="primary" icon="el-icon-edit"
                                size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
                            <el-button v-if="isAuth('dictionaryChat', '删除')" type="danger" icon="el-icon-delete"
                                size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination clsss="pages" :layout="layouts" @size-change="sizeChangeHandle"
                    @current-change="currentChangeHandle" :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]"
                    :page-size="Number(contents.pageEachNum)" :total="totalPage" :small="contents.pageStyle"
                    class="pagination-content" :background="contents.pageBtnBG"
                    :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }">
                </el-pagination>
            </div>
        </div>
        <!-- 添加/修改页面 -->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { isAuth } from '@/utils/auth'
import { getDictionaryList, deleteDictionary } from '@/api/dictionary'
import styleJs from '@/utils/style.js'
import AddOrUpdate from './add-or-update.vue'

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
        const { data } = await getDictionaryList({
            page: pageIndex.value,
            limit: pageSize.value,
            dicCode: 'chat_types',
            indexName: searchForm.indexNameSearch
        })
        dataList.value = data.list
        totalPage.value = data.total
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
        await ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        await deleteDictionary(ids)
        ElMessage.success('删除成功')
        getDataList()
    } catch (error) {
        console.error('删除失败:', error)
        ElMessage.error('删除失败')
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

.table-content {
    margin-top: 20px;
}

.pagination-content {
    margin-top: 20px;
    text-align: right;
}
</style>