<template>
  <div class="content">
    <div class="text main-text">欢迎使用 {{ projectName }}</div>
    
    <!-- 统计图表区域 -->
    <div class="statistics-container">
      <!-- 日期选择 -->
      <div class="date-selector">
        <el-date-picker 
          v-model="selectedDate" 
          type="month" 
          placeholder="选择月份"
          format="YYYY/MM"
          value-format="YYYY-MM"
          @change="refreshData"
        />
        <el-button type="primary" @click="refreshData">刷新数据</el-button>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-container">
        <!-- 主要统计图表 -->
        <div class="main-chart">
          <div id="activity-chart" class="chart-box"></div>
        </div>
        
        <!-- 辅助统计图表 -->
        <div class="sub-charts">
          <div id="volunteer-chart" class="chart-box"></div>
          <div id="admin-chart" class="chart-box"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { getStorage } from '@/utils/storage'
import base from '@/utils/base'
import * as echarts from 'echarts/core'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  DataZoomComponent,
  ToolboxComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { getCombinedStatistics } from '@/api/statistics'

// 注册必要的组件
echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  DataZoomComponent,
  ToolboxComponent,
  BarChart,
  LineChart,
  PieChart,
  CanvasRenderer
])

const sessionTable = ref('')
const role = ref('')
const userId = ref('')
const projectName = ref(base.projectName)

// 图表实例
let activityChart = null
let volunteerChart = null
let adminChart = null

// 选择的日期
const selectedDate = ref(new Date().toISOString().slice(0, 7)) // 默认为当前年月 YYYY-MM

// 从选择的日期提取年和月
const selectedYear = computed(() => {
  if (!selectedDate.value) return new Date().getFullYear()
  return parseInt(selectedDate.value.split('-')[0])
})

const selectedMonth = computed(() => {
  if (!selectedDate.value) return new Date().getMonth() + 1
  return parseInt(selectedDate.value.split('-')[1])
})

// 监听日期变化
watch(selectedDate, () => {
  refreshData()
})

// 统计数据
const statisticsData = ref({
  activities: [],
  volunteers: [],
  admins: []
})

onMounted(() => {
  sessionTable.value = getStorage('sessionTable')
  role.value = getStorage('role')
  userId.value = getStorage('userId')
  
  // 初始化图表
  initCharts()
  
  // 加载数据
  loadStatisticsData()
  
  // 窗口大小变化时调整图表大小
  window.addEventListener('resize', handleResize)
})

// 初始化图表
const initCharts = () => {
  // 活动统计图表
  const activityChartDom = document.getElementById('activity-chart')
  activityChart = echarts.init(activityChartDom)
  
  // 志愿者统计图表
  const volunteerChartDom = document.getElementById('volunteer-chart')
  volunteerChart = echarts.init(volunteerChartDom)
  
  // 管理员统计图表
  const adminChartDom = document.getElementById('admin-chart')
  adminChart = echarts.init(adminChartDom)
}

// 加载统计数据
const loadStatisticsData = async () => {
  try {
    // 调用API获取数据
    const params = {
      year: selectedYear.value,
      month: selectedMonth.value
    }
    
    const response = await getCombinedStatistics(params)
    
    if (response.code === 0) {
      statisticsData.value = response.data
      renderCharts()
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 刷新数据
const refreshData = () => {
  loadStatisticsData()
}

// 渲染图表
const renderCharts = () => {
  renderActivityChart()
  renderVolunteerChart()
  renderAdminChart()
}

// 渲染活动统计图表
const renderActivityChart = () => {
  if (!activityChart) return
  
  const { activities } = statisticsData.value
  
  // 准备数据
  const dates = activities.map(item => item.date)
  const zhihuodongData = activities.map(item => item.zhihuodong)
  const feihuodongData = activities.map(item => item.feihuodong)
  
  const option = {
    title: {
      text: '志愿活动发起统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['志愿者活动', '游客活动'],
      top: 'bottom'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        interval: Math.floor(dates.length / 15), // 控制显示的日期数量
        formatter: (value) => value.substring(8) // 只显示日期的天
      }
    },
    yAxis: {
      type: 'value',
      name: '数量'
    },
    series: [
      {
        name: '志愿者活动',
        type: 'bar',
        stack: 'total',
        data: zhihuodongData,
        itemStyle: { color: '#5470c6' }
      },
      {
        name: '游客活动',
        type: 'bar',
        stack: 'total',
        data: feihuodongData,
        itemStyle: { color: '#91cc75' }
      }
    ],
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100
      },
      {
        type: 'slider',
        start: 0,
        end: 100
      }
    ]
  }
  
  activityChart.setOption(option)
}

// 渲染志愿者统计图表
const renderVolunteerChart = () => {
  if (!volunteerChart) return
  
  const { volunteers } = statisticsData.value
  
  // 准备数据
  const dates = volunteers.map(item => item.date)
  const counts = volunteers.map(item => item.count)
  
  const option = {
    title: {
      text: '志愿者注册统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'line' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        interval: Math.floor(dates.length / 10), // 控制显示的日期数量
        formatter: (value) => value.substring(8) // 只显示日期的天
      }
    },
    yAxis: {
      type: 'value',
      name: '人数'
    },
    series: [
      {
        name: '新注册志愿者',
        type: 'line',
        data: counts,
        itemStyle: { color: '#ee6666' },
        lineStyle: { width: 3 },
        symbol: 'circle',
        symbolSize: 8,
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(238, 102, 102, 0.5)' },
              { offset: 1, color: 'rgba(238, 102, 102, 0.1)' }
            ]
          }
        }
      }
    ],
    dataZoom: [
      {
        type: 'inside',
        start: 0,
        end: 100
      }
    ]
  }
  
  volunteerChart.setOption(option)
}

// 渲染管理员统计图表
const renderAdminChart = () => {
  if (!adminChart) return
  
  const { admins } = statisticsData.value
  
  // 准备数据
  const dates = admins.map(item => item.date)
  const counts = admins.map(item => item.count)
  
  // 非零值的总天数
  const nonZeroDays = counts.filter(count => count > 0).length
  
  const option = {
    title: {
      text: '管理员注册统计',
      left: 'center',
      subtext: `本月共有${nonZeroDays}天有新管理员注册`
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '注册人数',
        type: 'pie',
        radius: '60%',
        center: ['50%', '50%'],
        data: [
          { value: nonZeroDays, name: '有注册的天数' },
          { value: admins.length - nonZeroDays, name: '无注册的天数' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  adminChart.setOption(option)
}

// 处理窗口大小变化
const handleResize = () => {
  activityChart && activityChart.resize()
  volunteerChart && volunteerChart.resize()
  adminChart && adminChart.resize()
}
</script>

<style lang="scss" scoped>
.content {
  padding: 20px;

  .main-text {
    font-size: 24px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 20px;
  }
  
  .statistics-container {
    width: 100%;
    
    .date-selector {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
      gap: 10px;
    }
    
    .charts-container {
      display: flex;
      flex-direction: column;
      gap: 20px;
      
      .main-chart {
        width: 100%;
        
        .chart-box {
          height: 400px;
          width: 100%;
          background-color: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        }
      }
      
      .sub-charts {
        display: flex;
        gap: 20px;
        
        .chart-box {
          flex: 1;
          height: 300px;
          background-color: #fff;
          border-radius: 8px;
          box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        }
        
        @media (max-width: 768px) {
          flex-direction: column;
          
          .chart-box {
            width: 100%;
          }
        }
      }
    }
  }
}
</style>