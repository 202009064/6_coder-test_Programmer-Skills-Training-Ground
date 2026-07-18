<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-banner">
        <el-icon :size="40" color="#cd9b1d"><Flag /></el-icon>
        <h2 class="register-title">报名从军</h2>
        <p class="register-subtitle">登记入册，共赴沙场</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="请输入账号">
            <template #prefix><el-icon><User /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称（选填）">
            <template #prefix><el-icon><EditPen /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input v-model="form.checkPassword" type="password" placeholder="请再次输入密码" show-password>
            <template #prefix><el-icon><Lock /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister" size="large" style="width: 100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <p class="switch-link">
        已有账号？<el-button type="primary" link @click="$router.push('/login')">去登录</el-button>
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, EditPen } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  password: '',
  checkPassword: '',
})

const validateCheckPassword = (_rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 位', trigger: 'blur' },
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateCheckPassword, trigger: 'blur' },
  ],
}

async function handleRegister() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await userStore.doRegister({
      username: form.username,
      password: form.password,
      checkPassword: form.checkPassword,
      nickname: form.nickname || undefined,
    })
    ElMessage.success('注册成功！')
    router.push('/home')
  } catch (e) {
    ElMessage.error(e.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  min-height: calc(100vh - 200px);
}

.register-card {
  width: 440px;
  padding: 40px;
  background: var(--sand-card);
  border: 1px solid var(--sand-border);
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(92, 64, 51, 0.2), inset 0 0 0 1px rgba(184, 134, 11, 0.1);
  position: relative;
}

.register-card::before {
  content: '';
  position: absolute;
  inset: 6px;
  border: 1px solid rgba(184, 134, 11, 0.25);
  border-radius: 4px;
  pointer-events: none;
}

.register-banner {
  text-align: center;
  margin-bottom: 28px;
}

.register-title {
  text-align: center;
  margin: 12px 0 6px;
  color: var(--sand-darker);
  font-size: 24px;
  font-weight: 900;
  letter-spacing: 4px;
}

.register-subtitle {
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