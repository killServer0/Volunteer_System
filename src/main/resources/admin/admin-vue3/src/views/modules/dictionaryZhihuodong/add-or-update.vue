<template>
    <div class="add-edit-block">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="auto" class="detail-form-content"
            :style="{ backgroundColor: addEditBox.addEditBoxColor, borderColor: addEditBox.addEditBoxBorderColor }">
            <el-row :gutter="20">
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="活动类型编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="活动类型编码" clearable :readonly="ro.codeIndex"
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                    <el-form-item v-else label="活动类型编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="活动类型编码" readonly
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="活动类型" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="活动类型" clearable :readonly="ro.indexName"
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                    <el-form-item v-else label="活动类型" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="活动类型" readonly
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="父字段id" prop="superId">
                        <el-input v-model="form.superId" placeholder="父字段id" clearable :readonly="ro.superId"
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                    <el-form-item v-else label="父字段id" prop="superId">
                        <el-input v-model="form.superId" placeholder="父字段id" readonly
                            :style="{ height: addEditForm.inputHeight, lineHeight: addEditForm.inputHeight, color: addEditForm.inputFontColor, fontSize: addEditForm.inputFontSize, borderWidth: addEditForm.inputBorderWidth, borderStyle: addEditForm.inputBorderStyle, borderColor: addEditForm.inputBorderColor, borderRadius: addEditForm.inputBorderRadius, backgroundColor: addEditForm.inputBgColor }">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type !== 'info'" type="primary" @click="onSubmit"
                    :style="{ backgroundColor: addEditForm.btnSaveBgColor, color: addEditForm.btnSaveFontColor, fontSize: addEditForm.btnSaveFontSize, borderWidth: addEditForm.btnSaveBorderWidth, borderStyle: addEditForm.btnSaveBorderStyle, borderColor: addEditForm.btnSaveBorderColor, borderRadius: addEditForm.btnSaveBorderRadius }">
                    提交
                </el-button>
                <el-button @click="back"
                    :style="{ backgroundColor: addEditForm.btnCancelBgColor, color: addEditForm.btnCancelFontColor, fontSize: addEditForm.btnCancelFontSize, borderWidth: addEditForm.btnCancelBorderWidth, borderStyle: addEditForm.btnCancelBorderStyle, borderColor: addEditForm.btnCancelBorderColor, borderRadius: addEditForm.btnCancelBorderRadius }">
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

const formRef = ref(null)
const type = ref('')
const id = ref('')

const ro = reactive({
    codeIndex: true,
    indexName: false,
    superId: false
})

const form = reactive({
    codeIndex: '',
    indexName: '',
    superId: '',
    dicCode: 'zhihuodong_types',
    dicName: '活动类型'
})

const rules = reactive({
    codeIndex: [
        { required: true, message: '活动类型编码不能为空', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: '活动类型编码只能为正整数', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '活动类型不能为空', trigger: 'blur' }
    ],
    superId: [
        { required: true, message: '父字段id不能为空', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: '父字段id只能为正整数', trigger: 'blur' }
    ]
})

const addEditBox = reactive({
    addEditBoxColor: 'rgba(255, 255, 255, 1)',
    addEditBoxBorderColor: 'rgba(0, 0, 0, 0.1)'
})

const addEditForm = reactive({
    inputHeight: '40px',
    inputFontColor: '#333',
    inputFontSize: '14px',
    inputBorderWidth: '1px',
    inputBorderStyle: 'solid',
    inputBorderColor: '#DCDFE6',
    inputBorderRadius: '4px',
    inputBgColor: '#fff',
    inputLableColor: '#606266',
    inputLableFontSize: '14px',
    btnSaveBgColor: '#409EFF',
    btnSaveFontColor: '#fff',
    btnSaveFontSize: '14px',
    btnSaveBorderWidth: '1px',
    btnSaveBorderStyle: 'solid',
    btnSaveBorderColor: '#409EFF',
    btnSaveBorderRadius: '4px',
    btnCancelBgColor: '#fff',
    btnCancelFontColor: '#606266',
    btnCancelFontSize: '14px',
    btnCancelBorderWidth: '1px',
    btnCancelBorderStyle: 'solid',
    btnCancelBorderColor: '#DCDFE6',
    btnCancelBorderRadius: '4px'
})

const init = async (newId, newType) => {
    id.value = newId
    type.value = newType

    if (type.value === 'info' || type.value === 'else') {
        await info(id.value)
    } else {
        // 获取最大编码
        try {
            const { data } = await getMaxCodeIndex({ dicCode: 'zhihuodong_types' })
            if (data && data.code === 0) {
                form.codeIndex = data.maxCodeIndex
            } else {
                ElMessage.error(data.msg)
            }
        } catch (error) {
            console.error('获取最大编码失败:', error)
            ElMessage.error('获取最大编码失败')
        }
    }
}

const info = async (id) => {
    try {
        const { data } = await getDictionaryInfo(id)
        if (data && data.code === 0) {
            Object.assign(form, data.data)
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('获取字典信息失败:', error)
        ElMessage.error('获取字典信息失败')
    }
}

const onSubmit = async () => {
    if (!form.indexName) {
        ElMessage.error('活动类型不能为空')
        return
    }

    try {
        await formRef.value.validate()
        const { data } = await (id.value ? updateDictionary(form) : saveDictionary(form))
        if (data && data.code === 0) {
            ElMessage.success('操作成功')
            emit('refreshDataList')
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('提交失败:', error)
        ElMessage.error('提交失败')
    }
}

const back = () => {
    emit('refreshDataList')
}

onMounted(() => {
    init(props.id, props.type)
})

defineExpose({
    init
})
</script>

<style scoped>
.add-edit-block {
    padding: 20px;
}

.detail-form-content {
    background-color: #fff;
    padding: 20px;
    border-radius: 4px;
}

.btn {
    margin-top: 20px;
    text-align: center;
}

.btn-success {
    margin-right: 10px;
}

:deep(.el-input__inner) {
    height: 40px;
    line-height: 40px;
}

:deep(.el-form-item__label) {
    font-weight: 500;
}
</style>