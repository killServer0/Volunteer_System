<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" v-model="visible" :close-on-click-modal="false" width="600px">
    <el-form ref="dataFormRef" :model="dataForm" :rules="dataRule" label-width="80px" @keyup.enter="dataFormSubmit()">
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="0">目录</el-radio>
          <el-radio :label="1">菜单</el-radio>
          <el-radio :label="2">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="dataForm.name" placeholder="菜单名称"></el-input>
      </el-form-item>
      <el-form-item label="上级菜单" prop="parentId">
        <el-tree-select v-model="dataForm.parentId" :data="menuList" :props="{ label: 'name', value: 'id' }"
          placeholder="请选择上级菜单" check-strictly clearable />
      </el-form-item>
      <el-form-item label="图标" prop="icon" v-if="dataForm.type !== 2">
        <el-input v-model="dataForm.icon" placeholder="图标"></el-input>
      </el-form-item>
      <el-form-item label="路由地址" prop="url" v-if="dataForm.type !== 2">
        <el-input v-model="dataForm.url" placeholder="路由地址"></el-input>
      </el-form-item>
      <el-form-item label="权限标识" prop="perms">
        <el-input v-model="dataForm.perms" placeholder="权限标识"></el-input>
      </el-form-item>
      <el-form-item label="排序号" prop="sort">
        <el-input-number v-model="dataForm.sort" :min="0" :max="999" controls-position="right"></el-input-number>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, onMounted, onErrorCaptured } from 'vue'
import { ElMessage } from 'element-plus'
import { http } from '@/utils/http'
import axios from 'axios'

console.log('菜单编辑组件开始初始化...');
console.log('HTTP模块导入状态:', http ? '成功' : '失败');

// 备用HTTP请求方法
const httpFallback = {
  get: async (url, params = {}) => {
    console.log('使用备用HTTP GET方法:', url);
    try {
      const response = await axios.get('/api' + url, { params });
      console.log('备用HTTP响应:', response);
      return response.data;
    } catch (error) {
      console.error('备用HTTP请求失败:', error);
      throw error;
    }
  },
  post: async (url, data = {}) => {
    console.log('使用备用HTTP POST方法:', url);
    try {
      const response = await axios.post('/api' + url, data);
      console.log('备用HTTP响应:', response);
      return response.data;
    } catch (error) {
      console.error('备用HTTP请求失败:', error);
      throw error;
    }
  }
};

// 使用http模块或备用方法
const httpClient = http || httpFallback;

const visible = ref(false)
const dataFormRef = ref(null)
const menuList = ref([])
const dataForm = reactive({
  id: 0,
  type: 1,
  name: '',
  parentId: 0,
  icon: '',
  url: '',
  perms: '',
  sort: 0
})

const dataRule = {
  type: [
    { required: true, message: '类型不能为空', trigger: 'change' }
  ],
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' }
  ]
}

// 捕获组件错误
onErrorCaptured((err, instance, info) => {
  console.error('菜单编辑组件捕获到错误:', err);
  console.error('错误信息:', info);
  return false; // 阻止错误向上传播
})

// 监听类型变化
watch(() => dataForm.type, (newVal) => {
  console.log('菜单类型变更为:', newVal);
  if (newVal === 2) {
    dataForm.icon = ''
    dataForm.url = ''
  }
})

// 获取菜单列表
const getMenuList = () => {
  console.log('获取菜单列表数据...');
  httpClient.get('/menu/list').then(({ data }) => {
    console.log('菜单列表响应:', data);
    if (data && data.code === 0) {
      menuList.value = data.data || []
      console.log('菜单树加载成功, 数量:', menuList.value.length);
    } else {
      console.error('获取菜单列表失败:', data?.msg);
      ElMessage.error(data?.msg || '获取菜单列表失败')
    }
  }).catch(error => {
    console.error('获取菜单列表异常:', error);
    ElMessage.error('获取菜单列表失败: ' + (error.message || '未知错误'))
  })
}

// 菜单名称映射（中文到英文）
const menuNameMapReverse = {
  '系统管理': 'System Management',
  '用户管理': 'User Management',
  '角色管理': 'Role Management',
  '首页': 'Home',
  '个人中心': 'Personal Center',
  '修改密码': 'Change Password',
  '社区活动': 'Community Activities',
  '社区活动预约': 'Community Activity Reservation',
  '志愿者活动': 'Volunteer Activities',
  '志愿者活动预约': 'Volunteer Activity Reservation',
  '志愿者管理': 'Volunteer Management',
  '志愿者留言': 'Volunteer Messages',
  '游客管理': 'Visitor Management',
  '论坛管理': 'Forum Management',
  '公告管理': 'Announcement Management',
  '聊天管理': 'Chat Management',
  '系统配置': 'System Configuration',
  '数据库备份': 'Database Backup',
  '菜单管理': 'Menu Management',
  '字典管理': 'Dictionary Management',
  '社区活动字典': 'Community Activity Dictionary',
  '社区活动预约字典': 'Community Activity Reservation Dictionary',
  '论坛状态字典': 'Forum Status Dictionary',
  '公告字典': 'Announcement Dictionary',
  '性别字典': 'Gender Dictionary',
  '志愿者活动字典': 'Volunteer Activity Dictionary',
  '志愿者活动预约字典': 'Volunteer Activity Reservation Dictionary',
  '状态字典': 'Status Dictionary'
}

