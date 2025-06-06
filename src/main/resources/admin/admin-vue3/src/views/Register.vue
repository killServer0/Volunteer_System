<template>
    <div>
        <div class="container">
            <div class="login-form">
                <h1 class="h1">社区志愿者管理系统注册</h1>
                <el-form class="rgs-form" :model="ruleForm" ref="ruleFormRef">
                    <el-form-item label="账号" class="input">
                        <el-input v-model="ruleForm.username" autocomplete="off" placeholder="账号" />
                    </el-form-item>
                    <el-form-item label="密码" class="input">
                        <el-input type="password" v-model="ruleForm.password" autocomplete="off" show-password />
                    </el-form-item>
                    <el-form-item label="重复密码" class="input">
                        <el-input type="password" v-model="ruleForm.repetitionPassword" autocomplete="off"
                            show-password />
                    </el-form-item>

                    <!-- 游客表单项 -->
                    <template v-if="tableName === 'feizhiyuan'">
                        <el-form-item label="游客姓名" class="input">
                            <el-input v-model="ruleForm.feizhiyuanName" autocomplete="off" placeholder="游客姓名" />
                        </el-form-item>
                        <el-form-item label="游客手机号" class="input">
                            <el-input v-model="ruleForm.feizhiyuanPhone" autocomplete="off" placeholder="游客手机号" />
                        </el-form-item>
                        <el-form-item label="游客身份证号" class="input">
                            <el-input v-model="ruleForm.feizhiyuanIdNumber" autocomplete="off" placeholder="游客身份证号" />
                        </el-form-item>
                        <el-form-item label="游客邮箱" class="input">
                            <el-input v-model="ruleForm.feizhiyuanEmail" autocomplete="off" placeholder="游客邮箱" />
                        </el-form-item>
                    </template>

                    <!-- 志愿者表单项 -->
                    <template v-if="tableName === 'zhiyuanzhe'">
                        <el-form-item label="志愿者姓名" class="input">
                            <el-input v-model="ruleForm.zhiyuanzheName" autocomplete="off" placeholder="志愿者姓名" />
                        </el-form-item>
                        <el-form-item label="志愿者手机号" class="input">
                            <el-input v-model="ruleForm.zhiyuanzhePhone" autocomplete="off" placeholder="志愿者手机号" />
                        </el-form-item>
                        <el-form-item label="志愿者身份证号" class="input">
                            <el-input v-model="ruleForm.zhiyuanzheIdNumber" autocomplete="off" placeholder="志愿者身份证号" />
                        </el-form-item>
                        <el-form-item label="志愿者邮箱" class="input">
                            <el-input v-model="ruleForm.zhiyuanzheEmail" autocomplete="off" placeholder="志愿者邮箱" />
                        </el-form-item>
                    </template>

                    <div style="display: flex; flex-wrap: wrap; width: 100%; justify-content: center;">
                        <el-button class="btn" type="primary" @click="register">注册</el-button>
                        <el-button class="btn close" @click="close">取消</el-button>
                    </div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useStorage } from '@/utils/storage'
import { http } from '@/utils/http'
import { validate } from '@/utils/validate'

const router = useRouter()
const storage = useStorage()
const ruleFormRef = ref(null)

const tableName = ref('')
const ruleForm = reactive({
    username: '',
    password: '',
    repetitionPassword: '',
    feizhiyuanName: '',
    feizhiyuanPhone: '',
    feizhiyuanIdNumber: '',
    feizhiyuanEmail: '',
    zhiyuanzheName: '',
    zhiyuanzhePhone: '',
    zhiyuanzheIdNumber: '',
    zhiyuanzheEmail: ''
})

onMounted(() => {
    tableName.value = storage.get('loginTable')
})

const close = () => {
    router.push('/login')
}

