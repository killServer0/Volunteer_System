<template>
    <div class="file-upload">
        <el-upload :action="action" :headers="headers" :on-success="handleSuccess" :on-error="handleError"
            :before-upload="beforeUpload" :file-list="fileList" :limit="limit" :on-exceed="handleExceed"
            :on-remove="handleRemove" :multiple="multiple" :accept="accept">
            <el-button type="primary">点击上传</el-button>
            <template #tip>
                <div class="el-upload__tip">{{ tip }}</div>
            </template>
        </el-upload>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'

const props = defineProps({
    action: {
        type: String,
        required: true
    },
    limit: {
        type: Number,
        default: 1
    },
    multiple: {
        type: Boolean,
        default: false
    },
    accept: {
        type: String,
        default: '*'
    },
    tip: {
        type: String,
        default: ''
    }
})

const emit = defineEmits(['success', 'error', 'remove'])

const fileList = ref([])

const headers = computed(() => ({
    token: getToken()
}))

const handleSuccess = (response, file, fileList) => {
    emit('success', response, file, fileList)
}

const handleError = (error, file, fileList) => {
    ElMessage.error('上传失败')
    emit('error', error, file, fileList)
}

const beforeUpload = (file) => {
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
        ElMessage.error('上传文件大小不能超过 2MB!')
    }
    return isLt2M
}

const handleExceed = (files, uploadFiles) => {
    ElMessage.warning(`当前限制选择 ${props.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + uploadFiles.length} 个文件`)
}

const handleRemove = (file, fileList) => {
    emit('remove', file, fileList)
}
</script>

<style scoped>
.file-upload {
    width: 100%;
}
</style>