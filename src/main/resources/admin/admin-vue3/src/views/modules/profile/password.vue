<template>
    <div class="password-container">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>修改密码</span>
                </div>
            </template>
            <el-form :model="passwordForm" :rules="rules" ref="passwordFormRef" label-width="100px">
                <el-form-item label="原密码" prop="oldPassword">
                    <el-input v-model="passwordForm.oldPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="passwordForm.newPassword" type="password" show-password />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm">确认修改</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/http'

const router = useRouter()
const passwordFormRef = ref(null)

const passwordForm = ref({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
})

const validatePass = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入密码'))
    } else {
        if (passwordForm.value.confirmPassword !== '') {
            passwordFormRef.value.validateField('confirmPassword')
        }
        callback()
    }
}

const validatePass2 = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== passwordForm.value.newPassword) {
        callback(new Error('两次输入密码不一致!'))
    } else {
        callback()
    }
}

const rules = {
    oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    newPassword: [{ validator: validatePass, trigger: 'blur' }],
    confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
}

const submitForm = () => {
    passwordFormRef.value.validate((valid) => {
        if (valid) {
            request({
                url: '/user/updatePassword',
                method: 'post',
                data: {
                    oldPassword: passwordForm.value.oldPassword,
                    newPassword: passwordForm.value.newPassword
                }
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    ElMessage.success('密码修改成功')
                    router.push('/login')
                } else {
                    ElMessage.error(data.msg)
                }
            })
        }
    })
}
</script>

<style lang="scss" scoped>
.password-container {
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
}
</style>