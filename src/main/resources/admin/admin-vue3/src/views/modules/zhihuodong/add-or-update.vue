<template>
    <div class="add-edit-block">
        <el-form class="detail-form-content" ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="auto"
            :style="contents.formStyle">
            <el-row>
                <el-col :span="12" v-if="sessionTable !== 'zhiyuanzhe'">
                    <el-form-item class="select" v-if="type !== 'info'" label="志愿者" prop="zhiyuanzheId">
                        <el-select v-model="ruleForm.zhiyuanzheId" :disabled="ro.zhiyuanzheId" filterable
                            placeholder="请选择志愿者" @change="zhiyuanzheChange" :style="contents.selectStyle">
                            <el-option v-for="(item, index) in zhiyuanzheOptions" :key="item.id"
                                :label="item.zhiyuanzheName" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-col>

                <el-col :span="12" v-if="sessionTable !== 'zhiyuanzhe'">
                    <el-form-item class="input" v-if="type !== 'info'" label="志愿者姓名" prop="zhiyuanzheName">
                        <el-input v-model="zhiyuanzheForm.zhiyuanzheName" placeholder="志愿者姓名" clearable readonly
                            :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="志愿者姓名" prop="zhiyuanzheName">
                            <el-input v-model="ruleForm.zhiyuanzheName" placeholder="志愿者姓名" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12" v-if="sessionTable !== 'zhiyuanzhe'">
                    <el-form-item class="input" v-if="type !== 'info'" label="志愿者手机号" prop="zhiyuanzhePhone">
                        <el-input v-model="zhiyuanzheForm.zhiyuanzhePhone" placeholder="志愿者手机号" clearable readonly
                            :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="志愿者手机号" prop="zhiyuanzhePhone">
                            <el-input v-model="ruleForm.zhiyuanzhePhone" placeholder="志愿者手机号" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12" v-if="sessionTable !== 'zhiyuanzhe'">
                    <el-form-item class="input" v-if="type !== 'info'" label="志愿者身份证号" prop="zhiyuanzheIdNumber">
                        <el-input v-model="zhiyuanzheForm.zhiyuanzheIdNumber" placeholder="志愿者身份证号" clearable readonly
                            :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="志愿者身份证号" prop="zhiyuanzheIdNumber">
                            <el-input v-model="ruleForm.zhiyuanzheIdNumber" placeholder="志愿者身份证号" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12" v-if="sessionTable !== 'zhiyuanzhe'">
                    <el-form-item class="input" v-if="type !== 'info'" label="志愿者邮箱" prop="zhiyuanzheEmail">
                        <el-input v-model="zhiyuanzheForm.zhiyuanzheEmail" placeholder="志愿者邮箱" clearable readonly
                            :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="志愿者邮箱" prop="zhiyuanzheEmail">
                            <el-input v-model="ruleForm.zhiyuanzheEmail" placeholder="志愿者邮箱" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <input id="updateId" name="id" type="hidden" v-model="ruleForm.id">
                <input id="zhiyuanzheId" name="zhiyuanzheId" type="hidden" v-model="ruleForm.zhiyuanzheId">

                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="活动名称" prop="zhihuodongName">
                        <el-input v-model="ruleForm.zhihuodongName" placeholder="活动名称" clearable
                            :readonly="ro.zhihuodongName" :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="活动名称" prop="zhihuodongName">
                            <el-input v-model="ruleForm.zhihuodongName" placeholder="活动名称" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="活动编号" prop="zhihuodongUuidNumber">
                        <el-input v-model="ruleForm.zhihuodongUuidNumber" placeholder="活动编号" clearable
                            :readonly="ro.zhihuodongUuidNumber" :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="活动编号" prop="zhihuodongUuidNumber">
                            <el-input v-model="ruleForm.zhihuodongUuidNumber" placeholder="活动编号" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="upload" v-if="type !== 'info' && !ro.zhihuodongPhoto" label="活动照片" prop="zhihuodongPhoto">
                        <el-upload
                            class="avatar-uploader"
                            :action="uploadUrl"
                            :show-file-list="false"
                            :on-success="handleUploadSuccess"
                            :before-upload="beforeUpload">
                            <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                        </el-upload>
                    </el-form-item>
                    <div v-else>
                        <el-form-item label="活动图片" prop="zhihuodongPhoto">
                            <span>已上传图片</span>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="活动地点" prop="zhihuodongAddress">
                        <el-input v-model="ruleForm.zhihuodongAddress" placeholder="活动地点" clearable
                            :readonly="ro.zhihuodongAddress" :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="活动地点" prop="zhihuodongAddress">
                            <el-input v-model="ruleForm.zhihuodongAddress" placeholder="活动地点" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="select" v-if="type !== 'info'" label="活动类型" prop="zhihuodongTypes">
                        <el-select v-model="ruleForm.zhihuodongTypes" :disabled="ro.zhihuodongTypes"
                            placeholder="请选择活动类型" :style="contents.selectStyle">
                            <el-option v-for="(item, index) in zhihuodongTypesOptions" :key="item.codeIndex"
                                :label="item.indexName" :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="活动类型" prop="zhihuodongValue">
                            <el-input v-model="ruleForm.zhihuodongValue" placeholder="活动类型" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="活动人数" prop="zhihuodongKucunNumber">
                        <el-input v-model="ruleForm.zhihuodongKucunNumber" placeholder="活动人数" clearable
                            :readonly="ro.zhihuodongKucunNumber" :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="活动人数" prop="zhihuodongKucunNumber">
                            <el-input v-model="ruleForm.zhihuodongKucunNumber" placeholder="活动人数" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="参加活动条件" prop="zhihuodongTiaojian">
                        <el-input v-model="ruleForm.zhihuodongTiaojian" placeholder="参加活动条件" clearable
                            :readonly="ro.zhihuodongTiaojian" :style="contents.inputStyle"></el-input>
                    </el-form-item>
                    <div v-else>
                        <el-form-item class="input" label="参加活动条件" prop="zhihuodongTiaojian">
                            <el-input v-model="ruleForm.zhihuodongTiaojian" placeholder="参加活动条件" readonly
                                :style="contents.inputStyle"></el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <el-col :span="24">
                    <el-form-item v-if="type !== 'info'" label="活动介绍" prop="zhihuodongContent">
                        <editor 
                            style="min-width: 200px; max-width: 600px;" 
                            v-model="ruleForm.zhihuodongContent"
                            class="editor" 
                            action="file/upload"
                            @change="handleContentChange">
                        </editor>
                    </el-form-item>
                    <div v-else>
                        <el-form-item v-if="ruleForm.zhihuodongContent" label="活动介绍" prop="zhihuodongContent">
                            <span v-html="ruleForm.zhihuodongContent"></span>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>

            <el-form-item class="btn">
                <el-button v-if="type !== 'info'" type="primary" class="btn-success" @click="onSubmit"
                    :style="contents.buttonStyle">提交</el-button>
                <el-button class="btn-close" @click="back()" :style="contents.buttonStyle">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { getToken } from '@/utils/auth'
