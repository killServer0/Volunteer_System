<template>
    <div class="addEdit-block">
        <el-form label-position="top" class="detail-form-content" ref="ruleFormRef" :model="ruleForm" :rules="rules">
            <el-form-item class="input" label="MySQL安装目录" prop="mysqlUrl">
                <el-input clearable placeholder="MySQL安装目录" v-model="ruleForm.mysqlUrl"></el-input>
            </el-form-item>
            <el-form-item class="input" label="MySQL数据库所在服务器地址IP" prop="hostIP">
                <el-input clearable placeholder="MySQL数据库所在服务器地址IP" v-model="ruleForm.hostIP"></el-input>
            </el-form-item>
            <el-form-item class="input" label="进入数据库所需要的用户名" prop="userName">
                <el-input clearable placeholder="进入数据库所需要的用户名" v-model="ruleForm.userName"></el-input>
            </el-form-item>
            <el-form-item class="input" label="数据库端口" prop="hostPort">
                <el-input clearable placeholder="数据库端口" v-model="ruleForm.hostPort"></el-input>
            </el-form-item>
            <el-form-item class="input" label="进入数据库所需要的密码" prop="password">
                <el-input clearable placeholder="进入数据库所需要的密码" v-model="ruleForm.password" type="password"></el-input>
            </el-form-item>
            <el-form-item class="input" label="数据库文件保存路径" prop="savePath">
                <el-input clearable placeholder="数据库还原文件保存路径" v-model="ruleForm.savePath"></el-input>
            </el-form-item>
            <el-form-item class="input" label="数据库还原文件文件名(.sql结尾)" prop="fileName">
                <el-input clearable placeholder="数据库还原文件文件名" v-model="ruleForm.fileName"></el-input>
            </el-form-item>
            <el-form-item class="input" label="要还原的数据库名" prop="databaseName">
                <el-input clearable placeholder="要还原的数据库名" v-model="ruleForm.databaseName"></el-input>
            </el-form-item>
            
            <div class="warning-box">
                <el-alert
                    title="数据库恢复操作将覆盖现有数据，请确保您已充分了解操作后果！"
                    type="warning"
                    :closable="false"
                    show-icon
                />
            </div>
            
            <el-form-item class="btn">
                <el-button type="primary" class="btn-success" @click="huanyuan" :loading="loading">恢复数据库</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineProps, defineEmits } from 'vue'
import { ElMessage, ElLoading, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'

// 定义属性
const props = defineProps({
    fileName: String
})

// 定义事件
const emits = defineEmits(['success'])

const ruleFormRef = ref()
const loading = ref(false)

const ruleForm = reactive({
    mysqlUrl: "D:\\MySQL\\bin", //MySQL安装目录
    hostIP: '127.0.0.1', //MySQL数据库所在服务器地址IP
    userName: 'root', //进入数据库所需要的用户名
    hostPort: '3306', //数据库端口
    password: '', //进入数据库所需要的密码
    savePath: 'E:/backup', //数据库还原文件保存路径
    fileName: "", //数据库还原文件文件名
    databaseName: "zhiyuanzheguanlixitong", //要还原的数据库名
})

const rules = {
    mysqlUrl: [{
        required: true,
        message: 'MySQL安装目录不能为空',
        trigger: 'blur'
    }],
    hostIP: [{
        required: true,
        message: 'MySQL数据库所在服务器地址IP不能为空',
        trigger: 'blur'
    }],
    userName: [{
        required: true,
        message: '进入数据库所需要的用户名不能为空',
        trigger: 'blur'
    }],
    hostPort: [{
        required: true,
        message: '数据库端口不能为空',
        trigger: 'blur'
    }],
    password: [{
        required: true,
        message: '进入数据库所需要的密码不能为空',
        trigger: 'blur'
    }],
    savePath: [{
        required: true,
        message: '数据库还原文件保存路径不能为空',
        trigger: 'blur'
    }],
    fileName: [{
        required: true,
        message: '数据库还原文件文件名不能为空',
        trigger: 'blur'
    }, {
        pattern: /\.sql$/,
        message: '文件名必须以.sql结尾',
        trigger: 'blur'
    }],
    databaseName: [{
        required: true,
        message: '要还原的数据库名不能为空',
        trigger: 'blur'
    }]
}

const huanyuan = async () => {
    if (!ruleFormRef.value) return

    await ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            // 二次确认弹窗
            try {
                await ElMessageBox.confirm(
                    '数据库恢复操作将覆盖现有数据，是否确认继续？', 
                    '警告', 
                    {
                        confirmButtonText: '确认恢复',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }
                )
                
                loading.value = true
                const loadingInstance = ElLoading.service({
                    text: '正在恢复数据库，请稍候...',
                    background: 'rgba(0, 0, 0, 0.7)'
                })

                // 处理路径格式
                const formData = {
                    ...ruleForm,
                    // 确保使用正确的路径分隔符
                    mysqlUrl: ruleForm.mysqlUrl.replaceAll("\\", "/").endsWith("/") ? ruleForm.mysqlUrl.replaceAll("\\", "/") : ruleForm.mysqlUrl.replaceAll("\\", "/") + "/",
                    hostIP: ruleForm.hostIP.replaceAll("\\", "/"),
                    savePath: ruleForm.savePath.replaceAll("\\", "/")
                }

                try {
                    console.log('发送恢复请求，参数:', formData);
                    const res = await http.get('/huanyuan', {
                        params: formData
                    })
                    
                    if (res.code === 0) {
                        ElMessage.success('数据库恢复成功')
                        // 通知父组件恢复成功
                        emits('success')
                    } else {
                        ElMessage.error(res.msg || '数据库恢复失败')
                    }
                } catch (error) {
                    console.error('恢复失败:', error)
                    ElMessage.error('恢复失败：' + (error.message || '未知错误'))
                } finally {
                    loadingInstance.close()
                    loading.value = false
                }
            } catch (e) {
                // 用户取消操作
                console.log('用户取消了恢复操作')
            }
        }
    })
}

// 使用传入的文件名
onMounted(() => {
    if (props.fileName) {
        ruleForm.fileName = props.fileName
    }
})
</script>

<style lang="scss" scoped>
.addEdit-block {
    margin: 20px;

    .el-form {
        max-width: 800px;
        margin: 0 auto;
    }

    .btn {
        text-align: center;
        margin-top: 20px;
    }

    .btn-success {
        min-width: 120px;
    }
}
</style>