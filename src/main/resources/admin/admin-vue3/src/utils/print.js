/**
 * 打印工具类
 */
export default {
  /**
   * 打印指定元素
   * @param {string} elementId 要打印的元素ID
   * @param {Object} options 打印选项
   */
  print(elementId, options = {}) {
    const element = document.getElementById(elementId)
    if (!element) {
      console.error('打印元素不存在')
      return
    }

    const defaultOptions = {
      title: document.title,
      noPrint: '.no-print',
      beforePrint: null,
      afterPrint: null
    }

    const printOptions = { ...defaultOptions, ...options }

    // 创建打印样式
    const style = document.createElement('style')
    style.innerHTML = `
      @media print {
        body * {
          visibility: hidden;
        }
        #${elementId}, #${elementId} * {
          visibility: visible;
        }
        #${elementId} {
          position: absolute;
          left: 0;
          top: 0;
        }
        ${printOptions.noPrint} {
          display: none !important;
        }
      }
    `
    document.head.appendChild(style)

    // 执行打印前的回调
    if (typeof printOptions.beforePrint === 'function') {
      printOptions.beforePrint()
    }

    // 执行打印
    window.print()

    // 执行打印后的回调
    if (typeof printOptions.afterPrint === 'function') {
      printOptions.afterPrint()
    }

    // 移除打印样式
    document.head.removeChild(style)
  },

  /**
   * 打印表格数据
   * @param {Array} data 表格数据
   * @param {Array} columns 表格列配置
   * @param {string} title 打印标题
   */
  printTable(data, columns, title = '表格数据') {
    const printContent = document.createElement('div')
    printContent.id = 'print-table'
    printContent.innerHTML = `
      <div style="text-align: center; margin-bottom: 20px;">
        <h2>${title}</h2>
      </div>
      <table style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr>
            ${columns.map(col => `
              <th style="border: 1px solid #000; padding: 8px; text-align: left;">
                ${col.label}
              </th>
            `).join('')}
          </tr>
        </thead>
        <tbody>
          ${data.map(row => `
            <tr>
              ${columns.map(col => `
                <td style="border: 1px solid #000; padding: 8px;">
                  ${row[col.prop] || ''}
                </td>
              `).join('')}
            </tr>
          `).join('')}
        </tbody>
      </table>
    `
    document.body.appendChild(printContent)
    this.print('print-table')
    document.body.removeChild(printContent)
  }
} 