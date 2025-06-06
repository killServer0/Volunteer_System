<template>
    <div class="addOrUpdate-block">
        <el-form class="detail-form-content" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="80px">
            <el-row>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type != 'info'" label="名称" prop="name">
                        <el-input v-model="ruleForm.name" placeholder="名称" clearable></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="名称" prop="name">
                            <el-input v-model="ruleForm.name" placeholder="名称" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="upload" v-if="type != 'info' && !ro.value" label="轮播图" prop="value">
                        <file-upload
                            tip="点击上传轮播图"
                            action="file/upload"
                            :limit="1"
                            :multiple="true"
                            :fileUrls="ruleForm.value ? ruleForm.value : ''"
                            @change="valueUploadChange"
                        ></file-upload>
                    </el-form-item>
                    <el-form-item v-else class="upload" label="轮播图" prop="value">
                        <img v-if="ruleForm.value" :src="getImageUrl(ruleForm.value)" class="upload-img" />
                        </el-form-item>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type != 'info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
                <el-button v-if="type != 'info'" class="btn-close" @click="back()">取消</el-button>
                <el-button v-if="type == 'info'" class="btn-close" @click="back()">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import { useRoute, useRouter } from 'vue-router'
import FileUpload from '@/components/file-upload/index.vue'
import styleJs from '@/utils/style.js'
import { isNumber, isIntNumer, isEmail, isPhone, isMobile, isURL, checkIdCard } from '@/utils/validate'
import base from '@/utils/base'

const route = useRoute()
const router = useRouter()

const props = defineProps({
    type: {
        type: String,
        default: 'add'
    }
})

const emit = defineEmits(['refreshDataList', 'close'])

const ruleForm = reactive({
    id: '',
    name: '',
    value: ''
})

const rules = reactive({
    name: [
        { required: true, message: '名称不能为空', trigger: 'blur' }
    ],
    value: [
        { required: true, message: '轮播图不能为空', trigger: 'change' }
    ]
})

const ro = reactive({
    name: false,
    value: false
})

const baseUrl = base.uploadUrl || ''
const addEditForm = ref(styleJs.addStyle())

// 图片URL处理
const getImageUrl = (value) => {
    if (!value) return ''
    // 移除upload/前缀
    const fileName = value.startsWith('upload/') ? value.substring(7) : value
    return `/file/download?fileName=${fileName}`
}

// 初始化
const init = async () => {
    console.log('init called')
    console.log('route params:', route.params)
    console.log('route query:', route.query)
    
    // 重置表单
    ruleForm.id = ''
    ruleForm.name = ''
    ruleForm.value = ''
    
    // 设置只读状态
    const isInfo = route.name === 'ConfigInfo'
    ro.name = isInfo
    ro.value = isInfo
    
    const id = route.params.id || route.query.id
    if (id) {
        try {
            console.log('开始获取配置信息，ID:', id)
            const res = await http.get(`/config/info/${id}`)
            console.log('获取到的响应数据:', res)

            if (res.code === 0 && res.data) {
                console.log('获取到的配置信息:', res.data)
                Object.assign(ruleForm, res.data)
                console.log('更新后的表单数据:', ruleForm)
                console.log('图片路径:', ruleForm.value)
                console.log('图片完整URL:', getImageUrl(ruleForm.value))
            } else {
                console.error('获取配置信息失败，错误码:', res.code, '错误信息:', res.msg)
                ElMessage.error(res.msg || '获取配置信息失败')
            }
        } catch (error) {
            console.error('获取配置信息失败:', error)
            ElMessage.error('获取配置信息失败')
        }
    }
}

// 图片上传回调
const valueUploadChange = (fileUrl) => {
    console.log('图片上传回调，fileUrl:', fileUrl)
    ruleForm.value = fileUrl
}

// 提交表单
const onSubmit = async () => {
    try {
        console.log('提交表单，数据:', ruleForm)
        const res = await http.post('/config/' + (ruleForm.id ? 'update' : 'add'), ruleForm)
        if (res.code === 0) {
            ElMessage.success(ruleForm.id ? '修改成功' : '新增成功')
            router.push('/config')
                } else {
            ElMessage.error(res.msg || '操作失败')
                }
            } catch (error) {
        console.error('提交表单失败:', error)
        ElMessage.error('提交表单失败')
            }
}

// 返回
const back = () => {
    console.log('back called')
    router.push('/config')
}

// 初始化
onMounted(() => {
    init()
})

// 样式处理
const addEditStyleChange = () => {
    nextTick(() => {
        document.querySelectorAll('.addEdit-block .el-input__inner').forEach(el => {
            el.style.height = addEditForm.value.inputHeight
            el.style.color = addEditForm.value.inputFontColor
            el.style.fontSize = addEditForm.value.inputFontSize
            el.style.borderWidth = addEditForm.value.inputBorderWidth
            el.style.borderStyle = addEditForm.value.inputBorderStyle
            el.style.borderColor = addEditForm.value.inputBorderColor
            el.style.borderRadius = addEditForm.value.inputBorderRadius
            el.style.backgroundColor = addEditForm.value.inputBgColor
        })
        document.querySelectorAll('.addEdit-block .el-button--success').forEach(el => {
            el.style.height = addEditForm.value.btnHeight
            el.style.color = addEditForm.value.btnFontColor
            el.style.fontSize = addEditForm.value.btnFontSize
            el.style.borderWidth = addEditForm.value.btnBorderWidth
            el.style.borderStyle = addEditForm.value.btnBorderStyle
            el.style.borderColor = addEditForm.value.btnBorderColor
            el.style.borderRadius = addEditForm.value.btnBorderRadius
            el.style.backgroundColor = addEditForm.value.btnBgColor
        })
        document.querySelectorAll('.addEdit-block .el-button--danger').forEach(el => {
            el.style.height = addEditForm.value.btnHeight
            el.style.color = addEditForm.value.btnFontColor
            el.style.fontSize = addEditForm.value.btnFontSize
            el.style.borderWidth = addEditForm.value.btnBorderWidth
            el.style.borderStyle = addEditForm.value.btnBorderStyle
            el.style.borderColor = addEditForm.value.btnBorderColor
            el.style.borderRadius = addEditForm.value.btnBorderRadius
            el.style.backgroundColor = addEditForm.value.btnBgColor
        })
    })
}

const addEditUploadStyleChange = () => {
    nextTick(() => {
        document.querySelectorAll('.addEdit-block .upload .el-upload-list--picture-card .el-upload-list__item').forEach(el => {
            el.style.width = addEditForm.value.uploadHeight
            el.style.height = addEditForm.value.uploadHeight
            el.style.borderWidth = addEditForm.value.uploadBorderWidth
            el.style.borderStyle = addEditForm.value.uploadBorderStyle
            el.style.borderColor = addEditForm.value.uploadBorderColor
            el.style.borderRadius = addEditForm.value.uploadBorderRadius
            el.style.backgroundColor = addEditForm.value.uploadBgColor
        })
    })
}

// 暴露方法给父组件
defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.addOrUpdate-block {
    margin: -10px;
}

    .detail-form-content {
    padding: 12px;
    background-color: transparent;
}

.btn-close {
    margin-left: 10px;
        }

.upload-img {
    width: 200px;
    height: 200px;
    object-fit: cover;
}
</style>