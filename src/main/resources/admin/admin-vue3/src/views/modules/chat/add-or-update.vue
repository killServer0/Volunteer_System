<template>
    <div>
        <el-form class="detail-form-content" ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="80px">
            <div class="chat-content">
                <div v-for="item in dataList" :key="item.id">
                    <div v-if="item.chatIssue" class="left-content">
                        <el-alert class="text-content" :title="item.chatIssue" :closable="false" type="success" />
                    </div>
                    <div v-else class="right-content">
                        <el-alert class="text-content" :title="item.chatReply" :closable="false" type="warning" />
                    </div>
                    <div class="clear-float"></div>
                </div>
            </div>
            <div class="clear-float"></div>
            <el-form-item label="回复" prop="chatReply">
                <el-input v-model="ruleForm.chatReply" placeholder="回复" clearable />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">回复</el-button>
                <el-button @click="back">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'

const emit = defineEmits(['update:showFlag', 'refreshList'])

const props = defineProps({
    parent: {
        type: Object,
        required: true
    }
})

const ruleFormRef = ref(null)
const id = ref('')
const ruleForm = reactive({
    chatReply: '',
    zhiyuanzheId: '',
    id: ''
})
const dataList = ref([])
const chatDate = reactive({})
let inter = null

const rules = {
    chatReply: [
        { required: true, message: '回复内容不能为空', trigger: 'blur' }
    ]
}

// 获取当前日期时间
const getCurDateTime = () => {
    const date = new Date()
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 获取聊天列表
const getList = async () => {
    try {
        console.log('获取聊天列表，参数:', { zhiyuanzheId: id.value })
        const response = await http({
            url: 'chat/page',
            method: 'get',
            params: {
                zhiyuanzheId: id.value,
                order: 'asc'
            }
        })
        console.log('获取聊天列表响应:', response)

        if (response && response.data) {
            ruleForm.zhiyuanzheId = id.value
            dataList.value = response.data.list || []
            console.log('聊天列表数据:', dataList.value)
        } else {
            console.warn('获取聊天列表失败:', response)
            ElMessage.error('获取聊天列表失败')
        }
    } catch (error) {
        console.error('获取聊天列表失败:', error)
        ElMessage.error('获取聊天列表失败')
    }
}

// 初始化
const init = (row) => {
    console.log('初始化回复表单，接收到的数据:', row)
    Object.assign(chatDate, row)
    id.value = row.zhiyuanzheId
    ruleForm.id = row.id // 添加聊天记录ID
    ruleForm.zhiyuanzheId = row.zhiyuanzheId // 添加志愿者ID
    getList()

    // 设置定时刷新
    inter = setInterval(() => {
        getList()
    }, 5000)
}

// 提交
const onSubmit = async () => {
    if (!ruleFormRef.value) return

    await ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            console.log('提交回复，表单数据:', ruleForm)
            const submitData = {
                id: ruleForm.id,
                zhiyuanzheId: ruleForm.zhiyuanzheId,
                chatReply: ruleForm.chatReply,
                replyTime: getCurDateTime(),
                chatTypes: 2,
                zhuangtaiTypes: 2 // 设置为已回复状态
            }
            console.log('提交数据:', submitData)

            try {
                const response = await http({
                    url: 'chat/update',
                    method: 'post',
                    data: submitData
                })
                console.log('提交回复响应:', response)

                if (response && response.code === 0) {
                    ElMessage({
                        message: '回复成功',
                        type: 'success',
                        duration: 1500,
                        onClose: () => {
                            // 清除定时器
                            if (inter) {
                                clearInterval(inter)
                            }
                            // 清空表单
                            ruleForm.chatReply = ''
                            // 通知父组件更新状态
                            emit('update:showFlag', false)
                            emit('refreshList')
                        }
                    })
                } else {
                    console.warn('提交回复失败:', response)
                    ElMessage.error(response.msg || '回复失败')
                }
            } catch (error) {
                console.error('提交回复失败:', error)
                ElMessage.error('提交回复失败')
            }
        }
    })
}

// 返回
const back = () => {
    console.log('返回列表页面')
    // 清除定时器
    if (inter) {
        clearInterval(inter)
    }
    // 清空表单
    ruleForm.chatReply = ''
    // 通知父组件更新状态
    emit('update:showFlag', false)
    emit('refreshList')
}

// 组件卸载前清除定时器
onBeforeUnmount(() => {
    if (inter) {
        clearInterval(inter)
    }
})

// 暴露方法给父组件
defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.chat-content {
    margin-left: 80px;
    padding-bottom: 60px;
    width: 500px;
    margin-bottom: 30px;
    max-height: 300px;
    height: 300px;
    overflow-y: scroll;
    border: 1px solid #eeeeee;

    .left-content {
        float: left;
        margin-bottom: 10px;
        padding: 10px;
    }

    .right-content {
        float: right;
        margin-bottom: 10px;
        padding: 10px;
    }
}

.clear-float {
    clear: both;
}
</style>