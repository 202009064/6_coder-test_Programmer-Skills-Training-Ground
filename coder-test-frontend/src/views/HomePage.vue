<template>
  <div class="home-page">
    <div class="welcome-card">
      <h1>欢迎来到程序员技术练兵场 🚀</h1>
      <p class="subtitle">AI 驱动的编程技能闯关平台，根据你的薪资水平动态调整难度，助你步步高升！</p>

      <div v-if="userStore.isLoggedIn" class="user-info">
        <el-card shadow="hover">
          <div class="info-row">
            <span class="label">昵称：</span>
            <span>{{ userStore.user?.nickname || '未设置' }}</span>
          </div>
          <div class="info-row">
            <span class="label">当前薪资：</span>
            <span class="salary-value">{{ formatSalary(userStore.salary) }}</span>
          </div>
          <div class="info-row">
            <span class="label">难度等级：</span>
            <el-tag :type="difficultyTagType">{{ difficultyLabel }}</el-tag>
          </div>
          <div class="actions">
            <el-button type="primary" size="large" @click="$router.push('/level')">
              <el-icon><EditPen /></el-icon> 开始闯关
            </el-button>
            <el-button size="large" @click="$router.push('/records')">
              <el-icon><List /></el-icon> 查看记录
            </el-button>
          </div>
        </el-card>
      </div>

      <div v-else class="no-login">
        <p>请先登录后开始闯关</p>
        <div class="actions">
          <el-button type="primary" size="large" @click="$router.push('/login')">登录</el-button>
          <el-button size="large" @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </div>

    <div class="feature-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="feature-icon"><el-icon :size="40" color="#409eff"><Cpu /></el-icon></div>
            <h3>AI 智能出题</h3>
            <p>AI 模拟真实产品经理，根据你的薪资动态生成关卡，包含真实业务场景和多种技术选项</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="feature-icon"><el-icon :size="40" color="#67c23a"><DataAnalysis /></el-icon></div>
            <h3>AI 智能评估</h3>
            <p>提交答案后 AI 自动评分，给出详细评价和标准答案解析，帮你查漏补缺</p>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover">
            <div class="feature-icon"><el-icon :size="40" color="#e6a23c"><TrendCharts /></el-icon></div>
            <h3>薪资动态成长</h3>
            <p>根据答题表现调整虚拟薪资，关卡难度随之变化，模拟真实的职业成长路径</p>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

function formatSalary(salary) {
  if (salary >= 10000) return (salary / 10000).toFixed(1) + ' 万/年'
  return salary + ' 元/月'
}

const difficultyLabel = computed(() => {
  const s = userStore.salary
  if (s <= 6000) return '初级'
  if (s <= 15000) return '中级'
  if (s <= 30000) return '高级'
  return '专家'
})

const difficultyTagType = computed(() => {
  const s = userStore.salary
  if (s <= 6000) return 'success'
  if (s <= 15000) return 'warning'
  if (s <= 30000) return 'danger'
  return ''
})
</script>

<style scoped>
.home-page { max-width: 900px; margin: 0 auto; }
.welcome-card { text-align: center; padding: 20px 0 40px; }
.welcome-card h1 { font-size: 32px; color: #303133; margin-bottom: 12px; }
.subtitle { font-size: 16px; color: #909399; margin-bottom: 30px; }
.user-info .info-row { padding: 10px 0; font-size: 16px; }
.user-info .label { color: #909399; margin-right: 8px; }
.salary-value { color: #e6a23c; font-weight: bold; font-size: 18px; }
.actions { margin-top: 20px; display: flex; gap: 12px; justify-content: center; }
.no-login { padding: 40px; }
.no-login p { font-size: 16px; color: #909399; margin-bottom: 20px; }
.feature-cards { margin-top: 30px; }
.feature-cards .el-card { text-align: center; }
.feature-icon { margin-bottom: 16px; }
.feature-cards h3 { margin-bottom: 10px; color: #303133; }
.feature-cards p { color: #909399; font-size: 14px; line-height: 1.6; }
</style>