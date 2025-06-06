<template>
    <div class="main-content">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>{{ title }}</span>
                    <el-button @click="goBack">返回</el-button>
                </div>
            </template>
            <div class="form-content" v-loading="loading">
                <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="活动名称" prop="feihuodongName">
                        <el-input v-model="form.feihuodongName" placeholder="请输入活动名称" />
                    </el-form-item>
                    <el-form-item label="活动编号" prop="feihuodongUuidNumber">
                        <el-input v-model="form.feihuodongUuidNumber" placeholder="请输入活动编号" />
                    </el-form-item>
                    <el-form-item label="活动照片" prop="feihuodongPhoto">
                        <el-upload
                            class="avatar-uploader"
                            :action="uploadUrl"
                            :show-file-list="false"
                            :on-success="handleUploadSuccess"
                            :before-upload="beforeUpload">
                            <img v-if="form.feihuodongPhoto" :src="getImageUrl(form.feihuodongPhoto)" class="avatar" @error="handleImageError" />
                            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="活动地点" prop="feihuodongAddress">
                        <el-input v-model="form.feihuodongAddress" placeholder="请输入活动地点" />
            </el-form-item>
            <el-form-item label="活动类型" prop="feihuodongTypes">
                        <el-select v-model="form.feihuodongTypes" placeholder="请选择活动类型" style="width: 100%">
                            <el-option label="公益活动" :value="1" />
                            <el-option label="其他活动" :value="2" />
                </el-select>
            </el-form-item>
                    <el-form-item label="活动人数" prop="feihuodongKucunNumber">
                        <el-input-number v-model="form.feihuodongKucunNumber" :min="1" :max="999" />
            </el-form-item>
                    <el-form-item label="参加活动条件" prop="feihuodongTiaojian">
                        <el-input v-model="form.feihuodongTiaojian" type="textarea" :rows="3" placeholder="请输入参加活动条件" />
            </el-form-item>
                    <el-form-item label="活动介绍" prop="feihuodongContent">
                        <el-input v-model="form.feihuodongContent" type="textarea" :rows="5" placeholder="请输入活动介绍" />
            </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm">提交</el-button>
                        <el-button @click="resetForm">重置</el-button>
            </el-form-item>
        </el-form>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { http } from '@/utils/http'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

// 表单数据
const form = reactive({
    id: undefined,
    feihuodongName: '',
    feihuodongUuidNumber: '',
    feihuodongPhoto: '',
    feihuodongAddress: '',
    feihuodongTypes: 1,
    feihuodongKucunNumber: 1,
    feihuodongTiaojian: '',
    feihuodongContent: '',
    feihuodongDelete: 0
})

// 表单验证规则
const rules = {
    feihuodongName: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
    feihuodongUuidNumber: [{ required: true, message: '请输入活动编号', trigger: 'blur' }],
    feihuodongAddress: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
    feihuodongTypes: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
    feihuodongKucunNumber: [{ required: true, message: '请输入活动人数', trigger: 'blur' }],
    feihuodongTiaojian: [{ required: true, message: '请输入参加活动条件', trigger: 'blur' }],
    feihuodongContent: [{ required: true, message: '请输入活动介绍', trigger: 'blur' }]
}

// 计算标题
const title = computed(() => {
    return form.id ? '修改活动' : '新增活动'
})

// 上传相关
const uploadUrl = 'http://localhost:8081/zhiyuanzheguanlixitong/file/upload'

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
    console.log('上传成功，服务器返回:', res)
    if (res.code === 0) {
        // 保持服务器返回的完整路径（包含upload/前缀）
        form.feihuodongPhoto = res.file
        console.log('设置图片路径:', form.feihuodongPhoto)
        ElMessage.success('上传成功')
        } else {
        ElMessage.error(res.msg || '上传失败')
    }
}

// 获取活动详情
const getInfo = async (id) => {
    try {
        loading.value = true
        const res = await http.get(`/feihuodong/info/${id}`)
        console.log('获取活动详情:', res)
        if (res.code === 0) {
            // 处理图片路径 - 不再移除upload/前缀
            const data = { ...res.data }
            if (data.feihuodongPhoto && data.feihuodongPhoto !== 'undefined') {
                // 不再移除upload/前缀
                data.feihuodongPhoto = data.feihuodongPhoto
            } else {
                data.feihuodongPhoto = ''  // 设置为空字符串而不是 'undefined'
            }
            Object.assign(form, data)
            console.log('设置表单数据:', form)
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取活动详情失败:', error)
        ElMessage.error('获取活动详情失败')
    } finally {
        loading.value = false
    }
}

// 提交表单
const submitForm = async () => {
    if (!formRef.value) return
    
    try {
        await formRef.value.validate()
        loading.value = true
        
        // 处理照片路径，但保留upload/前缀
        const submitForm = { ...form }
        console.log('提交前的表单数据:', submitForm)
        
        // 如果没有图片或为undefined，则设为空字符串
        if (!submitForm.feihuodongPhoto || submitForm.feihuodongPhoto === 'undefined') {
            submitForm.feihuodongPhoto = ''  // 设置为空字符串而不是 'undefined'
        }
        
        const url = submitForm.id ? '/feihuodong/update' : '/feihuodong/save'
        console.log('提交URL:', url)
        console.log('提交数据:', submitForm)
        
        const res = await http.post(url, submitForm)
        console.log('提交响应:', res)

        if (res.code === 0) {
            ElMessage.success(submitForm.id ? '修改成功' : '新增成功')
            goBack()
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
    } finally {
        loading.value = false
    }
}

// 重置表单
const resetForm = () => {
    if (formRef.value) {
        formRef.value.resetFields()
    }
}

// 返回列表页
const goBack = () => {
    router.push('/feihuodong')
}

// 获取图片URL
const getImageUrl = (photoPath) => {
    if (!photoPath) {
        return 'https://via.placeholder.com/100x100?text=No+Image'
    }
    
    if (photoPath.startsWith('http')) {
        return photoPath
    }
    
    // 不再改变文件名，保持原始路径
    return `http://localhost:8081/zhiyuanzheguanlixitong/file/download?fileName=${photoPath}`
}

// 处理图片加载错误
const handleImageError = (e) => {
    e.target.src = 'https://via.placeholder.com/100x100?text=No+Image'
}

onMounted(() => {
    const id = route.query.id
    if (id) {
        form.id = id
        getInfo(id)
            }
})
</script>

<style scoped>
.main-content {
    padding: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.form-content {
    margin-top: 20px;
    max-width: 800px;
}

.avatar-uploader {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
}

.avatar-uploader:hover {
    border-color: var(--el-color-primary);
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
    object-fit: cover;
}
</style>