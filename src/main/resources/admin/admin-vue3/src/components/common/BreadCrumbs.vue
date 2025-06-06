<template>
  <el-breadcrumb class="app-breadcrumb" separator="/"
    style="height:50px;backgroundColor:var(--publicMainColor);borderRadius:4px;padding:0px 20px 0px 20px;boxShadow:0px 0px 0px #f903d4;borderWidth:0;borderStyle:dotted solid double dashed;borderColor:#ff0000;">
    <transition-group name="breadcrumb" class="box"
      :style="1 == 1 ? 'justifyContent:flex-start;' : 1 == 2 ? 'justifyContent:center;' : 'justifyContent:flex-end;'">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noRedirect' || index == levelList.length - 1" class="no-redirect"
          style="color:rgba(255, 255, 255, 1)">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import pathToRegexp from 'path-to-regexp'

const route = useRoute()
const router = useRouter()
const levelList = ref([])

const getBreadcrumb = () => {
  let matched = route.matched.filter(item => item.meta && item.meta.title)
  const first = matched[0]

  if (!isDashboard(first)) {
    matched = [{ path: '/index', meta: { title: '首页' } }].concat(matched)
  }

  levelList.value = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
}

const isDashboard = (route) => {
  const name = route && route.name
  if (!name) {
    return false
  }
  return name.trim().toLocaleLowerCase() === 'Index'.toLocaleLowerCase()
}

const pathCompile = (path) => {
  const { params } = route
  const toPath = pathToRegexp.compile(path)
  return toPath(params)
}

const handleLink = (item) => {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

const breadcrumbStyleChange = () => {
  document.querySelectorAll('.app-breadcrumb .el-breadcrumb__separator').forEach(el => {
    el.innerText = "/"
    el.style.color = "#C0C4CC"
  })
  document.querySelectorAll('.app-breadcrumb .el-breadcrumb__inner a').forEach(el => {
    el.style.color = "rgba(255, 255, 255, 1)"
  })
  document.querySelectorAll('.app-breadcrumb .el-breadcrumb__inner .no-redirect').forEach(el => {
    el.style.color = "rgba(255, 255, 255, 1)"
  })

  let str = "2"
  if (2 == str) {
    let headHeight = "60px"
    headHeight = parseInt(headHeight) + 10 + 'px'
    document.querySelectorAll('.app-breadcrumb').forEach(el => {
      el.style.marginTop = headHeight
    })
  }
}

watch(
  () => route.path,
  () => {
    getBreadcrumb()
  },
  { immediate: true }
)

onMounted(() => {
  getBreadcrumb()
  breadcrumbStyleChange()
})
</script>

<style lang="scss" scoped>
.app-breadcrumb {
  display: block;
  font-size: 14px;
  line-height: 50px;

  .box {
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: flex-start;
    align-items: center;
  }

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }
}

.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all .5s;
}

.breadcrumb-enter-from,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style>