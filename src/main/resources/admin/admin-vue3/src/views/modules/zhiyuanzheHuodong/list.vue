<template>
    <div class="app-container">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
            <el-form-item label="活动名称">
                <el-input v-model="queryParams.name" placeholder="请输入活动名称" clearable />
            </el-form-item>
            <el-form-item label="活动状态">
                <el-select v-model="queryParams.status" placeholder="请选择活动状态" clearable>
                    <el-option label="未开始" value="0" />
                    <el-option label="进行中" value="1" />
                    <el-option label="已结束" value="2" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleQuery">查询</el-button>
                <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain @click="handleAdd">
                    <el-icon><Plus /></el-icon>新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain :disabled="single" @click="handleUpdate">
                    <el-icon><Edit /></el-icon>修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain :disabled="multiple" @click="handleDelete">
                    <el-icon><Delete /></el-icon>删除
                </el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="活动名称" align="center" prop="name" />
            <el-table-column label="活动地点" align="center" prop="location" />
            <el-table-column label="活动时间" align="center" prop="time" />
            <el-table-column label="活动状态" align="center" prop="status">
                <template #default="scope">
                    <el-tag
                        :type="getStatusType(scope.row.status)">
                        {{ getStatusText(scope.row.status) }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button type="primary" link @click="handleUpdate(scope.row)">
                        <el-icon><Edit /></el-icon>修改
                    </el-button>
                    <el-button type="danger" link @click="handleDelete(scope.row)">
                        <el-icon><Delete /></el-icon>删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.page"
            v-model:limit="queryParams.limit" @pagination="getList" />

        <!-- 添加或修改活动对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="活动名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入活动名称" />
                </el-form-item>
                <el-form-item label="活动地点" prop="location">
                    <el-input v-model="form.location" placeholder="请输入活动地点" />
                </el-form-item>
                <el-form-item label="活动时间" prop="time">
                    <el-date-picker v-model="form.time" type="datetime" placeholder="选择活动时间"
                        value-format="YYYY-MM-DD HH:mm:ss" />
                </el-form-item>
                <el-form-item label="活动状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择活动状态">
                        <el-option label="未开始" value="0" />
                        <el-option label="进行中" value="1" />
                        <el-option label="已结束" value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="活动描述" prop="description">
                    <el-input type="textarea" v-model="form.description" placeholder="请输入活动描述" :rows="4" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/utils/http'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const title = ref('')
const open = ref(false)
const single = ref(true)
const multiple = ref(true)
const ids = ref([])

const queryParams = reactive({
    page: 1,
    limit: 10,
    name: '',
    status: ''
})

const form = reactive({
    id: undefined,
    name: '',
    location: '',
    time: '',
    status: '',
    description: ''
})

const rules = reactive({
    name: [{ required: true, message: '活动名称不能为空', trigger: 'blur' }],
    location: [{ required: true, message: '活动地点不能为空', trigger: 'blur' }],
    time: [{ required: true, message: '活动时间不能为空', trigger: 'change' }],
    status: [{ required: true, message: '活动状态不能为空', trigger: 'change' }],
    description: [{ required: true, message: '活动描述不能为空', trigger: 'blur' }]
})

const getList = async () => {
    loading.value = true
    try {
        const res = await http.get('/zhiyuanzheHuodong/page', { 
            params: {
                ...queryParams,
                page: queryParams.page - 1
            }
        })
        if (res.code === 0) {
            list.value = res.data.list
            total.value = res.data.total
        }
    } catch (error) {
        console.error('获取列表失败:', error)
    } finally {
        loading.value = false
    }
}

const handleQuery = () => {
    queryParams.page = 1
    getList()
}

const resetQuery = () => {
    queryParams.name = ''
    queryParams.status = ''
    handleQuery()
}

const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.id)
    single.value = selection.length !== 1
    multiple.value = !selection.length
}

const handleAdd = () => {
    title.value = '添加活动'
    open.value = true
}

const getStatusType = (status) => {
    const statusNum = Number(status || 0)
    switch (statusNum) {
        case 0:
            return 'info'
        case 1:
            return 'success'
        case 2:
            return 'warning'
        default:
            return 'info'
    }
}

const getStatusText = (status) => {
    const statusNum = Number(status || 0)
    switch (statusNum) {
        case 0:
            return '未开始'
        case 1:
            return '进行中'
        case 2:
            return '已结束'
        default:
            return '未知'
    }
}

const handleUpdate = (row) => {
    title.value = '修改活动'
    open.value = true
    nextTick(() => {
        form.value = {
            ...row,
            status: Number(row.status || 0)
        }
    })
}

const handleDelete = (row) => {
    const id = row.id || ids.value
    ElMessageBox.confirm('是否确认删除选中的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post('/zhiyuanzheHuodong/delete', { ids: Array.isArray(id) ? id : [id] })
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getList()
            }
        } catch (error) {
            console.error('删除失败:', error)
        }
    })
}

const submitForm = async () => {
    try {
        const submitData = {
            ...form.value,
            status: Number(form.value.status || 0)
        }
        const url = submitData.id ? '/zhiyuanzheHuodong/update' : '/zhiyuanzheHuodong/save'
        const res = await http.post(url, submitData)
        if (res.code === 0) {
            ElMessage.success(submitData.id ? '修改成功' : '新增成功')
            open.value = false
            getList()
        }
    } catch (error) {
        console.error('提交失败:', error)
    }
}

const cancel = () => {
    open.value = false
    reset()
}

const reset = () => {
    form.id = undefined
    form.name = ''
    form.location = ''
    form.time = ''
    form.status = ''
    form.description = ''
}

onMounted(() => {
    getList()
})
</script>

<style scoped>
.app-container {
    padding: 20px;
}
</style>