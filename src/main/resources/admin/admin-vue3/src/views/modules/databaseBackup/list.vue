<template>
  <div class="main-content">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="left">
            <span class="title">数据库备份管理</span>
          </div>
          <el-button type="primary" @click="handleBackup">创建备份</el-button>
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
        <el-table-column prop="fileName" label="备份文件名" min-width="150" align="center" show-overflow-tooltip />
        <el-table-column prop="fileSize" label="文件大小" width="120" align="center">
          <template #default="scope">
            {{ formatFileSize(scope.row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="创建人" width="120" align="center">
          <template #default>
            admin
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleDownload(scope.row)">下载</el-button>
            <el-button type="success" link @click="handleRestore(scope.row)">恢复</el-button>
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
    
    <!-- 创建备份的抽屉 -->
    <el-drawer
      v-model="backupDrawerVisible"
      title="创建数据库备份"
      size="40%"
      :destroy-on-close="true"
    >
      <beifen-form @success="onBackupSuccess" />
    </el-drawer>
    
    <!-- 恢复备份的抽屉 -->
    <el-drawer
      v-model="restoreDrawerVisible"
      title="恢复数据库备份"
      size="40%"
      :destroy-on-close="true"
    >
      <huanyuan-form 
        :file-name="selectedFile?.fileName" 
        @success="onRestoreSuccess" 
      />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, ElLoading } from 'element-plus'
import { http } from '@/utils/http'
import BeifenForm from './beifen.vue'
import HuanyuanForm from './huanyuan.vue'

// 表格数据
const tableData = ref([])
const loading = ref(false)

// 分页相关
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 抽屉控制
const backupDrawerVisible = ref(false)
const restoreDrawerVisible = ref(false)
const selectedFile = ref(null)

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '0 KB'
  
  if (size < 1024) {
    return size + ' B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  } else {
    return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
  }
}

// 获取列表数据 - 从后端获取备份文件列表
const getList = async () => {
  loading.value = true
  try {
    // 构建查询参数
    const params = {
      page: page.value,
      limit: pageSize.value,
      savePath: 'E:/backup' // 默认备份路径，和备份表单中保持一致
    }
    
    const res = await http.get('/backup/list', { params })
    
    if (res.code === 0) {
      tableData.value = res.data.list || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '获取数据失败')
      // 如果获取失败，使用模拟数据
      fallbackToMockData()
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
    // 如果发生错误，使用模拟数据
    fallbackToMockData()
  } finally {
    loading.value = false
  }
}

// 使用模拟数据作为后备
const fallbackToMockData = () => {
  // 模拟数据
  const mockData = Array(10).fill().map((_, index) => ({
    id: index + 1,
    fileName: `backup_${new Date().toISOString().split('T')[0].replace(/-/g, '')}_${index + 1}.sql`,
    fileSize: Math.floor(Math.random() * 10000000) + 100000,
    createTime: new Date().toLocaleString()
  }))
  
  tableData.value = mockData
  total.value = 30
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

// 创建备份
const handleBackup = () => {
  backupDrawerVisible.value = true
}

// 备份成功回调
const onBackupSuccess = () => {
  backupDrawerVisible.value = false
  getList()
}

// 下载备份
const handleDownload = (row) => {
  try {
    ElMessage.success(`即将下载文件: ${row.fileName}`)
    
    // 创建一个模拟的下载链接
    // 实际应用中，这里应该是指向真实文件的URL
    const a = document.createElement('a')
    a.href = `/backup/download?fileName=${row.fileName}`
    a.download = row.fileName
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
  } catch (error) {
    console.error('下载失败:', error)
    ElMessage.error('下载失败')
  }
}

// 恢复备份
const handleRestore = (row) => {
  selectedFile.value = row
  restoreDrawerVisible.value = true
}

// 恢复成功回调
const onRestoreSuccess = () => {
  restoreDrawerVisible.value = false
  ElMessage.success('数据库恢复成功')
}

// 删除备份
const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除 ${row.fileName} 备份文件吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await http.post('/backup/delete', {
        savePath: 'E:/backup', // 默认备份路径，和备份表单中保持一致
        fileName: row.fileName
      })
      
      if (res.code === 0) {
        ElMessage.success('删除成功')
        getList() // 刷新列表
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
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
</style> 