<template>
    <el-container>
        <index-header></index-header>
        <el-container>
            <index-aside></index-aside>
            <index-main></index-main>
        </el-container>
    </el-container>
</template>

<script setup>
import IndexHeader from '@/components/index/IndexHeader.vue'
import IndexAside from '@/components/index/IndexAsideStatic.vue'
import IndexMain from '@/components/index/IndexMain.vue'
</script>

<style lang="scss" scoped>
// 铺满全屏
.el-container {
    position: absolute;
    width: 100%;
    top: 0;
    left: 0;
    bottom: 0;
}
</style>