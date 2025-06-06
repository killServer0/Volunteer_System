<template>
    <el-dialog :title="!dataForm.id ? '新增' : '回复'" v-model="visible" width="600px" :close-on-click-modal="false"
        :close-on-press-escape="false" :destroy-on-close="true">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="auto">
            <el-form-item label="留言标题" prop="liuyanName">
                <el-input v-model="dataForm.liuyanName" placeholder="留言标题" :disabled="!!dataForm.id" />
            </el-form-item>
            <el-form-item label="志愿者" prop="zhiyuanzheId">
                <el-select v-model="dataForm.zhiyuanzheId" placeholder="请选择志愿者" :disabled="!!dataForm.id">
                    <el-option v-for="(item, index) in zhiyuanzheOptions" :key="index" :label="item.zhiyuanzheName"
                        :value="item.id" />
                </el-select>
            </el-form-item>
            <el-form-item label="留言内容" prop="liuyanText">
                <el-input v-model="dataForm.liuyanText" type="textarea" :rows="4" placeholder="留言内容"
                    :disabled="!!dataForm.id" />
            </el-form-item>
            <el-form-item label="回复内容" prop="replyText" v-if="dataForm.id">
                <el-input v-model="dataForm.replyText" type="textarea" :rows="4" placeholder="回复内容" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'

const emit = defineEmits(['refreshDataList'])

// 弹窗显示状态
const visible = ref(false)

// 表单数据
const dataForm = reactive({
    id: '',
    liuyanName: '',
    zhiyuanzheId: '',
    liuyanText: '',
    replyText: ''
})

// 志愿者选项
const zhiyuanzheOptions = ref([])

// 表单校验规则
const dataRule = {
    liuyanName: [{ required: true, message: '留言标题不能为空', trigger: 'blur' }],
    zhiyuanzheId: [{ required: true, message: '志愿者不能为空', trigger: 'change' }],
    liuyanText: [{ required: true, message: '留言内容不能为空', trigger: 'blur' }],
    replyText: [{ required: true, message: '回复内容不能为空', trigger: 'blur' }]
}

// 表单引用
const dataFormRef = ref(null)

// 获取志愿者选项
const getZhiyuanzheOptions = async () => {
    try {
        const { data } = await http({
            url: 'zhiyuanzhe/list',
            method: 'get'
        })

        if (data && data.code === 0) {
            zhiyuanzheOptions.value = data.data
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('获取志愿者选项失败:', error)
        ElMessage.error('获取志愿者选项失败')
    }
}

// 初始化
const init = async (id, type) => {
    dataForm.id = id || ''
    visible.value = true

    // 获取志愿者选项
    await getZhiyuanzheOptions()

    // 如果是修改，获取数据
    if (dataForm.id) {
        try {
            const { data } = await http({
                url: `zhiyuanzheLiuyan/info/${dataForm.id}`,
                method: 'get'
            })

            if (data && data.code === 0) {
                Object.assign(dataForm, data.data)
            } else {
                ElMessage.error(data.msg)
            }
        } catch (error) {
            console.error('获取志愿者留言信息失败:', error)
            ElMessage.error('获取志愿者留言信息失败')
        }
    }
}

// 表单提交
const dataFormSubmit = () => {
    dataFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const { data } = await http({
                    url: !dataForm.id ? 'zhiyuanzheLiuyan/save' : 'zhiyuanzheLiuyan/update',
                    method: 'post',
                    data: dataForm
                })

                if (data && data.code === 0) {
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
                    ElMessage.error(data.msg)
                }
            } catch (error) {
                console.error('保存失败:', error)
                ElMessage.error('保存失败')
            }
        }
    })
}

// 暴露方法
defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.dialog-footer {
    text-align: right;
}
</style>