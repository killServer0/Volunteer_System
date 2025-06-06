<template>
    <el-sub-menu v-if="menu.list && menu.list.length >= 1" :index="menu.menuId + ''">
        <template #title>
            <span>{{ menu.name }}</span>
        </template>
        <sub-menu v-for="item in menu.list" :key="item.menuId" :menu="item"
            :dynamic-menu-routes="dynamicMenuRoutes"></sub-menu>
    </el-sub-menu>
    <el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)">
        <span>{{ menu.name }}</span>
    </el-menu-item>
</template>

<script setup>
import { defineProps } from 'vue'
import { useRouter } from 'vue-router'
import SubMenu from './IndexAsideSub.vue'

const props = defineProps({
    menu: {
        type: Object,
        required: true
    },
    dynamicMenuRoutes: {
        type: Array,
        required: true
    }
})

const router = useRouter()

// 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
const gotoRouteHandle = (menu) => {
    const route = props.dynamicMenuRoutes.filter(
        item => item.meta.menuId === menu.menuId
    )
    if (route.length >= 1) {
        if (route[0].component != null) {
            router.replace({ name: route[0].name })
        } else {
            router.push({ name: '404' })
        }
    }
}
</script>