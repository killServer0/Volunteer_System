<template>
    <div class="main-content">
        <el-card class="box-card">
            <template #header>
                <div class="card-header">
                    <div class="left">
                        <span class="title">轮播图管理</span>
                        <el-input
                            v-model="searchForm.name"
                            placeholder="请输入名称搜索"
                            style="width: 200px; margin-left: 20px"
                            clearable
                            @keyup.enter="handleSearch"
                        />
                        <el-button type="primary" style="margin-left: 10px" @click="handleSearch">搜索</el-button>
                        <el-button @click="resetSearch">重置</el-button>
                    </div>
                    <el-button type="primary" @click="handleAdd">新增轮播图</el-button>
                </div>
            </template>
            
            <el-table 
                :data="tableData" 
                style="width: 100%" 
                v-loading="loading"
                border
                stripe
            >
                <el-table-column type="index" label="序号" width="60" align="center" />
                <el-table-column prop="name" label="轮播图名称" min-width="120" align="center" show-overflow-tooltip />
                <el-table-column label="轮播图" width="200" align="center">
                        <template #default="scope">
                        <el-image 
                            v-if="scope.row.value"
                            :src="getImageUrl(scope.row.value)"
                            style="width: 120px; height: 80px"
                            :preview-src-list="[getImageUrl(scope.row.value)]"
                            fit="cover"
                            :initial-index="0"
                            preview-teleported
                        />
                        <span v-else>无图片</span>
                        </template>
                    </el-table-column>
                <el-table-column label="创建人" width="120" align="center">
                    <template #default>
                        admin
                        </template>
                    </el-table-column>
                <el-table-column label="操作" width="250" fixed="right" align="center">
                        <template #default="scope">
                        <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
                        <el-button type="primary" link @click="handlePreview(scope.row)">预览</el-button>
                        <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>

            <!-- 分页 -->
            <div class="pagination-container">
                <el-pagination
                    v-model:current-page="page"
                    v-model:page-size="pageSize"
                    :page-sizes="[10, 20, 50, 100]"
                    :total="total"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                />
            </div>
        </el-card>

        <!-- 新增/编辑对话框 -->
        <el-dialog
            v-model="dialogVisible"
            :title="dialogType === 'add' ? '新增轮播图' : '编辑轮播图'"
            width="600px"
            :close-on-click-modal="false"
            destroy-on-close
        >
            <el-form 
                ref="formRef"
                :model="form" 
                :rules="rules"
                label-width="100px"
                class="dialog-form"
            >
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入名称" maxlength="50" show-word-limit />
                </el-form-item>
                <el-form-item label="轮播图" prop="value">
                    <el-upload
                        class="avatar-uploader"
                        :action="uploadUrl"
                        :show-file-list="false"
                        :on-success="handleUploadSuccess"
                        :before-upload="beforeUpload"
                        :headers="uploadHeaders"
                        accept="image/jpeg,image/png,image/gif"
                    >
                        <img v-if="form.value" :src="getImageUrl(form.value)" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                    </el-upload>
                    <div class="upload-tip">
                        <p>建议尺寸：1920x600 像素</p>
                        <p>支持格式：JPG、PNG、GIF</p>
                        <p>文件大小：不超过2MB</p>
        </div>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 预览对话框 -->
        <el-dialog
            v-model="previewVisible"
            title="轮播图预览"
            width="800px"
            destroy-on-close
        >
            <div class="preview-container">
                <el-image
                    v-if="previewImage"
                    :src="getImageUrl(previewImage)"
                    fit="contain"
                    style="width: 100%; max-height: 500px"
                />
            </div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import { getToken } from '@/utils/auth'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const submitLoading = ref(false)

// 分页相关
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 上传接口完整地址
const uploadUrl = 'http://localhost:8081/zhiyuanzheguanlixitong/file/upload'

// 搜索表单
const searchForm = reactive({
    name: ''
})

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const form = reactive({
    id: '',
    name: '',
    value: ''
})

// 表单验证规则
const rules = {
    name: [
        { required: true, message: '请输入名称', trigger: 'blur' },
        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
    ],
    value: [
        { required: true, message: '请上传轮播图', trigger: 'change' }
    ]
}

// 预览相关
const previewVisible = ref(false)
const previewImage = ref('')

// 上传相关
const uploadHeaders = computed(() => ({
    token: getToken()
}))

// 获取图片URL
const getImageUrl = (value) => {
    if (!value) return ''
    let fileName = value
    if (fileName.startsWith('upload/')) fileName = fileName.substring(7)
    return `/file/download?fileName=${fileName}`
}

