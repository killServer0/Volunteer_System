<template>
    <div class="main-content">
        <!-- 搜索表单 -->
        <el-form :inline="true" :model="searchForm" class="form-content"
            :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
            <el-row :gutter="20" class="slt"
                :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
                <el-form-item :label="contents.inputTitle == 1 ? '公告类型' : ''">
                    <el-input v-model="searchForm.indexNameSearch" placeholder="公告类型" clearable
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
                    <el-button v-if="isAuth('dictionaryGonggao', '新增')" type="success" @click="addOrUpdateHandler()"
                        :style="{ backgroundColor: contents.btnAdAllAddBgColor, color: contents.btnAdAllAddFontColor, fontSize: contents.btnAdAllAddFontSize, borderWidth: contents.btnAdAllAddBorderWidth, borderStyle: contents.btnAdAllAddBorderStyle, borderColor: contents.btnAdAllAddBorderColor, borderRadius: contents.btnAdAllAddBorderRadius }">
                        <el-icon>
                            <Plus />
                        </el-icon>新增
                    </el-button>
                    <el-button v-if="isAuth('dictionaryGonggao', '删除')" :disabled="dataListSelections.length <= 0"
                        type="danger" @click="deleteHandler()"
                        :style="{ backgroundColor: contents.btnAdAllDelBgColor, color: contents.btnAdAllDelFontColor, fontSize: contents.btnAdAllDelFontSize, borderWidth: contents.btnAdAllDelBorderWidth, borderStyle: contents.btnAdAllDelBorderStyle, borderColor: contents.btnAdAllDelBorderColor, borderRadius: contents.btnAdAllDelBorderRadius }">
                        <el-icon>
                            <Delete />
                        </el-icon>删除
                    </el-button>
                </el-form-item>
            </el-row>
        </el-form>

        <!-- 数据表格 -->
        <el-table v-loading="dataListLoading" :data="dataList" border
            :style="{ width: '100%', fontSize: contents.tableContentFontSize, color: contents.tableContentFontColor }"
            :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :row-style="rowStyle"
            :cell-style="cellStyle" @selection-change="selectionChangeHandler">
            <el-table-column type="selection" width="55" align="center" v-if="contents.tableSelection" />
            <el-table-column label="索引" type="index" width="50" v-if="contents.tableIndex" />
            <el-table-column prop="codeIndex" label="公告类型编码" align="center" :sortable="contents.tableSortable"
                :align="contents.tableAlign" />
            <el-table-column prop="indexName" label="公告类型名称" align="center" :sortable="contents.tableSortable"
                :align="contents.tableAlign" />
            <el-table-column label="操作" width="300" align="center">
                <template #default="scope">
                    <el-button v-if="isAuth('dictionaryGonggao', '查看')" type="success" size="small"
                        @click="addOrUpdateHandler(scope.row.id, 'info')"
                        :style="{ backgroundColor: contents.btnInfoBgColor, color: contents.btnInfoFontColor, fontSize: contents.btnInfoFontSize, borderWidth: contents.btnInfoBorderWidth, borderStyle: contents.btnInfoBorderStyle, borderColor: contents.btnInfoBorderColor, borderRadius: contents.btnInfoBorderRadius }">
                        <el-icon>
                            <View />
                        </el-icon>详情
                    </el-button>
                    <el-button v-if="isAuth('dictionaryGonggao', '修改')" type="primary" size="small"
                        @click="addOrUpdateHandler(scope.row.id)"
                        :style="{ backgroundColor: contents.btnEditBgColor, color: contents.btnEditFontColor, fontSize: contents.btnEditFontSize, borderWidth: contents.btnEditBorderWidth, borderStyle: contents.btnEditBorderStyle, borderColor: contents.btnEditBorderColor, borderRadius: contents.btnEditBorderRadius }">
                        <el-icon>
                            <Edit />
                        </el-icon>修改
                    </el-button>
                    <el-button v-if="isAuth('dictionaryGonggao', '删除')" type="danger" size="small"
                        @click="deleteHandler(scope.row.id)"
                        :style="{ backgroundColor: contents.btnDelBgColor, color: contents.btnDelFontColor, fontSize: contents.btnDelFontSize, borderWidth: contents.btnDelBorderWidth, borderStyle: contents.btnDelBorderStyle, borderColor: contents.btnDelBorderColor, borderRadius: contents.btnDelBorderRadius }">
                        <el-icon>
                            <Delete />
                        </el-icon>删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination v-model:current-page="pageIndex" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
            :total="totalPage" :layout="layouts" @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
            class="pagination-content" :background="contents.pageBtnBG"
            :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }">
        </el-pagination>

        <!-- 添加/修改弹窗 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList">
        </add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, View, Edit } from '@element-plus/icons-vue'
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import { getDictionaryGonggaoList, deleteDictionaryGonggao } from '@/api/dictionaryGonggao'
import styleJs from '@/utils/style.js'

// 搜索表单
const searchForm = reactive({
    indexNameSearch: ''
})

// 数据列表
const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const dataListSelections = ref([])
const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref(null)
const contents = ref(null)
const layouts = ref('total, sizes, prev, pager, next, jumper')

// 获取数据列表
const getDataList = async () => {
    dataListLoading.value = true
    try {
        const params = {
            page: pageIndex.value,
            limit: pageSize.value,
            indexName: searchForm.indexNameSearch
        }
        const { data } = await getDictionaryGonggaoList(params)
        dataList.value = data.list
        totalPage.value = data.total
    } catch (error) {
        console.error('获取公告类型字典列表失败:', error)
        ElMessage.error('获取公告类型字典列表失败')
    } finally {
        dataListLoading.value = false
    }
}

// 搜索
const search = () => {
    pageIndex.value = 1
    getDataList()
}

// 多选
const selectionChangeHandler = (selection) => {
    dataListSelections.value = selection
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

// 新增 / 修改
const addOrUpdateHandler = (id, type) => {
    addOrUpdateVisible.value = true
    nextTick(() => {
        addOrUpdateRef.value.init(id, type)
    })
}

// 删除
const deleteHandler = async (id) => {
    const ids = id ? [id] : dataListSelections.value.map(item => item.id)
    try {
        await ElMessageBox.confirm('确定要删除选中的公告类型吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        await deleteDictionaryGonggao(ids)
        ElMessage.success('删除成功')
        getDataList()
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除公告类型失败:', error)
            ElMessage.error('删除公告类型失败')
        }
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