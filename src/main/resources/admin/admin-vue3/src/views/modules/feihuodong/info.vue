<template>
    <div class="main-content">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <span>活动详情</span>
                    <el-button @click="goBack">返回</el-button>
                </div>
            </template>
            <div class="info-content" v-loading="loading">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="活动名称">{{ info.feihuodongName }}</el-descriptions-item>
                    <el-descriptions-item label="活动编号">{{ info.feihuodongUuidNumber }}</el-descriptions-item>
                    <el-descriptions-item label="活动照片">
                        <img v-if="info.feihuodongPhoto" :src="info.feihuodongPhoto" style="width: 200px; height: 200px; object-fit: cover;">
                    </el-descriptions-item>
                    <el-descriptions-item label="活动地点">{{ info.feihuodongAddress }}</el-descriptions-item>
                    <el-descriptions-item label="活动类型">
                        {{ info.feihuodongTypes === 1 ? '公益活动' : '其他活动' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="活动人数">{{ info.feihuodongKucunNumber }}</el-descriptions-item>
                    <el-descriptions-item label="参加活动条件" :span="2">{{ info.feihuodongTiaojian }}</el-descriptions-item>
                    <el-descriptions-item label="活动介绍" :span="2">{{ info.feihuodongContent }}</el-descriptions-item>
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

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const info = ref({})

// 获取活动详情
const getInfo = async () => {
    try {
        loading.value = true
        const res = await http.get(`/feihuodong/info/${route.params.id}`)
        if (res.code === 0) {
            info.value = {
                ...res.data,
                feihuodongPhoto: res.data.feihuodongPhoto ? `/file/download?fileName=${res.data.feihuodongPhoto.replace('upload/', '')}` : ''
            }
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

// 返回列表页
const goBack = () => {
    router.push('/feihuodong')
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