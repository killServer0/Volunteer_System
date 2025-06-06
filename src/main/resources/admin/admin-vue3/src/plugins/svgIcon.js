import SvgIcon from '@/components/SvgIcon/index.vue'
import { importAllSvgIcons } from '@/components/SvgIcon/svgBuilder'

export default {
  install: (app) => {
    // 注册SvgIcon组件
    app.component('SvgIcon', SvgIcon)
    
    // 导入所有svg图标
    importAllSvgIcons()
  }
} 