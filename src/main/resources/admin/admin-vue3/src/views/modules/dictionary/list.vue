<template>
  <div class="main-content">
    <!-- 列表页 -->
    <div v-if="showFlag">
      <el-form :inline="true" :model="searchForm" class="form-content"
        :style="{ backgroundColor: contents.searchFormBgColor, color: contents.searchFormFontColor }">
        <el-row :gutter="20" class="slt"
          :style="{ justifyContent: contents.searchBoxPosition == '1' ? 'flex-start' : contents.searchBoxPosition == '2' ? 'center' : 'flex-end' }">
          <el-form-item :label="contents.inputTitle == 1 ? '字典名称' : ''">
            <el-input v-model="searchForm.indexName" placeholder="请输入字典名称" clearable
              :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }">
              <template #prefix>
                <el-icon>
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search"
              :style="{ backgroundColor: contents.searchBtnBgColor, color: contents.searchBtnFontColor, fontSize: contents.searchBtnFontSize, borderWidth: contents.searchBtnBorderWidth, borderStyle: contents.searchBtnBorderStyle, borderColor: contents.searchBtnBorderColor, borderRadius: contents.searchBtnBorderRadius }">
              <el-icon>
                <Search />
              </el-icon>查询
            </el-button>
            <el-button type="success" @click="resetSearch"
              :style="{ backgroundColor: contents.resetBtnBgColor, color: contents.resetBtnFontColor, fontSize: contents.resetBtnFontSize, borderWidth: contents.resetBtnBorderWidth, borderStyle: contents.resetBtnBorderStyle, borderColor: contents.resetBtnBorderColor, borderRadius: contents.resetBtnBorderRadius }">
              <el-icon>
                <Refresh />
              </el-icon>重置
            </el-button>
            <el-button type="info" @click="refreshData">
              <el-icon>
                <Refresh />
              </el-icon>刷新数据
            </el-button>
          </el-form-item>
        </el-row>
        <el-row class="ad"
          :style="{ justifyContent: contents.btnAdAllBoxPosition == '1' ? 'flex-start' : contents.btnAdAllBoxPosition == '2' ? 'center' : 'flex-end' }">
          <el-form-item>
            <el-button type="success" @click="addOrUpdateHandler()"
              :style="{ backgroundColor: contents.btnAdAllAddBgColor, color: contents.btnAdAllAddFontColor, fontSize: contents.btnAdAllAddFontSize, borderWidth: contents.btnAdAllAddBorderWidth, borderStyle: contents.btnAdAllAddBorderStyle, borderColor: contents.btnAdAllAddBorderColor, borderRadius: contents.btnAdAllAddBorderRadius }">
              <el-icon>
                <Plus />
              </el-icon>新增
            </el-button>
            <el-button :disabled="dataListSelections.length <= 0"
              type="danger" @click="deleteHandler()"
              :style="{ backgroundColor: contents.btnAdAllDelBgColor, color: contents.btnAdAllDelFontColor, fontSize: contents.btnAdAllDelFontSize, borderWidth: contents.btnAdAllDelBorderWidth, borderStyle: contents.btnAdAllDelBorderStyle, borderColor: contents.btnAdAllDelBorderColor, borderRadius: contents.btnAdAllDelBorderRadius }">
              <el-icon>
                <Delete />
              </el-icon>批量删除
            </el-button>
          </el-form-item>
        </el-row>
      </el-form>
      <div class="table-content">
        <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
          :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :border="contents.tableBorder"
          :fit="contents.tableFit" :stripe="contents.tableStripe" :row-style="rowStyle" :cell-style="cellStyle"
          style="width: 100%; font-size: 14px; color: #333; margin-top: 10px; border: 1px solid #dcdfe6;"
          :data="dataList" v-loading="dataListLoading"
          @selection-change="selectionChangeHandler">
          <el-table-column v-if="contents.tableSelection" type="selection" header-align="center" align="center"
            width="50">
          </el-table-column>
          <el-table-column label="索引" v-if="contents.tableIndex" type="index" width="50" />
          <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="dicCode"
            header-align="center" label="字典编码">
          </el-table-column>
          <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="dicName"
            header-align="center" label="字典名称">
          </el-table-column>
          <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="codeIndex"
            header-align="center" label="编码">
          </el-table-column>
          <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="indexName"
            header-align="center" label="编码名称">
          </el-table-column>
          <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="beizhu"
            header-align="center" label="备注">
            <template #default="scope">
              {{ scope.row.beizhu || '-' }}
            </template>
          </el-table-column>
          <el-table-column width="280" :align="contents.tableAlign" header-align="center" label="操作">
            <template #default="scope">
              <el-button type="success" size="small"
                @click="addOrUpdateHandler(scope.row.id, 'info')"
                :style="{ backgroundColor: contents.btnInfoBgColor, color: contents.btnInfoFontColor, fontSize: contents.btnInfoFontSize, borderWidth: contents.btnInfoBorderWidth, borderStyle: contents.btnInfoBorderStyle, borderColor: contents.btnInfoBorderColor, borderRadius: contents.btnInfoBorderRadius }">
                <el-icon><View /></el-icon>详情
              </el-button>
              <el-button type="primary" size="small"
                @click="addOrUpdateHandler(scope.row.id)"
                :style="{ backgroundColor: contents.btnEditBgColor, color: contents.btnEditFontColor, fontSize: contents.btnEditFontSize, borderWidth: contents.btnEditBorderWidth, borderStyle: contents.btnEditBorderStyle, borderColor: contents.btnEditBorderColor, borderRadius: contents.btnEditBorderRadius }">
                <el-icon><Edit /></el-icon>修改
              </el-button>
              <el-button type="danger" size="small"
                @click="deleteHandler(scope.row.id)"
                :style="{ backgroundColor: contents.btnDelBgColor, color: contents.btnDelFontColor, fontSize: contents.btnDelFontSize, borderWidth: contents.btnDelBorderWidth, borderStyle: contents.btnDelBorderStyle, borderColor: contents.btnDelBorderColor, borderRadius: contents.btnDelBorderRadius }">
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination clsss="pages" :layout="layouts" @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle" :current-page="pageIndex" :page-sizes="[10, 20, 50, 100]"
          :page-size="Number(contents.pageEachNum)" :total="totalPage" :small="contents.pageStyle"
          class="pagination-content" :background="contents.pageBtnBG"
          :style="{ textAlign: contents.pagePosition == 1 ? 'left' : contents.pagePosition == 2 ? 'center' : 'right' }">
        </el-pagination>
      </div>
    </div>
    <!-- 添加/修改页面 -->
    <add-or-update v-if="addOrUpdateFlag" :parent="parent" ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>

    <el-dialog title="打印" v-model="printVisiable" width="500px">
      <el-form ref="printContent" :model="printFrom" label-width="auto">
        <el-form-item label="发件人">
          <div style="display: flex">
            <el-input v-model="printFrom.kuaidiFajianName" disabled></el-input>
            :
            <el-input v-model="printFrom.kuaidiFajianShoujihao" disabled></el-input>
          </div>
        </el-form-item>
        <el-form-item label="发件人地址">
          <el-input v-model="printFrom.kuaidiFajianAddress" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="printContent()" icon="el-icon-printer" type="success">打印</el-button>
          <el-button @click="printVisiable = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="统计报表" v-model="chartVisiable" width="80%">
      <el-date-picker v-model="echartsDate" type="year" placeholder="选择年"> </el-date-picker>
      <el-button @click="chartDialog()">查询</el-button>
      <div id="statistic" style="width:100%;height:600px;"></div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="chartVisiable = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getToken } from '@/utils/auth'
