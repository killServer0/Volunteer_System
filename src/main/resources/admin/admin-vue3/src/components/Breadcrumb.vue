<template>
    <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index" :to="item.path">
            {{ item.meta.title }}
        </el-breadcrumb-item>
    </el-breadcrumb>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const breadcrumbs = computed(() => {
    const matched = route.matched.filter(item => item.meta && item.meta.title)
    return matched
})
</script>

<style lang="scss" scoped>
.el-breadcrumb {
    margin-left: 20px;
}
</style>