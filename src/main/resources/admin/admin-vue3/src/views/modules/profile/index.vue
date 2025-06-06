<template>
    <div class="profile-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>个人信息</span>
                </div>
            </template>
            <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="userForm.username" disabled></el-input>
                </el-form-item>
                <el-form-item label="头像" prop="photo">
                    <el-upload class="avatar-uploader" action="file/upload" :show-file-list="false"
                        :before-upload="beforeAvatarUpload" :on-success="handleAvatarSuccess"
                        :on-error="handleAvatarError">
                        <img v-if="userForm.photo" :src="userForm.photo" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">保存修改</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { http } from '@/utils/http'

const userFormRef = ref(null)
const userForm = reactive({
    username: '',
    photo: ''
})

const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
    ]
}

// 文件上传前的验证
const beforeAvatarUpload = (file) => {
    const isImage = file.type.startsWith('image/')
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isImage) {
        ElMessage.error('上传文件只能是图片格式!')
        return false
    }
    if (!isLt2M) {
        ElMessage.error('上传图片大小不能超过 2MB!')
        return false
    }
    return true
}

// 上传成功
const handleAvatarSuccess = (res) => {
    if (res.code === 0) {
        userForm.photo = res.data.url
        ElMessage.success('上传成功')
    } else {
        ElMessage.error(res.msg || '上传失败')
    }
}

// 上传失败
const handleAvatarError = () => {
    ElMessage.error('上传失败')
}

// 获取用户信息
const getUserInfo = () => {
    http.get('users/session').then(({ data }) => {
        if (data && data.code === 0) {
            Object.assign(userForm, data.data)
        } else {
            ElMessage.error(data.msg || '获取用户信息失败')
        }
    })
}

// 提交表单
const submitForm = () => {
    userFormRef.value.validate((valid) => {
        if (valid) {
            http.post('users/update', userForm).then(({ data }) => {
                if (data && data.code === 0) {
                    ElMessage.success('修改成功')
                } else {
                    ElMessage.error(data.msg || '修改失败')
                }
            })
        }
    })
}

onMounted(() => {
    getUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-container {
    padding: 20px;

    .box-card {
        width: 480px;
        margin: 0 auto;
    }

    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .avatar-uploader {
        :deep(.el-upload) {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);

            &:hover {
                border-color: var(--el-color-primary);
            }
        }
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
    }
}
</style>