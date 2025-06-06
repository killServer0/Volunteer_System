<template>
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" width="40%" :close-on-click-modal="false">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="80px">
            <el-form-item label="活动类型编码" prop="codeIndex">
                <el-input v-model="dataForm.codeIndex" placeholder="活动类型编码" :readonly="type == 'info'"
                    :style="{ width: '100%' }"></el-input>
            </el-form-item>
            <el-form-item label="活动类型名称" prop="indexName">
                <el-input v-model="dataForm.indexName" placeholder="活动类型名称" :readonly="type == 'info'"
                    :style="{ width: '100%' }"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="beizhu">
                <el-input type="textarea" v-model="dataForm.beizhu" placeholder="备注" :readonly="type == 'info'"
                    :style="{ width: '100%' }"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit()" v-if="type != 'info'">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDictionaryFeihuodongInfo, saveDictionaryFeihuodong, updateDictionaryFeihuodong } from '@/api/dictionaryFeihuodong'
import styleJs from '@/utils/style.js'

const visible = ref(false)
const dataFormRef = ref(null)
const dataForm = reactive({
    id: '',
    codeIndex: '',
    indexName: '',
    beizhu: ''
})
const type = ref('')
const contents = ref(null)

// 表单验证规则
const dataRule = {
    codeIndex: [
        { required: true, message: '活动类型编码不能为空', trigger: 'blur' },
        { pattern: /^\d+$/, message: '活动类型编码只能为数字', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '活动类型名称不能为空', trigger: 'blur' }
    ],
    beizhu: [
        { max: 200, message: '备注不能超过200个字符', trigger: 'blur' }
    ]
}

// 初始化方法
const init = async (id, typeParam) => {
    type.value = typeParam
    visible.value = true
    dataForm.id = id || ''
    if (dataForm.id) {
        try {
            const { data } = await getDictionaryFeihuodongInfo(dataForm.id)
            Object.assign(dataForm, data)
        } catch (error) {
            console.error('获取数据失败:', error)
            ElMessage.error('获取数据失败')
        }
    } else {
        // 新增时自动获取最大编码
        try {
            const { data } = await getDictionaryFeihuodongList({
                page: 1,
                limit: 1,
                sort: 'codeIndex',
                order: 'desc'
            })
            if (data.list && data.list.length > 0) {
                dataForm.codeIndex = (parseInt(data.list[0].codeIndex) + 1).toString()
            } else {
                dataForm.codeIndex = '1'
            }
        } catch (error) {
            console.error('获取最大编码失败:', error)
            dataForm.codeIndex = '1'
        }
    }
}

// 表单提交
const dataFormSubmit = async () => {
    if (!dataFormRef.value) return
    await dataFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (!dataForm.id) {
                    await saveDictionaryFeihuodong(dataForm)
                    ElMessage.success('新增成功')
                } else {
                    await updateDictionaryFeihuodong(dataForm)
                    ElMessage.success('修改成功')
                }
                visible.value = false
                emit('refreshDataList')
            } catch (error) {
                console.error('保存失败:', error)
                ElMessage.error('保存失败')
            }
        }
    })
}

// 样式系统
const initStyle = () => {
    contents.value = styleJs.addEditStyle()
}

defineExpose({
    init
})

const emit = defineEmits(['refreshDataList'])

onMounted(() => {
    initStyle()
})
</script>

<style scoped>
.dialog-footer {
    text-align: right;
}
</style>