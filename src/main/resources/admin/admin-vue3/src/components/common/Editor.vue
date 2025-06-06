<template>
  <div>
    <!-- 图片上传组件辅助-->
    <el-upload class="avatar-uploader" :action="getActionUrl" name="file" :headers="header" :show-file-list="false"
      :on-success="uploadSuccess" :on-error="uploadError" :before-upload="beforeUpload"></el-upload>

    <quill-editor class="editor" v-model="content" ref="myQuillEditor" :options="editorOption" @blur="onEditorBlur"
      @focus="onEditorFocus" @change="onEditorChange"></quill-editor>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { quillEditor } from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import { ElMessage } from 'element-plus'
import storage from '@/utils/storage'

// 工具栏配置
const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // 加粗 斜体 下划线 删除线
  ["blockquote", "code-block"], // 引用  代码块
  [{ header: 1 }, { header: 2 }], // 1、2 级标题
  [{ list: "ordered" }, { list: "bullet" }], // 有序、无序列表
  [{ script: "sub" }, { script: "super" }], // 上标/下标
  [{ indent: "-1" }, { indent: "+1" }], // 缩进
  [{ size: ["small", false, "large", "huge"] }], // 字体大小
  [{ header: [1, 2, 3, 4, 5, 6, false] }], // 标题
  [{ color: [] }, { background: [] }], // 字体颜色、字体背景颜色
  [{ font: [] }], // 字体种类
  [{ align: [] }], // 对齐方式
  ["clean"], // 清除文本格式
  ["image"] // 图片
]

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  action: {
    type: String,
    default: 'file/upload'
  },
  maxSize: {
    type: Number,
    default: 4000 //kb
  }
})

const emit = defineEmits(['update:modelValue', 'blur', 'focus', 'change'])

const content = ref('')
const quillUpdateImg = ref(false)
const myQuillEditor = ref(null)

const editorOption = {
  placeholder: '',
  theme: 'snow',
  modules: {
    toolbar: {
      container: toolbarOptions,
      handlers: {
        image: function (value) {
          if (value) {
            document.querySelector('.avatar-uploader input').click()
          } else {
            this.quill.format('image', false)
          }
        }
      }
    }
  }
}

const header = computed(() => ({
  'Token': storage.get('Token')
}))

const getActionUrl = computed(() => {
  setContent(props.modelValue)
  return `/${import.meta.env.VITE_APP_NAME}/${props.action}`
})

// 设置内容
const setContent = (value) => {
  content.value = value
}

// 编辑器失去焦点
const onEditorBlur = () => {
  emit('blur')
}

// 编辑器获得焦点
const onEditorFocus = () => {
  emit('focus')
}

// 编辑器内容改变
const onEditorChange = () => {
  emit('update:modelValue', content.value)
  emit('change', content.value)
}

// 上传前校验
const beforeUpload = () => {
  quillUpdateImg.value = true
}

// 上传成功
const uploadSuccess = (res) => {
  const quill = myQuillEditor.value.quill
  if (res.code === 0) {
    const length = quill.getSelection().index
    quill.insertEmbed(length, 'image', `${import.meta.env.VITE_API_BASE_URL}/upload/${res.file}`)
    quill.setSelection(length + 1)
  } else {
    ElMessage.error('图片插入失败')
  }
  quillUpdateImg.value = false
}

// 上传失败
const uploadError = () => {
  quillUpdateImg.value = false
  ElMessage.error('图片插入失败')
}

// 监听modelValue变化
watch(() => props.modelValue, setContent, { immediate: true })
</script>

<style>
.editor {
  line-height: normal !important;
}

.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "请输入链接地址:";
}

.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "保存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "请输入视频地址:";
}

.ql-container {
  height: 400px;
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}

.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文本";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "标题1";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "标题2";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "标题3";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "标题4";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "标题5";
}

.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "标题6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "标准字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "衬线字体";
}

.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等宽字体";
}
</style>