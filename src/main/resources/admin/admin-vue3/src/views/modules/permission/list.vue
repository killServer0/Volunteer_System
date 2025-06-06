<template>
    <div class="app-container">
        <el-form :inline="true">
            <el-form-item>
                <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="dataList" border row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" v-loading="dataListLoading"
            style="width: 100%">
            <el-table-column prop="name" header-align="center" align="left" label="权限名称"></el-table-column>
            <el-table-column prop="code" header-align="center" align="center" label="权限编码"></el-table-column>
            <el-table-column prop="type" header-align="center" align="center" label="类型" width="100">
                <template #default="scope">
                    <el-tag v-if="scope.row.type === 0" type="primary">模块</el-tag>
                    <el-tag v-else-if="scope.row.type === 1" type="success">页面</el-tag>
                    <el-tag v-else type="warning">按钮</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="url" header-align="center" align="center" label="权限路径"></el-table-column>
            <el-table-column prop="orderNum" header-align="center" align="center" label="排序号"
                width="100"></el-table-column>
            <el-table-column fixed="right" header-align="center" align="center" width="200" label="操作">
                <template #default="scope">
                    <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
                    <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
    </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'
import AddOrUpdate from './add-or-update.vue'

const dataList = ref([])
const dataListLoading = ref(false)
const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref(null)

// 获取数据列表
const getDataList = () => {
    dataListLoading.value = true
    http.get('permission/list').then(({ data }) => {
        if (data && data.code === 0) {
            dataList.value = data.data
        } else {
            dataList.value = []
        }
        dataListLoading.value = false
    })
}

// 新增 / 修改
const addOrUpdateHandle = (id) => {
    addOrUpdateVisible.value = true
    addOrUpdateRef.value?.init(id)
}

// 删除
const deleteHandle = (id) => {
    ElMessageBox.confirm('确定要删除该权限吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        http.post('permission/delete', [id]).then(({ data }) => {
            if (data && data.code === 0) {
                ElMessage.success('操作成功')
                getDataList()
            } else {
                ElMessage.error(data.msg || '操作失败')
            }
        })
    })
}

// 初始化
getDataList()
</script>

<style lang="scss" scoped>
.app-container {
    padding: 20px;
}
</style>