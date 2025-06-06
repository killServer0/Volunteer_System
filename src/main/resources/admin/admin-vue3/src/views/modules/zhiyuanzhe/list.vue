<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline" ref="queryForm">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="queryParams.zhiyuanzheName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.zhiyuanzhePhone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="queryParams.zhiyuanzheIdNumber" placeholder="请输入身份证号" clearable />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="queryParams.sexTypes" placeholder="请选择性别" clearable style="width: 120px">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" @click="batchDelete" :disabled="selectedRows.length === 0">批量删除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="volunteerList" border style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="zhiyuanzheName" label="姓名" width="120" />
      <el-table-column prop="zhiyuanzhePhone" label="手机号" width="120" />
      <el-table-column prop="zhiyuanzheIdNumber" label="身份证号" width="180" />
      <el-table-column prop="sexTypes" label="性别" width="80">
        <template #default="scope">
          {{ scope.row.sexTypes === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="zhiyuanzheEmail" label="邮箱" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="queryParams.page" v-model:page-size="queryParams.limit"
      :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" @close="handleClose">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="zhiyuanzheName">
          <el-input v-model="form.zhiyuanzheName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="zhiyuanzhePhone">
          <el-input v-model="form.zhiyuanzhePhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="zhiyuanzheIdNumber">
          <el-input v-model="form.zhiyuanzheIdNumber" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="性别" prop="sexTypes">
          <el-select v-model="form.sexTypes" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="zhiyuanzheEmail">
          <el-input v-model="form.zhiyuanzheEmail" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="头像" prop="zhiyuanzhePhoto">
          <el-upload class="avatar-uploader" action="/api/zhiyuanzhe/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" :headers="uploadHeaders">
            <img v-if="form.zhiyuanzhePhoto" :src="form.zhiyuanzhePhoto" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
          <div class="upload-tip">支持 jpg、png 格式，大小不超过 2MB</div>
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="form.id">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option :value="1" label="正常" />
            <el-option :value="2" label="审核中" />
            <el-option :value="3" label="禁用" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/utils/http'

const loading = ref(false)
const volunteerList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const selectedRows = ref([])

const queryParams = ref({
  page: 1,
  limit: 10,
  username: '',
  zhiyuanzheName: '',
  zhiyuanzhePhone: '',
  zhiyuanzheIdNumber: '',
  sexTypes: '',
  status: ''
})

const form = ref({
  id: '',
  username: '',
  password: '',
  zhiyuanzheName: '',
  zhiyuanzhePhone: '',
  zhiyuanzheIdNumber: '',
  zhiyuanzhePhoto: '',
  sexTypes: '',
  zhiyuanzheEmail: '',
  status: 1,
  role: 'zhiyuanzhe'
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  zhiyuanzheName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  zhiyuanzhePhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  zhiyuanzheIdNumber: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  zhiyuanzheEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' },
    { 
      validator: (rule, value, callback) => {
        const numValue = Number(value)
        if (![1, 2, 3].includes(numValue)) {
          callback(new Error('状态值无效'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
}

const isComponentMounted = ref(true)

const getList = async () => {
  if (!isComponentMounted.value) return
  
  try {
    loading.value = true
    const res = await http.get('/zhiyuanzhe/page', {
      params: {
        ...queryParams.value,
        page: queryParams.value.page - 1
      }
    })
    
    if (res.code === 0 && isComponentMounted.value) {
      console.log('获取到的列表数据:', res.data.list)
      
      volunteerList.value = res.data.list.map(item => ({
        ...item,
        status: Number(item.status || 1)
      }))
      
      console.log('处理后的列表数据:', volunteerList.value)
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取列表失败:', error)
  } finally {
    if (isComponentMounted.value) {
      loading.value = false
    }
  }
}

const handleQuery = () => {
  queryParams.value.page = 1
  getList()
}

const handleAdd = () => {
  dialogTitle.value = '新增志愿者'
  dialogVisible.value = true
  form.value = {
    id: '',
    username: '',
    password: '',
    zhiyuanzheName: '',
    zhiyuanzhePhone: '',
    zhiyuanzheIdNumber: '',
    zhiyuanzhePhoto: '',
    sexTypes: '',
    zhiyuanzheEmail: '',
    status: 1,
    role: 'zhiyuanzhe'
  }
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑志愿者'
  dialogVisible.value = true
  
  console.log('编辑前的数据:', row)
  
  form.value = {
    ...row,
    status: Number(row.status || 1)
  }
  
  console.log('编辑后的表单数据:', form.value)
}

const handleDelete = async (id) => {
  if (!isComponentMounted.value) return
  
  try {
    await ElMessageBox.confirm('确认要删除该记录吗？', '提示', {
      type: 'warning'
    })
    
    const res = await http.post('/zhiyuanzhe/delete', { ids: [id] })
    
    if (res.code === 0 && isComponentMounted.value) {
      ElMessage.success('删除成功')
      getList()
    } else if (isComponentMounted.value) {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel' && isComponentMounted.value) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleStatusChange = async (row) => {
  try {
    const response = await http.post('/zhiyuanzhe/update', {
      id: row.id,
      status: row.status
    })
    if (response.data.code === 0) {
      ElMessage.success('状态更新成功')
      getList()
    } else {
      ElMessage.error(response.data.msg || '状态更新失败')
      // 恢复原状态
      row.status = !row.status
    }
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败，请稍后重试')
    // 恢复原状态
    row.status = !row.status
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    const submitData = {
      ...form.value,
      status: Number(form.value.status || 1)
    }
    
    console.log('提交的表单数据:', submitData)
    
    const url = submitData.id ? '/zhiyuanzhe/update' : '/zhiyuanzhe/save'
    const res = await http.post(url, submitData)
    
    if (res.code === 0) {
      ElMessage.success(submitData.id ? '修改成功' : '添加成功')
      dialogVisible.value = false
      getList()
    }
  } catch (error) {
    console.error('表单提交失败:', error)
    ElMessage.error('操作失败')
  }
}

const handleSizeChange = (val) => {
  queryParams.value.limit = val
  getList()
}

const handleCurrentChange = (val) => {
  queryParams.value.page = val
  getList()
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const resetQuery = () => {
  queryParams.value = {
    page: 1,
    limit: 10,
    username: '',
    zhiyuanzheName: '',
    zhiyuanzhePhone: '',
    zhiyuanzheIdNumber: '',
    sexTypes: '',
    status: ''
  }
  getList()
}

const batchDelete = async () => {
  if (!isComponentMounted.value) return
  
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的记录')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确认要删除选中的 ${selectedRows.value.length} 条记录吗？`, '提示', {
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const res = await http.post('/zhiyuanzhe/delete', { ids })
    
    if (res.code === 0 && isComponentMounted.value) {
      ElMessage.success('删除成功')
      getList()
    } else if (isComponentMounted.value) {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel' && isComponentMounted.value) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

const handleAvatarSuccess = (response) => {
  if (response.code === 0) {
    form.value.zhiyuanzhePhoto = response.data
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(response.msg || '头像上传失败')
  }
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('头像只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

const handleClose = () => {
  dialogVisible.value = false
  formRef.value?.resetFields()
}

const getToken = () => {
  try {
    return localStorage.getItem('token') || sessionStorage.getItem('token') || ''
  } catch (error) {
    console.error('获取token失败:', error)
    return ''
  }
}

const uploadHeaders = computed(() => ({
  token: getToken()
}))

const getStatusType = (status) => {
  const statusNum = Number(status || 1)
  switch (statusNum) {
    case 1:
      return 'success'
    case 2:
      return 'warning'
    case 3:
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusText = (status) => {
  const statusNum = Number(status || 1)
  switch (statusNum) {
    case 1:
      return '正常'
    case 2:
      return '审核中'
    case 3:
      return '禁用'
    default:
      return '未知'
  }
}

// 监听状态值变化
watch(() => form.value.status, (newVal) => {
  if (newVal !== undefined) {
    form.value.status = Number(newVal)
  }
})

onMounted(() => {
  getList()
})

onBeforeUnmount(() => {
  isComponentMounted.value = false
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;

  .filter-container {
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    justify-content: flex-end;
  }
}

.el-select {
  width: 120px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>