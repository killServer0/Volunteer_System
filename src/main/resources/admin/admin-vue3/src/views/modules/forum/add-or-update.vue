<template>
    <el-dialog v-model="visible" :title="!dataForm.id ? '新增' : '修改'" :style="contents.dialogStyle">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="80px" :style="contents.formStyle">
            <el-form-item label="帖子标题" prop="forumName">
                <el-input v-model="dataForm.forumName" placeholder="帖子标题" :style="contents.inputStyle" :disabled="!!dataForm.id" />
            </el-form-item>
            <el-form-item label="帖子图片" prop="forumPhoto" v-if="!dataForm.id">
                <el-upload class="avatar-uploader" :action="getActionUrl" :show-file-list="false"
                    :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                    <img v-if="dataForm.forumPhoto" :src="dataForm.forumPhoto" class="avatar"
                        :style="contents.uploadStyle">
                    <el-icon v-else class="avatar-uploader-icon" :style="contents.uploadIconStyle">
                        <Plus />
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="帖子内容" prop="forumContent">
                <el-input v-if="!dataForm.id" v-model="dataForm.forumContent" type="textarea" :rows="10" placeholder="帖子内容" :style="contents.inputStyle" />
                <div v-else class="forum-content" v-html="dataForm.forumContent"></div>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false" :style="contents.buttonStyle">取消</el-button>
                <el-button v-if="!dataForm.id" type="primary" @click="dataFormSubmit()" :style="contents.buttonStyle">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, defineEmits, onUnmounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { http } from '@/utils/http'

// 定义事件
const emit = defineEmits(['refreshDataList'])

// 对话框显示控制
const visible = ref(false)

// 样式配置
const contents = ref({
    formStyle: {
        padding: '20px',
        backgroundColor: '#fff',
        borderRadius: '4px',
        boxShadow: '0 2px 12px 0 rgba(0, 0, 0, 0.1)'
    },
    inputStyle: {
        width: '100%'
    },
    buttonStyle: {
        marginRight: '10px'
    },
    dialogStyle: {
        padding: '20px'
    },
    uploadStyle: {
        width: '178px',
        height: '178px',
        display: 'block'
    },
    uploadIconStyle: {
        fontSize: '28px',
        color: '#8c939d',
        width: '178px',
        height: '178px',
        lineHeight: '178px',
        textAlign: 'center'
    }
})

// 表单数据
const dataForm = reactive({
    id: 0,
    forumName: '',
    forumContent: '',
    forumPhoto: '',
    usersId: '',
    zhiyuanzheId: '',
    feizhiyuanId: '',
    insertTime: '',
    updateTime: ''
})

// 表单验证规则
const dataRule = reactive({
    forumName: [
        { required: true, message: '帖子标题不能为空', trigger: 'blur' },
        { min: 1, max: 100, message: '帖子标题长度必须在1到100个字符之间', trigger: 'blur' }
    ],
    forumContent: [
        { required: true, message: '帖子内容不能为空', trigger: 'blur' },
        { min: 1, max: 1000, message: '帖子内容长度必须在1到1000个字符之间', trigger: 'blur' }
    ],
    forumPhoto: [
        { required: true, message: '请上传帖子图片', trigger: 'change' }
    ]
})

// 图片上传相关
const getActionUrl = ref('http://localhost:8081/zhiyuanzheguanlixitong/file/upload')

const handleAvatarSuccess = (res) => {
    if (res.code === 0) {
        dataForm.forumPhoto = res.data
    } else {
        ElMessage.error(res.msg)
    }
}

const beforeAvatarUpload = (file) => {
    const isJPG = file.type === 'image/jpeg'
    const isPNG = file.type === 'image/png'
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isJPG && !isPNG) {
        ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
        return false
    }
    if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
        return false
    }
    return true
}

// 表单ref
const dataFormRef = ref(null)

// 组件挂载
onMounted(() => {
    console.log('add-or-update 组件挂载')
})

// 组件卸载
onUnmounted(() => {
    console.log('add-or-update 组件卸载')
    // 清理数据
    Object.assign(dataForm, {
        id: 0,
        forumName: '',
        forumContent: '',
        forumPhoto: '',
        usersId: '',
        zhiyuanzheId: '',
        feizhiyuanId: '',
        insertTime: '',
        updateTime: ''
    })
    visible.value = false
})

// 监听 visible 变化
watch(visible, (newVal) => {
    console.log('对话框显示状态变化:', newVal)
    if (!newVal) {
        // 关闭对话框时清理数据
        Object.assign(dataForm, {
            id: 0,
            forumName: '',
            forumContent: '',
            forumPhoto: '',
            usersId: '',
            zhiyuanzheId: '',
            feizhiyuanId: '',
            insertTime: '',
            updateTime: ''
        })
    }
})

// 初始化
const init = async (id) => {
    console.log('初始化开始，ID:', id)
    dataForm.id = id || 0
    if (id) {
        try {
            console.log('准备获取论坛信息，ID:', id)
            const response = await http({
                url: 'forum/info/' + id,
                method: 'get'
            })

            console.log('获取论坛信息响应:', response)

            if (response && response.code === 0) {
                console.log('准备设置论坛信息:', response.data)
                Object.assign(dataForm, response.data)
                visible.value = true
                console.log('论坛信息设置完成，对话框状态:', visible.value)
            } else {
                console.error('获取论坛信息失败: 响应数据为空')
                ElMessage.error(response.msg || '获取论坛信息失败')
            }
        } catch (error) {
            console.error('获取论坛信息失败:', error)
            ElMessage.error('获取论坛信息失败')
        }
    } else {
        console.log('新增模式，显示对话框')
        visible.value = true
    }
}

// 提交
const dataFormSubmit = async () => {
    if (!dataFormRef.value) return

    await dataFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const response = await http({
                    url: dataForm.id ? 'forum/update' : 'forum/save',
                    method: 'post',
                    data: dataForm
                })

                console.log('提交响应:', response)

                if (response && response.code === 0) {
                    ElMessage({
                        message: '操作成功',
                        type: 'success',
                        duration: 1500,
                        onClose: () => {
                            visible.value = false
                            emit('refreshDataList')
                        }
                    })
                } else {
                    console.error('提交失败:', response.msg)
                    ElMessage.error(response.msg || '操作失败')
                }
            } catch (error) {
                console.error('提交失败:', error)
                ElMessage.error('提交失败')
            }
        }
    })
}

// 暴露方法给父组件
defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.dialog-footer {
    margin-top: 20px;
    text-align: right;
}

.avatar-uploader {
    :deep(.el-upload) {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;

        &:hover {
            border-color: #409EFF;
        }
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
}

.forum-content {
    padding: 10px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    min-height: 200px;
    background-color: #f5f7fa;
}
</style>