import { getDictionaryList, deleteDictionary } from '@/api/dictionary'
import styleJs from '@/utils/style.js'
import AddOrUpdate from './add-or-update.vue'
import { Search, Plus, Delete, View, Edit, Upload, Download, Printer, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'

const searchForm = reactive({
  indexName: ''
})

const dataList = ref([])
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const dataListLoading = ref(false)
const dataListSelections = ref([])
const showFlag = ref(true)
const addOrUpdateFlag = ref(false)
const contents = ref(styleJs.listStyle())
const printVisiable = ref(false)
const chartVisiable = ref(false)
const echartsDate = ref('')
const printFrom = reactive({
  kuaidiFajianName: '',
  kuaidiFajianShoujihao: '',
  kuaidiFajianAddress: ''
})
const sessionTable = ref('false')
const layouts = ref('total, sizes, prev, pager, next, jumper')
const json_fields = reactive({
  '字段': 'dicCode',
  '字段名': 'dicName',
  '编码': 'codeIndex',
  '编码名字': 'indexName',
  '备注': 'beizhu'
})

const addOrUpdateRef = ref(null)

// 创建一个响应式的parent对象，供子组件使用
const parent = reactive({
  showFlag,
  addOrUpdateFlag
});

// 监听响应式对象变化
watch(() => parent.showFlag, (newVal) => {
  console.log('parent.showFlag变化:', newVal);
  showFlag.value = newVal;
});

watch(() => parent.addOrUpdateFlag, (newVal) => {
  console.log('parent.addOrUpdateFlag变化:', newVal);
  addOrUpdateFlag.value = newVal;
});

watch(showFlag, (newVal) => {
  console.log('showFlag变化:', newVal);
  parent.showFlag = newVal;
});

watch(addOrUpdateFlag, (newVal) => {
  console.log('addOrUpdateFlag变化:', newVal);
  parent.addOrUpdateFlag = newVal;
});

// 权限检查 - 基础字典始终返回true
const isAuth = () => true

// 获取数据列表
const getDataList = async () => {
  dataListLoading.value = true
  try {
    console.log('开始获取字典列表数据')
    const res = await getDictionaryList({
      page: pageIndex.value,
      limit: pageSize.value,
      indexName: searchForm.indexName
    })
    console.log('getDictionaryList返回数据:', res)
    
    if (res.code === 0) {
      console.log('数据格式正确，处理列表...')
      // 兼容不同的返回格式
      if (res.data && res.data.list) {
        // 使用解构创建新数组，确保Vue 3响应式系统能够正确追踪
        dataList.value = [...res.data.list]
        totalPage.value = res.data.total
      } else if (res.data && Array.isArray(res.data)) {
        dataList.value = [...res.data]
        totalPage.value = res.data.length
      } else if (res.list && typeof res.total !== 'undefined') {
        dataList.value = [...res.list]
        totalPage.value = res.total
      } else {
        console.warn('不支持的数据格式:', res)
        dataList.value = []
        totalPage.value = 0
      }
      console.log('处理后的数据列表长度:', dataList.value.length)
      if (dataList.value.length > 0) {
        console.log('处理后的第一条数据:', dataList.value[0])
      } else {
        console.warn('数据列表为空')
        
        // 如果列表为空但总数不为0，可能是分页问题，尝试获取第一页
        if (totalPage.value > 0 && pageIndex.value > 1) {
          console.log('尝试切换到第一页...')
          pageIndex.value = 1
          await getDataList()
          return
        }
      }
    } else {
      console.warn('数据格式异常:', res)
      ElMessage.warning(`获取数据异常: ${res.msg || '未知错误'}`)
      dataList.value = []
      totalPage.value = 0
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取字典数据失败')
    dataList.value = []
    totalPage.value = 0
    
    // 显示更详细的错误信息
    if (error.response) {
      console.error('错误响应:', error.response.status, error.response.data)
    }
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

// 多选
const selectionChangeHandler = (val) => {
  dataListSelections.value = val
}

// 搜索
const search = () => {
  pageIndex.value = 1
  getDataList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.indexName = ''
  search()
}

// 新增 / 修改
const addOrUpdateHandler = (id, type) => {
  console.log('执行addOrUpdateHandler, id=', id, 'type=', type);
  showFlag.value = false;
  parent.showFlag = false;
  
  addOrUpdateFlag.value = true;
  parent.addOrUpdateFlag = true;
  
  nextTick(() => {
    if (addOrUpdateRef.value) {
      console.log('使用ref引用初始化add-or-update组件...');
      addOrUpdateRef.value.init(id, type);
    } else {
      console.error('无法获取add-or-update组件引用');
    }
  });
}

// 删除
const deleteHandler = async (id) => {
  const ids = id ? [id] : dataListSelections.value.map(item => item.id)
  try {
    await ElMessageBox.confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteDictionary(ids)
    ElMessage.success('删除成功')
    getDataList()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

// 打印
const dayinOpen = (row) => {
  printVisiable.value = true
  printFrom.kuaidiFajianName = row.dicName
  printFrom.kuaidiFajianShoujihao = row.codeIndex
  printFrom.kuaidiFajianAddress = row.beizhu
}

const printContent = () => {
  window.print()
}

// 报表
const chartDialog = () => {
  chartVisiable.value = true
  nextTick(() => {
    const myChart = echarts.init(document.getElementById('statistic'))
    const option = {
      title: {
        text: '字典统计'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['字段数']
      },
      xAxis: {
        type: 'category',
        data: dataList.value.map(item => item.dicName)
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: '字段数',
        type: 'bar',
        data: dataList.value.map(item => item.codeIndex)
      }]
    }
    myChart.setOption(option)
  })
}

// 样式系统
const initStyle = () => {
  if (!contents.value) {
    contents.value = styleJs.listStyle()
  }
}

// 表格样式
const headerRowStyle = () => {
  return {
    color: contents.value.tableHeaderFontColor,
    fontSize: contents.value.tableHeaderFontSize,
    backgroundColor: contents.value.tableHeaderBgColor
  }
}

const headerCellStyle = () => {
  return {
    color: contents.value.tableHeaderFontColor,
    fontSize: contents.value.tableHeaderFontSize,
    backgroundColor: contents.value.tableHeaderBgColor
  }
}

const rowStyle = () => {
  return {
    color: contents.value.tableContentFontColor,
    fontSize: contents.value.tableContentFontSize,
    backgroundColor: contents.value.tableContentBgColor
  }
}

const cellStyle = () => {
  return {
    color: contents.value.tableContentFontColor,
    fontSize: contents.value.tableContentFontSize,
    backgroundColor: contents.value.tableContentBgColor
  }
}

// 上传相关
const getActionUrl = ref('http://localhost:8081/zhiyuanzheguanlixitong/dictionary/import')

const dictionaryUploadSuccess = (response) => {
  if (response.code === 0) {
    ElMessage.success('导入成功')
    getDataList()
  } else {
    ElMessage.error(response.msg)
  }
}

const dictionaryUploadError = () => {
  ElMessage.error('导入失败')
}

// 调试工具：检查后端连接
const checkBackendConnection = () => {
  console.log('检查后端API连接...');
  
  // 请求头添加调试信息
  const headers = {
    'X-Demo-Mode': 'true',
    'X-Debug': 'true'
  };
  
  // 尝试获取一页字典数据
  request({
    url: '/dictionary/page',
    method: 'get',
    params: { page: 1, limit: 10 },
    headers
  }).then(res => {
    console.log('后端API连接正常:', res);
    ElMessage.success('后端API连接正常');
  }).catch(err => {
    console.error('后端API连接异常:', err);
    
    // 显示错误详情
    if (err.response) {
      console.log('响应状态:', err.response.status);
      console.log('响应数据:', err.response.data);
      ElMessage.error(`后端API错误: HTTP ${err.response.status}`);
    } else if (err.request) {
      console.log('请求已发送但未收到响应:', err.request);
      ElMessage.error('后端无响应，服务可能未启动');
    } else {
      console.log('请求配置错误:', err.message);
      ElMessage.error(`请求错误: ${err.message}`);
    }
  });
}

// 刷新数据
const refreshData = () => {
  ElMessage.info('正在刷新数据...')
  // 重置分页
  pageIndex.value = 1
  // 清空搜索条件
  searchForm.indexName = ''
  // 重新获取数据
  getDataList()
}

onMounted(() => {
  initStyle()
  // 检查后端连接
  checkBackendConnection();
  
  // 添加一个短延迟确保DOM加载完成
  setTimeout(() => {
    getDataList()
  }, 100)
})
</script>

<style scoped>
.main-content {
  padding: 20px;
}

.form-content {
  margin-bottom: 20px;
}

.table-content {
  margin-top: 20px;
}

.pagination-content {
  margin-top: 20px;
  text-align: right;
}
</style>