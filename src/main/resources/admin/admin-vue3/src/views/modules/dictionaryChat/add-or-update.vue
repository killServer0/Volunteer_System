<template>
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" :close-on-click-modal="false"
        :close-on-press-escape="false" :destroy-on-close="true">
        <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="100px"
            :style="{ backgroundColor: contents.formBgColor, color: contents.formFontColor }">
            <el-row>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="数据类型编码" prop="codeIndex">
                        <el-input v-model="dataForm.codeIndex" placeholder="数据类型编码" clearable
                            :readonly="ro.codeIndex"></el-input>
                    </el-form-item>
                    <div v-else-if="type === 'info'">
                        <el-form-item class="input" label="数据类型编码" prop="codeIndex">
                            <el-input v-model="dataForm.codeIndex" placeholder="数据类型编码" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type !== 'info'" label="数据类型" prop="indexName">
                        <el-input v-model="dataForm.indexName" placeholder="数据类型" clearable
                            :readonly="ro.indexName"></el-input>
                    </el-form-item>
                    <div v-else-if="type === 'info'">
                        <el-form-item class="input" label="数据类型" prop="indexName">
                            <el-input v-model="dataForm.indexName" placeholder="数据类型" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false"
                    :style="{ backgroundColor: contents.btnCancelBgColor, color: contents.btnCancelFontColor }">取消</el-button>
                <el-button v-if="type !== 'info'" type="primary" @click="dataFormSubmit()"
                    :style="{ backgroundColor: contents.btnConfirmBgColor, color: contents.btnConfirmFontColor }">确定</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style.js'

const visible = ref(false)
const dataFormRef = ref(null)
const type = ref('')
const contents = ref(null)
const ro = ref({
    codeIndex: true,
    indexName: false
})

const dataForm = reactive({
    id: '',
    codeIndex: '',
    indexName: '',
    dicCode: 'chat_types',
    dicName: '数据类型'
})

const dataRule = {
    codeIndex: [
        { required: true, message: '数据类型编码不能为空', trigger: 'blur' },
        { pattern: /^[0-9]*$/, message: '只允许输入整数', trigger: 'blur' }
    ],
    indexName: [
        { required: true, message: '数据类型不能为空', trigger: 'blur' }
    ]
}

const init = async (id, type) => {
    dataForm.id = id || ''
    type.value = type || ''
    visible.value = true
    nextTick(() => {
        dataFormRef.value?.resetFields()
        if (id) {
            http({
                url: `dictionary/info/${id}`,
                method: 'get'
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    Object.assign(dataForm, data.data)
                }
            })
        } else {
            // 查询最大值
            http({
                url: 'dictionary/maxCodeIndex',
                method: 'post',
                data: { dicCode: 'chat_types' }
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

const dataFormSubmit = () => {
    dataFormRef.value?.validate(async (valid) => {
        if (valid) {
            const url = dataForm.id ? 'dictionary/update' : 'dictionary/add'
            const method = dataForm.id ? 'put' : 'post'
            try {
                const { data } = await http({
                    url,
                    method,
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
                console.error('提交失败:', error)
                ElMessage.error('提交失败')
            }
        }
    })
}

// 样式系统
const initStyle = () => {
    contents.value = styleJs.style()
}

onMounted(() => {
    initStyle()
})

defineExpose({
    init
})

const emit = defineEmits(['refreshDataList'])
</script>

<style lang="scss" scoped>
.dialog-footer {
    padding: 20px;
    text-align: right;
}
</style>