// 英文到中文映射
const menuNameMap = {
  'System Management': '系统管理',
  'User Management': '用户管理',
  'Role Management': '角色管理',
  'Home': '首页',
  'Personal Center': '个人中心',
  'Change Password': '修改密码',
  'Community Activities': '社区活动',
  'Community Activity Reservation': '社区活动预约',
  'Volunteer Activities': '志愿者活动',
  'Volunteer Activity Reservation': '志愿者活动预约',
  'Volunteer Management': '志愿者管理',
  'Volunteer Messages': '志愿者留言',
  'Visitor Management': '游客管理',
  'Forum Management': '论坛管理',
  'Announcement Management': '公告管理',
  'Chat Management': '聊天管理',
  'System Configuration': '系统配置',
  'Database Backup': '数据库备份',
  'Menu Management': '菜单管理',
  'Dictionary Management': '字典管理',
  'Community Activity Dictionary': '社区活动字典',
  'Community Activity Reservation Dictionary': '社区活动预约字典',
  'Forum Status Dictionary': '论坛状态字典',
  'Announcement Dictionary': '公告字典',
  'Gender Dictionary': '性别字典',
  'Volunteer Activity Dictionary': '志愿者活动字典',
  'Volunteer Activity Reservation Dictionary': '志愿者活动预约字典',
  'Status Dictionary': '状态字典'
}

// 初始化
const init = (id) => {
  console.log('初始化菜单编辑组件, ID:', id)
  dataForm.id = id || 0
  visible.value = true
  getMenuList()
  dataFormRef.value && dataFormRef.value.resetFields()
  
  if (dataForm.id) {
    console.log('获取菜单详情数据, ID:', dataForm.id)
    httpClient.get(`/menu/info/${dataForm.id}`).then(({ data }) => {
      console.log('菜单详情响应:', data)
      if (data && data.code === 0) {
        const menuData = { ...data.data } || {}
        // 如果是英文菜单名，转换为中文显示
        if (menuNameMap[menuData.name]) {
          menuData.name = menuNameMap[menuData.name]
        }
        Object.assign(dataForm, menuData)
        console.log('菜单详情加载成功:', dataForm)
      } else {
        console.error('获取菜单详情失败:', data?.msg)
        ElMessage.error(data?.msg || '获取菜单详情失败')
      }
    }).catch(error => {
      console.error('获取菜单详情异常:', error)
      ElMessage.error('获取菜单详情失败: ' + (error.message || '未知错误'))
    })
  }
}

// 表单提交
const dataFormSubmit = () => {
  console.log('提交菜单表单...');
  if (!dataFormRef.value) {
    console.warn('表单引用不存在');
    return;
  }
  
  dataFormRef.value.validate((valid) => {
    console.log('表单验证结果:', valid);
    if (valid) {
      // 如果输入的是中文菜单名称，转换为英文保存到数据库
      const submitData = { ...dataForm }
      if (menuNameMapReverse[submitData.name]) {
        submitData.name = menuNameMapReverse[submitData.name]
      }
      
      const url = !dataForm.id ? '/menu/save' : '/menu/update'
      console.log('执行表单提交, URL:', url, '数据:', submitData);
      
      httpClient.post(url, submitData).then(({ data }) => {
        console.log('表单提交响应:', data);
        if (data && data.code === 0) {
          ElMessage.success('操作成功')
          visible.value = false
          emit('refreshDataList')
        } else {
          console.error('表单提交失败:', data?.msg);
          ElMessage.error(data?.msg || '操作失败')
        }
      }).catch(error => {
        console.error('表单提交异常:', error);
        ElMessage.error('操作失败: ' + (error.message || '未知错误'))
      })
    }
  })
}

const emit = defineEmits(['refreshDataList'])

onMounted(() => {
  console.log('菜单编辑组件已挂载');
})

defineExpose({
  init
})

console.log('菜单编辑组件初始化完成');
</script>

<style lang="scss" scoped>
.el-dialog {
  .el-form {
    padding: 20px;
  }
}
</style>