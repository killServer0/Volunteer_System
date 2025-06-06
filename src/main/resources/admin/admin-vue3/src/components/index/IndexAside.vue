<template>
    <el-aside class="index-aside" width="200px">
        <div class="index-aside-inner">
            <el-menu default-active="1">
                <el-menu-item @click="menuHandler('/')" index="1">
                    <!-- <el-icon><HomeFilled /></el-icon> -->
                    首页
                </el-menu-item>
                <sub-menu v-for="menu in menuList" :key="menu.menuId" :menu="menu"
                    :dynamic-menu-routes="dynamicMenuRoutes"></sub-menu>
            </el-menu>
        </div>
    </el-aside>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import SubMenu from '@/components/index/IndexAsideSub.vue'

const router = useRouter()
const menuList = ref([])
const dynamicMenuRoutes = ref([])

const menuHandler = (path) => {
    router.push({ path })
}

onMounted(() => {
    // 获取动态菜单数据并且渲染
    menuList.value = JSON.parse(sessionStorage.getItem('menuList') || '[]')
    dynamicMenuRoutes.value = JSON.parse(
        sessionStorage.getItem('dynamicMenuRoutes') || '[]'
    )
})
</script>

<style lang="scss" scoped>
.index-aside {
    margin-top: 80px;
    overflow: hidden;

    .index-aside-inner {
        width: 217px;
        height: 100%;
        overflow-y: scroll;
    }
}
</style>