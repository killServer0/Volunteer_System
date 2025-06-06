import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 状态
  const sidebar = ref({
    opened: localStorage.getItem('sidebarStatus') ? !!+localStorage.getItem('sidebarStatus') : true,
    withoutAnimation: false
  })
  const device = ref('desktop')
  const size = ref(localStorage.getItem('size') || 'medium')
  const language = ref(localStorage.getItem('language') || 'zh-CN')

  // 计算属性
  const sidebarStatus = computed(() => sidebar.value.opened)

  // 方法
  const toggleSideBar = () => {
    sidebar.value.opened = !sidebar.value.opened
    sidebar.value.withoutAnimation = false
    if (sidebar.value.opened) {
      localStorage.setItem('sidebarStatus', 1)
    } else {
      localStorage.setItem('sidebarStatus', 0)
    }
  }

  const closeSideBar = ({ withoutAnimation }) => {
    localStorage.setItem('sidebarStatus', 0)
    sidebar.value.opened = false
    sidebar.value.withoutAnimation = withoutAnimation
  }

  const toggleDevice = (val) => {
    device.value = val
  }

  const setSize = (val) => {
    size.value = val
    localStorage.setItem('size', val)
  }

  const setLanguage = (val) => {
    language.value = val
    localStorage.setItem('language', val)
  }

  return {
    sidebar,
    device,
    size,
    language,
    sidebarStatus,
    toggleSideBar,
    closeSideBar,
    toggleDevice,
    setSize,
    setLanguage
  }
}) 