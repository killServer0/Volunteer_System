/**
 * 样式工具类
 */
export default {
  // 搜索表单样式
  searchFormBgColor: '#fff',
  searchFormFontColor: '#333',
  searchBoxPosition: '1',
  inputTitle: 1,
  inputHeight: '40px',
  inputFontColor: '#333',
  inputFontSize: '14px',
  inputBorderWidth: '1px',
  inputBorderStyle: 'solid',
  inputBorderColor: '#DCDFE6',
  inputBorderRadius: '4px',
  inputBgColor: '#fff',

  // 表单样式
  formStyle: {
    backgroundColor: '#fff',
    padding: '20px',
    borderRadius: '4px',
    boxShadow: '0 2px 12px 0 rgba(0, 0, 0, 0.1)'
  },
  formItemStyle: {
    marginBottom: '20px'
  },
  inputStyle: {
    width: '200px',
    height: '40px',
    lineHeight: '40px',
    color: '#333',
    fontSize: '14px',
    borderWidth: '1px',
    borderStyle: 'solid',
    borderColor: '#DCDFE6',
    borderRadius: '4px',
    backgroundColor: '#fff'
  },
  selectStyle: {
    width: '200px',
    height: '40px',
    lineHeight: '40px',
    color: '#333',
    fontSize: '14px',
    borderWidth: '1px',
    borderStyle: 'solid',
    borderColor: '#DCDFE6',
    borderRadius: '4px',
    backgroundColor: '#fff'
  },
  buttonStyle: {
    marginLeft: '10px'
  },

  // 按钮样式
  searchBtnBgColor: '#409EFF',
  searchBtnFontColor: '#fff',
  searchBtnFontSize: '14px',
  searchBtnBorderWidth: '1px',
  searchBtnBorderStyle: 'solid',
  searchBtnBorderColor: '#409EFF',
  searchBtnBorderRadius: '4px',

  // 表格样式
  tableStyle: {
    width: '100%',
    borderCollapse: 'collapse',
    marginTop: '20px'
  },
  tableContentFontSize: '14px',
  tableContentFontColor: '#333',
  tableHeaderFontColor: '#333',
  tableHeaderFontSize: '14px',
  tableHeaderBgColor: '#f5f7fa',
  tableContentBgColor: '#fff',
  tableSelection: true,
  tableSortable: true,
  tableAlign: 'center',
  tableSize: 'default',
  tableShowHeader: true,
  tableBorder: true,
  tableFit: true,
  tableStripe: false,

  // 分页样式
  pagePosition: '2',
  pageBtnBG: true,
  pageStyle: false,
  pageEachNum: 10,

  // 按钮位置
  btnAdAllBoxPosition: '1',
  btnAdAllIcon: 1,
  btnAdAllIconPosition: 1,
  btnAdAllFont: 1,

  // 表格按钮样式
  tableBtnIcon: 1,
  tableBtnIconPosition: 1,
  tableBtnFont: 1,

  // 新增按钮样式
  btnAdAllAddBgColor: '#67C23A',
  btnAdAllAddFontColor: '#fff',
  btnAdAllAddFontSize: '14px',
  btnAdAllAddBorderWidth: '1px',
  btnAdAllAddBorderStyle: 'solid',
  btnAdAllAddBorderColor: '#67C23A',
  btnAdAllAddBorderRadius: '4px',

  // 删除按钮样式
  btnAdAllDelBgColor: '#F56C6C',
  btnAdAllDelFontColor: '#fff',
  btnAdAllDelFontSize: '14px',
  btnAdAllDelBorderWidth: '1px',
  btnAdAllDelBorderStyle: 'solid',
  btnAdAllDelBorderColor: '#F56C6C',
  btnAdAllDelBorderRadius: '4px',

  // 表格索引
  tableIndex: true,

  /**
   * 生成随机颜色
   * @returns {string} 随机颜色值
   */
  randomColor() {
    const colors = [
      '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399',
      '#36A3F7', '#FF9F7F', '#9FE6B8', '#FFDB5C', '#FB7293'
    ]
    return colors[Math.floor(Math.random() * colors.length)]
  },

  /**
   * 生成随机背景色
   * @param {number} opacity 透明度
   * @returns {string} 随机背景色值
   */
  randomBgColor(opacity = 0.1) {
    const color = this.randomColor()
    return color.replace(')', `, ${opacity})`).replace('rgb', 'rgba')
  },

  /**
   * 生成渐变色
   * @param {string} startColor 起始颜色
   * @param {string} endColor 结束颜色
   * @param {number} angle 渐变角度
   * @returns {string} 渐变色值
   */
  gradientColor(startColor, endColor, angle = 45) {
    return `linear-gradient(${angle}deg, ${startColor}, ${endColor})`
  },

  /**
   * 生成阴影
   * @param {number} level 阴影等级
   * @returns {string} 阴影值
   */
  boxShadow(level = 1) {
    const shadows = [
      '0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04)',
      '0 2px 12px 0 rgba(0, 0, 0, 0.1)',
      '0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)',
      '0 2px 12px 0 rgba(0, 0, 0, .1)',
      '0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)'
    ]
    return shadows[level - 1] || shadows[0]
  },

  /**
   * 生成动画
   * @param {string} name 动画名称
   * @param {number} duration 持续时间
   * @param {string} timing 时间函数
   * @returns {string} 动画值
   */
  animation(name, duration = 0.3, timing = 'ease-in-out') {
    return `${name} ${duration}s ${timing}`
  },

  /**
   * 获取列表样式
   * @returns {Object} 列表样式对象
   */
  listStyle() {
    return {
      tableSize: this.tableSize,
      tableShowHeader: this.tableShowHeader,
      tableBorder: this.tableBorder,
      tableFit: this.tableFit,
      tableStripe: this.tableStripe,
      tableContentFontSize: this.tableContentFontSize,
      tableContentFontColor: this.tableContentFontColor,
      tableSelection: this.tableSelection,
      tableSortable: this.tableSortable,
      tableAlign: this.tableAlign,
      tableIndex: this.tableIndex,
      pagePosition: this.pagePosition,
      pageBtnBG: this.pageBtnBG,
      pageStyle: this.pageStyle,
      pageEachNum: this.pageEachNum,
      btnAdAllBoxPosition: this.btnAdAllBoxPosition,
      btnAdAllIcon: this.btnAdAllIcon,
      btnAdAllIconPosition: this.btnAdAllIconPosition,
      btnAdAllFont: this.btnAdAllFont,
      tableBtnIcon: this.tableBtnIcon,
      tableBtnIconPosition: this.tableBtnIconPosition,
      tableBtnFont: this.tableBtnFont
    }
  },

  /**
   * 获取新增/修改表单样式
   * @returns {Object} 表单样式对象
   */
  addStyle() {
    return {
      inputHeight: this.inputHeight,
      inputFontColor: this.inputFontColor,
      inputFontSize: this.inputFontSize,
      inputBorderWidth: this.inputBorderWidth,
      inputBorderStyle: this.inputBorderStyle,
      inputBorderColor: this.inputBorderColor,
      inputBorderRadius: this.inputBorderRadius,
      inputBgColor: this.inputBgColor,
      btnHeight: this.searchBtnHeight,
      btnFontColor: this.searchBtnFontColor,
      btnFontSize: this.searchBtnFontSize,
      btnBorderWidth: this.searchBtnBorderWidth,
      btnBorderStyle: this.searchBtnBorderStyle,
      btnBorderColor: this.searchBtnBorderColor,
      btnBorderRadius: this.searchBtnBorderRadius,
      btnBgColor: this.searchBtnBgColor,
      uploadHeight: '100px',
      uploadBorderWidth: '1px',
      uploadBorderStyle: 'solid',
      uploadBorderColor: '#DCDFE6',
      uploadBorderRadius: '4px',
      uploadBgColor: '#fff'
    }
  }
} 