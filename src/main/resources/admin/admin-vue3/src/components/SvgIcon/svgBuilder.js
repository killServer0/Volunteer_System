import { createFromIconfontCN } from '@ant-design/icons-vue'

// 创建图标组件
export const IconFont = createFromIconfontCN({
  // 在 iconfont.cn 上生成在线链接
  scriptUrl: '//at.alicdn.com/t/font_8d5l8fzk5b87iudi.js',
})

// 自动导入所有svg图标
export function importAllSvgIcons() {
  try {
    const requireAll = requireContext => requireContext.keys().map(requireContext)
    const req = require.context('@/icons/svg', false, /\.svg$/)
    requireAll(req)
  } catch (error) {
    console.error('导入SVG图标失败:', error)
  }
} 