import config from '@/config'
import FileUpload from '@/components/FileUpload.vue'
import Editor from '@/components/Editor.vue'
import styleJs from '@/utils/style'
import { get, post, put } from '@/utils/http'
import axios from 'axios'
import { Plus } from '@element-plus/icons-vue'

const contents = reactive(styleJs)
const router = useRouter()
const emit = defineEmits(['refreshDataList'])
const baseUrl = config.baseUrl

// 表单引用
const ruleFormRef = ref(null)

// 表单数据
const ruleForm = reactive({
    id: null,
    zhihuodongName: '',
    zhihuodongPhoto: '',
    zhihuodongTypes: '',
    zhihuodongKucunNumber: '',
    zhihuodongOldMoney: '',
    zhihuodongNewMoney: '',
    zhihuodongClicknum: '',
    shangxiaTypes: '',
    zhihuodongDelete: '',
    zhihuodongContent: '',
    zhiyuanzheId: null,
    zhiyuanzheName: '',
    zhiyuanzhePhone: '',
    zhiyuanzheIdNumber: '',
    zhiyuanzheEmail: ''
})

// 表单验证规则
const rules = reactive({
    zhihuodongName: [
        { required: true, message: '活动名称不能为空', trigger: 'blur' }
    ],
    zhihuodongPhoto: [
        { required: true, message: '活动图片不能为空', trigger: 'blur' }
    ],
    zhihuodongTypes: [
        { required: true, message: '活动类型不能为空', trigger: 'change' }
    ],
    zhihuodongKucunNumber: [
        { required: true, message: '活动人数不能为空', trigger: 'blur' }
    ],
    zhihuodongOldMoney: [
        { required: true, message: '活动原价不能为空', trigger: 'blur' }
    ],
    zhihuodongNewMoney: [
        { required: true, message: '现价不能为空', trigger: 'blur' }
    ],
    zhihuodongContent: [
        { required: true, message: '活动介绍不能为空', trigger: 'blur' }
    ],
    zhiyuanzheId: [
        { required: true, message: '志愿者不能为空', trigger: 'change' }
    ]
})

