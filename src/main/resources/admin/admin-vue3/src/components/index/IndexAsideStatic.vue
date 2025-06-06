<template>
  <el-aside class="index-aside" width="150px">
    <div class="index-aside-inner menulist">
      <div v-for="item in menuList" :key="item.roleName" v-if="role === item.roleName" class="menulist-item">
        <div class="menulistImg" v-if="false && 2 === 2">
          <el-image :style="imageStyle" v-if="defaultImage" :src="defaultImage" fit="cover" />
        </div>
        <el-menu :mode="menuMode" :unique-opened="false" class="el-menu-demo" :default-active="defaultActive">
          <el-menu-item index="0" @click="menuHandler('')">
            <el-icon v-if="false"><HomeFilled /></el-icon>首页
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon v-if="false"><User /></el-icon>
              <span>个人中心</span>
            </template>
            <el-menu-item index="1-1" @click="menuHandler('updatePassword')">修改密码</el-menu-item>
            <el-menu-item index="1-2" @click="menuHandler('center')">个人信息</el-menu-item>
          </el-sub-menu>
          <el-sub-menu v-for="(menu, index) in item.backMenu" :key="menu.menu" :index="(index + 2).toString()">
            <template #title>
              <el-icon v-if="false" :class="icons[index]"></el-icon>
              <span>{{ menu.menu }}</span>
            </template>
            <el-menu-item v-for="(child, sort) in menu.child" :key="sort" :index="`${index + 2}-${sort}`" @click="menuHandler(child.tableName)">
              {{ child.menu }}
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
    </div>
  </el-aside>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { HomeFilled, User } from '@element-plus/icons-vue'
import menu from '@/utils/menu'
import storage from '@/utils/storage'

const router = useRouter()
const menuList = ref([])
const role = ref('')
const defaultActive = ref('0')
const defaultImage = 'http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg'
const menuMode = '2' === '1' ? 'horizontal' : 'vertical'

const imageStyle = {
  padding: '0',
  boxShadow: '0 0 6px rgba(0,0,0,0)',
  margin: '0',
  borderColor: 'rgba(0,0,0,0)',
  borderRadius: '0',
  borderWidth: '0',
  width: '100%',
  borderStyle: 'solid',
  height: 'auto'
}

const icons = [
  'el-icon-s-cooperation',
  'el-icon-s-order',
  'el-icon-s-platform',
  'el-icon-s-fold',
  'el-icon-s-unfold',
  'el-icon-s-operation',
  'el-icon-s-promotion',
  'el-icon-s-release',
  'el-icon-s-ticket',
  'el-icon-s-management',
  'el-icon-s-open',
  'el-icon-s-shop',
  'el-icon-s-marketing',
  'el-icon-s-flag',
  'el-icon-s-comment',
  'el-icon-s-finance',
  'el-icon-s-claim',
  'el-icon-s-custom',
  'el-icon-s-opportunity',
  'el-icon-s-data',
  'el-icon-s-check',
  'el-icon-s-grid',
  'el-icon-menu',
  'el-icon-chat-dot-square',
  'el-icon-message',
  'el-icon-postcard',
  'el-icon-position',
  'el-icon-microphone',
  'el-icon-close-notification',
  'el-icon-bangzhu',
  'el-icon-time',
  'el-icon-odometer',
  'el-icon-crop',
  'el-icon-aim',
  'el-icon-switch-button',
  'el-icon-full-screen',
  'el-icon-copy-document',
  'el-icon-mic',
  'el-icon-stopwatch',
].sort(() => 0.5 - Math.random())

const menuHandler = (name) => {
  router.push('/' + name)
}

const initMenus = async () => {
  const menus = menu.list()
  if (menus) {
    menuList.value = menus
  } else {
    const params = {
      page: 1,
      limit: 1,
      sort: 'id',
    }
    try {
      const { data } = await http.get('menu/list', { params })
      if (data && data.code === 0) {
        menuList.value = JSON.parse(data.data.list[0].menujson)
        storage.set('menus', menuList.value)
      }
    } catch (error) {
      console.error('获取菜单失败:', error)
    }
  }
}

const setMenulistHoverColor = () => {
  nextTick(() => {
    document.querySelectorAll('.menulist .el-menu-item').forEach(el => {
      el.addEventListener('mouseenter', e => {
        e.stopPropagation()
        el.style.backgroundColor = 'var(--el-menu-hover-bg-color)'
      })
      el.addEventListener('mouseleave', e => {
        e.stopPropagation()
        el.style.background = 'none'
      })
      el.addEventListener('focus', e => {
        e.stopPropagation()
        el.style.backgroundColor = 'var(--el-menu-hover-bg-color)'
      })
    })
    document.querySelectorAll('.menulist .el-sub-menu__title').forEach(el => {
      el.addEventListener('mouseenter', e => {
        e.stopPropagation()
        el.style.backgroundColor = 'var(--el-menu-hover-bg-color)'
      })
      el.addEventListener('mouseleave', e => {
        e.stopPropagation()
        el.style.background = 'none'
      })
    })
  })
}

const setMenulistStyle = () => {
  nextTick(() => {
    if ('2' === '1') {
      document.querySelectorAll('.el-container .el-container').forEach(el => {
        el.style.display = 'block'
        el.style.paddingTop = '60px'
      })
      document.querySelectorAll('.el-aside').forEach(el => {
        el.style.width = '100%'
        el.style.height = '100%'
        el.style.paddingTop = '0'
      })
      document.querySelectorAll('.index-aside .index-aside-inner').forEach(el => {
        el.style.paddingTop = '0'
        el.style.width = '100%'
      })
    } else {
      document.querySelectorAll('.index-aside .index-aside-inner').forEach(el => {
        el.style.paddingTop = '60px'
      })
    }
  })
}

