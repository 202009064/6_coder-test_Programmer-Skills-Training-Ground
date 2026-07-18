<template>
  <el-container class="layout-container" direction="vertical">
    <AppHeader />
    <el-main class="layout-main">
      <router-view />
    </el-main>
    <AppFooter />
  </el-container>
</template>

<script setup>
import AppHeader from '../components/AppHeader.vue'
import AppFooter from '../components/AppFooter.vue'
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
}

.layout-main {
  background: var(--sand-bg);
  padding: 20px;
  background-image:
    radial-gradient(ellipse at 20% 50%, rgba(184, 134, 11, 0.04) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 20%, rgba(107, 142, 35, 0.03) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 90%, rgba(139, 115, 85, 0.03) 0%, transparent 40%);
}
</style>