<template>
  <div class="main-content">
    <div class="data-list">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="社区活动审核" name="feihuodong">
          <div class="table-operations">
            <el-button type="primary" :disabled="feihuodongSelections.length <= 0" @click="batchSetStatus('feihuodong', 2)">
              批量设为已审核
            </el-button>
          </div>
          <el-table :data="feihuodongList" border @selection-change="(val) => handleSelectionChange(val, 'feihuodong')">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="feihuodongName" label="活动名称"></el-table-column>
            <el-table-column prop="feihuodongAddress" label="活动地点"></el-table-column>
            <el-table-column prop="feihuodongTypes" label="活动类型">
              <template #default="scope">
                <el-tag>{{ getActivityType(scope.row.feihuodongTypes) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="feihuodongKucunNumber" label="活动人数"></el-table-column>
            <el-table-column prop="feihuodongTiaojian" label="参加条件"></el-table-column>
            <el-table-column prop="huodong_status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.huodong_status)">
                  {{ getStatusText(scope.row.huodong_status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleAudit(scope.row, 'feihuodong')">审核</el-button>
                <el-button type="info" size="small" @click="handleDetail(scope.row, 'feihuodong')">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane label="志愿者活动审核" name="zhihuodong">
          <div class="table-operations">
            <el-button type="primary" :disabled="zhihuodongSelections.length <= 0" @click="batchSetStatus('zhihuodong', 2)">
              批量设为已审核
            </el-button>
          </div>
          <el-table :data="zhihuodongList" border @selection-change="(val) => handleSelectionChange(val, 'zhihuodong')">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="zhihuodongName" label="活动名称"></el-table-column>
            <el-table-column prop="zhihuodongAddress" label="活动地点"></el-table-column>
            <el-table-column prop="zhihuodongTypes" label="活动类型">
              <template #default="scope">
                <el-tag>{{ getActivityType(scope.row.zhihuodongTypes) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="zhihuodongKucunNumber" label="活动人数"></el-table-column>
            <el-table-column prop="zhihuodongTiaojian" label="参加条件"></el-table-column>
            <el-table-column prop="huodong_status" label="状态">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.huodong_status)">
                  {{ getStatusText(scope.row.huodong_status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleAudit(scope.row, 'zhihuodong')">审核</el-button>
                <el-button type="info" size="small" @click="handleDetail(scope.row, 'zhihuodong')">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 审核对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="活动状态">
          <el-select v-model="auditForm.huodong_status">
            <el-option label="待审核" :value="1"></el-option>
            <el-option label="未开始" :value="2"></el-option>
            <el-option label="进行中" :value="3"></el-option>
            <el-option label="已结束" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input type="textarea" v-model="auditForm.audit_comment"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'

const activeTab = ref('feihuodong')
const feihuodongList = ref([])
const zhihuodongList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const auditForm = ref({
  id: null,
  type: '',
  huodong_status: 1,
  audit_comment: ''
})

// 选中的数据
const feihuodongSelections = ref([])
const zhihuodongSelections = ref([])

// 选择变化
const handleSelectionChange = (val, type) => {
  if (type === 'feihuodong') {
    feihuodongSelections.value = val
  } else {
    zhihuodongSelections.value = val
  }
}

// 批量设置状态
const batchSetStatus = async (type, status) => {
  try {
    const selections = type === 'feihuodong' ? feihuodongSelections.value : zhihuodongSelections.value
    if (selections.length === 0) {
      ElMessage.warning('请选择要批量操作的活动')
      return
    }

    await ElMessageBox.confirm(`确定要将选中的${selections.length}个活动状态设置为"已审核"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const url = type === 'feihuodong' ? '/feihuodong/update' : '/zhihuodong/update'
    
    // 显示加载提示
    const loading = ElMessage({
      message: '正在处理中，请稍候...',
      duration: 0,
      type: 'info',
      showClose: true
    })
    
    // 创建一个包含所有更新请求的数组
    const updatePromises = selections.map(item => {
      return http.post(url, {
        id: item.id,
        huodongStatus: status
      })
    })
    
    // 使用Promise.all等待所有请求完成
    const results = await Promise.all(updatePromises)
    
    // 关闭加载提示
    loading.close()
    
    // 检查结果
    const failCount = results.filter(res => res.code !== 0).length
    
    if (failCount === 0) {
      ElMessage.success('批量设置成功')
      getList() // 重新加载列表
    } else if (failCount < selections.length) {
      ElMessage.warning(`部分操作成功，${failCount}个活动设置失败`)
      getList() // 重新加载列表
    } else {
      ElMessage.error('批量设置失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量设置失败:', error)
      ElMessage.error('批量设置失败: ' + (error.message || error))
    }
  }
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    1: 'info',
    2: 'warning',
    3: 'success',
    4: 'danger'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    1: '待审核',
    2: '未开始',
    3: '进行中',
    4: '已结束'
  }
  return texts[status] || '未知'
}

// 获取活动类型
const getActivityType = (type) => {
  // 为feihuodongTypes和zhihuodongTypes提供映射
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

// 处理审核
const handleAudit = (row, type) => {
  dialogTitle.value = type === 'feihuodong' ? '社区活动审核' : '志愿者活动审核'
  auditForm.value.id = row.id
  auditForm.value.type = type
  auditForm.value.huodong_status = row.huodong_status
  dialogVisible.value = true
}

// 提交审核
const submitAudit = async () => {
  try {
    console.log('提交审核数据:', auditForm.value)
    
    const url = auditForm.value.type === 'feihuodong' 
      ? '/feihuodong/update' 
      : '/zhihuodong/update'
    
    // 构建更完整的更新数据
    const updateData = {
      id: auditForm.value.id,
      huodongStatus: auditForm.value.huodong_status // 注意使用huodongStatus字段名
    }
    
    console.log('发送更新请求:', url, updateData)
    
    const res = await http.post(url, updateData)
    
    console.log('审核结果:', res)
    
    if (res.code === 0) {
      ElMessage.success('审核成功')
      dialogVisible.value = false
      getList() // 重新加载列表
    } else {
      ElMessage.error(res.msg || '审核失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败: ' + (error.message || error))
  }
}

// 获取列表数据
const getList = async () => {
  try {
    console.log('开始获取活动审核数据...')
    
    const [feihuodongRes, zhihuodongRes] = await Promise.all([
      http.get('/feihuodong/page', {
        params: {
          page: 1,
          limit: 100,
          huodong_status: 1 // 只获取待审核的活动
        }
      }),
      http.get('/zhihuodong/page', {
        params: {
          page: 1,
          limit: 100,
          huodong_status: 1 // 只获取待审核的活动
        }
      })
    ])
    
    console.log('社区活动结果:', feihuodongRes)
    console.log('志愿者活动结果:', zhihuodongRes)
    
    if (feihuodongRes.code === 0) {
      feihuodongList.value = feihuodongRes.data.list || []
      console.log('社区活动数据:', feihuodongList.value)
    } else {
      console.error('获取社区活动数据失败:', feihuodongRes.msg)
      ElMessage.error('获取社区活动数据失败: ' + feihuodongRes.msg)
    }
    
    if (zhihuodongRes.code === 0) {
      zhihuodongList.value = zhihuodongRes.data.list || []
      console.log('志愿者活动数据:', zhihuodongList.value)
    } else {
      console.error('获取志愿者活动数据失败:', zhihuodongRes.msg)
      ElMessage.error('获取志愿者活动数据失败: ' + zhihuodongRes.msg)
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败: ' + (error.message || error))
  }
}

// 处理详情
const handleDetail = (row, type) => {
  // 改为路由跳转而不是打开新窗口
  const path = type === 'feihuodong' 
    ? `/feihuodong-detail` 
    : `/zhihuodong-detail`
  
  // 存储当前活动ID到本地存储，以便详情页面可以获取
  localStorage.setItem(type === 'feihuodong' ? 'feihuodongId' : 'zhihuodongId', row.id)
  
  // 使用路由跳转
  window.location.href = `#${path}?id=${row.id}`
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.main-content {
  padding: 20px;
}

.data-list {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.el-tag {
  margin-right: 8px;
}

.table-operations {
  margin-bottom: 15px;
}
</style> 