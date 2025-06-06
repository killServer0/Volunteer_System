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
                <el-input clearable placeholder="数据库文件保存路径" v-model="ruleForm.savePath"></el-input>
            </el-form-item>
            <el-form-item class="input" label="数据库导出文件文件名(.sql结尾)" prop="fileName">
                <el-input clearable placeholder="数据库导出文件文件名" v-model="ruleForm.fileName"></el-input>
                <div class="form-tip">文件名请以.sql结尾，例如：backup_20230101.sql</div>
            </el-form-item>
            <el-form-item class="input" label="要导出的数据库名" prop="databaseName">
                <el-input clearable placeholder="要导出的数据库名" v-model="ruleForm.databaseName"></el-input>
            </el-form-item>
            <el-form-item class="btn">
                <el-button type="primary" class="btn-success" @click="beifen" :loading="loading">备份数据库</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineEmits } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style.js'

// 定义事件
const emits = defineEmits(['success'])

const ruleFormRef = ref()
const addEditForm = ref(styleJs.addStyle())
const loading = ref(false)

// 生成默认文件名（当前日期时间）
const generateDefaultFileName = () => {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    const hours = String(now.getHours()).padStart(2, '0')
    const minutes = String(now.getMinutes()).padStart(2, '0')
    return `backup_${year}${month}${day}_${hours}${minutes}.sql`
}

const ruleForm = reactive({
    mysqlUrl: "D:\\MySQL\\bin", //MySQL安装目录
    hostIP: '127.0.0.1', //MySQL数据库所在服务器地址IP
    userName: 'root', //进入数据库所需要的用户名
    hostPort: '3306', //数据库端口
    password: '', //进入数据库所需要的密码
    savePath: 'E:/backup', //数据库导出文件保存路径
    fileName: generateDefaultFileName(), //数据库导出文件文件名
    databaseName: "zhiyuanzheguanlixitong", //要导出的数据库名
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
        message: '数据库导出文件保存路径不能为空',
        trigger: 'blur'
    }],
    fileName: [{
        required: true,
        message: '数据库导出文件文件名不能为空',
        trigger: 'blur'
    }, {
        pattern: /\.sql$/,
        message: '文件名必须以.sql结尾',
        trigger: 'blur'
    }],
    databaseName: [{
        required: true,
        message: '要导出的数据库名不能为空',
        trigger: 'blur'
    }]
}

const beifen = async () => {
    if (!ruleFormRef.value) return

    await ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true
            const loadingInstance = ElLoading.service({
                text: '正在创建备份，请稍候...',
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
                console.log('发送备份请求，参数:', formData);
                const res = await http.get('/beifen', {
                    params: formData
                })
                
                if (res.code === 0) {
                    ElMessage.success('备份创建成功')
                    // 通知父组件备份成功
                    emits('success')
                } else {
                    ElMessage.error(res.msg || '备份创建失败')
                }
            } catch (error) {
                console.error('备份创建失败:', error)
                ElMessage.error('备份创建失败：' + (error.message || '未知错误'))
            } finally {
                loadingInstance.close()
                loading.value = false
            }
        }
    })
}

// 样式处理
const addEditStyleChange = () => {
    nextTick(() => {
        document.querySelectorAll('.addEdit-block .input .el-input__inner').forEach(el => {
            el.style.height = addEditForm.value.inputHeight
            el.style.color = addEditForm.value.inputFontColor
            el.style.fontSize = addEditForm.value.inputFontSize
            el.style.borderWidth = addEditForm.value.inputBorderWidth
            el.style.borderStyle = addEditForm.value.inputBorderStyle
            el.style.borderColor = addEditForm.value.inputBorderColor
            el.style.borderRadius = addEditForm.value.inputBorderRadius
            el.style.backgroundColor = addEditForm.value.inputBgColor
        })
        document.querySelectorAll('.addEdit-block .input .el-form-item__label').forEach(el => {
            el.style.lineHeight = addEditForm.value.inputHeight
            el.style.color = addEditForm.value.inputLableColor
            el.style.fontSize = addEditForm.value.inputLableFontSize
        })
        document.querySelectorAll('.addEdit-block .btn .btn-success').forEach(el => {
            el.style.width = addEditForm.value.btnSaveWidth
            el.style.height = addEditForm.value.btnSaveHeight
            el.style.color = addEditForm.value.btnSaveFontColor
            el.style.fontSize = addEditForm.value.btnSaveFontSize
            el.style.borderWidth = addEditForm.value.btnSaveBorderWidth
            el.style.borderStyle = addEditForm.value.btnSaveBorderStyle
            el.style.borderColor = addEditForm.value.btnSaveBorderColor
            el.style.borderRadius = addEditForm.value.btnSaveBorderRadius
            el.style.backgroundColor = addEditForm.value.btnSaveBgColor
        })
    })
}

onMounted(() => {
    addEditStyleChange()
})
</script>

<style scoped>
.addEdit-block {
    margin: 0 auto;
    width: 100%;
    padding: 20px;
}

.detail-form-content {
    max-width: 600px;
    margin: 0 auto;
}

.btn {
    text-align: center;
    margin-top: 20px;
}

.btn-success {
    width: 100%;
}

.form-tip {
    color: #909399;
    font-size: 12px;
    line-height: 1.2;
    padding-top: 4px;
}
</style>