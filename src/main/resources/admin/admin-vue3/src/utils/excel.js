/**
 * Excel 导出工具
 */

/**
 * 将 JSON 数据导出为 Excel 文件
 * @param {Array} data 要导出的数据数组
 * @param {string} filename 文件名
 */
export function json2excel(data, filename = 'export') {
  // 创建工作表数据
  const worksheet = XLSX.utils.json_to_sheet(data)
  
  // 创建工作簿
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1')
  
  // 导出文件
  XLSX.writeFile(workbook, `${filename}.xlsx`)
}

/**
 * 将 HTML 表格导出为 Excel 文件
 * @param {HTMLElement} table 表格元素
 * @param {string} filename 文件名
 */
export function table2excel(table, filename = 'export') {
  // 将表格转换为工作表
  const worksheet = XLSX.utils.table_to_sheet(table)
  
  // 创建工作簿
  const workbook = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1')
  
  // 导出文件
  XLSX.writeFile(workbook, `${filename}.xlsx`)
}

/**
 * 将数据导出为 CSV 文件
 * @param {Array} data 要导出的数据数组
 * @param {string} filename 文件名
 */
export function json2csv(data, filename = 'export') {
  // 获取表头
  const headers = Object.keys(data[0])
  
  // 创建 CSV 内容
  const csvContent = [
    headers.join(','),
    ...data.map(row => headers.map(header => row[header]).join(','))
  ].join('\n')
  
  // 创建 Blob 对象
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  
  // 创建下载链接
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `${filename}.csv`
  
  // 触发下载
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
} 