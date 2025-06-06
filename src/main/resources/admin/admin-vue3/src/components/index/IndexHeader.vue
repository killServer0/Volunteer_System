<template>
  <div class="navbar" :style="{background:heads.headBgColor,height:heads.headHeight,boxShadow:heads.headBoxShadow,lineHeight:heads.headHeight}">
    <div class="title-menu" :style="{justifyContent:heads.headTitleStyle==='1'?'flex-start':'center'}">
      <el-image v-if="heads.headTitleImg" class="title-img" :style="{width:heads.headTitleImgWidth,height:heads.headTitleImgHeight,boxShadow:heads.headTitleImgBoxShadow,borderRadius:heads.headTitleImgBorderRadius}" :src="heads.headTitleImgUrl" fit="cover" />
      <div class="title-name" :style="{color:heads.headFontColor,fontSize:heads.headFontSize}">{{ projectName }}</div>
    </div>
    <div class="right-menu">
      <div class="user-info" :style="{color:heads.headUserInfoFontColor,fontSize:heads.headUserInfoFontSize}">{{ role }} {{ adminName }}</div>
      <div class="logout" :style="{color:heads.headLogoutFontColor,fontSize:heads.headLogoutFontSize}" @click="onLogout">退出登录</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { get } from '@/utils/http'
import storage from '@/utils/storage'
import { ElMessage } from 'element-plus'

const router = useRouter()
const projectName = import.meta.env.VITE_APP_NAME
const role = ref('')
const adminName = ref('')
const user = ref({})

const heads = ref({
  headLogoutFontHoverColor: '#fff',
  headFontSize: '20px',
  headUserInfoFontColor: 'rgba(255, 255, 255, 1)',
  headBoxShadow: '0 1px 6px #444',
  headTitleImgHeight: '44px',
  headLogoutFontHoverBgColor: 'rgba(232, 232, 232, 0.54)',
  headFontColor: 'rgba(255, 255, 255, 1)',
  headTitleImg: false,
  headHeight: '60px',
  headTitleImgBorderRadius: '22px',
  headTitleImgUrl: 'http://codegen.caihongy.cn/20201021/cc7d45d9c8164b58b18351764eba9be1.jpg',
  headBgColor: 'radial-gradient(circle, var(--publicMainColor) 0%, var(--publicSubColor) 100%)',
  headTitleImgBoxShadow: '0 1px 6px #444',
  headLogoutFontColor: 'rgba(255, 255, 255, 1)',
  headUserInfoFontSize: '16px',
  headTitleImgWidth: '44px',
  headTitleStyle: '2',
  headLogoutFontSize: '16px'
})

const onLogout = () => {
  storage.clear()
  router.replace({ name: 'login' })
}

const initUserInfo = async () => {
  const sessionTable = storage.get('sessionTable')
  try {
    const { data } = await get(`${sessionTable}/session`)
    if (data && data.code === 200) {
      user.value = data.data
      storage.set('userid', data.data.id)
    } else {
      ElMessage.error(data.msg)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

const setHeaderStyle = () => {
  document.querySelectorAll('.navbar .right-menu .logout').forEach(el => {
    el.addEventListener('mouseenter', e => {
      e.stopPropagation()
      el.style.backgroundColor = heads.value.headLogoutFontHoverBgColor
      el.style.color = heads.value.headLogoutFontHoverColor
    })
    el.addEventListener('mouseleave', e => {
      e.stopPropagation()
      el.style.backgroundColor = 'transparent'
      el.style.color = heads.value.headLogoutFontColor
    })
  })
}

onMounted(() => {
  role.value = storage.get('role')
  adminName.value = storage.get('adminName')
  initUserInfo()
  setHeaderStyle()
})
</script>

<style lang="scss" scoped>
.navbar {
  height: 60px;
  line-height: 60px;
  width: 100%;
  padding: 0 34px;
  box-sizing: border-box;
  background-color: #ff00ff;
  position: relative;
  z-index: 111;
  
  .right-menu {
    position: absolute;
    right: 34px;
    top: 0;
    height: 100%;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    z-index: 111;
    
    .user-info {
      font-size: 16px;
      color: red;
      padding: 0 12px;
    }
    
    .logout {
      font-size: 16px;
      color: red;
      padding: 0 12px;
      cursor: pointer;
    }
  }
  
  .title-menu {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 100%;
    
    .title-img {
      width: 44px;
      height: 44px;
      border-radius: 22px;
      box-shadow: 0 1px 6px #444;
      margin-right: 16px;
    }
    
    .title-name {
      font-size: 24px;
      color: #fff;
      font-weight: 700;
    }
  }
}
</style> 