// 只读控制
const ro = reactive({
    zhiyuanzheId: false,
    zhihuodongName: false,
    zhihuodongUuidNumber: false,
    zhihuodongPhoto: false,
    zhihuodongAddress: false,
    zhihuodongTypes: false,
    zhihuodongKucunNumber: false,
    zhihuodongTiaojian: false
})

// 志愿者表单数据
const zhiyuanzheForm = reactive({
    zhiyuanzheName: '',
    zhiyuanzhePhone: '',
    zhiyuanzheIdNumber: '',
    zhiyuanzhePhoto: '',
    zhiyuanzheEmail: ''
})

// 选项数据
const zhiyuanzheOptions = ref([])
const zhihuodongTypesOptions = ref([])

// 类型控制
const type = ref('')
const sessionTable = ref('')

// 取消请求
const cancelTokenSource = ref(null)

// 上传相关
const uploadUrl = 'http://localhost:8081/zhiyuanzheguanlixitong/file/upload'

// 初始化方法
const init = async () => {
    const route = useRoute()
    const id = route.query.id || route.params.id
    type.value = route.query.type || ''
    sessionTable.value = localStorage.getItem('sessionTable')

    try {
        // 获取志愿者选项
        await getZhiyuanzheOptions()
        // 获取活动类型选项
        await getZhihuodongTypesOptions()
        // 获取详情
        if (id) {
            await getInfo(id)
        }
    } catch (error) {
        if (axios.isCancel(error)) {
            console.log('请求被取消:', error.message)
        } else {
            console.error('初始化失败:', error)
            ElMessage.error('初始化失败')
        }
    }
}

