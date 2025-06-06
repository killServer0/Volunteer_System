<template>
    <div class="add-edit-block">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="auto" class="detail-form-content"
            :style="{ backgroundColor: contents.formBgColor, color: contents.formFontColor }">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="公告类型编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="公告类型编码" clearable :readonly="ro.codeIndex"
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
                    </el-form-item>
                    <el-form-item v-else label="公告类型编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="公告类型编码" readonly
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="公告类型名称" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="公告类型名称" clearable :readonly="ro.indexName"
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
                    </el-form-item>
                    <el-form-item v-else label="公告类型名称" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="公告类型名称" readonly
                            :style="{ width: '100%', height: contents.inputHeight, lineHeight: contents.inputHeight, color: contents.inputFontColor, fontSize: contents.inputFontSize, borderWidth: contents.inputBorderWidth, borderStyle: contents.inputBorderStyle, borderColor: contents.inputBorderColor, borderRadius: contents.inputBorderRadius, backgroundColor: contents.inputBgColor }" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type !== 'info'" type="primary" @click="onSubmit"
                    :style="{ backgroundColor: contents.btnSubmitBgColor, color: contents.btnSubmitFontColor, fontSize: contents.btnSubmitFontSize, borderWidth: contents.btnSubmitBorderWidth, borderStyle: contents.btnSubmitBorderStyle, borderColor: contents.btnSubmitBorderColor, borderRadius: contents.btnSubmitBorderRadius }">
                    提交
                </el-button>
                <el-button @click="back"
                    :style="{ backgroundColor: contents.btnCancelBgColor, color: contents.btnCancelFontColor, fontSize: contents.btnCancelFontSize, borderWidth: contents.btnCancelBorderWidth, borderStyle: contents.btnCancelBorderStyle, borderColor: contents.btnCancelBorderColor, borderRadius: contents.btnCancelBorderRadius }">
                    {{ type === 'info' ? '返回' : '取消' }}
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDictionaryInfo, saveDictionary, updateDictionary, getMaxCodeIndex } from '@/api/dictionary'
import styleJs from '@/utils/style.js'

const emit = defineEmits(['refreshDataList'])

// 表单ref
const formRef = ref(null)

// 表单数据
const form = reactive({
    id: '',
    codeIndex: '',
    indexName: '',
    dicCode: 'gonggao_types',
    dicName: '公告类型'
})

// 只读控制
const ro = reactive({
    codeIndex: true,
    indexName: false
})

// 样式系统
const contents = ref(null)

// 表单校验规则
const rules = reactive({
    codeIndex: [
        { required: true, message: '公告类型编码不能为空', trigger: 'blur' },
        { pattern: /^\d+$/, message: '公告类型编码只能为数字', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '公告类型名称不能为空', trigger: 'blur' }
    ]
})

// 类型（info: 详情, update: 修改, add: 新增）
const type = ref('')

// 初始化样式
const initStyle = () => {
    contents.value = styleJs.formStyle()
}

// 初始化
const init = async (id, type) => {
    form.id = id || ''
    type.value = type || 'add'

    if (type === 'info' || type === 'update') {
        // 获取详情
        try {
            const { data } = await getDictionaryInfo(id)
            Object.assign(form, data)
        } catch (error) {
            console.error('获取详情失败:', error)
            ElMessage.error('获取详情失败')
        }
    } else {
        // 获取最大编码
        try {
            const { data } = await getMaxCodeIndex({ dicCode: 'gonggao_types' })
            form.codeIndex = data.maxCodeIndex
        } catch (error) {
            console.error('获取最大编码失败:', error)
            ElMessage.error('获取最大编码失败')
        }
    }
}

// 提交
const onSubmit = async () => {
    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (type.value === 'add') {
                    await saveDictionary(form)
                    ElMessage.success('新增成功')
                } else {
                    await updateDictionary(form)
                    ElMessage.success('修改成功')
                }
                emit('refreshDataList')
            } catch (error) {
                console.error('保存失败:', error)
                ElMessage.error('保存失败')
            }
        }
    })
}

// 返回
const back = () => {
    emit('refreshDataList')
}

defineExpose({
    init
})

onMounted(() => {
    initStyle()
})
</script>

<style scoped>
.add-edit-block {
    padding: 20px;
}

.detail-form-content {
    margin-bottom: 20px;
}

.btn {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}
</style>