<template>
  <el-main class="index-main">
    <bread-crumbs :title="title" class="bread-crumbs"></bread-crumbs>
    <router-view class="router-view"></router-view>
  </el-main>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import menu from '@/utils/menu'
import storage from '@/utils/storage'
import BreadCrumbs from '@/components/common/BreadCrumbs.vue'

const router = useRouter()
const menuList = ref([])
const role = ref('')
const currentIndex = ref(-2)
const itemMenu = ref([])
const title = ref('')

const init = () => {
  nextTick(() => {
    // 如果需要根据侧边栏高度调整主内容区域高度，可以在这里实现
  })
}

const menuHandler = (menu) => {
  router.push({
    name: menu.tableName
  })
  title.value = menu.menu
}

const titleChange = (index, menus) => {
  currentIndex.value = index
  itemMenu.value = menus
  console.log(menus)
}

const homeChange = (index) => {
  itemMenu.value = []
  title.value = ''
  currentIndex.value = index
  router.push({
    name: 'home'
  })
}

const centerChange = (index) => {
  itemMenu.value = [{
    buttons: ['新增', '查看', '修改', '删除'],
    menu: '修改密码',
    tableName: 'updatePassword'
  }, {
    buttons: ['新增', '查看', '修改', '删除'],
    menu: '个人信息',
    tableName: 'center'
  }]
  title.value = ''
  currentIndex.value = index
  router.push({
    name: 'home'
  })
}

onMounted(() => {
  const menus = menu.list()
  if (menus) {
    menuList.value = menus
  }
  role.value = storage.get('role')
  init()
})
</script>

<style lang="scss" scoped>
.index-main {
  padding: 10px 20px;
  background-image: url(/zhiyuanzheguanlixitong/img/back-list-img-bg.jpg);
  background-size: 100% 100%;
  box-shadow: 0 0 6px rgba(0,0,0,0);
  height: calc(100vh - 60px);

  .bread-crumbs {
    width: 100%;
    margin-top: 10px;
    box-sizing: border-box;
  }

  .router-view {
    padding: 10px;
    margin-top: 10px;
    background: transparent;
    box-sizing: border-box;
    height: auto;
  }
}

.nav-list {
  width: 100%;
  margin: 0 auto;
  text-align: left;
  margin-top: 20px;

  .nav-title {
    display: inline-block;
    font-size: 15px;
    color: #333;
    padding: 15px 25px;
    border: none;

    &.active {
      color: #555;
      cursor: default;
      background-color: #fff;
    }
  }
}

.nav-item {
  margin-top: 20px;
  background: #FFFFFF;
  padding: 15px 0;

  .menu {
    padding: 15px 25px;
  }
}

a {
  text-decoration: none;
  color: #555;

  &:hover {
    background: #00c292;
  }
}

:deep(.detail-form-content) {
  background: transparent;
}
</style> 