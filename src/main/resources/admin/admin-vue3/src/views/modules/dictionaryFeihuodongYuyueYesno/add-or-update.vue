<template>
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" width="50%" :close-on-click-modal="false">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="100px" :style="contents.formStyle">
            <el-form-item label="报名状态编码" prop="codeIndex">
                <el-input v-model="dataForm.codeIndex" placeholder="报名状态编码" :readonly="type === 'info'"
                    :style="contents.inputStyle"></el-input>
            </el-form-item>
            <el-form-item label="报名状态名称" prop="indexName">
                <el-input v-model="dataForm.indexName" placeholder="报名状态名称" :readonly="type === 'info'"
                    :style="contents.inputStyle"></el-input>
            </el-form-item>
            <el-form-item label="备注" prop="beizhu">
                <el-input v-model="dataForm.beizhu" type="textarea" placeholder="备注" :rows="4"
                    :style="contents.textareaStyle"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false" :style="contents.buttonStyle">取消</el-button>
                <el-button type="primary" @click="dataFormSubmit()" v-if="type !== 'info'"
                    :style="contents.buttonStyle">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style.js'

const emit = defineEmits(['refreshDataList'])

const visible = ref(false)
const dataFormRef = ref(null)
const type = ref('')
const contents = ref(styleJs.listStyle())

// 表单数据
const dataForm = reactive({
    id: 0,
    codeIndex: '',
    indexName: '',
    beizhu: ''
})

// 表单验证规则
const dataRule = reactive({
    codeIndex: [
        { required: true, message: '报名状态编码不能为空', trigger: 'blur' },
        { pattern: /^\d+$/, message: '报名状态编码只能为数字', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '报名状态名称不能为空', trigger: 'blur' },
        { min: 2, max: 50, message: '报名状态名称长度在2到50个字符之间', trigger: 'blur' }
    ],
    beizhu: [
        { max: 200, message: '备注长度不能超过200个字符', trigger: 'blur' }
    ]
})

// 初始化
const init = (id, t) => {
    dataForm.id = id || 0
    type.value = t || ''
    visible.value = true
    nextTick(() => {
        dataFormRef.value?.resetFields()
        if (dataForm.id) {
            http({
                url: `dictionary/info/${dataForm.id}`,
                method: 'get'
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    Object.assign(dataForm, data.data)
                }
            })
        } else {
            // 新增时自动获取最大编码
            http({
                url: 'dictionary/maxCodeIndex',
                method: 'post',
                data: { dicCode: 'feihuodong_yuyue_yesno_types' }
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    dataForm.codeIndex = data.maxCodeIndex
                } else {
                    ElMessage.error(data.msg)
                }
            })
        }
    })
}

// 表单提交
const dataFormSubmit = () => {
    dataFormRef.value?.validate((valid) => {
        if (valid) {
            const ruleForm = { ...dataForm }
            ruleForm.dicCode = 'feihuodong_yuyue_yesno_types'
            ruleForm.dicName = '报名状态'
            http({
                url: dataForm.id ? 'dictionary/update' : 'dictionary/save',
                method: 'post',
                data: ruleForm
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    ElMessage.success('操作成功')
                    visible.value = false
                    emit('refreshDataList')
                } else {
                    ElMessage.error(data.msg)
                }
            })
        }
    })
}

const closeDialog = () => {
    visible.value = false
    dataFormRef.value?.resetFields()
    Object.assign(dataForm, {
        id: null,
        codeIndex: '',
        indexName: '',
        beizhu: ''
    })
}

defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.dialog-footer {
    text-align: right;
    padding: 20px;
}
</style>