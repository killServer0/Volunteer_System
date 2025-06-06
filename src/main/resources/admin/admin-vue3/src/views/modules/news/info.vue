<template>
    <el-dialog title="新闻详情" v-model="visible" :close-on-click-modal="false" width="800px">
        <el-descriptions :column="1" border>
            <el-descriptions-item label="标题">{{ dataForm.title }}</el-descriptions-item>
            <el-descriptions-item label="类型">
                <el-tag v-if="dataForm.type === '1'" type="warning">通知</el-tag>
                <el-tag v-else-if="dataForm.type === '2'" type="success">公告</el-tag>
                <el-tag v-else type="info">新闻</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="作者">{{ dataForm.author }}</el-descriptions-item>
            <el-descriptions-item label="浏览量">{{ dataForm.viewCount }}</el-descriptions-item>
            <el-descriptions-item label="封面图">
                <el-image v-if="dataForm.coverImg" :src="dataForm.coverImg" :preview-src-list="[dataForm.coverImg]"
                    fit="contain" style="max-width: 200px; max-height: 200px;" />
                <span v-else>暂无封面图</span>
            </el-descriptions-item>
            <el-descriptions-item label="摘要">{{ dataForm.summary }}</el-descriptions-item>
            <el-descriptions-item label="内容">
                <div class="content-box" v-html="dataForm.content"></div>
            </el-descriptions-item>
            <el-descriptions-item label="发布时间">{{ dataForm.createTime }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ dataForm.updateTime }}</el-descriptions-item>
        </el-descriptions>
        <template #footer>
            <el-button @click="visible = false">关闭</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { http } from '@/utils/http'

const visible = ref(false)
const dataForm = reactive({
    id: 0,
    title: '',
    type: '',
    author: '',
    viewCount: 0,
    coverImg: '',
    content: '',
    summary: '',
    createTime: '',
    updateTime: ''
})

// 初始化
const init = (id) => {
    visible.value = true
    http.get(`news/info/${id}`).then(({ data }) => {
        if (data && data.code === 0) {
            Object.assign(dataForm, data.data)
        }
    })
}

defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.content-box {
    padding: 10px;
    max-height: 400px;
    overflow-y: auto;
    line-height: 1.5;
    white-space: pre-wrap;
    word-wrap: break-word;
}

:deep(.el-descriptions__body) {
    background-color: #fff;
}

:deep(.el-descriptions__label) {
    width: 120px;
    background-color: #f5f7fa;
}
</style>