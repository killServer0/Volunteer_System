<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" width="50%" :close-on-click-modal="false" @close="closeDialog">
    <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="100px" :style="contents.formStyle">
      <el-form-item label="公告名称" prop="gonggaoName">
        <el-input v-model="dataForm.gonggaoName" placeholder="公告名称" :style="contents.inputStyle"></el-input>
      </el-form-item>
      <el-form-item label="公告类型" prop="gonggaoTypes">
        <el-select v-model="dataForm.gonggaoTypes" placeholder="请选择公告类型" :style="contents.selectStyle">
          <el-option v-for="item in gonggaoTypesOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="公告图片" prop="gonggaoPhoto">
        <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload">
            <img v-if="dataForm.gonggaoPhoto" :src="getImageUrl(dataForm.gonggaoPhoto)" class="avatar" @error="handleImageError" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="公告内容" prop="gonggaoContent">
        <el-input type="textarea" v-model="dataForm.gonggaoContent" placeholder="公告内容" :rows="5" :style="contents.textareaStyle"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { get, post, put } from '@/utils/http'
import styleJs from '@/utils/style.js'

// 定义emit
const emit = defineEmits(['refreshDataList'])

// 对话框可见性
const visible = ref(false)
const dataFormRef = ref(null)

// 样式配置
const contents = reactive(styleJs)

// 表单数据
const dataForm = reactive({
  id: null,
  gonggaoName: '',
  gonggaoTypes: '',
  gonggaoPhoto: '',
  gonggaoContent: '',
  insertTime: '',
  createTime: ''
})

// 表单验证规则
const dataRule = {
  gonggaoName: [
    { required: true, message: '公告名称不能为空', trigger: 'blur' }
  ],
  gonggaoTypes: [
    { required: true, message: '公告类型不能为空', trigger: 'change' }
  ],
  gonggaoContent: [
    { required: true, message: '公告内容不能为空', trigger: 'blur' }
  ]
}

// 公告类型选项
const gonggaoTypesOptions = ref([])

// 初始化
const init = async (id, type) => {
  console.log('Initializing component:', { id, type })
  dataForm.id = id || null
  visible.value = true
  
  // 获取公告类型选项
  try {
    const res = await get('dictionary/page', {
      dicCode: 'gonggao_types'
    })
    if (res.code === 0) {
      gonggaoTypesOptions.value = res.data.list.map(item => ({
        value: item.codeIndex,
        label: item.indexName,
        indexValue: item.codeIndex
      }))
      console.log('公告类型选项:', gonggaoTypesOptions.value)
    } else {
      ElMessage.error(res.msg || '获取公告类型失败')
  }
  } catch (error) {
    console.error('获取公告类型失败:', error)
    ElMessage.error('获取公告类型失败')
  }

  // 如果是修改或查看，获取详情
  if (id) {
    try {
      const res = await get(`gonggao/info/${id}`)
  if (res.code === 0) {
        // 只更新需要的字段
        dataForm.gonggaoName = res.data.gonggaoName || ''
        // 根据数值找到对应的选项
        const typeOption = gonggaoTypesOptions.value.find(option => option.indexValue === res.data.gonggaoTypes)
        dataForm.gonggaoTypes = typeOption ? typeOption.value : ''
        dataForm.gonggaoPhoto = res.data.gonggaoPhoto || ''
        dataForm.gonggaoContent = res.data.gonggaoContent || ''
        dataForm.insertTime = res.data.insertTime || ''
        dataForm.createTime = res.data.createTime || ''
        console.log('获取到的公告详情:', dataForm)
  } else {
        ElMessage.error(res.msg || '获取详情失败')
      }
    } catch (error) {
      console.error('获取详情失败:', error)
      ElMessage.error('获取详情失败')
  }
}

  // 如果是查看模式，禁用所有输入
  if (type === 'info') {
  nextTick(() => {
      if (dataFormRef.value) {
        const fields = dataFormRef.value.fields
        if (fields) {
          Object.keys(fields).forEach(key => {
            if (fields[key]) {
              fields[key].disabled = true
        }
      })
        }
    }
  })
  }
}

// 表单提交
const submitForm = async () => {
  if (!dataFormRef.value) return
  
  await dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 准备提交数据
        const submitData = {
          id: dataForm.id,
          gonggaoName: dataForm.gonggaoName,
          gonggaoTypes: dataForm.gonggaoTypes,
          gonggaoPhoto: dataForm.gonggaoPhoto,
          gonggaoContent: dataForm.gonggaoContent,
          insertTime: dataForm.insertTime,
          createTime: dataForm.createTime
        }

        const url = dataForm.id ? 'gonggao/update' : 'gonggao/add'
        const method = dataForm.id ? put : post
        console.log('Submitting data:', submitData)
        const res = await method(url, submitData)
        
        if (res.code === 0) {
          ElMessage.success(dataForm.id ? '修改成功' : '新增成功')
          visible.value = false
          emit('refreshDataList')
        } else {
          ElMessage.error(res.msg || (dataForm.id ? '修改失败' : '新增失败'))
        }
      } catch (error) {
        console.error(dataForm.id ? '修改失败:' : '新增失败:', error)
        ElMessage.error(dataForm.id ? '修改失败' : '新增失败')
      }
    }
  })
}

// 关闭弹窗
const closeDialog = () => {
  visible.value = false
  if (dataFormRef.value) {
    dataFormRef.value.resetFields()
  }
  Object.keys(dataForm).forEach(key => {
    dataForm[key] = key === 'gonggaoTypes' ? '' : null
  })
}

// 上传相关
const uploadUrl = 'http://localhost:8081/zhiyuanzheguanlixitong/file/upload'

const beforeUpload = (file) => {
    const isImage = file.type.startsWith('image/')
    if (!isImage) {
        ElMessage.error('只能上传图片文件!')
        return false
    }
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB!')
        return false
    }
    return true
}

const handleUploadSuccess = (res) => {
    console.log('上传成功，服务器返回:', res)
    if (res.code === 0) {
        dataForm.gonggaoPhoto = res.file
        console.log('设置图片路径:', dataForm.gonggaoPhoto)
        ElMessage.success('上传成功')
    } else {
        ElMessage.error(res.msg || '上传失败')
    }
}

// 获取图片URL
const getImageUrl = (photoPath) => {
    if (!photoPath || photoPath === 'undefined') {
        return 'https://via.placeholder.com/100x100?text=No+Image'
    }
    
    if (photoPath.startsWith('http')) {
        return photoPath
    }
    
    return `http://localhost:8081/zhiyuanzheguanlixitong/file/download?fileName=${photoPath.startsWith('upload/') ? photoPath.substring(7) : photoPath}`
}

// 处理图片加载错误
const handleImageError = (e) => {
    e.target.src = 'https://via.placeholder.com/100x100?text=No+Image'
}

// 暴露方法给父组件
defineExpose({
  init
})
</script>

<style scoped>
.avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 178px;
    height: 178px;
    display: flex;
    justify-content: center;
    align-items: center;
  }

.avatar-uploader:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
    width: 178px;
    height: 178px;
  text-align: center;
    line-height: 178px;
}

.avatar {
    width: 178px;
    height: 178px;
  display: block;
    object-fit: cover;
}

.dialog-footer {
    text-align: right;
}
</style>