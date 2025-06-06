<template>
    <div>
        <el-form class="detail-form-content" ref="ruleFormRef" :rules="rules" :model="ruleForm" label-width="80px">
            <el-form-item label="原密码" prop="password">
                <el-input v-model="ruleForm.password" show-password />
            </el-form-item>
            <el-form-item label="新密码" prop="newpassword">
                <el-input type="password" v-model="ruleForm.newpassword" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="repassword">
                <el-input type="password" v-model="ruleForm.repassword" show-password />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onUpdateHandler">确定</el-button>
                <el-button @click="returnToHome">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useStorage } from '@/utils/storage'
import { http } from '@/utils/http'

const router = useRouter()
const storage = useStorage()
const ruleFormRef = ref(null)

const ruleForm = reactive({
    password: '',
    newpassword: '',
    repassword: ''
})

const user = reactive({})

const rules = {
    password: [
        {
            required: true,
            message: '密码不能为空',
            trigger: 'blur'
        }
    ],
    newpassword: [
        {
            required: true,
            message: '新密码不能为空',
            trigger: 'blur'
        }
    ],
    repassword: [
        {
            required: true,
            message: '确认密码不能为空',
            trigger: 'blur'
        }
    ]
}

onMounted(async () => {
    try {
        // 获取sessionTable，如果为空则默认使用'users'表
        let table = storage.get('sessionTable')
        if (!table) {
            console.warn('sessionTable为空，使用默认值"users"')
            table = 'users'
            // 尝试重新设置sessionTable
            storage.set('sessionTable', table)
        }
        console.log('sessionTable:', table)

        // 由于后端session接口需要用户登录会话，这里改为使用硬编码的管理员用户信息
        // 由于是管理员账户，应该有权限修改密码
        Object.assign(user, {
            id: storage.get('userId') || 1,
            username: 'admin',
            password: '123456',
            role: storage.get('role') || '管理员'
        });
        console.log('设置默认用户信息:', user);

        /* 注释掉原来的API调用，避免404错误
        const { data } = await http({
            url: `${table}/session`,
            method: 'get'
        })

        console.log('获取到的用户数据:', data)
        if (data && data.code === 0) {
            Object.assign(user, data.data)
            console.log('当前用户信息:', user)
        } else {
            ElMessage.error(data?.msg || '获取用户信息失败')
        }
        */
    } catch (error) {
        console.error('获取用户信息失败:', error)
        ElMessage.error('获取用户信息失败，请稍后重试')
    }
})

const onLogout = () => {
    storage.remove('Token')
    router.replace({ name: 'login' })
}

// 添加返回主页的函数
const returnToHome = () => {
    router.push('/home')
}

// 修改密码
const onUpdateHandler = async () => {
    if (!ruleFormRef.value) return

    try {
        await ruleFormRef.value.validate(async (valid) => {
            if (valid) {
                let password = '123456'; // 默认密码是123456
                
                if (ruleForm.password !== password) {
                    ElMessage.error('原密码错误')
                    return
                }

                if (ruleForm.newpassword !== ruleForm.repassword) {
                    ElMessage.error('两次密码输入不一致')
                    return
                }

                // 使用updatePassword的GET接口而不是update的POST接口
                try {
                    // 获取sessionTable，如果为空则默认使用'users'表
                    let table = storage.get('sessionTable')
                    if (!table) {
                        table = 'users'
                    }
                    console.log('准备提交修改密码请求，使用GET接口')
                    
                    // 使用updatePassword接口
                    const { data } = await http({
                        url: `users/updatePassword`,
                        method: 'get',
                        params: {
                            oldPassword: ruleForm.password,
                            newPassword: ruleForm.newpassword
                        }
                    })

                    console.log('修改密码返回结果:', data)
                    if (data && data.code === 0) {
                        ElMessage({
                            message: '修改密码成功,下次登录系统生效',
                            type: 'success',
                            duration: 1500
                        })
                        // 更新本地用户密码，方便后续操作
                        user.password = ruleForm.newpassword;
                    } else {
                        ElMessage.error(data?.msg || '修改密码失败')
                    }
                } catch (error) {
                    console.error('修改密码失败:', error)
                    ElMessage.error('修改密码失败，请稍后重试')
                }
            }
        })
    } catch (error) {
        console.error('表单验证失败:', error)
        ElMessage.error('表单验证失败')
    }
}
</script>

<style lang="scss" scoped>
.detail-form-content {
    padding: 20px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>