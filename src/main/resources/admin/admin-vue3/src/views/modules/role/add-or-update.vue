<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" :close-on-click-modal="false" width="500px"
    :style="contents.dialogStyle">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRule" label-width="80px" @keyup.enter="dataFormSubmit()"
      :style="contents.formStyle">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="dataForm.roleName" placeholder="角色名称" :style="contents.inputStyle"></el-input>
      </el-form-item>
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="dataForm.roleCode" placeholder="角色编码" :style="contents.inputStyle"></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="roleDesc">
        <el-input type="textarea" v-model="dataForm.roleDesc" placeholder="角色描述"
          :style="contents.textareaStyle"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false" :style="contents.buttonStyle">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :style="contents.buttonStyle">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style'

const contents = reactive(styleJs)

const visible = ref(false)
const dataFormRef = ref(null)
const dataForm = reactive({
  id: 0,
  roleName: '',
  roleCode: '',
  roleDesc: ''
})

const dataRule = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '角色名称长度必须在2到20个字符之间', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '角色编码不能为空', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '角色编码只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  roleDesc: [
    { max: 200, message: '角色描述不能超过200个字符', trigger: 'blur' }
  ]
}

// 初始化
const init = (id) => {
  dataForm.id = id || 0
  visible.value = true
  if (dataForm.id) {
    http.get(`role/info/${dataForm.id}`).then(({ data }) => {
      if (data && data.code === 0) {
        Object.assign(dataForm, data.data)
      }
    })
  }
}

// 表单提交
const dataFormSubmit = () => {
  dataFormRef.value.validate((valid) => {
    if (valid) {
      const url = !dataForm.id ? 'role/save' : 'role/update'
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