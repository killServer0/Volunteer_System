<template>
    <el-dialog title="分配权限" v-model="visible" :close-on-click-modal="false" width="600px" :style="contents.dialogStyle">
        <el-tree ref="treeRef" :data="menuList" show-checkbox node-key="id" :props="defaultProps"
            :default-expand-all="true" :style="contents.treeStyle"></el-tree>
        <template #footer>
            <el-button @click="visible = false" :style="contents.buttonStyle">取消</el-button>
            <el-button type="primary" @click="dataFormSubmit()" :style="contents.buttonStyle">确定</el-button>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import styleJs from '@/utils/style'

const contents = reactive(styleJs)
const visible = ref(false)
const treeRef = ref(null)
const menuList = ref([])
const roleId = ref(0)

const defaultProps = {
    children: 'children',
    label: 'name'
}

// 初始化
const init = (id) => {
    roleId.value = id
    visible.value = true
    // 获取所有菜单列表
    http.get('menu/list').then(({ data }) => {
        menuList.value = data.data
        // 获取角色拥有的菜单ID列表
        http.get(`role/menu/${roleId.value}`).then(({ data }) => {
            if (data && data.code === 0) {
                treeRef.value.setCheckedKeys(data.data)
            }
        })
    })
}

// 表单提交
const dataFormSubmit = () => {
    const checkedKeys = treeRef.value.getCheckedKeys()
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys()
    const menuIds = [...checkedKeys, ...halfCheckedKeys]

    http.post('role/menu/assign', {
        roleId: roleId.value,
        menuIds: menuIds
    }).then(({ data }) => {
        if (data && data.code === 0) {
            ElMessage.success('操作成功')
            visible.value = false
        } else {
            ElMessage.error(data.msg || '操作失败')
        }
    })
}

defineExpose({
    init
})
</script>

<style lang="scss" scoped>
.el-dialog {
    .el-tree {
        padding: 20px;
    }
}
</style>