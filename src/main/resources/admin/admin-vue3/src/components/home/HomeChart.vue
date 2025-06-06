<template>
  <div ref="chartRef" style="width:100%;height:400px;"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  chartData: {
    type: Object,
    default: () => ({
      xAxis: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
      series: [
        {
          name: '访问量',
          data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90, 230]
        },
        {
          name: '用户量',
          data: [220, 182, 191, 234, 290, 330, 310, 182, 191, 234, 290, 330, 310]
        },
        {
          name: '收入',
          data: [150, 232, 201, 154, 190, 330, 410, 232, 201, 154, 190, 330, 410]
        }
      ]
    })
  }
})

const chartRef = ref(null)
let chart = null

const initChart = () => {
  if (!chartRef.value) return

  chart = echarts.init(chartRef.value)
  updateChart()
}

const updateChart = () => {
  if (!chart) return

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: props.chartData.series.map(item => item.name)
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: props.chartData.xAxis
    },
    yAxis: {
      type: 'value'
    },
    series: props.chartData.series.map(item => ({
      name: item.name,
      type: 'line',
      stack: '总量',
      data: item.data
    }))
  }

  chart.setOption(option)
}

const handleResize = () => {
  chart?.resize()
}

watch(() => props.chartData, () => {
  updateChart()
}, { deep: true })

onMounted(() => {
  initChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
})
</script>

<style lang="scss" scoped>
div {
  background: #ffffff;
  padding: 20px 0;
}
</style>