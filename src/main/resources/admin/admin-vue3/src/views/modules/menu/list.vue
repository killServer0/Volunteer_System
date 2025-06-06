<template>
  <div class="menu-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <div>
            <el-button type="primary" @click="addOrUpdateHandler()">新增</el-button>
            <el-button type="success" @click="initTestMenu">初始化测试菜单</el-button>
          </div>
        </div>
      </template>
      <el-table :data="menuList" style="width: 100%" row-key="id" border>
        <el-table-column 
          label="菜单名称" 
          :formatter="(row) => row.displayName || row.name"
        />
        <el-table-column prop="url" label="路由路径" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="图标" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="text" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
            <el-button type="text" class="delete-btn" @click="deleteHandler(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/修改弹窗 -->
    <el-dialog v-model="addOrUpdateVisible" :title="addOrUpdateTitle" width="500px">
      <el-form ref="dataFormRef" :model="dataForm" :rules="dataRule" label-width="100px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="url">
          <el-input v-model="dataForm.url" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="dataForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="dataForm.icon" placeholder="请输入图标" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="dataForm.sort" :min="0" :max="999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addOrUpdateVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onErrorCaptured } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { http } from '@/utils/http'
import axios from 'axios'

console.log('菜单管理组件开始初始化...');
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

const menuList = ref([])
const addOrUpdateVisible = ref(false)
const addOrUpdateTitle = ref('')
const dataFormRef = ref()
const dataForm = reactive({
  id: null,
  name: '',
  url: '',
  component: '',
  icon: '',
  sort: 0
})

const dataRule = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  url: [{ required: true, message: '请输入路由路径', trigger: 'blur' }],
  component: [{ required: true, message: '请输入组件路径', trigger: 'blur' }]
}

// 菜单名称映射（英文到中文）
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
  'Dictionary Management': '基础字典',
  'Activity Audit': '活动审核'
}

// 捕获组件错误
onErrorCaptured((err, instance, info) => {
  console.error('菜单管理组件捕获到错误:', err);
  console.error('错误信息:', info);
  return false; // 阻止错误向上传播
})

// 获取菜单列表
const getMenuList = async () => {
  try {
    console.log('正在获取菜单列表...');
    const res = await httpClient.get('/menu/list')
    console.log('菜单列表响应:', res);
    if (res.code === 0) {
      // 打印每个菜单项的详细信息，特别是组件路径
      console.log('菜单项详情：');
      (res.data || []).forEach(item => {
        console.log(`ID: ${item.id}, 名称: ${item.name}, 路径: ${item.url}, 组件: ${item.component}`);
      });
      
      menuList.value = res.data.map(item => {
        if (menuNameMap[item.name]) {
          return {
            ...item,
            displayName: menuNameMap[item.name],
            originalName: item.name
          }
        }
        return item
      })
      console.log('菜单数据加载成功, 数量:', menuList.value.length);
    } else {
      console.error('获取菜单列表失败:', res.msg);
      ElMessage.error(res.msg || '获取菜单列表失败')
    }
  } catch (error) {
    console.error('获取菜单列表异常:', error);
    ElMessage.error('获取菜单列表失败: ' + (error.message || '未知错误'))
  }
}

// 新增/修改
const addOrUpdateHandler = (id) => {
  console.log('调用新增/修改处理器, ID:', id);
  addOrUpdateVisible.value = true
  addOrUpdateTitle.value = id ? '修改菜单' : '新增菜单'
  if (id) {
    // 修改
    const menu = menuList.value.find(item => item.id === id)
    if (menu) {
      console.log('找到要修改的菜单:', menu);
      Object.assign(dataForm, menu)
    } else {
      console.warn('未找到ID为', id, '的菜单');
    }
  } else {
    // 新增
    console.log('初始化新菜单表单');
    Object.assign(dataForm, {
      id: null,
      name: '',
      url: '',
      component: '',
      icon: '',
      sort: 0
    })
  }
}

// 删除
const deleteHandler = (id) => {
  console.log('调用删除处理器, ID:', id);
  ElMessageBox.confirm('确定要删除该菜单吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      console.log('执行删除请求, ID:', id);
      const res = await httpClient.post('/menu/delete', { id })
      console.log('删除响应:', res);
      if (res.code === 0) {
        ElMessage.success('删除成功')
        getMenuList()
      } else {
        console.error('删除失败:', res.msg);
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除异常:', error);
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }).catch(err => {
    console.log('用户取消删除操作');
  })
}

// 提交表单
const submitForm = async () => {
  if (!dataFormRef.value) {
    console.warn('表单引用不存在');
    return;
  }
  
  await dataFormRef.value.validate(async (valid) => {
    console.log('表单验证结果:', valid);
    if (valid) {
      try {
        // 准备提交数据，确保数据格式与后端接口一致
        const submitData = { ...dataForm };
        // 转换sort字段为数字类型
        if (typeof submitData.sort === 'string') {
          submitData.sort = parseInt(submitData.sort, 10) || 0;
        }
        
        // 确保字段名与数据库一致
        if (submitData.path && !submitData.url) {
          submitData.url = submitData.path;
          delete submitData.path;
        }
        
        // 更改API路径以确保与后端接口一致
        const url = dataForm.id ? '/menu/update' : '/menu/save';
        console.log('执行表单提交, URL:', url, '数据:', submitData);
        
        const res = await httpClient.post(url, submitData)
        console.log('表单提交响应:', res);
        
        if (res.code === 0) {
          ElMessage.success(dataForm.id ? '修改成功' : '新增成功')
          addOrUpdateVisible.value = false
          getMenuList()
        } else {
          console.error('表单提交失败:', res.msg);
          ElMessage.error(res.msg || (dataForm.id ? '修改失败' : '新增失败'))
        }
      } catch (error) {
        console.error('表单提交异常:', error);
        ElMessage.error((dataForm.id ? '修改失败: ' : '新增失败: ') + (error.message || '未知错误'))
      }
    }
  })
}

