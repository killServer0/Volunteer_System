<template>
    <div class="info-container">
        <el-card class="box-card" :style="contents.cardStyle">
            <template #header>
                <div class="card-header">
                    <span>活动详情</span>
                    <el-button @click="goBack" type="primary" size="small" :style="contents.buttonStyle">返回</el-button>
                </div>
            </template>
            <el-descriptions :column="2" border :style="contents.tableStyle">
                <el-descriptions-item label="活动名称">{{ info.zhihuodongName }}</el-descriptions-item>
                <el-descriptions-item label="活动类型">{{ info.zhihuodongValue }}</el-descriptions-item>
                <el-descriptions-item label="活动地点">{{ info.zhihuodongAddress }}</el-descriptions-item>
                <el-descriptions-item label="活动人数">{{ info.zhihuodongKucunNumber }}</el-descriptions-item>
                <el-descriptions-item label="参加活动条件">{{ info.zhihuodongTiaojian }}</el-descriptions-item>
                <el-descriptions-item label="活动编号">{{ info.zhihuodongUuidNumber }}</el-descriptions-item>
                <el-descriptions-item label="活动照片" :span="2">
                    <el-image v-if="info.zhihuodongPhoto"
                        :src="`/file/download?fileName=${info.zhihuodongPhoto.split('/').pop()}`"
                        style="width: 200px; height: 200px; object-fit: cover;"
                        :preview-src-list="[`/file/download?fileName=${info.zhihuodongPhoto.split('/').pop()}`]"
                        fit="cover" @error="handleImageError" />
                    <span v-else>暂无图片</span>
                </el-descriptions-item>
                <el-descriptions-item label="活动介绍" :span="2">
                    <div v-html="info.zhihuodongContent"></div>
                </el-descriptions-item>
            </el-descriptions>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import config from '@/config'
import FileUpload from '@/components/FileUpload.vue'
import Editor from '@/components/Editor.vue'
import styleJs from '@/utils/style'
import { get } from '@/utils/http'

const contents = reactive(styleJs)
const route = useRoute()
const router = useRouter()
const info = ref({})
const baseUrl = config.baseUrl

onMounted(() => {
    const id = route.params.id
    if (id) {
        getInfo(id)
    }
})

const getInfo = async (id) => {
    try {
        console.log('开始获取活动信息，ID:', id)
        const res = await get(`/zhihuodong/info/${id}`)
        console.log('获取到的响应数据:', res)

        if (res.code === 0) {
            console.log('获取到的活动信息:', res.data)
            info.value = res.data
            console.log('更新后的活动信息:', info.value)
            console.log('活动图片路径:', info.value.zhihuodongPhoto)
            // 修改图片URL拼接方式，只传递文件名
            const fileName = info.value.zhihuodongPhoto ?
                info.value.zhihuodongPhoto.split('/').pop() :
                null
            const imageUrl = fileName ?
                `/file/download?fileName=${fileName}` :
                '无图片'
            console.log('图片文件名:', fileName)
            console.log('图片完整URL:', imageUrl)

            // 处理富文本内容中的图片路径
            if (info.value.zhihuodongContent) {
                info.value.zhihuodongContent = info.value.zhihuodongContent.replaceAll("src=\"upload/", "src=\"" + baseUrl + "upload/")
            }
        } else {
            console.error('获取活动信息失败，错误码:', res.code, '错误信息:', res.msg)
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('获取活动详情失败:', error)
        ElMessage.error('获取活动详情失败')
    }
}

const goBack = () => {
    router.go(-1)
}

const handleImageError = (e) => {
    console.error('图片加载失败:', e)
    console.error('图片路径:', info.value.zhihuodongPhoto)
    const fileName = info.value.zhihuodongPhoto ?
        info.value.zhihuodongPhoto.split('/').pop() :
        null
    const imageUrl = fileName ?
        `/file/download?fileName=${fileName}` :
        '无图片'
    console.error('图片文件名:', fileName)
    console.error('完整URL:', imageUrl)
}
</script>

<style scoped>
.info-container {
    padding: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}
</style>