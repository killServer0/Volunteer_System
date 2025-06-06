<template>
    <div class="editor-container">
        <div ref="editorRef" class="editor"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import { ElMessage } from 'element-plus'
import E from 'wangeditor'

const props = defineProps({
    modelValue: {
        type: String,
        default: ''
    },
    height: {
        type: Number,
        default: 300
    }
})

const emit = defineEmits(['update:modelValue'])

const editorRef = ref(null)
let editor = null

onMounted(() => {
    editor = new E(editorRef.value)
    editor.config.height = props.height
    editor.config.uploadImgShowBase64 = true
    editor.config.uploadImgServer = '/api/file/upload'
    editor.config.uploadImgHeaders = {
        token: localStorage.getItem('token')
    }
    editor.config.uploadImgHooks = {
        customInsert: function (insertImgFn, result) {
            if (result.code === 0) {
                insertImgFn(result.data)
            } else {
                ElMessage.error(result.msg || '上传失败')
            }
        }
    }
    editor.create()
    editor.txt.html(props.modelValue)
})

onBeforeUnmount(() => {
    if (editor) {
        editor.destroy()
        editor = null
    }
})

watch(() => props.modelValue, (newVal) => {
    if (editor && newVal !== editor.txt.html()) {
        editor.txt.html(newVal)
    }
})

const getContent = () => {
    return editor ? editor.txt.html() : ''
}

const setContent = (content) => {
    if (editor) {
        editor.txt.html(content)
    }
}

defineExpose({
    getContent,
    setContent
})
</script>

<style scoped>
.editor-container {
    width: 100%;
}

.editor {
    text-align: left;
}
</style>