// 初始化测试菜单
const initTestMenu = async () => {
  try {
    console.log('开始初始化测试菜单');
    
    // 定义所有菜单项
    const testMenus = [
      {
        name: 'System Management',
        url: '/system',
        component: 'Layout',
        icon: 'setting',
        type: 0,
        sort: 1
      },
      {
        name: 'User Management',
        url: '/users',
        component: 'views/modules/users/list',
        icon: 'user',
        type: 1,
        sort: 2
      },
      {
        name: 'Role Management',
        url: '/roles',
        component: 'views/modules/role/list',
        icon: 'lock',
        type: 1,
        sort: 3
      },
      {
        name: 'Home',
        url: '/home',
        component: 'views/Home',
        icon: 'home',
        type: 1,
        sort: 4
      },
      {
        name: 'Personal Center',
        url: '/center',
        component: 'views/Center',
        icon: 'user',
        type: 1,
        sort: 5
      },
      {
        name: 'Change Password',
        url: '/updatePassword',
        component: 'views/UpdatePassword',
        icon: 'lock',
        type: 1,
        sort: 6
      },
      {
        name: 'Community Activities',
        url: '/feihuodong',
        component: 'views/modules/feihuodong/list',
        icon: 'activity',
        type: 1,
        sort: 7
      },
      {
        name: 'Community Activity Reservation',
        url: '/feihuodongYuyue',
        component: 'views/modules/feihuodongYuyue/list',
        icon: 'calendar',
        type: 1,
        sort: 8
      },
      {
        name: 'Volunteer Activities',
        url: '/zhihuodong',
        component: 'views/modules/zhihuodong/list',
        icon: 'activity',
        type: 1,
        sort: 9
      },
      {
        name: 'Activity Audit',
        url: '/huodongshenhe',
        component: 'views/modules/huodong/shenhe',
        icon: 'check',
        type: 1,
        sort: 10
      },
      {
        name: 'Volunteer Activity Reservation',
        url: '/zhihuodongYuyue',
        component: 'views/modules/zhihuodongYuyue/list',
        icon: 'calendar',
        type: 1,
        sort: 11
      },
      {
        name: 'Volunteer Management',
        url: '/zhiyuanzhe',
        component: 'views/modules/zhiyuanzhe/list',
        icon: 'user',
        type: 1,
        sort: 12
      },
      {
        name: 'Volunteer Messages',
        url: '/zhiyuanzheLiuyan',
        component: 'views/modules/zhiyuanzheLiuyan/list',
        icon: 'message',
        type: 1,
        sort: 13
      },
      {
        name: 'Visitor Management',
        url: '/feizhiyuan',
        component: 'views/modules/feizhiyuan/list',
        icon: 'user',
        type: 1,
        sort: 14
      },
      {
        name: 'Forum Management',
        url: '/forum',
        component: 'views/modules/forum/list',
        icon: 'chat',
        type: 1,
        sort: 15
      },
      {
        name: 'Announcement Management',
        url: '/gonggao',
        component: 'views/modules/gonggao/list',
        icon: 'bell',
        type: 1,
        sort: 16
      },
      {
        name: 'Chat Management',
        url: '/chat',
        component: 'views/modules/chat/list',
        icon: 'chat',
        type: 1,
        sort: 17
      },
      {
        name: 'System Configuration',
        url: '/config',
        component: 'views/modules/config/list',
        icon: 'setting',
        type: 1,
        sort: 18
      },
      {
        name: 'Database Backup',
        url: '/databaseBackup',
        component: 'views/modules/databaseBackup/list',
        icon: 'database',
        type: 1,
        sort: 19
      },
      {
        name: 'Menu Management',
        url: '/menu',
        component: 'views/modules/menu/list',
        icon: 'menu',
        type: 1,
        sort: 20
      },
      {
        name: 'Dictionary Management',
        url: '/dictionary',
        component: 'views/modules/dictionary/list',
        icon: 'dict',
        type: 1,
        sort: 21
      }
    ];
    
    // 先清空现有的菜单，确保没有重复
    try {
      console.log('清空现有菜单...');
      const res = await httpClient.post('/menu/clear');
      console.log('清空菜单响应:', res);
    } catch (error) {
      console.warn('清空菜单失败，继续创建新菜单:', error);
    }
    
    // 依次创建菜单
    for (const menu of testMenus) {
      console.log('创建菜单:', menu);
      const res = await httpClient.post('/menu/save', menu);
      console.log('创建菜单响应:', res);
      
      if (res.code !== 0) {
        console.error('创建菜单失败:', res.msg);
        ElMessage.error(`创建菜单 ${menu.name} 失败: ${res.msg || '未知错误'}`);
        return;
      }
    }
    
    ElMessage.success('测试菜单初始化成功');
    getMenuList(); // 刷新列表
  } catch (error) {
    console.error('初始化测试菜单异常:', error);
    ElMessage.error('初始化测试菜单失败: ' + (error.message || '未知错误'));
  }
};

onMounted(() => {
  console.log('菜单管理组件已挂载');
  getMenuList();
  console.log('菜单列表请求已发送');
})

console.log('菜单管理组件初始化完成');
</script>

<style scoped>
.menu-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.delete-btn {
  color: #F56C6C;
}
</style>