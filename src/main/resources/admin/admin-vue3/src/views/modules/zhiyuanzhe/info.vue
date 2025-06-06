<template>
  <el-dialog
    title="志愿者详情"
    v-model="visible"
    width="500px"
    :close-on-click-modal="false"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="账户">{{ dataForm.username }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ dataForm.zhiyuanzheName }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ dataForm.zhiyuanzhePhone }}</el-descriptions-item>
      <el-descriptions-item label="身份证号">{{ dataForm.zhiyuanzheIdNumber }}</el-descriptions-item>
      <el-descriptions-item label="头像">
        <el-image
          style="width: 100px; height: 100px"
          :src="dataForm.zhiyuanzhePhoto"
          fit="cover"
        />
      </el-descriptions-item>
      <el-descriptions-item label="性别">
        <el-tag :type="dataForm.sexTypes === 1 ? 'success' : 'danger'">
          {{ dataForm.sexTypes === 1 ? '男' : '女' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ dataForm.zhiyuanzheEmail }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ dataForm.createTime }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { http } from '@/utils/http'

const visible = ref(false)
const dataForm = reactive({
  id: 0,
  username: '',
  zhiyuanzheName: '',
  zhiyuanzhePhone: '',
  zhiyuanzheIdNumber: '',
  zhiyuanzhePhoto: '',
  sexTypes: '',
  zhiyuanzheEmail: '',
  createTime: ''
})

// 初始化
const init = (id) => {
  dataForm.id = id
  visible.value = true
  http.get(`zhiyuanzhe/info/${dataForm.id}`).then(({ data }) => {
    if (data && data.code === 0) {
      Object.assign(dataForm, data.data)
    }
  })
}

defineExpose({
  init
})
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-descriptions {
    padding: 20px;
  }
}
</style> 