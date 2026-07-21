<template>
  <el-header class="app-header">
    <div class="header-left">
      <el-icon :size="28" color="#cd9b1d"><Flag /></el-icon>
      <span class="app-title" @click="$router.push('/home')">程序员技术练兵场</span>
    </div>
    <div class="header-right">
      <template v-if="userStore.isLoggedIn">
        <el-tag type="warning" size="large" class="salary-tag">
          军饷：{{ formatSalary(userStore.salary) }}
        </el-tag>
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="nickname-trigger">
            <el-icon><User /></el-icon>
            {{ userStore.user?.nickname || userStore.user?.username }}
            <el-icon class="caret"><CaretBottom /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="/home">
                <el-icon><HomeFilled /></el-icon>中军大帐
              </el-dropdown-item>
              <el-dropdown-item command="/level">
                <el-icon><KnifeFork /></el-icon>开疆拓土
              </el-dropdown-item>
              <el-dropdown-item command="/records">
                <el-icon><Notebook /></el-icon>战功簿
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>卸甲归田
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" @click="$router.push('/login')">入伍登录</el-button>
        <el-button @click="$router.push('/register')">报名从军</el-button>
      </template>
    </div>
  </el-header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { useLevelStore } from '../stores/level'

const router = useRouter()
const userStore = useUserStore()
const levelStore = useLevelStore()
const logoutLoading = ref(false)

function formatSalary(salary) {
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + ' 万/月'
  }
  return salary + ' 元/月'
}

// 卸甲归田：二次确认 + loading + 清空关卡状态 + 跳登录
async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要卸甲归田、退出登录吗？', '卸甲归田', {
      confirmButtonText: '确定卸甲',
      cancelButtonText: '再战一会',
      type: 'warning',
    })
  } catch {
    return
  }
  logoutLoading.value = true
  try {
    await userStore.doLogout()
    levelStore.clearLevel()
    ElMessage.success('已卸甲归田，江湖再见！')
    router.push('/login')
  } catch (e) {
    ElMessage.error(e.message || '卸甲失败，请稍后再试')
  } finally {
    logoutLoading.value = false
  }
}

// 下拉菜单命令处理：路由跳转 or 登出
function handleCommand(command) {
  if (command === 'logout') {
    handleLogout()
  } else {
    router.push(command)
  }
}
</script>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--sand-header);
  padding: 0 24px;
  height: 60px;
  border-bottom: 2px solid var(--sand-accent);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.app-title {
  color: #edd678;
  font-size: 20px;
  font-weight: 900;
  cursor: pointer;
  letter-spacing: 3px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.salary-tag {
  font-weight: bold;
}

.nickname-trigger {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #d4c5a9;
  font-size: 15px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  outline: none;
}

.nickname-trigger:hover {
  color: var(--sand-accent-hover);
  background: rgba(184, 134, 11, 0.15);
}

.nickname-trigger .caret {
  font-size: 12px;
}
</style>