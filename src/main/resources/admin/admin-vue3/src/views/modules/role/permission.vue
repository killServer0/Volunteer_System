<template>
  <el-dialog title="分配权限" v-model="visible" width="500px" :close-on-click-modal="false" :style="contents.dialogStyle">
    <el-tree ref="treeRef" :data="menuList" :props="menuListTreeProps" node-key="id" show-checkbox default-expand-all
      :style="contents.treeStyle"></el-tree>
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
const roleId = ref(0)
const menuList = ref([])
const menuListTreeProps = {
  label: 'name',
  children: 'children'
}

// 初始化
const init = (id) => {
  roleId.value = id
  visible.value = true
  // 加载菜单列表
  http.get('menu/list').then(({ data }) => {
    if (data && data.code === 0) {
      menuList.value = data.data
      // 加载角色已有权限
      http.get(`role/menu/${roleId.value}`).then(({ data }) => {
        if (data && data.code === 0) {
          treeRef.value?.setCheckedKeys(data.data)
        }
      })
    }
  })
}

// 表单提交
const dataFormSubmit = () => {
  const menuIds = treeRef.value?.getCheckedKeys()
  http.post('role/menu', {
    roleId: roleId.value,
    menuIds
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
    margin: 20px;
    max-height: 400px;
    overflow: auto;
  }
}
</style>