<template>
  <div class="app-container">
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item label="活动名称">
        <el-input v-model="searchForm.feihuodongName" placeholder="请输入活动名称" clearable />
      </el-form-item>
      <el-form-item label="活动类型">
        <el-select v-model="searchForm.feihuodongTypes" placeholder="请选择活动类型" clearable style="width: 200px">
          <el-option v-for="item in feihuodongTypesSelectSearch" :key="item.codeIndex" 
                  :label="item.indexName" :value="item.codeIndex">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="志愿者姓名">
        <el-input v-model="searchForm.zhiyuanzheName" placeholder="请输入志愿者姓名" clearable></el-input>
      </el-form-item>
      <el-form-item label="报名状态">
        <el-select v-model="searchForm.feihuodongYuyueYesnoTypes" placeholder="请选择报名状态" clearable style="width: 200px">
          <el-option v-for="item in feihuodongYuyueYesnoTypesSelectSearch" :key="item.codeIndex" 
                  :label="item.indexName" :value="item.codeIndex">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="feihuodongName" label="活动名称" width="180" />
      <el-table-column prop="feihuodongYuyueUuidNumber" label="报名编号" width="180" />
      <el-table-column prop="feihuodongYuyueText" label="报名理由" width="200" />
      <el-table-column prop="feihuodongYuyueYesnoTypes" label="报名状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.feihuodongYuyueYesnoTypes)">
            {{ getStatusText(scope.row.feihuodongYuyueYesnoTypes) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="feihuodongYuyueYesnoText" label="审核回复" width="200" />
      <el-table-column prop="feihuodongYuyueShenheTime" label="审核时间" width="180" />
      <el-table-column prop="insertTime" label="报名时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleUpdate(scope.row)">审核</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
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
      layout="total, sizes, prev, pager, next, jumper"
    />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="审核状态" prop="feihuodongYuyueYesnoTypes">
          <el-select v-model="form.feihuodongYuyueYesnoTypes" placeholder="请选择审核状态">
            <el-option label="待审核" value="1" />
            <el-option label="同意" value="2" />
            <el-option label="拒绝" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核回复" prop="feihuodongYuyueYesnoText">
          <el-input type="textarea" v-model="form.feihuodongYuyueYesnoText" placeholder="请输入审核回复" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/utils/http'

const dataListLoading = ref(false)
const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

const searchForm = reactive({
  feihuodongName: '',
  feihuodongTypes: '',
  zhiyuanzheName: '',
  feihuodongYuyueYesnoTypes: ''
})

const form = reactive({
  id: undefined,
  feihuodongYuyueYesnoTypes: '',
  feihuodongYuyueYesnoText: ''
})

const rules = {
  feihuodongYuyueYesnoTypes: [
    { required: true, message: '请选择审核状态', trigger: 'change' }
  ],
  feihuodongYuyueYesnoText: [
    { required: true, message: '请输入审核回复', trigger: 'blur' }
  ]
}

const getStatusType = (status) => {
  switch (Number(status)) {
    case 1:
      return 'info'
    case 2:
      return 'success'
    case 3:
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusText = (status) => {
  switch (Number(status)) {
    case 1:
      return '待审核'
    case 2:
      return '同意'
    case 3:
      return '拒绝'
    default:
      return '未知'
  }
}

const getDataList = async () => {
  try {
    dataListLoading.value = true
    console.log('开始获取数据列表，参数:', {
      page: pageIndex.value,
      limit: pageSize.value,
      ...searchForm
    })
    
    const res = await http.get('feihuodongYuyue/page', {
      params: {
        page: pageIndex.value,
        limit: pageSize.value,
        ...searchForm
      }
    })
    
    console.log('获取数据列表响应:', res)
    
    if (res.code === 0) {
      dataList.value = res.data.list
      totalPage.value = res.data.total
    } else {
      ElMessage.error(res.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据列表失败:', error)
    console.error('错误详情:', {
      message: error.message,
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data,
      config: error.config
    })
    ElMessage.error('获取数据列表失败: ' + (error.message || '未知错误'))
  } finally {
    dataListLoading.value = false
  }
}

const resetQuery = () => {
  searchForm.feihuodongName = ''
  searchForm.feihuodongTypes = ''
  searchForm.zhiyuanzheName = ''
  searchForm.feihuodongYuyueYesnoTypes = ''
  getDataList()
}

const sizeChangeHandle = (val) => {
  pageSize.value = val
  pageIndex.value = 1
  getDataList()
}

const currentChangeHandle = (val) => {
  pageIndex.value = val
  getDataList()
}

const handleUpdate = (row) => {
  dialogTitle.value = '审核报名'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认要删除该报名记录吗？', '提示', {
      type: 'warning'
    })
    console.log('开始删除记录，ID:', row.id)
    const res = await http.post('feihuodongYuyue/delete', [row.id])
    console.log('删除响应:', res)
    if (res.code === 0) {
      ElMessage.success('删除成功')
      getDataList()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      console.error('错误详情:', {
        message: error.message,
        status: error.response?.status,
        statusText: error.response?.statusText,
        data: error.response?.data,
        config: error.config
      })
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        console.log('开始提交表单数据:', form)
        const res = await http.post('feihuodongYuyue/update', form)
        console.log('提交响应:', res)
        if (res.code === 0) {
          ElMessage.success('操作成功')
          dialogVisible.value = false
          getDataList()
        } else {
          ElMessage.error(res.msg || '操作失败')
        }
      } catch (error) {
        console.error('操作失败:', error)
        console.error('错误详情:', {
          message: error.message,
          status: error.response?.status,
          statusText: error.response?.statusText,
          data: error.response?.data,
          config: error.config
        })
        ElMessage.error('操作失败: ' + (error.message || '未知错误'))
      }
    }
  })
}

const feihuodongTypesSelectSearch = ref([])
const feihuodongYuyueYesnoTypesSelectSearch = ref([])

const getOptions = async () => {
  try {
    // 获取报名状态选项
    const res1 = await http.get('dictionary/page', {
      params: {
        dicCode: 'feihuodong_yuyue_yesno_types'
      }
    })
    if (res1.code === 0) {
      feihuodongYuyueYesnoTypesSelectSearch.value = res1.data.list
    }

    // 获取活动类型选项
    const res2 = await http.get('dictionary/page', {
      params: {
        dicCode: 'feihuodong_types'
      }
    })
    if (res2.code === 0) {
      feihuodongTypesSelectSearch.value = res2.data.list
    }
  } catch (error) {
    console.error('获取选项数据失败:', error)
    ElMessage.error('获取选项数据失败')
  }
}

onMounted(() => {
  getDataList()
  getOptions()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.demo-form-inline {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.search-form :deep(.el-form-item) {
  margin-right: 20px;
}

.search-form :deep(.el-select) {
  width: 200px;
}

.operation-buttons {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.el-image {
  border-radius: 4px;
}

.el-tag {
  margin-right: 10px;
}
</style> 