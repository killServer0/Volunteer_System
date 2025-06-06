<template>
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" width="600px" :close-on-click-modal="false"
        :close-on-press-escape="false" :destroy-on-close="true">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="auto"
            :style="{ backgroundColor: contents.formBgColor, color: contents.formFontColor }">
            <el-form-item label="账户" prop="username">
                <el-input v-model="dataForm.username" placeholder="账户"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="游客姓名" prop="feizhiyuanName">
                <el-input v-model="dataForm.feizhiyuanName" placeholder="游客姓名"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="游客手机号" prop="feizhiyuanPhone">
                <el-input v-model="dataForm.feizhiyuanPhone" placeholder="游客手机号"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="游客身份证号" prop="feizhiyuanIdNumber">
                <el-input v-model="dataForm.feizhiyuanIdNumber" placeholder="游客身份证号"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="游客邮箱" prop="feizhiyuanEmail">
                <el-input v-model="dataForm.feizhiyuanEmail" placeholder="游客邮箱"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="性别" prop="sexTypes">
                <el-select v-model="dataForm.sexTypes" placeholder="请选择性别"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
                    <el-option v-for="(item, index) in sexTypesOptions" :key="index" :label="item.indexName"
                        :value="item.codeIndex" />
                </el-select>
            </el-form-item>
            <el-form-item label="游客头像" prop="feizhiyuanPhoto">
                <file-upload tip="点击上传游客头像" :uploadUrl="baseUrl + 'file/upload'" :limit="1" :multiple="false"
                    :value="dataForm.feizhiyuanPhoto" @update:value="val => dataForm.feizhiyuanPhoto = val" />
            </el-form-item>
            <el-form-item label="密码" prop="password" v-if="!dataForm.id">
                <el-input v-model="dataForm.password" type="password" placeholder="密码"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword" v-if="!dataForm.id">
                <el-input v-model="dataForm.confirmPassword" type="password" placeholder="确认密码"
                    :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false"
                    :style="{ backgroundColor: contents.btnCancelBgColor, color: contents.btnCancelFontColor, fontSize: contents.btnCancelFontSize, borderWidth: contents.btnCancelBorderWidth, borderStyle: contents.btnCancelBorderStyle, borderColor: contents.btnCancelBorderColor, borderRadius: contents.btnCancelBorderRadius }">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit()"
                    :style="{ backgroundColor: contents.btnSubmitBgColor, color: contents.btnSubmitFontColor, fontSize: contents.btnSubmitFontSize, borderWidth: contents.btnSubmitBorderWidth, borderStyle: contents.btnSubmitBorderStyle, borderColor: contents.btnSubmitBorderColor, borderRadius: contents.btnSubmitBorderRadius }">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import FileUpload from '@/components/common/FileUpload.vue'
import styleJs from '@/utils/style.js'

const emit = defineEmits(['refreshDataList'])

// 样式配置
const contents = ref(styleJs.listStyle())

// 弹窗显示状态
const visible = ref(false)
const baseUrl = import.meta.env.VITE_API_URL || ''

// 表单数据
const dataForm = reactive({
    id: '',
    username: '',
    password: '',
    confirmPassword: '',
    feizhiyuanName: '',
    feizhiyuanPhone: '',
    feizhiyuanIdNumber: '',
    feizhiyuanEmail: '',
    sexTypes: '',
    feizhiyuanPhoto: '',
    feizhiyuanUuidNumber: ''
})

// 性别选项
const sexTypesOptions = ref([])

// 表单校验规则
const dataRule = {
    username: [
        { required: true, message: '账户不能为空', trigger: 'blur' },
        { min: 3, max: 20, message: '账户长度必须在3到20个字符之间', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '密码不能为空', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度必须在6到20个字符之间', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '确认密码不能为空', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== dataForm.password) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ],
    feizhiyuanName: [
        { required: true, message: '游客姓名不能为空', trigger: 'blur' },
        { min: 2, max: 20, message: '游客姓名长度必须在2到20个字符之间', trigger: 'blur' }
    ],
    feizhiyuanPhone: [
        { required: true, message: '游客手机号不能为空', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
    ],
    feizhiyuanIdNumber: [
        { required: true, message: '游客身份证号不能为空', trigger: 'blur' },
        {
            pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
            message: '请输入正确的身份证号',
            trigger: 'blur'
        }
    ],
    feizhiyuanEmail: [
        { required: true, message: '游客邮箱不能为空', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ],
    sexTypes: [
        { required: true, message: '性别不能为空', trigger: 'change' }
    ],
    feizhiyuanPhoto: [
        { required: true, message: '游客头像不能为空', trigger: 'change' }
    ]
}

// 表单引用
const dataFormRef = ref(null)

// 获取性别选项
const getSexTypesOptions = () => {
    http.get('dictionary/page', {
        params: {
            dicCode: 'sex_types'
        }
    }).then(({ data }) => {
        if (data && data.code === 0) {
            sexTypesOptions.value = data.data.list
        } else {
            ElMessage.error(data.msg)
        }
    })
}

// 获取最大编号
const getMaxCodeIndex = () => {
    http.get('feizhiyuan/maxCodeIndex').then(({ data }) => {
        if (data && data.code === 0) {
            dataForm.feizhiyuanUuidNumber = data.data
        } else {
            ElMessage.error(data.msg)
        }
    })
}

// 初始化
const init = (id, type) => {
    dataForm.id = id || ''
    visible.value = true

    // 获取性别选项
    getSexTypesOptions()

    // 如果是新增，获取最大编号
    if (!dataForm.id) {
        getMaxCodeIndex()
    }

    // 如果是修改或查看，获取详情
    if (dataForm.id) {
        http.get(`feizhiyuan/info/${dataForm.id}`).then(({ data }) => {
            if (data && data.code === 0) {
                Object.assign(dataForm, data.data)
            } else {
                ElMessage.error(data.msg)
            }
        })
    }
}

// 提交表单
const dataFormSubmit = () => {
    dataFormRef.value.validate((valid) => {
        if (valid) {
            const url = dataForm.id ? 'feizhiyuan/update' : 'feizhiyuan/save'
            http.post(url, dataForm).then(({ data }) => {
                if (data && data.code === 0) {
                    ElMessage.success('操作成功')
                    visible.value = false
                    emit('refreshDataList')
                } else {
                    ElMessage.error(data.msg)
                }
            })
        }
    })
}

// 暴露方法
defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.dialog-footer {
    text-align: right;
}
</style>