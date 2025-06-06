<template>
    <div class="main-content">
        <!-- 列表页 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content"
                :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
                <el-row :gutter="20" class="slt"
                    :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item label="编码名称">
                        <el-input v-model="searchForm.indexName" placeholder="编码名称" clearable
                            :style="{ width: contents.inputWidth }"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="success" @click="search()"
                            :style="{ backgroundColor: contents.btnSearchBgColor, color: contents.btnSearchFontColor }">查询</el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad"
                    :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
                    <el-form-item>
                        <el-button v-if="isAuth('dictionaryFeihuodong', '新增')" type="success"
                            @click="addOrUpdateHandler()"
                            :style="{ backgroundColor: contents.btnAddBgColor, color: contents.btnAddFontColor }">新增</el-button>
                        <el-button v-if="isAuth('dictionaryFeihuodong', '删除')"
                            :disabled="dataListSelections.length <= 0" type="danger" @click="deleteHandler()"
                            :style="{ backgroundColor: contents.btnDelBgColor, color: contents.btnDelFontColor }">删除</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandler"
                    :style="{ backgroundColor: contents.tableBgColor, color: contents.tableFontColor }">
                    <el-table-column type="selection" header-align="center" align="center" width="50">
                    </el-table-column>
                    <el-table-column label="索引" type="index" width="50"></el-table-column>
                    <el-table-column prop="dicCode" label="字段"></el-table-column>
                    <el-table-column prop="dicName" label="字段名"></el-table-column>
                    <el-table-column prop="codeIndex" label="编码"></el-table-column>
                    <el-table-column prop="indexName" label="编码名称"></el-table-column>
                    <el-table-column prop="beizhu" label="备注"></el-table-column>
                    <el-table-column fixed="right" label="操作" width="300" header-align="center" align="center">
                        <template #default="scope">
                            <el-button v-if="isAuth('dictionaryFeihuodong', '查看')" type="success" size="small"
                                @click="addOrUpdateHandler(scope.row.id, 'info')"
                                :style="{ color: contents.btnInfoFontColor }">详情</el-button>
                            <el-button v-if="isAuth('dictionaryFeihuodong', '修改')" type="primary" size="small"
                                @click="addOrUpdateHandler(scope.row.id)"
                                :style="{ color: contents.btnEditFontColor }">修改</el-button>
                            <el-button v-if="isAuth('dictionaryFeihuodong', '删除')" type="danger" size="small"
                                @click="deleteHandler(scope.row.id)"
                                :style="{ color: contents.btnDelFontColor }">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                    :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                    layout="total, sizes, prev, pager, next, jumper" class="pagination-content"
                    :style="{ backgroundColor: contents.paginationBgColor, color: contents.paginationFontColor }">
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
import AddOrUpdate from './add-or-update.vue'
import { isAuth } from '@/utils/auth'
import { getDictionaryFeihuodongList, deleteDictionaryFeihuodong } from '@/api/dictionaryFeihuodong'
import styleJs from '@/utils/style.js'
import http from '@/utils/http'

const searchForm = reactive({
    indexName: ''
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
        const res = await http.get('/dictionaryFeihuodong/page', {
            params: {
                page: pageIndex.value,
                limit: pageSize.value,
                indexName: searchForm.indexName
            }
        })
        
        if (res.code === 0) {
            dataList.value = res.data.list
            totalPage.value = res.data.total
        } else {
            dataList.value = []
            totalPage.value = 0
        }
    } catch (error) {
        console.error('获取社区活动字典列表失败', error)
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
const selectionChangeHandler = (selections) => {
    dataListSelections.value = selections.map(item => item.id)
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
const deleteHandler = (id) => {
    const ids = id ? [id] : dataListSelections.value
    if (ids.length === 0) {
        ElMessage.warning('请选择要删除的数据')
        return
    }
    
    ElMessageBox.confirm('确定要删除选中的数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post('/dictionaryFeihuodong/delete', ids)
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getDataList()
            } else {
                ElMessage.error(res.msg || '删除失败')
            }
        } catch (error) {
            console.error('删除失败', error)
            ElMessage.error('操作失败')
        }
    }).catch(() => {})
}

// 样式系统
const initStyle = () => {
    contents.value = styleJs.listStyle()
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