<template>
  <div class="app-container">
    <el-form :inline="true" :model="dataForm" @keyup.enter="getDataList()" :style="contents.formStyle">
      <el-form-item>
        <el-input v-model="dataForm.roleName" placeholder="角色名称" clearable :style="contents.inputStyle"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()" :style="contents.buttonStyle">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()" :style="contents.buttonStyle">新增</el-button>
        <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0"
          :style="contents.buttonStyle">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="dataList" border v-loading="dataListLoading" @selection-change="selectionChangeHandle"
      style="width: 100%" :style="contents.tableStyle" :header-cell-style="contents.tableHeaderStyle"
      :cell-style="contents.tableCellStyle">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="roleName" header-align="center" align="center" label="角色名称"></el-table-column>
      <el-table-column prop="roleCode" header-align="center" align="center" label="角色编码"></el-table-column>
      <el-table-column prop="roleDesc" header-align="center" align="center" label="角色描述"></el-table-column>
      <el-table-column prop="createTime" header-align="center" align="center" label="创建时间"
        width="180"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="250" label="操作">
        <template #default="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)"
            :style="contents.buttonStyle">修改</el-button>
          <el-button type="text" size="small" @click="assignPermissionHandle(scope.row.id)"
            :style="contents.buttonStyle">分配权限</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)"
            :style="contents.buttonStyle">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle" :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper" :style="contents.paginationStyle"></el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
    <!-- 弹窗, 分配权限 -->
    <assign-permission v-if="assignPermissionVisible" ref="assignPermissionRef"></assign-permission>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style'
import AddOrUpdate from './add-or-update.vue'
import AssignPermission from './assign-permission.vue'

const contents = reactive(styleJs)
const dataForm = reactive({
  roleName: ''
})
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataList = ref([])
const dataListLoading = ref(false)
const dataListSelections = ref([])
const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref(null)
const assignPermissionVisible = ref(false)
const assignPermissionRef = ref(null)

// 获取数据列表
const getDataList = () => {
  dataListLoading.value = true
  http.get('role/page', {
    params: {
      page: pageIndex.value,
      limit: pageSize.value,
      roleName: dataForm.roleName ? '%' + dataForm.roleName + '%' : undefined
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
const selectionChangeHandle = (val) => {
  dataListSelections.value = val
}

// 新增 / 修改
const addOrUpdateHandle = (id) => {
  addOrUpdateVisible.value = true
  addOrUpdateRef.value?.init(id)
}

// 分配权限
const assignPermissionHandle = (id) => {
  assignPermissionVisible.value = true
  assignPermissionRef.value?.init(id)
}

// 删除
const deleteHandle = (id) => {
  const ids = id ? [id] : dataListSelections.value.map(item => item.id)
  ElMessageBox.confirm('确定要删除所选项吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    http.post('role/delete', ids).then(({ data }) => {
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