<template>
    <div class="list-container">
        <el-card class="box-card" :style="contents.cardStyle">
            <template #header>
                <div class="card-header">
                    <span>活动列表</span>
                    <el-button type="primary" @click="handleAdd" :style="contents.buttonStyle">新增活动</el-button>
                </div>
            </template>
            <el-table :data="tableData" border :style="contents.tableStyle">
                <el-table-column prop="zhihuodongName" label="活动名称"
                    :style="contents.tableHeaderStyle"></el-table-column>
                <el-table-column prop="zhihuodongTypes" label="活动类型" width="120">
                    <template #default="scope">
                        <el-tag :type="getActivityTypeStyle(scope.row.zhihuodongTypes)">
                            {{ getActivityType(scope.row.zhihuodongTypes) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="zhihuodongAddress" label="活动地点"
                    :style="contents.tableHeaderStyle"></el-table-column>
                <el-table-column prop="zhihuodongKucunNumber" label="活动人数"
                    :style="contents.tableHeaderStyle"></el-table-column>
                <el-table-column prop="zhihuodongTiaojian" label="参加条件"
                    :style="contents.tableHeaderStyle"></el-table-column>
                <el-table-column prop="huodong_status" label="活动状态" width="100">
                    <template #default="scope">
                        <el-tag :type="getStatusType(scope.row.huodong_status)">
                            {{ getStatusText(scope.row.huodong_status) }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="200" :style="contents.tableHeaderStyle">
                    <template #default="scope">
                        <el-button type="text" @click="handleEdit(scope.row)"
                            :style="contents.buttonStyle">编辑</el-button>
                        <el-button type="text" @click="handleDelete(scope.row)"
                            :style="contents.buttonStyle">删除</el-button>
                        <el-button type="text" @click="handleView(scope.row)"
                            :style="contents.buttonStyle">查看</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination-container">
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="[10, 20, 50, 100]" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total" :style="contents.paginationStyle">
                </el-pagination>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getToken } from '@/utils/auth'
import styleJs from '@/utils/style'
import http from '@/utils/http'

const contents = reactive(styleJs)
const router = useRouter()
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
    console.log('组件已挂载，开始获取数据')
    console.log('当前token:', getToken())
    getList()
})

const getList = async () => {
    try {
        console.log('开始请求数据，参数:', {
            page: currentPage.value,
            limit: pageSize.value
        })

        const response = await http({
            url: '/zhihuodong/page',
            method: 'get',
            params: {
                page: currentPage.value,
                limit: pageSize.value
            }
        })

        console.log('API响应数据:', response)

        if (response && response.code === 0) {
            console.log('数据列表:', response.data.list)
            console.log('总记录数:', response.data.total)
            tableData.value = response.data.list
            total.value = response.data.total
        } else {
            console.error('API返回错误:', response)
            ElMessage.error(response.msg || '获取数据失败')
        }
    } catch (error) {
        console.error('获取活动列表失败:', error)
        ElMessage.error('获取活动列表失败')
    }
}

const handleAdd = () => {
    console.log('handleAdd token:', getToken())
    console.log('准备跳转到新增页面')
    router.push({
        name: 'ZhihuodongAddOrUpdate'
    })
}

const handleEdit = (row) => {
    console.log('handleEdit token:', getToken())
    console.log('准备跳转到编辑页面，id:', row.id)
    router.push({
        name: 'ZhihuodongAddOrUpdate',
        query: { id: row.id }
    })
}

const handleDelete = (row) => {
    console.log('handleDelete token:', getToken())
    ElMessageBox.confirm('确定要删除该活动吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            await http({
                url: '/zhihuodong/delete',
                method: 'post',
                data: [row.id]
            })
            ElMessage.success('删除成功')
            getList()
        } catch (error) {
            console.error('删除活动失败:', error)
            ElMessage.error('删除活动失败')
        }
    })
}

const handleView = (row) => {
    console.log('handleView token:', getToken())
    console.log('准备跳转到详情页面，id:', row.id)
    router.push({
        name: 'ZhihuodongInfo',
        params: { id: row.id }
    })
}

const handleSizeChange = (val) => {
    pageSize.value = val
    getList()
}

const handleCurrentChange = (val) => {
    currentPage.value = val
    getList()
}

const getStatusType = (status) => {
    const types = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'danger'
    }
    return types[status] || 'info'
}

const getStatusText = (status) => {
    const texts = {
        1: '待审核',
        2: '未开始',
        3: '进行中',
        4: '已结束'
    }
    return texts[status] || '未知'
}

const getActivityType = (type) => {
    if (!type) return '未知类型';
    
    const types = {
        1: '公益活动',
        2: '社会实践',
        3: '志愿服务',
        4: '文化活动',
        5: '培训活动',
        6: '其他活动'
    }
    return types[type] || '其他类型'
}

const getActivityTypeStyle = (type) => {
    const styles = {
        1: 'success',
        2: 'warning',
        3: 'primary',
        4: 'info',
        5: 'danger',
        6: ''
    }
    return styles[type] || ''
}
</script>

<style scoped>
.list-container {
    padding: 20px;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}
</style>