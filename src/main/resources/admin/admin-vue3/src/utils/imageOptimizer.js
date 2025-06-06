/**
 * 图片压缩工具
 * @param {File} file - 图片文件
 * @param {Object} options - 压缩选项
 * @param {number} options.maxWidth - 最大宽度
 * @param {number} options.maxHeight - 最大高度
 * @param {number} options.quality - 压缩质量（0-1）
 * @returns {Promise<Blob>} - 压缩后的图片Blob对象
 */
export function compressImage(file, options = {}) {
  const {
    maxWidth = 1920,
    maxHeight = 1080,
    quality = 0.8
  } = options

  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = (e) => {
      const img = new Image()
      img.src = e.target.result
      img.onload = () => {
        const canvas = document.createElement('canvas')
        let width = img.width
        let height = img.height

        // 计算缩放比例
        if (width > maxWidth) {
          height = Math.round((height * maxWidth) / width)
          width = maxWidth
        }
        if (height > maxHeight) {
          width = Math.round((width * maxHeight) / height)
          height = maxHeight
        }

        canvas.width = width
        canvas.height = height
        const ctx = canvas.getContext('2d')
        ctx.drawImage(img, 0, 0, width, height)

        // 转换为Blob对象
        canvas.toBlob(
          (blob) => {
            resolve(blob)
          },
          file.type,
          quality
        )
      }
      img.onerror = (err) => reject(err)
    }
    reader.onerror = (err) => reject(err)
  })
}

/**
 * 图片懒加载指令
 */
export const lazyLoad = {
  mounted(el, binding) {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            el.src = binding.value
            observer.unobserve(el)
          }
        })
      },
      {
        rootMargin: '50px'
      }
    )
    observer.observe(el)
  }
}

/**
 * 图片预加载
 * @param {string[]} urls - 图片URL数组
 * @returns {Promise<void>}
 */
export function preloadImages(urls) {
  const promises = urls.map((url) => {
    return new Promise((resolve, reject) => {
      const img = new Image()
      img.src = url
      img.onload = () => resolve()
      img.onerror = () => reject()
    })
  })
  return Promise.all(promises)
}

/**
 * 获取图片的主要颜色
 * @param {string} imageUrl - 图片URL
 * @returns {Promise<string>} - 主要颜色的十六进制值
 */
export function getImageDominantColor(imageUrl) {
  return new Promise((resolve, reject) => {
    const img = new Image()
    img.crossOrigin = 'Anonymous'
    img.src = imageUrl

    img.onload = () => {
      const canvas = document.createElement('canvas')
      const ctx = canvas.getContext('2d')
      canvas.width = img.width
      canvas.height = img.height
      ctx.drawImage(img, 0, 0)

      const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height).data
      const colorMap = {}
      let maxCount = 0
      let dominantColor = '#000000'

      for (let i = 0; i < imageData.length; i += 4) {
        const r = imageData[i]
        const g = imageData[i + 1]
        const b = imageData[i + 2]
        const rgb = `#${((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1)}`

        colorMap[rgb] = (colorMap[rgb] || 0) + 1
        if (colorMap[rgb] > maxCount) {
          maxCount = colorMap[rgb]
          dominantColor = rgb
        }
      }

      resolve(dominantColor)
    }

    img.onerror = reject
  })
} 