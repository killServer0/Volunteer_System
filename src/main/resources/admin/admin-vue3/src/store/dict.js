import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getDictList } from '@/api/dict'

export const useDictStore = defineStore('dict', () => {
  // 状态
  const dictData = ref({})

  // 方法
  const getDict = async (dictType) => {
    if (dictData.value[dictType]) {
      return dictData.value[dictType]
    }
    
    try {
      const { data } = await getDictList({ type: dictType })
      dictData.value[dictType] = data
      return data
    } catch (error) {
      console.error('获取字典数据失败:', error)
      return []
    }
  }

  const resetDict = () => {
    dictData.value = {}
  }

  return {
    dictData,
    getDict,
    resetDict
  }
}) 