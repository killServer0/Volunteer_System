<template>
  <el-dialog :title="!dataForm.id ? '新增' : type === 'info' ? '详情' : '修改'" v-model="visible" width="500px"
    :close-on-click-modal="false" :style="contents.dialogStyle">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRule" label-width="80px" @keyup.enter="dataFormSubmit()"
      :style="contents.formStyle">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="账户" prop="username">
            <el-input v-model="dataForm.username" placeholder="账户" :disabled="type === 'info'"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" prop="password" v-if="!dataForm.id">
            <el-input v-model="dataForm.password" placeholder="密码" type="password"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="zhiyuanzheName">
            <el-input v-model="dataForm.zhiyuanzheName" placeholder="姓名" :disabled="type === 'info'"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="zhiyuanzhePhone">
            <el-input v-model="dataForm.zhiyuanzhePhone" placeholder="手机号" :disabled="type === 'info'"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身份证号" prop="zhiyuanzheIdNumber">
            <el-input v-model="dataForm.zhiyuanzheIdNumber" placeholder="身份证号" :disabled="type === 'info'"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="sexTypes">
            <el-select v-model="dataForm.sexTypes" placeholder="性别" :disabled="type === 'info'"
              :style="contents.selectStyle">
              <el-option label="男" :value="1"></el-option>
              <el-option label="女" :value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="头像" prop="zhiyuanzhePhoto">
            <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :show-file-list="false"
                :on-success="handleUploadSuccess"
                :on-error="handleUploadError"
                :before-upload="beforeUpload"
                :headers="uploadHeaders"
                name="file">
                <img v-if="dataForm.zhiyuanzhePhoto" :src="getImageUrl(dataForm.zhiyuanzhePhoto)" class="avatar" @error="handleImageError" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="邮箱" prop="zhiyuanzheEmail">
            <el-input v-model="dataForm.zhiyuanzheEmail" placeholder="邮箱" :disabled="type === 'info'"
              :style="contents.inputStyle"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="visible = false" :style="contents.buttonStyle">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" v-if="type !== 'info'"
        :style="contents.buttonStyle">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import { styleJs } from '@/utils/style'

const visible = ref(false)
const dataFormRef = ref(null)
const type = ref('')
const contents = reactive(styleJs.addStyle())

const dataForm = reactive({
  id: 0,
  username: '',
  password: '',
  zhiyuanzheName: '',
  zhiyuanzhePhone: '',
  zhiyuanzheIdNumber: '',
  zhiyuanzhePhoto: '',
  sexTypes: '',
  zhiyuanzheEmail: ''
})

const dataRule = {
  username: [
    { required: true, message: '账户不能为空', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  zhiyuanzheName: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2-20个字符之间', trigger: 'blur' }
  ],
  zhiyuanzhePhone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  zhiyuanzheIdNumber: [
    { required: true, message: '身份证号不能为空', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  sexTypes: [
    { required: true, message: '性别不能为空', trigger: 'change' }
  ],
  zhiyuanzheEmail: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

// 初始化
const init = (id, t) => {
  console.log('初始化表单，ID:', id, '类型:', t)
  dataForm.id = id || 0
  type.value = t || ''
  visible.value = true
  if (dataForm.id) {
    console.log('获取志愿者详情，ID:', dataForm.id)
    http.get(`/zhiyuanzhe/info/${dataForm.id}`).then(({ data }) => {
      console.log('获取到的志愿者详情:', data)
      if (data && data.code === 0) {
        // 处理图片路径，但保留upload/前缀
        const formData = { ...data.data }
        console.log('原始表单数据:', formData)
        // 如果没有图片或undefined，设置为空字符串
        if (!formData.zhiyuanzhePhoto || formData.zhiyuanzhePhoto === 'undefined') {
          formData.zhiyuanzhePhoto = ''
        }
        console.log('处理后的表单数据:', formData)
        Object.assign(dataForm, formData)
        console.log('最终表单数据:', dataForm)
      }
    }).catch(error => {
      console.error('获取志愿者详情失败:', error)
      ElMessage.error('获取志愿者详情失败')
    })
  }
}

// 表单提交
const dataFormSubmit = () => {
  dataFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 处理图片路径，不再移除upload/前缀
        const submitForm = { ...dataForm }
        // 如果没有图片，则设置为空字符串
        if (!submitForm.zhiyuanzhePhoto || submitForm.zhiyuanzhePhoto === 'undefined') {
          submitForm.zhiyuanzhePhoto = ''
        }
        
        console.log('提交的表单数据:', submitForm)
        const { data } = await http({
          url: !submitForm.id ? '/zhiyuanzhe/save' : '/zhiyuanzhe/update',
          method: 'post',
          data: submitForm
        })

        if (data && data.code === 0) {
          ElMessage({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              visible.value = false
              emit('refreshDataList')
            }
          })
        } else {
          ElMessage.error(data.msg)
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败')
      }
    }
  })
}

// 上传相关
const uploadUrl = 'http://localhost:8081/zhiyuanzheguanlixitong/file/upload'
const uploadHeaders = {
    'X-Requested-With': 'XMLHttpRequest'
}

const beforeUpload = (file) => {
    console.log('准备上传文件:', file)
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
    console.log('文件验证通过，准备上传')
    return true
}

const handleUploadSuccess = (res, file) => {
    console.log('上传成功，完整响应:', res)
    console.log('上传的文件信息:', file)
    if (res.code === 0) {
        // 直接使用服务器返回的完整路径（包含upload/前缀）
        dataForm.zhiyuanzhePhoto = res.file
        console.log('设置图片路径:', dataForm.zhiyuanzhePhoto)
        ElMessage.success('上传成功')
    } else {
        console.error('上传失败，服务器返回:', res)
        ElMessage.error(res.msg || '上传失败')
    }
}

const handleUploadError = (error, file) => {
    console.error('上传错误:', error)
    console.error('上传失败的文件:', file)
    console.error('错误详情:', {
        status: error.status,
        statusText: error.statusText,
        response: error.response,
        message: error.message
    })
    ElMessage.error('上传失败: ' + (error.message || '未知错误'))
}

// 获取图片URL
const getImageUrl = (photoPath) => {
    console.log('获取图片URL，原始路径:', photoPath)
    if (!photoPath || photoPath === 'undefined') {
        console.log('使用默认图片')
        return 'https://via.placeholder.com/100x100?text=No+Image'
    }
    
    if (photoPath.startsWith('http')) {
        console.log('使用完整URL:', photoPath)
        return photoPath
    }
    
    // 从路径中提取文件名，考虑到可能的upload/前缀
    let fileName = photoPath
    if (photoPath.startsWith('upload/')) {
        fileName = photoPath.substring(7)
    }
    
    const finalUrl = `http://localhost:8081/zhiyuanzheguanlixitong/file/download?fileName=${fileName}`
    console.log('构建的下载URL:', finalUrl)
    return finalUrl
}

// 处理图片加载错误
const handleImageError = (e) => {
    console.error('图片加载失败:', e)
    console.error('图片URL:', e.target.src)
    e.target.src = 'https://via.placeholder.com/100x100?text=No+Image'
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

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;

    &:hover {
      border-color: #409EFF;
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>