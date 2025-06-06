<template>
  <el-dialog
    title="分配角色"
    v-model="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-form
      ref="dataFormRef"
      :model="dataForm"
      label-width="80px"
    >
      <el-form-item label="用户名">
        <span>{{ dataForm.username }}</span>
      </el-form-item>
      <el-form-item label="角色">
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
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'

const visible = ref(false)
const dataFormRef = ref(null)
const dataForm = reactive({
  id: 0,
  username: '',
  roleId: ''
})
const roleList = ref([])

// 初始化
const init = (row) => {
  dataForm.id = row.id
  dataForm.username = row.username
  dataForm.roleId = row.roleId
  visible.value = true
  // 加载角色列表
  http.get('role/list').then(({ data }) => {
    if (data && data.code === 0) {
      roleList.value = data.data
    }
  })
}

// 表单提交
const dataFormSubmit = () => {
  http.post('user/role', {
    userId: dataForm.id,
    roleId: dataForm.roleId
  }).then(({ data }) => {
    if (data && data.code === 0) {
      ElMessage.success('操作成功')
      visible.value = false
      emit('refreshDataList')
    } else {
      ElMessage.error(data.msg || '操作失败')
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