// 获取列表数据
const getList = async () => {
    loading.value = true
    try {
        const params = {
            page: page.value,
            limit: pageSize.value
            // 移除name参数，因为后端不处理
        }
        console.log('请求参数:', params)
        
        // 增加调试信息
        console.log('请求基础URL:', http.defaults?.baseURL || '未设置')
        
        // 尝试使用原始路径
        const res = await http.get('/config/list', { params })
        console.log('接口返回:', res)
        
        if (res.code === 0) {
            // 先获取所有数据
            const allData = res.data.list || []
            // 记录总数据量
            const totalCount = res.data.total || 0
            
            // 如果有搜索关键词，前端自行过滤
            if (searchForm.name && searchForm.name.trim()) {
                const keyword = searchForm.name.trim().toLowerCase()
                console.log('搜索关键词:', keyword)
                
                // 前端过滤匹配的数据
                const filteredData = allData.filter(item => 
                    item.name && item.name.toLowerCase().includes(keyword)
                )
                
                console.log('前端过滤前数据条数:', allData.length)
                console.log('前端过滤后数据条数:', filteredData.length)
                
                // 使用过滤后的数据
                tableData.value = filteredData
                // 更新总数为过滤后的数量
                total.value = filteredData.length
            } else {
                // 没有搜索词，显示所有数据
                tableData.value = allData
                total.value = totalCount
            }
            
            console.log('最终显示数据:', tableData.value)
        } else {
            ElMessage.error(res.msg || '获取数据失败')
        }
    } catch (error) {
        console.error('获取数据失败:', error)
        ElMessage.error('获取数据失败')
    } finally {
        loading.value = false
    }
}

// 搜索
const handleSearch = () => {
    page.value = 1
    getList()
}

// 重置搜索
const resetSearch = () => {
    searchForm.name = ''
    handleSearch()
}

// 分页大小改变
const handleSizeChange = (val) => {
    pageSize.value = val
    getList()
}

// 页码改变
const handleCurrentChange = (val) => {
    page.value = val
    getList()
}

// 新增
const handleAdd = () => {
    dialogType.value = 'add'
    form.id = ''
    form.name = ''
    form.value = ''
    dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
    dialogType.value = 'edit'
    form.id = row.id
    form.name = row.name
    form.value = row.value
    dialogVisible.value = true
}

// 预览
const handlePreview = (row) => {
    previewImage.value = row.value
    previewVisible.value = true
}

// 删除
const handleDelete = (row) => {
    ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            const res = await http.post('/config/delete', [row.id])
            if (res.code === 0) {
                ElMessage.success('删除成功')
                getList()
            } else {
                ElMessage.error(res.msg || '删除失败')
            }
        } catch (error) {
            console.error('删除失败:', error)
            ElMessage.error('删除失败')
        }
    })
}

// 上传前校验
const beforeUpload = (file) => {
    const isImage = file.type.startsWith('image/')
    if (!isImage) {
        ElMessage.error('只能上传图片文件！')
        return false
    }
    const isLt2M = file.size / 1024 / 1024 < 2
    if (!isLt2M) {
        ElMessage.error('图片大小不能超过 2MB！')
        return false
    }
    return true
}

// 上传成功回调
const handleUploadSuccess = (res) => {
    console.log('上传返回:', res)
    if (res.code === 0) {
        form.value = res.file || res.data
        console.log('设置图片路径:', form.value)
        formRef.value?.clearValidate('value')
        ElMessage.success('上传成功')
    } else {
        ElMessage.error(res.msg || '上传失败')
    }
}

// 提交表单
const handleSubmit = async () => {
    if (!formRef.value) return
    
    try {
        await formRef.value.validate()
        loading.value = true
        
        // 处理照片路径
        const submitForm = { ...form }
        console.log('提交前的表单数据:', submitForm)
        
        if (submitForm.value && submitForm.value !== 'undefined') {
            // 确保路径格式正确，添加upload/前缀
            if (!submitForm.value.startsWith('upload/')) {
                submitForm.value = 'upload/' + submitForm.value
            }
            console.log('处理后的图片路径:', submitForm.value)
        } else {
            submitForm.value = ''  // 设置为空字符串而不是 'undefined'
        }
        
        const url = submitForm.id ? '/config/update' : '/config/save'
        console.log('提交URL:', url)
        console.log('提交数据:', submitForm)
        
        const res = await http.post(url, submitForm)
        console.log('提交响应:', res)

        if (res.code === 0) {
            ElMessage.success(submitForm.id ? '修改成功' : '新增成功')
            dialogVisible.value = false
            getList()
        } else {
            ElMessage.error(res.msg)
        }
    } catch (error) {
        console.error('提交失败:', error)
        console.error('具体错误信息:', error.message, error.response?.data)
        console.error('请求配置:', error.config)
        ElMessage.error(`提交失败: ${error.message}`)
    } finally {
        loading.value = false
    }
}

// 初始化
onMounted(() => {
    getList()
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

.card-header .left {
    display: flex;
    align-items: center;
}

.card-header .title {
    font-size: 16px;
    font-weight: bold;
}

.pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.dialog-form {
    padding: 20px;
}

.avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 360px;
    height: 120px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.avatar-uploader:hover {
    border-color: #409eff;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 360px;
    height: 120px;
    text-align: center;
    line-height: 120px;
    }

.avatar {
    width: 360px;
    height: 120px;
    display: block;
    object-fit: cover;
}

.upload-tip {
    margin-top: 10px;
    color: #909399;
    font-size: 12px;
    line-height: 1.5;
}

.preview-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 200px;
    background-color: #f5f7fa;
    border-radius: 4px;
}
</style>