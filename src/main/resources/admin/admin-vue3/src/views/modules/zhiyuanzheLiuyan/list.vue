<template>
    <div class="app-container">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
            <el-form-item label="留言内容">
                <el-input v-model="queryParams.zhiyuanzheLiuyanText" placeholder="请输入留言内容" clearable />
            </el-form-item>
            <el-form-item label="留言人">
                <el-input v-model="queryParams.feizhiyuanName" placeholder="请输入留言人" clearable />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleQuery">查询</el-button>
                <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
            </el-col>
        </el-row>

        <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="留言内容" align="center" prop="zhiyuanzheLiuyanText" />
            <el-table-column label="留言人" align="center" prop="feizhiyuanName" />
            <el-table-column label="留言时间" align="center" prop="insertTime" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template #default="scope">
                    <el-button type="text" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
                    <el-button type="text" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination
            v-if="total > 0"
            :total="total"
            v-model:current-page="queryParams.page"
            v-model:page-size="queryParams.limit"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />

        <!-- 添加或修改留言对话框 -->
        <el-dialog :title="title" v-model="open" width="500px" append-to-body>
            <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="留言内容" prop="zhiyuanzheLiuyanText">
                    <el-input type="textarea" v-model="form.zhiyuanzheLiuyanText" placeholder="请输入留言内容" />
                </el-form-item>
                <el-form-item label="留言人" prop="feizhiyuanId">
                    <el-input v-model="form.feizhiyuanId" placeholder="请输入留言人" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const title = ref('')
const open = ref(false)
const single = ref(true)
const multiple = ref(true)
const ids = ref([])

const queryParams = ref({
    page: 1,
    limit: 10,
    zhiyuanzheLiuyanText: '',
    feizhiyuanName: ''
})

const form = reactive({
    id: undefined,
    zhiyuanzheLiuyanText: '',
    feizhiyuanId: undefined,
    zhiyuanzheId: undefined,
    insertTime: new Date(),
    createTime: new Date()
})

const rules = reactive({
    zhiyuanzheLiuyanText: [{ required: true, message: '留言内容不能为空', trigger: 'blur' }],
    feizhiyuanId: [{ required: true, message: '留言人不能为空', trigger: 'blur' }]
})

const getList = async () => {
    loading.value = true
    try {
        console.log('Fetching messages with params:', queryParams.value)
        const res = await http.get('/zhiyuanzheLiuyan/page', {
            params: queryParams.value
        })
        console.log('API Response:', res)
        console.log('Response data:', res.data)
        if (res.code === 0 && res.data) {
            console.log('Response data structure:', {
                list: res.data.list,
                total: res.data.total,
                pageSize: res.data.pageSize,
                currPage: res.data.currPage,
                totalPage: res.data.totalPage
            })
            list.value = res.data.list || []
            total.value = res.data.total || 0
            console.log('Processed list data:', list.value)
        } else {
            console.error('Invalid response format:', res)
            list.value = []
            total.value = 0
        }
    } catch (error) {
        console.error('Error fetching messages:', error)
        console.error('Error details:', {
            message: error.message,
            response: error.response,
            status: error.response?.status,
            data: error.response?.data
        })
        list.value = []
        total.value = 0
    } finally {
        loading.value = false
    }
}

const handleQuery = () => {
    queryParams.value.page = 1
    getList()
}

const resetQuery = () => {
    queryParams.value.zhiyuanzheLiuyanText = ''
    queryParams.value.feizhiyuanName = ''
    handleQuery()
}

const handleSelectionChange = (selection) => {
    ids.value = selection.map(item => item.id)
    single.value = selection.length !== 1
    multiple.value = !selection.length
}

const handleAdd = () => {
    title.value = '添加留言'
    open.value = true
}

const handleUpdate = (row) => {
    title.value = '修改留言'
    open.value = true
    Object.assign(form, row)
}

const handleDelete = (row) => {
    const id = row.id || ids.value
    ElMessageBox.confirm('是否确认删除选中的数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            // 将单个ID转换为数组格式
            const deleteIds = Array.isArray(id) ? id : [id]
            await http.delete('/zhiyuanzheLiuyan/delete', {
                data: deleteIds
            })
            ElMessage.success('删除成功')
            getList()
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败：' + (error.response?.data?.msg || error.message))
        }
    }).catch(() => {
        ElMessage.info('已取消删除')
    })
}

const submitForm = async () => {
    try {
        console.log('Submitting form:', form)
        if (form.id) {
            await http.put('/zhiyuanzheLiuyan/update', form)
            ElMessage.success('修改成功')
        } else {
            const role = localStorage.getItem('role')
            if (role === '志愿者') {
                form.zhiyuanzheId = parseInt(localStorage.getItem('userId'))
            } else if (role === '游客') {
                form.feizhiyuanId = parseInt(localStorage.getItem('userId'))
            }
            await http.post('/zhiyuanzheLiuyan/save', form)
            ElMessage.success('新增成功')
        }
        open.value = false
        getList()
    } catch (error) {
        console.error('提交失败:', error)
        console.error('Error details:', {
            message: error.message,
            response: error.response,
            status: error.response?.status,
            data: error.response?.data
        })
    }
}

const cancel = () => {
    open.value = false
    reset()
}

const reset = () => {
    form.id = undefined
    form.zhiyuanzheLiuyanText = ''
    form.feizhiyuanId = undefined
    form.zhiyuanzheId = undefined
}

const handleSizeChange = (val) => {
    queryParams.value.limit = val
    getList()
}

const handleCurrentChange = (val) => {
    queryParams.value.page = val
    getList()
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