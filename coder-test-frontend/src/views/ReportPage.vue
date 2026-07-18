<template>
  <div class="report-page">
    <div v-if="loading" class="loading-area">
      <el-icon :size="40" class="loading-icon"><Loading /></el-icon>
      <p>加载成绩报告中...</p>
    </div>

    <div v-else-if="report" class="report-content">
      <!-- 分数卡片 -->
      <el-card shadow="hover" class="score-card">
        <div class="score-circle">
          <el-progress
            type="dashboard"
            :percentage="report.score"
            :color="scoreColor"
            :stroke-width="12"
          >
            <template #default="{ percentage }">
              <span class="score-value">{{ percentage }}</span>
              <span class="score-label">分</span>
            </template>
          </el-progress>
        </div>
        <h2 class="score-title">你的闯关成绩</h2>
      </el-card>

      <!-- 评价卡片 -->
      <el-card shadow="hover" class="comment-card">
        <template #header>
          <span>🎯 AI 评价</span>
        </template>
        <div class="comment-text">{{ report.comment }}</div>
      </el-card>

      <!-- 薪资调整卡片 -->
      <el-card shadow="hover" class="salary-card">
        <template #header>
          <span>💰 薪资调整</span>
        </template>
        <div class="salary-change">
          <span :class="salaryChangeClass">{{ salaryChangeText }}</span>
        </div>
        <p class="salary-after">
          调整后薪资：<strong>{{
            formatSalary(userStore.salary + (report.salaryChange || 0))
          }}</strong>
        </p>
      </el-card>

      <!-- 原因卡片 -->
      <el-card shadow="hover" class="reason-card">
        <template #header>
          <span>📝 评分原因</span>
        </template>
        <div class="reason-text">{{ report.reason }}</div>
      </el-card>

      <!-- 建议卡片 -->
      <el-card shadow="hover" class="suggest-card">
        <template #header>
          <span>🏢 投递建议</span>
        </template>
        <div class="suggest-text">{{ report.suggest }}</div>
      </el-card>

      <!-- 标准答案卡片 -->
      <el-card shadow="hover" class="answer-card">
        <template #header>
          <span>✅ 标准答案</span>
        </template>
        <div class="true-options">
          <el-tag
            v-for="(opt, i) in trueOptions"
            :key="i"
            type="success"
            size="large"
            style="margin: 4px"
          >
            {{ opt }}
          </el-tag>
        </div>
      </el-card>

      <!-- 标准解析卡片 -->
      <el-card shadow="hover" class="standard-card">
        <template #header>
          <span>📖 标准答案解析</span>
        </template>
        <div class="standard-text">{{ report.standardAnswer }}</div>
      </el-card>

      <!-- 操作按钮 -->
      <div class="report-actions">
        <el-button size="large" @click="$router.push('/records')">
          <el-icon><List /></el-icon> 闯关记录
        </el-button>
        <el-button type="primary" size="large" @click="$router.push('/level')">
          <el-icon><EditPen /></el-icon> 继续闯关
        </el-button>
      </div>
    </div>

    <div v-else class="error-area">
      <el-result icon="error" title="加载失败" sub-title="未找到该成绩报告">
        <template #extra>
          <el-button type="primary" @click="$router.push('/records')"
            >返回记录列表</el-button
          >
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import { useUserStore } from "../stores/user";
import { getUserLevelDetail } from "../api";

const route = useRoute();
const userStore = useUserStore();

const loading = ref(true);
const report = ref(null);

const trueOptions = computed(() => {
  try {
    return JSON.parse(report.value?.trueOptions || "[]");
  } catch {
    return [];
  }
});

const scoreColor = computed(() => {
  const s = report.value?.score || 0;
  if (s >= 80) return "#67c23a";
  if (s >= 60) return "#e6a23c";
  return "#f56c6c";
});

const salaryChangeText = computed(() => {
  const change = report.value?.salaryChange || 0;
  if (change > 0) return "+" + change + " 元";
  if (change < 0) return change + " 元";
  return "不变";
});

const salaryChangeClass = computed(() => {
  const change = report.value?.salaryChange || 0;
  if (change > 0) return "salary-up";
  if (change < 0) return "salary-down";
  return "salary-same";
});

function formatSalary(salary) {
  if (salary >= 10000) return (salary / 10000).toFixed(1) + " 万/月";
  return salary + " 元/月";
}

onMounted(async () => {
  try {
    const id = route.params.id;
    const res = await getUserLevelDetail(id);
    report.value = res.data;
  } catch {
    report.value = null;
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
.report-page {
  max-width: 800px;
  margin: 0 auto;
  padding-bottom: 40px;
}
.loading-area {
  text-align: center;
  padding: 80px 0;
  color: var(--sand-dark);
}
.loading-icon {
  animation: spin 1s linear infinite;
}
@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
.loading-area p {
  margin-top: 16px;
  font-size: 16px;
  letter-spacing: 2px;
}
.score-card {
  text-align: center;
  margin-bottom: 20px;
  padding: 30px 0;
}
.score-circle {
  margin-bottom: 10px;
}
.score-value {
  font-size: 48px;
  font-weight: bold;
  color: var(--sand-darker);
}
.score-label {
  font-size: 16px;
  color: var(--sand-dark);
}
.score-title {
  font-size: 20px;
  color: var(--sand-darker);
  margin-top: 8px;
  letter-spacing: 3px;
}
.comment-card,
.salary-card,
.reason-card,
.suggest-card,
.answer-card,
.standard-card {
  margin-bottom: 20px;
}
.comment-text,
.reason-text,
.suggest-text,
.standard-text {
  font-size: 15px;
  line-height: 1.8;
  color: var(--el-text-color-regular);
  white-space: pre-wrap;
}
.salary-change {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
}
.salary-up {
  color: #6b8e23;
}
.salary-down {
  color: #8b3a3a;
}
.salary-same {
  color: var(--sand-dark);
}
.salary-after {
  text-align: center;
  font-size: 16px;
  color: var(--el-text-color-regular);
}
.true-options {
  text-align: center;
  padding: 10px;
}
.report-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  margin-top: 30px;
}
.error-area {
  padding: 60px 0;
}
</style>