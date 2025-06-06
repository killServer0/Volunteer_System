<template>
  <div class="login-container">
    <div class="login-box">
      <el-form class="login-form" label-position="left" :model="loginForm" :rules="rules" ref="loginFormRef">
        <div class="title-container">
          <h3 class="title">社区志愿者管理系统</h3>
        </div>
        <el-form-item prop="username">
          <span class="svg-container">
            <el-icon>
              <User />
            </el-icon>
          </span>
          <el-input v-model="loginForm.username" placeholder="请输入用户名" name="username" type="text" />
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <el-icon>
              <Lock />
            </el-icon>
          </span>
          <el-input v-model="loginForm.password" placeholder="请输入密码" name="password" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { setToken } from '@/utils/auth'
import axios from 'axios'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = ref({
  username: '',
  password: '',
  captcha: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, message: '密码长度不能小于3位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    const res = await axios.post('/api/users/login', {
      username: loginForm.value.username,
      password: loginForm.value.password,
      captcha: loginForm.value.captcha
    })

    if (res.data.code === 0) {
      const { token, role, userId } = res.data
      localStorage.setItem('token', token)
      localStorage.setItem('role', role)
      localStorage.setItem('userId', userId)
      localStorage.setItem('userTable', 'users')
      localStorage.setItem('sessionTable', 'users')
      axios.defaults.headers.common['token'] = token
      
      // 获取用户权限
      try {
        const permissionRes = await axios.get('/api/permission/user')
        if (permissionRes.data.code === 0) {
          // 确保权限信息是数组格式
          const permissions = Array.isArray(permissionRes.data.data) ? permissionRes.data.data : []
          // 设置默认权限
          const defaultPermissions = [
            'config:查看',
            'config:新增',
            'config:修改',
            'config:删除',
            'databaseBackup:查看',
            'databaseBackup:新增',
            'databaseBackup:修改',
            'databaseBackup:删除',
            'menu:查看',
            'menu:新增',
            'menu:修改',
            'menu:删除'
          ]
          // 合并默认权限和用户权限
          const allPermissions = [...new Set([...permissions, ...defaultPermissions])]
          localStorage.setItem('permissions', JSON.stringify(allPermissions))
        }
      } catch (error) {
        console.error('获取权限失败:', error)
        // 设置默认权限
        const defaultPermissions = [
          'config:查看',
          'config:新增',
          'config:修改',
          'config:删除',
          'databaseBackup:查看',
          'databaseBackup:新增',
          'databaseBackup:修改',
          'databaseBackup:删除',
          'menu:查看',
          'menu:新增',
          'menu:修改',
          'menu:删除'
        ]
        localStorage.setItem('permissions', JSON.stringify(defaultPermissions))
      }
      
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.data.msg || '登录失败')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error(error.response?.data?.msg || '登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  height: 100vh;
  width: 100vw;
  background-image: url(@/assets/img/back-img-bg.jpg);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-box {
    width: 420px;
    padding: 20px;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  .login-form {
    .title-container {
      margin-bottom: 30px;
      text-align: center;

      .title {
        font-size: 26px;
        color: #333;
        font-weight: bold;
      }
    }

    .svg-container {
      display: inline-flex;
      align-items: center;
      color: #889aa4;
      vertical-align: middle;
      margin-right: 10px;
      width: 30px;
      text-align: center;
    }

    .login-button {
      width: 100%;
      margin-top: 10px;
    }
  }
}
</style>