const register = async () => {
    // 基本验证
    if (!ruleForm.username) {
        ElMessage.error('账号不能为空')
        return
    }
    if (!ruleForm.password) {
        ElMessage.error('密码不能为空')
        return
    }
    if (!ruleForm.repetitionPassword) {
        ElMessage.error('重复密码不能为空')
        return
    }
    if (ruleForm.password !== ruleForm.repetitionPassword) {
        ElMessage.error('密码和重复密码不一致')
        return
    }

    // 游客验证
    if (tableName.value === 'feizhiyuan') {
        if (!ruleForm.feizhiyuanName) {
            ElMessage.error('游客姓名不能为空')
            return
        }
        if (ruleForm.feizhiyuanPhone && !validate.isMobile(ruleForm.feizhiyuanPhone)) {
            ElMessage.error('手机应输入正确的手机格式')
            return
        }
        if (!ruleForm.feizhiyuanIdNumber) {
            ElMessage.error('游客身份证号不能为空')
            return
        }
        if (ruleForm.feizhiyuanEmail && !validate.isEmail(ruleForm.feizhiyuanEmail)) {
            ElMessage.error('邮箱应输入正确的邮件格式')
            return
        }
    }

    // 志愿者验证
    if (tableName.value === 'zhiyuanzhe') {
        if (!ruleForm.zhiyuanzheName) {
            ElMessage.error('志愿者姓名不能为空')
            return
        }
        if (ruleForm.zhiyuanzhePhone && !validate.isMobile(ruleForm.zhiyuanzhePhone)) {
            ElMessage.error('手机应输入正确的手机格式')
            return
        }
        if (!ruleForm.zhiyuanzheIdNumber) {
            ElMessage.error('志愿者身份证号不能为空')
            return
        }
        if (ruleForm.zhiyuanzheEmail && !validate.isEmail(ruleForm.zhiyuanzheEmail)) {
            ElMessage.error('邮箱应输入正确的邮件格式')
            return
        }
    }

    try {
        const { data } = await http({
            url: `${tableName.value}/register`,
            method: 'post',
            data: ruleForm
        })

        if (data && data.code === 0) {
            ElMessage({
                message: '注册成功,请登录后在个人中心页面补充个人数据',
                type: 'success',
                duration: 1500,
                onClose: () => {
                    router.replace('/login')
                }
            })
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请重试')
    }
}
</script>

<style lang="scss" scoped>
.container {
    height: 100vh;
    background-position: center center;
    background-size: cover;
    background-repeat: no-repeat;
    background-image: url(/zhiyuanzheguanlixitong/img/back-img-bg.jpg);

    .login-form {
        position: absolute;
        right: 50%;
        top: 50%;
        transform: translate3d(50%, -50%, 0);
        width: 420px;
        height: auto;
        padding: 15px;
        margin: 0 auto;
        border-radius: 10px;
        border-width: 4px;
        border-style: solid;
        border-color: var(--el-color-primary);
        background-color: rgba(255, 255, 255, 0.5);
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

        .h1 {
            width: 80%;
            height: auto;
            line-height: 1.5;
            color: #000;
            font-size: 28px;
            padding: 0;
            margin: 10px auto;
            text-align: center;
        }

        .rgs-form {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            .el-form-item {
                width: 100%;
                display: flex;
                margin-bottom: 12px;

                :deep(.el-form-item__content) {
                    flex: 1;
                    display: flex;
                }

                :deep(.el-form-item__label) {
                    width: 84px;
                    line-height: 44px;
                    color: #606266;
                    font-size: 14px;
                    padding: 0 10px 0 0;
                }

                :deep(.el-input__inner) {
                    width: 100%;
                    height: 44px;
                    line-height: 44px;
                    color: #606266;
                    font-size: 14px;
                    padding: 0 12px;
                    border-radius: 4px;
                    border: 1px solid #dcdfe6;
                    background-color: #fff;

                    &:focus {
                        border-color: var(--el-color-primary);
                    }
                }
            }

            .btn {
                margin: 0 10px;
                padding: 0;
                width: 88px;
                height: 44px;
                line-height: 44px;
                font-size: 14px;
                border-radius: 4px;

                &.close {
                    color: var(--el-color-primary);
                    border: 1px solid var(--el-color-primary);
                    background-color: #fff;
                }
            }
        }
    }
}
</style>