// 获取志愿者选项
const getZhiyuanzheOptions = async () => {
    try {
        const res = await get('/zhiyuanzhe/page', {
            page: 1,
            limit: 1000 // 获取所有志愿者
        })
        if (res.code === 0) {
            zhiyuanzheOptions.value = res.data.list
            console.log('志愿者选项:', zhiyuanzheOptions.value)
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取志愿者选项失败:', error)
        ElMessage.error('获取志愿者选项失败')
    }
}

// 获取活动类型选项
const getZhihuodongTypesOptions = async () => {
    try {
        const res = await get('/dictionary/page', {
            dicCode: 'zhihuodong_types'
        })
        if (res.code === 0) {
            zhihuodongTypesOptions.value = res.data.list
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取活动类型选项失败:', error)
        ElMessage.error('获取活动类型选项失败')
    }
}

// 获取详情
const getInfo = async (id) => {
    try {
        console.log('开始获取活动信息，ID:', id)
        const res = await get(`/zhihuodong/info/${id}`)
        console.log('获取到的响应数据:', res)

        if (res.code === 0) {
            console.log('获取到的活动信息:', res.data)
            Object.assign(ruleForm, res.data)
            console.log('更新后的表单数据:', ruleForm)
            console.log('活动图片路径:', ruleForm.zhihuodongPhoto)
            console.log('图片完整URL:', ruleForm.zhihuodongPhoto.indexOf('http') > -1 ? ruleForm.zhihuodongPhoto : `/api/file/download?name=${ruleForm.zhihuodongPhoto}`)
            console.log('活动介绍内容:', ruleForm.zhihuodongContent)

            // 如果是查看模式，设置所有字段为只读
            if (type.value === 'info') {
                Object.keys(ro).forEach(key => {
                    ro[key] = true
                })
            }
        } else {
            console.error('获取活动信息失败，错误码:', res.code, '错误信息:', res.msg)
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取活动信息失败:', error)
        ElMessage.error('获取活动信息失败')
    }
}

// 志愿者选择变更
const zhiyuanzheChange = async (id) => {
    try {
        const res = await get(`/zhiyuanzhe/info/${id}`)
        if (res.code === 0) {
            console.log('志愿者信息:', res.data)
            Object.assign(zhiyuanzheForm, res.data)
            ruleForm.zhiyuanzheName = res.data.zhiyuanzheName
            ruleForm.zhiyuanzhePhone = res.data.zhiyuanzhePhone
            ruleForm.zhiyuanzheIdNumber = res.data.zhiyuanzheIdNumber
            ruleForm.zhiyuanzheEmail = res.data.zhiyuanzheEmail
            console.log('更新后的表单:', ruleForm)
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取志愿者信息失败:', error)
        ElMessage.error('获取志愿者信息失败')
    }
}

// 活动介绍变更处理
const handleContentChange = (content) => {
    console.log('活动介绍内容变更:', content)
    ruleForm.zhihuodongContent = content
}

// 提交表单
const onSubmit = () => {
    ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                console.log('提交的表单数据:', ruleForm)
                const url = ruleForm.id ? '/zhihuodong/update' : '/zhihuodong/save'
                const method = ruleForm.id ? put : post

                const res = await method(url, ruleForm)
                if (res.code === 0) {
                    ElMessage.success('操作成功')
                    back()
                } else {
                    ElMessage.error(res.msg)
                }
            } catch (error) {
                console.error('提交失败:', error)
                ElMessage.error('提交失败')
            }
        }
    })
}

// 返回
const back = () => {
    // 取消所有未完成的请求
    if (cancelTokenSource.value) {
        cancelTokenSource.value.cancel('路由跳转取消请求')
    }
    // 使用replace而不是push，避免历史记录堆积
    router.replace('/zhihuodong')
}

// 组件卸载时清理
onBeforeUnmount(() => {
    // 取消所有未完成的请求
    if (cancelTokenSource.value) {
        cancelTokenSource.value.cancel('组件卸载取消请求')
    }
})

// 页面加载时初始化
onMounted(() => {
    init()
})

// 暴露方法
defineExpose({
    init
})

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
    if (res.code === 0) {
        ruleForm.zhihuodongPhoto = res.file
        ElMessage.success('上传成功')
    } else {
        ElMessage.error(res.msg || '上传失败')
    }
}
</script>

<style scoped>
.add-edit-block {
    padding: 20px;
}

.detail-form-content {
    background-color: #fff;
    border-radius: 4px;
    padding: 20px;
}

.btn {
    margin-top: 20px;
    text-align: center;
}

.btn-success {
    margin-right: 10px;
}

.editor {
    height: 500px;
}

.editor /deep/ .ql-container {
    height: 310px;
}

/* 上传按钮样式 */
.avatar-uploader {
    border: 2px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 200px;
    height: 200px;
    display: flex;
    justify-content: center;
    align-items: center;
    transition: border-color 0.3s;
}

.avatar-uploader:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 48px;
    color: #8c939d;
    width: 48px;
    height: 48px;
    line-height: 48px;
    text-align: center;
}
</style>