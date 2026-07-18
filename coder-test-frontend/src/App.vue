<template>
  <el-config-provider :locale="zhCn">
    <router-view />
  </el-config-provider>
</template>

<script setup>
// App 根组件：注入 Element Plus 中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'
</script>

<style>
/* ========== 沙场练兵台 — 全局 CSS 变量 ========== */
:root {
  --sand-bg: #f5e6d3;
  --sand-card: #faf3e6;
  --sand-dark: #8b7355;
  --sand-darker: #5c4033;
  --sand-accent: #b8860b;
  --sand-accent-hover: #cd9b1d;
  --sand-border: #d4c5a9;
  --sand-header: #3e2723;
  --sand-aside: #4e342e;
  --sand-footer: #3e2723;
  --sand-overlay: rgba(62, 39, 35, 0.75);

  /* 覆盖 Element Plus 主色 */
  --el-color-primary: #b8860b;
  --el-color-primary-light-3: #c99718;
  --el-color-primary-light-5: #d4a830;
  --el-color-primary-light-7: #dfba48;
  --el-color-primary-light-8: #e6c860;
  --el-color-primary-light-9: #edd678;
  --el-color-primary-dark-2: #9e7209;
  --el-color-success: #6b8e23;
  --el-color-warning: #b8860b;
  --el-color-danger: #8b3a3a;
  --el-color-info: #8b7355;
  --el-border-color: #d4c5a9;
  --el-border-color-light: #e0d5c0;
  --el-fill-color: #faf3e6;
  --el-fill-color-blank: #fefdf8;
  --el-bg-color: #f5e6d3;
  --el-bg-color-overlay: #faf3e6;
  --el-text-color-primary: #3e2723;
  --el-text-color-regular: #5c4033;
  --el-text-color-secondary: #8b7355;
  --el-font-family: 'Noto Serif SC', 'SimSun', 'STSong', serif;
}

body {
  margin: 0;
  padding: 0;
  background: var(--sand-bg);
  font-family: 'Noto Serif SC', 'SimSun', 'STSong', 'Microsoft YaHei', serif;
  color: var(--el-text-color-regular);
}

#app {
  min-height: 100vh;
}

/* 全局卡片 */
.el-card {
  background: var(--sand-card) !important;
  border-color: var(--sand-border) !important;
}

.el-card__header {
  border-bottom-color: var(--sand-border) !important;
}

/* 全局输入框 */
.el-input__wrapper {
  border-color: var(--sand-border);
}
</style>