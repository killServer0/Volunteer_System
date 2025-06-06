<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    v-model="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      :rules="dataRule"
      label-width="80px"
      @keyup.enter="dataFormSubmit()"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="dataForm.username" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" v-if="!dataForm.id">
        <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword" v-if="!dataForm.id">
        <el-input v-model="dataForm.confirmPassword" type="password" placeholder="确认密码"></el-input>
      </el-form-item>
      <el-form-item label="角色" prop="roleId">
        <el-select v-model="dataForm.roleId" placeholder="请选择角色">
          <el-option
            v-for="item in roleList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'

const visible = ref(false)
const dataFormRef = ref(null)
const dataForm = reactive({
  id: 0,
  username: '',
  password: '',
  confirmPassword: '',
  roleId: ''
})
const roleList = ref([])
const dataRule = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '确认密码不能为空', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== dataForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  roleId: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

// 初始化
const init = (id) => {
  dataForm.id = id || 0
  visible.value = true
  nextTick(() => {
    dataFormRef.value?.resetFields()
    // 加载角色列表
    http.get('role/list').then(({ data }) => {
      if (data && data.code === 0) {
        roleList.value = data.data
        if (dataForm.id) {
          http.get(`user/info/${dataForm.id}`).then(({ data }) => {
            if (data && data.code === 0) {
              Object.assign(dataForm, data.data)
            }
          })
        }
      }
    })
  })
}

// 表单提交
const dataFormSubmit = () => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      const url = !dataForm.id ? 'user/save' : 'user/update'
      http.post(url, dataForm).then(({ data }) => {
        if (data && data.code === 0) {
          ElMessage.success('操作成功')
          visible.value = false
          emit('refreshDataList')
        } else {
          ElMessage.error(data.msg || '操作失败')
        }
      })
    }
  })
}

const emit = defineEmits(['refreshDataList'])

defineExpose({
  init
})
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-form {
    padding: 20px;
  }
}
</style> 