<template>
  <div>
    <el-upload ref="upload" :action="getActionUrl" list-type="picture-card" accept=".xls,.xlsx" :limit="limit"
      :headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed" :on-preview="handleUploadPreview"
      :on-remove="handleRemove" :on-success="handleUploadSuccess" :on-error="handleUploadErr"
      :before-upload="handleBeforeUpload">
      <i class="el-icon-plus"></i>
      <div slot="tip" class="el-upload__tip" style="color:#838fa1;">{{ tip }}</div>
    </el-upload>
    <el-dialog v-model="dialogVisible" size="tiny" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import storage from '@/utils/storage'

const props = defineProps({
  tip: {
    type: String,
    default: ''
  },
  action: {
    type: String,
    default: 'file/upload'
  },
  limit: {
    type: Number,
    default: 1
  },
  multiple: {
    type: Boolean,
    default: false
  },
  fileUrls: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['change'])

const upload = ref(null)
const dialogVisible = ref(false)
const dialogImageUrl = ref('')
const fileList = ref([])
const fileUrlList = ref([])
const myHeaders = ref({
  'Token': storage.get('Token')
})

const getActionUrl = computed(() => {
  return `/${import.meta.env.VITE_APP_NAME}/${props.action}`
})

// 初始化
const init = () => {
  if (props.fileUrls) {
    fileUrlList.value = props.fileUrls.split(',')
    let fileArray = []
    fileUrlList.value.forEach((item, index) => {
      var url = item
      var name = index
      var file = {
        name: name,
        url: url
      }
      fileArray.push(file)
    })
    setFileList(fileArray)
  }
}

// 上传前校验
const handleBeforeUpload = () => {
  // 可以在这里添加文件类型、大小等校验
  return true
}

// 上传成功
const handleUploadSuccess = (res, file, fileList) => {
  if (res && res.code === 0) {
    fileList[fileList.length - 1].url = 'upload/' + file.response.file
    setFileList(fileList)
    emit('change', fileUrlList.value.join(','))
    ElMessage.success('文件导入成功')
  } else {
    ElMessage.error(res.msg)
  }
}

// 上传失败
const handleUploadErr = () => {
  ElMessage.error('文件导入失败')
}

// 移除文件
const handleRemove = (file, fileList) => {
  setFileList(fileList)
  emit('change', fileUrlList.value.join(','))
}

// 预览文件
const handleUploadPreview = (file) => {
  dialogImageUrl.value = file.url
  dialogVisible.value = true
}

// 超出文件数量限制
const handleExceed = () => {
  ElMessage.warning(`最多上传${props.limit}张图片`)
}

// 设置文件列表
const setFileList = (fileList) => {
  var fileArray = []
  var fileUrlArray = []
  var token = storage.get('token')
  fileList.forEach((item, index) => {
    var url = item.url.split('?')[0]
    if (!url.startsWith('http')) {
      url = import.meta.env.VITE_API_BASE_URL + url
    }
    var name = item.name
    var file = {
      name: name,
      url: url + '?token=' + token
    }
    fileArray.push(file)
    fileUrlArray.push(url)
  })
  fileList.value = fileArray
  fileUrlList.value = fileUrlArray
}

// 监听fileUrls变化
watch(() => props.fileUrls, init, { immediate: true })
</script>

<style lang="scss" scoped>
.el-upload__tip {
  line-height: 1.2;
}
</style>