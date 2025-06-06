<template>
    <div class="main-content">
        <!-- 列表页 -->
        <div v-if="!showFlag">
            <!-- 筛选条件 -->
            <div class="filter-container">
                <el-select v-model="filterStatus" placeholder="筛选状态" clearable @change="handleFilter">
                    <el-option label="全部" value="" />
                    <el-option label="未回复" :value="1" />
                    <el-option label="已回复" :value="2" />
                </el-select>
            </div>

            <div class="table-content">
                <el-table :data="filteredDataList" empty-text="暂无消息" border v-loading="dataListLoading"
                    style="width: 100%;">
                    <el-table-column prop="zhiyuanzheName" label="志愿者" header-align="center" align="center" width="120">
                    </el-table-column>
                    <el-table-column label="消息内容" header-align="center" align="center">
                        <template #default="scope">
                            <div v-if="scope.row.chatIssue" class="message-content question">
                                <span class="message-text">{{ scope.row.chatIssue }}</span>
                                <span class="message-time">{{ formatTime(scope.row.issueTime) }}</span>
                            </div>
                            <div v-if="scope.row.chatReply" class="message-content reply">
                                <span class="message-text">{{ scope.row.chatReply }}</span>
                                <span class="message-time">{{ formatTime(scope.row.replyTime) }}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="状态" header-align="center" align="center" width="100">
                        <template #default="scope">
                            <el-tag :type="getStatusType(scope.row.zhuangtaiTypes)">
                                {{ getStatusText(scope.row.zhuangtaiTypes) }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" header-align="center" align="center" width="100" label="操作">
                        <template #default="scope">
                            <el-button type="primary" link @click="addOrUpdateHandler(scope.row)">
                                <el-icon><Edit /></el-icon>
                                回复
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination @size-change="sizeChangeHandle" @current-change="currentChangeHandle"
                    :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize" :total="totalPage"
                    layout="total, sizes, prev, pager, next, jumper" class="pagination-content" />
            </div>
        </div>
        <!-- 添加/修改页面 -->
        <add-or-update v-else :parent="this" ref="addOrUpdateRef" 
            @update:showFlag="showFlag = $event"
            @refreshList="getDataList" />
    </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { Edit } from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import AddOrUpdate from './add-or-update.vue'
import dayjs from 'dayjs'

const searchForm = reactive({})
const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const showFlag = ref(false)
const dataListSelections = ref([])
const addOrUpdateRef = ref(null)
const filterStatus = ref('')
let inter = null

// 计算属性：根据筛选条件过滤数据
const filteredDataList = computed(() => {
    if (!filterStatus.value) {
        return dataList.value
    }
    return dataList.value.filter(item => item.zhuangtaiTypes === filterStatus.value)
})

// 处理筛选
const handleFilter = () => {
    // 重置页码
    pageIndex.value = 1
    getDataList()
}

// 获取数据列表
const getDataList = async () => {
    dataListLoading.value = true
    try {
        const params = {
            page: pageIndex.value,
            limit: pageSize.value,
            sort: 'id',
            order: 'desc'
        }
        // 如果选择了筛选状态，添加到查询参数
        if (filterStatus.value) {
            params.zhuangtaiTypes = filterStatus.value
        }
        
        console.log('发送请求参数:', params)
        const response = await http({
            url: 'chat/page',
            method: 'get',
            params
        })

        // 直接使用后端返回的数据
        if (response && response.data) {
            const data = response.data
            dataList.value = data.list || []
            totalPage.value = data.total || 0
        } else {
            console.warn('数据格式不正确:', JSON.stringify(response, null, 2))
            dataList.value = []
            totalPage.value = 0
        }
    } catch (error) {
        console.error('获取聊天列表失败:', error)
        dataList.value = []
        totalPage.value = 0
    } finally {
        dataListLoading.value = false
    }
}

// 每页数
const sizeChangeHandle = (val) => {
    pageSize.value = val
    pageIndex.value = 1
    getDataList()
}

// 当前页
const currentChangeHandle = (val) => {
    pageIndex.value = val
    getDataList()
}

// 回复
const addOrUpdateHandler = (row) => {
    console.log('点击回复按钮，当前行数据:', row)
    showFlag.value = true
    nextTick(() => {
        console.log('初始化回复表单，传递数据:', row)
        addOrUpdateRef.value.init(row)
    })
}

// 格式化时间
const formatTime = (time) => {
    if (!time) return ''
    try {
        return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
    } catch (e) {
        console.error('时间格式化错误:', e, time)
        return ''
    }
}

// 获取状态类型
const getStatusType = (type) => {
    switch (type) {
        case 1:
            return 'warning' // 未回复
        case 2:
            return 'success' // 已回复
        default:
            return 'info'
    }
}

// 获取状态文本
const getStatusText = (type) => {
    switch (type) {
        case 1:
            return '未回复'
        case 2:
            return '已回复'
        default:
            return '未知'
    }
}

onMounted(() => {
    getDataList()
    // 设置定时刷新，每30秒刷新一次
    inter = setInterval(() => {
        getDataList()
    }, 30000)
})

onBeforeUnmount(() => {
    // 清除定时器
    if (inter) {
        clearInterval(inter)
    }
})
</script>

<style lang="scss" scoped>
.filter-container {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
}

.message-content {
    display: flex;
    flex-direction: column;
    padding: 10px;
    border-radius: 4px;
    margin: 5px 0;

    &.question {
        background-color: #f5f7fa;
        margin-right: 20%;
    }

    &.reply {
        background-color: #e6f7ff;
        margin-left: 20%;
    }

    .message-text {
        font-size: 14px;
        color: #333;
        margin-bottom: 5px;
        word-break: break-all;
    }

    .message-time {
        font-size: 12px;
        color: #999;
        text-align: right;
    }
}

.el-pagination {
    margin-top: 20px;
    justify-content: center;
}
</style>