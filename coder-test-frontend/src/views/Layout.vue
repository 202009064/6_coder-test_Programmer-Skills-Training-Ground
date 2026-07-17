<template>
  <el-container class="layout-container">
    <el-header class="layout-header">
      <div class="header-left">
        <el-icon :size="28" color="#409eff"><Monitor /></el-icon>
        <span class="app-title" @click="$router.push('/home')">程序员技术练兵场</span>
      </div>
      <div class="header-right">
        <template v-if="userStore.isLoggedIn">
          <el-tag type="warning" size="large" class="salary-tag">
            当前薪资：{{ formatSalary(userStore.salary) }}
          </el-tag>
          <span class="nickname">{{ userStore.user?.nickname || userStore.user?.username }}</span>
          <el-button type="danger" text @click="handleLogout">退出登录</el-button>
        </template>
        <template v-else>
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </el-header>

    <el-container class="layout-body">
      <el-aside class="layout-aside" width="200px">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/level" :disabled="!userStore.isLoggedIn">
            <el-icon><EditPen /></el-icon>
            <span>答题闯关</span>
          </el-menu-item>
          <el-menu-item index="/records" :disabled="!userStore.isLoggedIn">
            <el-icon><List /></el-icon>
            <span>闯关记录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => '/' + (route.meta?.menuKey || route.path.split('/')[1] || 'home'))

function formatSalary(salary) {
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + ' 万/年'
  }
  return salary + ' 元/月'
}

async function handleLogout() {
  await userStore.doLogout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
}

.layout-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #1f2d3d;
  padding: 0 24px;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.app-title {
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.salary-tag {
  font-weight: bold;
}

.nickname {
  color: #e0e0e0;
  font-size: 15px;
}

.layout-body {
  height: calc(100vh - 60px);
}

.layout-aside {
  background: #304156;
}

.layout-main {
  background: #f5f7fa;
  padding: 20px;
}
</style>