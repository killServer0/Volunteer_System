<template>
  <div class="file-upload">
    <el-upload class="upload-demo" :action="uploadUrl" :headers="headers" :on-success="handleSuccess"
      :on-error="handleError" :before-upload="beforeUpload" :show-file-list="showFileList" :multiple="multiple"
      :limit="limit" :file-list="fileList" :on-exceed="handleExceed" :on-remove="handleRemove">
      <el-button type="primary">点击上传</el-button>
      <template #tip>
        <div class="el-upload__tip">
          {{ tip }}
        </div>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'

const props = defineProps({
  value: {
    type: String,
    default: ''
  },
  uploadUrl: {
    type: String,
    default: '/api/file/upload'
  },
  showFileList: {
    type: Boolean,
    default: true
  },
  multiple: {
    type: Boolean,
    default: false
  },
  limit: {
    type: Number,
    default: 1
  },
  tip: {
    type: String,
    default: '只能上传jpg/png文件，且不超过500kb'
  }
})

const emit = defineEmits(['update:value', 'success', 'error'])

const fileList = ref([])
const headers = computed(() => ({
  token: getToken()
}))

const handleSuccess = (response, file) => {
  if (response.code === 0) {
    emit('update:value', response.data)
    emit('success', response.data)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const handleError = (error) => {
  emit('error', error)
  ElMessage.error('上传失败')
}

const beforeUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt500K = file.size / 1024 < 500

  if (!isImage) {
    ElMessage.error('只能上传jpg/png文件!')
    return false
  }
  if (!isLt500K) {
    ElMessage.error('文件大小不能超过500kb!')
    return false
  }
  return true
}

const handleExceed = (files) => {
  ElMessage.warning(`最多只能上传${props.limit}个文件`)
}

const handleRemove = (file) => {
  emit('update:value', '')
}
</script>

<style scoped>
.file-upload {
  width: 100%;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 7px;
}
</style>