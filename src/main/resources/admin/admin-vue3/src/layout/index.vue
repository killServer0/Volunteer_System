<template>
  <div class="app-wrapper">
    <div class="sidebar-container" :class="{ 'is-collapse': isCollapse }">
      <div class="logo-container">
        <img src="@/assets/img/logo.png" alt="logo" class="logo">
        <h1 class="title">志愿者管理系统</h1>
      </div>
      <el-menu :default-active="activeMenu" class="el-menu-vertical" :collapse="isCollapse" background-color="#304156"
        text-color="#bfcbd9" active-text-color="#409EFF" router>
        <el-menu-item index="/home">
          <el-icon>
            <House />
          </el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="1">
          <template #title>
            <el-icon>
              <User />
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="users">用户列表</el-menu-item>
          <el-menu-item index="zhiyuanzhe">志愿者管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon>
              <Calendar />
            </el-icon>
            <span>活动管理</span>
          </template>
          <el-menu-item index="/zhihuodong">志愿者活动</el-menu-item>
          <el-menu-item index="/feihuodong">社区活动</el-menu-item>
          <el-menu-item index="/zhihuodongYuyue">志愿者活动预约</el-menu-item>
          <el-menu-item index="/feihuodongYuyue">社区活动预约</el-menu-item>
          <el-menu-item index="/huodongshenhe">活动审核</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon>
              <Message />
            </el-icon>
            <span>交流管理</span>
          </template>
          <el-menu-item index="/forum">论坛管理</el-menu-item>
          <el-menu-item index="/chat">聊天管理</el-menu-item>
          <el-menu-item index="/zhiyuanzheLiuyan">志愿者留言</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon>
              <Notification />
            </el-icon>
            <span>公告管理</span>
          </template>
          <el-menu-item index="/gonggao">公告列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="5">
          <template #title>
            <el-icon>
              <Setting />
            </el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/config">系统配置</el-menu-item>
          <el-menu-item index="/databaseBackup">数据库备份</el-menu-item>
          <el-menu-item index="/menu">菜单管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="6">
          <template #title>
            <el-icon>
              <Collection />
            </el-icon>
            <span>字典管理</span>
          </template>
          <el-menu-item index="/dictionary">基础字典</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <div class="main-container">
      <div class="navbar">
        <div class="left">
          <el-button type="text" @click="toggleSidebar">
            <el-icon>
              <Fold v-if="!isCollapse" />
              <Expand v-else />
            </el-icon>
          </el-button>
          <Breadcrumb />
        </div>
        <div class="right">
          <el-dropdown trigger="click">
            <div class="avatar-container">
              <el-avatar :size="30" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
              <span class="username">{{ username }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleCommand('center')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleCommand('password')">修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="handleCommand('logout')">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <div class="app-main">
        <router-view v-slot="{ Component }">
          <Suspense>
            <template #default>
              <transition name="fade-transform" mode="out-in">
                <component :is="Component" />
              </transition>
            </template>
            <template #fallback>
              <div class="loading">加载中...</div>
            </template>
          </Suspense>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  House, User, Setting, Fold, Expand, Calendar, Message,
  Notification, Collection
} from '@element-plus/icons-vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import { removeToken } from '@/utils/auth'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const username = ref('Admin')

const activeMenu = computed(() => route.path)

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command) => {
  console.log('Dropdown command clicked:', command)
  try {
    switch (command) {
      case 'center':
        console.log('Navigating to center...')
        try {
          const centerPath = '/center'
          console.log('将跳转到路径:', centerPath)
          router.push({ name: 'CenterPage' })
        } catch (error) {
          console.error('导航到个人中心失败:', error)
          ElMessage.error('导航到个人中心失败')
          // 尝试使用window.location作为备选方法
          window.location.href = '#/center'
        }
        break
      case 'password':
        console.log('Navigating to updatePassword...')
        try {
          const passwordPath = '/updatePassword'
          console.log('将跳转到路径:', passwordPath)
          router.push({ name: 'UpdatePasswordPage' })
        } catch (error) {
          console.error('导航到修改密码失败:', error)
          ElMessage.error('导航到修改密码失败')
          // 尝试使用window.location作为备选方法
          window.location.href = '#/updatePassword'
        }
        break
      case 'logout':
        ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          removeToken()
          router.push('/login')
        })
        break
    }
  } catch (error) {
    console.error('处理下拉菜单命令时出错:', error)
    ElMessage.error('操作失败，请重试')
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100vh;
  width: 100%;

  .sidebar-container {
    width: 210px;
    height: 100%;
    background-color: #304156;
    transition: width 0.3s;
    overflow: hidden;

    &.is-collapse {
      width: 64px;
    }

    .logo-container {
      height: 60px;
      padding: 10px;
      display: flex;
      align-items: center;
      background: #2b2f3a;

      .logo {
        width: 32px;
        height: 32px;
        margin-right: 12px;
      }

      .title {
        color: #fff;
        font-size: 16px;
        font-weight: 600;
        white-space: nowrap;
      }
    }

    .el-menu {
      border: none;
    }
  }

  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .navbar {
      height: 60px;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 20px;

      .left {
        display: flex;
        align-items: center;
      }

      .right {
        .avatar-container {
          display: flex;
          align-items: center;
          cursor: pointer;

          .username {
            margin-left: 8px;
            color: #606266;
          }
        }
      }
    }

    .app-main {
      flex: 1;
      padding: 20px;
      overflow-y: auto;
      background: #f0f2f5;
    }
  }
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 14px;
  color: #909399;
}
</style>