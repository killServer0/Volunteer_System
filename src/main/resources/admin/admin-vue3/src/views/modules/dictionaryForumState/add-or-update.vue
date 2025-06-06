<template>
    <div class="add-edit-block">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="auto" class="form-content" :style="formStyle">
            <el-row>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="帖子状态编码" prop="codeIndex" :style="formItemStyle">
                        <el-input v-model="form.codeIndex" placeholder="帖子状态编码" clearable :readonly="ro.codeIndex"
                            :style="inputStyle" />
                    </el-form-item>
                    <el-form-item v-else label="帖子状态编码" prop="codeIndex" :style="formItemStyle">
                        <el-input v-model="form.codeIndex" placeholder="帖子状态编码" readonly :style="inputStyle" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="帖子状态" prop="indexName" :style="formItemStyle">
                        <el-input v-model="form.indexName" placeholder="帖子状态" clearable :readonly="ro.indexName"
                            :style="inputStyle" />
                    </el-form-item>
                    <el-form-item v-else label="帖子状态" prop="indexName" :style="formItemStyle">
                        <el-input v-model="form.indexName" placeholder="帖子状态" readonly :style="inputStyle" />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="父字段id" prop="superId" :style="formItemStyle">
                        <el-input v-model="form.superId" placeholder="父字段id" clearable :readonly="ro.superId"
                            :style="inputStyle" />
                    </el-form-item>
                    <el-form-item v-else label="父字段id" prop="superId" :style="formItemStyle">
                        <el-input v-model="form.superId" placeholder="父字段id" readonly :style="inputStyle" />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type !== 'info'" type="primary" @click="onSubmit" :style="buttonStyle">提交</el-button>
                <el-button @click="back" :style="buttonStyle">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getInfo, save, update, getMaxCodeIndex } from '@/api/dictionaryForumState'
import styleJs from '@/utils/style'

const emit = defineEmits(['refreshDataList'])

// 表单ref
const formRef = ref(null)

// 样式配置
const formStyle = ref({})
const formItemStyle = ref({})
const inputStyle = ref({})
const buttonStyle = ref({})

// 初始化样式
const initStyle = () => {
    const style = styleJs.addStyle()
    formStyle.value = {
        backgroundColor: style.addEditFormBgColor,
        color: style.addEditFormFontColor,
        fontSize: style.addEditFormFontSize
    }
    formItemStyle.value = {
        marginBottom: style.addEditFormItemMarginBottom
    }
    inputStyle.value = {
        height: style.inputHeight,
        color: style.inputFontColor,
        fontSize: style.inputFontSize,
        borderWidth: style.inputBorderWidth,
        borderStyle: style.inputBorderStyle,
        borderColor: style.inputBorderColor,
        borderRadius: style.inputBorderRadius,
        backgroundColor: style.inputBgColor
    }
    buttonStyle.value = {
        height: style.buttonHeight,
        color: style.buttonFontColor,
        fontSize: style.buttonFontSize,
        borderWidth: style.buttonBorderWidth,
        borderStyle: style.buttonBorderStyle,
        borderColor: style.buttonBorderColor,
        borderRadius: style.buttonBorderRadius,
        backgroundColor: style.buttonBgColor
    }
}

// 表单数据
const form = reactive({
    id: '',
    codeIndex: '',
    indexName: '',
    superId: '',
    dicCode: 'forum_state_types',
    dicName: '帖子状态'
})

// 只读控制
const ro = reactive({
    codeIndex: true,
    indexName: false,
    superId: false
})

// 表单校验规则
const rules = reactive({
    codeIndex: [
        { required: true, message: '帖子状态编码不能为空', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: '帖子状态编码只能为正整数', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '帖子状态不能为空', trigger: 'blur' },
        { min: 2, max: 20, message: '帖子状态长度在2到20个字符之间', trigger: 'blur' }
    ],
    superId: [
        { required: true, message: '父字段id不能为空', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: '父字段id只能为正整数', trigger: 'blur' }
    ]
})

// 类型（info: 详情, update: 修改, add: 新增）
const type = ref('')

// 初始化
const init = async (id, type) => {
    form.id = id || ''
    type.value = type || 'add'
    initStyle()

    if (type === 'info' || type === 'update') {
        // 获取详情
        try {
            const { data } = await getInfo(id)
            Object.assign(form, data)
        } catch (error) {
            console.error('获取详情失败:', error)
            ElMessage.error('获取详情失败')
        }
    } else {
        // 获取最大编码
        try {
            const { data } = await getMaxCodeIndex({ dicCode: 'forum_state_types' })
            form.codeIndex = data.maxCodeIndex
        } catch (error) {
            console.error('获取最大编码失败:', error)
            ElMessage.error('获取最大编码失败')
        }
    }
}

// 提交
const onSubmit = async () => {
    if (!form.indexName) {
        ElMessage.error('帖子状态不能为空')
        return
    }

    await formRef.value.validate(async (valid) => {
        if (valid) {
            try {
                if (form.id) {
                    await update(form)
                    ElMessage.success('修改成功')
                } else {
                    await save(form)
                    ElMessage.success('新增成功')
                }
                emit('refreshDataList')
                back()
            } catch (error) {
                console.error('提交失败:', error)
                ElMessage.error('提交失败')
            }
        }
    })
}

// 返回
const back = () => {
    emit('refreshDataList')
}

// 对外暴露方法
defineExpose({
    init
})
</script>

<style scoped>
.add-edit-block {
    padding: 20px;
}

.form-content {
    margin-bottom: 20px;
}

.btn {
    margin-top: 20px;
    text-align: center;
}
</style>