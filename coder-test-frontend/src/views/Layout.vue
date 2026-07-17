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

    <!-- 底部版权栏 -->
    <div class="footer-container">
      <div class="footer-content">
        <el-row :gutter="20">
          <!-- 站点信息 -->
          <el-col :xs="12" :sm="12" :md="8" :lg="6">
            <div class="footer-brand">
              <el-icon :size="48" color="#409eff"><Medal /></el-icon>
              <h5 class="footer-site-name">程序员技术练兵场</h5>
            </div>
            <div class="footer-links" style="padding-left: 60px">
              <a href="#" class="footer-link">《用户协议》</a>
              <a href="#" class="footer-link">《隐私政策》</a>
            </div>
          </el-col>

          <!-- 友情链接 -->
          <el-col :xs="12" :sm="12" :md="8" :lg="6">
            <h5 class="footer-col-title">友情链接</h5>
            <div class="footer-link-list">
              <a href="https://www.codefather.cn/" target="_blank" class="footer-link">编程导航</a>
              <a href="https://www.laoyujianli.com/" target="_blank" class="footer-link">老鱼简历</a>
              <a href="https://www.codecopy.cn/" target="_blank" class="footer-link">代码小抄</a>
              <a href="https://jianqiezhushou.com/" target="_blank" class="footer-link">剪切助手</a>
              <a href="https://mianshiya.com/" target="_blank" class="footer-link">面试鸭</a>
            </div>
          </el-col>

          <!-- 联系我们 -->
          <el-col :xs="12" :sm="12" :md="8" :lg="6">
            <h5 class="footer-col-title">联系我们</h5>
            <div class="footer-link-list">
              <a href="#" target="_blank" class="footer-link">商务合作</a>
              <a href="https://space.bilibili.com/12890453" target="_blank" class="footer-link">站长：程序员烨</a>
            </div>
          </el-col>

          <!-- 关注我们 -->
          <el-col :xs="12" :sm="12" :md="8" :lg="6">
            <h5 class="footer-col-title">关注我们</h5>
            <div class="footer-qrcode">
              <img
                :src="qrcodeImg"
                alt="微信联系方式"
                width="65"
                height="65"
                class="qrcode-img"
              />
              <span class="qrcode-text">扫码关注<br/>微信号</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <footer class="footer-bottom">
        <span style="margin-right: 20px">2026 程序员技术练兵场</span>
        <a class="footer-bottom-link" href="#" target="_blank" rel="noreferrer" style="display:inline-flex;align-items:center;gap:4px">
          <span style="font-size:12px">🔒</span>暂无备案号
        </a>
        <span>&nbsp;&nbsp;</span>
        <a class="footer-bottom-link" href="#" target="_blank">关于我们</a>
      </footer>
    </div>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import qrcodeImg from '../assets/微信联系方式.jpg'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = computed(() => '/' + (route.meta?.menuKey || route.path.split('/')[1] || 'home'))

function formatSalary(salary) {
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + ' 万/月'
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
  min-height: calc(100vh - 60px);
}

.layout-aside {
  background: #304156;
}

.layout-main {
  background: #f5f7fa;
  padding: 20px;
}

/* ========== 底部版权栏 ========== */
.footer-container {
  background: #1f2d3d;
  color: #bfcbd9;
}

.footer-content {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 20px 16px;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.footer-site-name {
  margin: 0;
  font-size: 18px;
  color: #fff;
}

.footer-col-title {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #fff;
}

.footer-links {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.footer-link-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.footer-link {
  color: #bfcbd9;
  text-decoration: none;
  font-size: 14px;
}

.footer-link:hover {
  color: #409eff;
}

.footer-qrcode {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.qrcode-placeholder {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 65px;
  height: 65px;
  background: #2d3a4a;
  border-radius: 4px;
  color: #606266;
}

.qrcode-img {
  border-radius: 4px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.qrcode-img:hover {
  transform: scale(1.25); /* hover 时放大到 1.25 倍*/
}

.qrcode-text {
  color: #999;
  font-size: 12px;
  line-height: 1.6;
  margin-bottom: 6px;
}

.footer-bottom {
  text-align: center;
  padding: 16px 20px 30px;
  border-top: 1px solid #2d3a4a;
  font-size: 14px;
  color: #909399;
}

.footer-bottom-link {
  color: #909399;
  text-decoration: none;
  margin-right: 20px;
}

.footer-bottom-link:hover {
  color: #409eff;
}
</style>