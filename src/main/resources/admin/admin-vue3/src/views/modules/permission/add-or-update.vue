<template>
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" :close-on-click-modal="false" width="600px">
        <el-form ref="dataFormRef" :model="dataForm" :rules="dataRule" label-width="80px"
            @keyup.enter="dataFormSubmit()">
            <el-form-item label="类型" prop="type">
                <el-radio-group v-model="dataForm.type">
                    <el-radio :label="0">模块</el-radio>
                    <el-radio :label="1">页面</el-radio>
                    <el-radio :label="2">按钮</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="名称" prop="name">
                <el-input v-model="dataForm.name" placeholder="权限名称"></el-input>
            </el-form-item>
            <el-form-item label="上级权限" prop="parentId">
                <el-tree-select v-model="dataForm.parentId" :data="permissionList"
                    :props="{ label: 'name', value: 'id' }" placeholder="请选择上级权限" check-strictly clearable />
            </el-form-item>
            <el-form-item label="编码" prop="code">
                <el-input v-model="dataForm.code" placeholder="权限编码"></el-input>
            </el-form-item>
            <el-form-item label="路径" prop="url" v-if="dataForm.type !== 2">
                <el-input v-model="dataForm.url" placeholder="权限路径"></el-input>
            </el-form-item>
            <el-form-item label="排序号" prop="orderNum">
                <el-input-number v-model="dataForm.orderNum" :min="0" :max="999"
                    controls-position="right"></el-input-number>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button @click="visible = false">取消</el-button>
            <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'

const visible = ref(false)
const dataFormRef = ref(null)
const permissionList = ref([])
const dataForm = reactive({
    id: 0,
    type: 1,
    name: '',
    parentId: 0,
    code: '',
    url: '',
    orderNum: 0
})

const dataRule = {
    type: [
        { required: true, message: '类型不能为空', trigger: 'change' }
    ],
    name: [
        { required: true, message: '名称不能为空', trigger: 'blur' }
    ],
    code: [
        { required: true, message: '编码不能为空', trigger: 'blur' }
    ]
}

// 监听类型变化
watch(() => dataForm.type, (newVal) => {
    if (newVal === 2) {
        dataForm.url = ''
    }
})

// 获取权限列表
const getPermissionList = () => {
    http.get('permission/list').then(({ data }) => {
        if (data && data.code === 0) {
            permissionList.value = data.data
        }
    })
}

// 初始化
const init = (id) => {
    dataForm.id = id || 0
    visible.value = true
    getPermissionList()
    if (dataForm.id) {
        http.get(`permission/info/${dataForm.id}`).then(({ data }) => {
            if (data && data.code === 0) {
                Object.assign(dataForm, data.data)
            }
        })
    }
}

// 表单提交
const dataFormSubmit = () => {
    dataFormRef.value.validate((valid) => {
        if (valid) {
            const url = !dataForm.id ? 'permission/save' : 'permission/update'
            http.post(url, dataForm).then(({ data }) => {
                if (data && data.code === 0) {
                    ElMessage.success('操作成功')
                    visible.value = false
                    emit('refreshDataList')
                } else {
                    ElMessage.error(data.msg || '操作失败')
                }
            })
        }
    })
}

const emit = defineEmits(['refreshDataList'])

defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.el-dialog {
    .el-form {
        padding: 20px;
    }
}
</style>