onMounted(async () => {
  role.value = storage.get('role')
  await initMenus()
  setMenulistHoverColor()
  setMenulistStyle()
})
</script>

<style lang="scss" scoped>
.el-container {
  display: block;
}

.index-aside {
  position: relative;
  overflow: hidden;
  display: flex;
  flex-wrap: wrap;

  .menulistImg {
    font-size: 0;
    box-sizing: border-box;

    .el-image {
      margin: 0 auto;
      width: 100px;
      height: 100px;
      border-radius: 100%;
      display: block;
    }
  }

  .index-aside-inner {
    height: 100%;
    margin-right: -17px;
    margin-bottom: -17px;
    overflow: scroll;
    overflow-x: hidden !important;
    padding-top: 60px;
    box-sizing: border-box;

    &:focus {
      outline: none;
    }

    :deep(.el-menu) {
      border: 0;
      background-color: transparent;
    }
  }

  .index-aside .index-aside-inner.menulist {
    height: 100% !important;
  }

  .menulist-item {
    width: 150px;
    padding: 15px 20px;
    margin: 0;
    border-radius: 0;
    border-width: 0 !important;
    border-style: solid !important;
    border-color: rgba(0,0,0,.3) !important;
    background: var(--publicMainColor) !important;
    box-shadow: 0 0 0px rgba(30, 144, 255, .2);
    box-sizing: border-box;
  }

  .el-menu-demo {
    box-sizing: border-box;
    min-height: calc(100vh - 60px);

    :deep(.el-menu-item) {
      width: 120px;
      height: auto !important;
      line-height: 24px !important;
      padding: 10px;
      margin: 15px 0;
      color: rgba(255, 255, 255, 1);
      font-size: 14px;
      border-radius: 50px;
      border-width: 5px 0px;
      border-style: solid;
      border-color: #fff !important;
      background-color: var(--publicSubColor) !important;
      box-shadow: 0 0 0px rgba(255,255,255,0);
      box-sizing: initial;
      display: flex;
      align-items: center;
      justify-content: flex-start;
      box-sizing: border-box;

      .el-icon {
        margin: 0 5px 0 0;
        padding: 0;
        width: 24px;
        line-height: 24px;
        color: rgba(255, 255, 255, 1);
        font-size: 16px;
        border-radius: 0;
        border-width: 0;
        border-style: solid;
        border-color: #fff;
        background-color: rgba(255,255,255,0);
        box-shadow: 0 0 6px rgba(255,255,255,0);
      }

      &.is-active,
      &:hover {
        color: rgba(255, 255, 255, 1) !important;
        background-color: var(--publicSubColor) !important;
      }
    }

    :deep(.el-sub-menu) {
      margin: 15px 0;

      .el-sub-menu__title {
        width: 120px;
        height: auto !important;
        line-height: 24px !important;
        padding: 10px;
        color: rgba(255, 255, 255, 1);
        font-size: 14px;
        border-radius: 50px;
        border-width: 5px 0px;
        border-style: solid;
        border-color: #fff !important;
        background-color: var(--publicSubColor) !important;
        box-shadow: 0 0 0px rgba(255,255,255,0);
        box-sizing: initial;
        display: flex;
        align-items: center;
        justify-content: flex-start;
        box-sizing: border-box;

        .el-icon {
          margin: 0 5px 0 0;
          padding: 0;
          width: 24px;
          line-height: 24px;
          color: rgba(255, 255, 255, 1);
          font-size: 16px;
          border-radius: 0;
          border-width: 0;
          border-style: solid;
          border-color: #fff;
          background-color: rgba(255,255,255,0);
          box-shadow: 0 0 6px rgba(255,255,255,0);
        }

        .el-sub-menu__icon-arrow {
          margin: 0 10px 0 0;
          padding: 0;
          width: 12px;
          line-height: 12px;
          color: rgba(255, 255, 255, 1) !important;
          font-size: 12px;
          border-radius: 0;
          border-width: 0;
          border-style: solid;
          border-color: #fff;
          background-color: rgba(255,255,255,0);
          box-shadow: 0 0 6px rgba(255,255,255,0);
          position: absolute;
          top: 50%;
          right: 0;
          transform: translateY(-50%);
          text-align: center;
          display: block;
        }
      }

      .el-menu--inline {
        width: 90%;
        height: auto;
        padding: 0;
        margin: 5px auto;
        border-radius: 4px;
        border-width: 0;
        border-style: solid;
        border-color: rgba(0,0,0,.3);
        background-color: rgba(255, 255, 255, 0);
        box-shadow: 0 0 0px rgba(0, 0, 0, .3);

        .el-menu-item {
          width: 100%;
          height: 44px;
          line-height: 44px;
          padding: 0 !important;
          margin: 5px auto;
          color: #000 !important;
          font-size: 14px;
          border-radius: 30px;
          border-width: 0;
          border-style: solid;
          border-color: rgba(0, 0, 0, 0);
          background-color: rgba(255, 255, 255, 1) !important;
          box-shadow: 0 0 6px rgba(30, 144, 255, 0);
          text-align: center;
          min-width: auto;

          &.is-active,
          &:hover {
            color: rgba(255, 255, 255, 1) !important;
            background-color: var(--publicSubColor) !important;
          }
        }
      }
    }
  }
}
</style> 