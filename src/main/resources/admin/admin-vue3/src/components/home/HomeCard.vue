<template>
  <el-card class="box-card">
    <template #header>
      <div class="header">
        <span>{{ title }}</span>
        <span v-if="titleTag || titleUnit">
          <el-tag size="small" :type="titleTag">{{ titleUnit }}</el-tag>
        </span>
      </div>
    </template>
    <div class="content">
      {{ content }}&nbsp;&nbsp;
      <span class="unit">{{ contentUnit }}</span>
    </div>
    <div v-if="hasBottom" class="bottom">
      <span>{{ bottomTitle }}</span>
      <span>
        {{ bottomContent }}
        <el-icon v-if="bottomIcon">
          <component :is="bottomIcon" />
        </el-icon>
      </span>
    </div>
  </el-card>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  titleTag: {
    type: String,
    default: ''
  },
  titleUnit: {
    type: String,
    default: ''
  },
  content: {
    type: [String, Number],
    default: ''
  },
  contentUnit: {
    type: String,
    default: ''
  },
  bottomTitle: {
    type: String,
    default: ''
  },
  bottomContent: {
    type: [String, Number],
    default: ''
  },
  bottomIcon: {
    type: String,
    default: ''
  }
})

const hasBottom = computed(() => props.bottomTitle || props.bottomContent || props.bottomIcon)
</script>

<style lang="scss" scoped>
.box-card {
  margin-right: 10px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .content {
    font-size: 30px;
    font-weight: bold;
    color: #666;
    text-align: center;

    .unit {
      font-size: 16px;
    }
  }

  .bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 10px;
  }
}
</style>