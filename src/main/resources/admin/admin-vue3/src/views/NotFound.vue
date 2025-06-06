<template>
    <div class="content">
        <img class="background" src="@/assets/img/404.png" alt="404">
        <div class="text main-text">出错了...页面失踪了</div>
        <div>
            <el-button class="text" @click="back()" type="primary" text>
                <el-icon>
                    <ArrowLeft />
                </el-icon>
                返回
            </el-button>
        </div>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()

const back = () => {
    window.history.length > 1 ? router.go(-1) : router.push("/login")
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    align-items: center;
    flex-direction: column;
    width: 100%;
    height: 100%;
    min-height: 900px;
    text-align: center;

    .background {
        display: inline-block;
        width: 200px;
        height: 200px;
        margin-top: 80px;
    }

    .main-text {
        margin-top: 80px;
    }

    .text {
        font-size: 24px;
        font-weight: bold;
        color: #333;
    }
}
</style>