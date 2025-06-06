<template>
    <div class="main-content">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>预约详情</span>
                    <el-button @click="goBack">返回</el-button>
                </div>
            </template>
            <div class="info-content" v-loading="loading">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="活动名称">{{ info.zhihuodongName }}</el-descriptions-item>
                    <el-descriptions-item label="活动编号">{{ info.zhihuodongUuidNumber }}</el-descriptions-item>
                    <el-descriptions-item label="活动照片">
                        <el-image v-if="info.zhihuodongPhoto"
                            :src="`/file/download?fileName=${info.zhihuodongPhoto.split('/').pop()}`"
                            style="width: 200px; height: 200px; object-fit: cover;"
                            :preview-src-list="[`/file/download?fileName=${info.zhihuodongPhoto.split('/').pop()}`]"
                            fit="cover" @error="handleImageError" />
                        <span v-else>暂无图片</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="活动类型">{{ info.zhihuodongValue }}</el-descriptions-item>
                    <el-descriptions-item label="志愿者姓名">{{ info.zhiyuanzheName }}</el-descriptions-item>
                    <el-descriptions-item label="志愿者手机号">{{ info.zhiyuanzhePhone }}</el-descriptions-item>
                    <el-descriptions-item label="志愿者头像">
                        <el-image v-if="info.zhiyuanzhePhoto"
                            :src="`/file/download?fileName=${info.zhiyuanzhePhoto.split('/').pop()}`"
                            style="width: 100px; height: 100px; object-fit: cover;"
                            :preview-src-list="[`/file/download?fileName=${info.zhiyuanzhePhoto.split('/').pop()}`]"
                            fit="cover" @error="handleImageError" />
                        <span v-else>暂无图片</span>
                    </el-descriptions-item>
                    <el-descriptions-item label="报名编号">{{ info.zhihuodongYuyueUuidNumber }}</el-descriptions-item>
                    <el-descriptions-item label="报名状态">
                        <el-tag :type="getStatusType(info.zhihuodongYuyueYesnoTypes)">
                            {{getStatusText(info.zhihuodongYuyueYesnoTypes)}}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="审核回复" :span="2">{{ info.zhihuodongYuyueYesnoText }}</el-descriptions-item>
                    <el-descriptions-item label="录入时间">{{ info.insertTime }}</el-descriptions-item>
                </el-descriptions>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import config from '@/config'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const info = ref({})
const baseUrl = config.baseUrl

// 获取预约详情
const getInfo = async () => {
    try {
        loading.value = true
        const res = await http.get(`/zhihuodongYuyue/info/${route.params.id}`)
        if (res.code === 0) {
            info.value = res.data
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取预约详情失败:', error)
        ElMessage.error('获取预约详情失败')
    } finally {
        loading.value = false
    }
}

// 返回列表页
const goBack = () => {
    router.push('/zhihuodongYuyue')
}

// 获取状态文本
const getStatusText = (status) => {
    if (status === null || status === undefined) {
        return '待审核'
    }
    
    const statusStr = String(status).trim()
    
    switch (statusStr) {
        case '1': return '待审核'
        case '2': return '已通过'
        case '3': return '已拒绝'
        default: return '待审核'
    }
}

// 获取状态类型
const getStatusType = (status) => {
    if (status === null || status === undefined) {
        return 'info'
    }
    
    const statusStr = String(status).trim()
    
    switch (statusStr) {
        case '1': return 'info'
        case '2': return 'success'
        case '3': return 'danger'
        default: return 'info'
    }
}

// 在 script setup 部分添加 handleImageError 方法
const handleImageError = (e) => {
    console.error('图片加载失败:', e)
    ElMessage.error('图片加载失败')
}

onMounted(() => {
    getInfo()
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

.info-content {
    margin-top: 20px;
}

:deep(.el-descriptions__label) {
    width: 120px;
    font-weight: bold;
}
</style> 