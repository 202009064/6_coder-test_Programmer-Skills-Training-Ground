<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-banner">
        <el-icon :size="40" color="#cd9b1d"><Flag /></el-icon>
        <h2 class="login-title">入伍登录</h2>
        <p class="login-subtitle">入营受训，攻城拔寨</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入账号">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin" size="large" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <p class="switch-link">
        尚未入伍？<el-button type="primary" link @click="$router.push('/register')">前往报名从军</el-button>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.doLogin(form)
    ElMessage.success('登录成功，欢迎归营！')
    router.push('/home')
  } catch (e) {
    ElMessage.error(e.message || '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  min-height: calc(100vh - 200px);
}

.login-card {
  width: 420px;
  padding: 40px;
  background: var(--sand-card);
  border: 1px solid var(--sand-border);
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(92, 64, 51, 0.2), inset 0 0 0 1px rgba(184, 134, 11, 0.1);
  position: relative;
}

.login-card::before {
  content: '';
  position: absolute;
  inset: 6px;
  border: 1px solid rgba(184, 134, 11, 0.25);
  border-radius: 4px;
  pointer-events: none;
}

.login-banner {
  text-align: center;
  margin-bottom: 28px;
}

.login-title {
  text-align: center;
  margin: 12px 0 6px;
  color: var(--sand-darker);
  font-size: 24px;
  font-weight: 900;
  letter-spacing: 4px;
}

.login-subtitle {
  color: var(--sand-dark);
  font-size: 14px;
  margin: 0;
  letter-spacing: 2px;
}

.switch-link {
  text-align: center;
  color: var(--sand-dark);
  font-size: 14px;
  margin-top: 8px;
}
</style>