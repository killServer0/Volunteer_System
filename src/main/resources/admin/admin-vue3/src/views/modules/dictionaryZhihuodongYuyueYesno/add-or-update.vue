<template>
    <div class="add-edit-block">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="auto" class="form-content">
            <el-row>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="状态编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="状态编码" clearable :readonly="ro.codeIndex" />
                    </el-form-item>
                    <el-form-item v-else label="状态编码" prop="codeIndex">
                        <el-input v-model="form.codeIndex" placeholder="状态编码" readonly />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item v-if="type !== 'info'" label="状态名称" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="状态名称" clearable :readonly="ro.indexName" />
                    </el-form-item>
                    <el-form-item v-else label="状态名称" prop="indexName">
                        <el-input v-model="form.indexName" placeholder="状态名称" readonly />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type !== 'info'" type="primary" @click="onSubmit">提交</el-button>
                <el-button @click="back">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getInfo, save, update, getMaxCodeIndex } from '@/api/dictionaryZhihuodongYuyueYesno'

const emit = defineEmits(['refreshDataList'])

// 表单ref
const formRef = ref(null)

// 表单数据
const form = reactive({
    id: '',
    codeIndex: '',
    indexName: '',
    dicCode: 'zhihuodong_yuyue_yesno_types',
    dicName: '活动预约状态'
})

// 只读控制
const ro = reactive({
    codeIndex: true,
    indexName: false
})

// 表单校验规则
const rules = reactive({
    indexName: [
        { required: true, message: '状态名称不能为空', trigger: 'blur' }
    ]
})

// 类型（info: 详情, update: 修改, add: 新增）
const type = ref('')

// 初始化
const init = async (id, type) => {
    form.id = id || ''
    type.value = type || 'add'

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
            const { data } = await getMaxCodeIndex({ dicCode: 'zhihuodong_yuyue_yesno_types' })
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
        ElMessage.error('状态名称不能为空')
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