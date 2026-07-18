<template>
  <div class="records-page">
    <div v-if="!userStore.isLoggedIn" class="no-login">
      <el-result icon="warning" title="请先登录" sub-title="登录后才能查看闯关记录">
        <template #extra>
          <el-button type="primary" @click="$router.push('/login')">去登录</el-button>
        </template>
      </el-result>
    </div>

    <div v-else>
      <div class="records-header">
        <h2>我的闯关记录</h2>
        <el-button type="primary" @click="$router.push('/level')">
          <el-icon><EditPen /></el-icon> 开始新闯关
        </el-button>
      </div>

      <!-- 记录列表 -->
      <div v-loading="loading">
        <div v-if="records.length === 0 && !loading" class="empty-area">
          <el-empty description="还没有闯关记录，快去挑战吧！" />
        </div>

        <div v-for="record in records" :key="record.id" class="record-card-wrapper">
          <el-card shadow="hover" class="record-card" @click="$router.push('/report/' + record.id)">
            <div class="record-header">
              <div class="record-score">
                <span class="score-num" :style="{ color: getScoreColor(record.score) }">{{ record.score }}</span>
                <span class="score-unit">分</span>
              </div>
              <div class="record-info">
                <div class="record-salary">
                  <span>薪资调整：</span>
                  <span :class="record.salaryChange > 0 ? 'up' : record.salaryChange < 0 ? 'down' : 'same'">
                    {{ formatChange(record.salaryChange) }}
                  </span>
                </div>
                <div class="record-time">{{ formatTime(record.createTime) }}</div>
              </div>
              <el-icon class="record-arrow"><ArrowRight /></el-icon>
            </div>
          </el-card>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > 0" class="pagination-area">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :total="total"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchRecords"
          @current-change="fetchRecords"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { listUserLevels } from '../api'

const userStore = useUserStore()

const loading = ref(false)
const records = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

function getScoreColor(score) {
  if (score >= 80) return '#6b8e23'
  if (score >= 60) return '#b8860b'
  return '#8b3a3a'
}

function formatChange(change) {
  if (change > 0) return '+' + change
  if (change < 0) return '' + change
  return '不变'
}

function formatTime(time) {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 19)
}

async function fetchRecords() {
  loading.value = true
  try {
    const res = await listUserLevels({ current: currentPage.value, size: pageSize.value })
    records.value = res.data.records || []
    total.value = res.data.total || 0
  } catch {
    records.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.records-page { max-width: 800px; margin: 0 auto; }
.no-login { padding: 60px 0; }
.records-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px; }
.records-header h2 { margin: 0; color: var(--sand-darker); letter-spacing: 3px; }
.empty-area { padding: 60px 0; }
.record-card-wrapper { margin-bottom: 12px; }
.record-card { cursor: pointer; transition: all 0.2s; }
.record-card:hover { border-color: var(--sand-accent) !important; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(92, 64, 51, 0.15); }
.record-header { display: flex; align-items: center; gap: 20px; }
.record-score { display: flex; align-items: baseline; gap: 2px; }
.score-num { font-size: 36px; font-weight: bold; }
.score-unit { font-size: 14px; color: var(--sand-dark); }
.record-info { flex: 1; }
.record-salary { font-size: 14px; color: var(--el-text-color-regular); margin-bottom: 4px; }
.record-salary .up { color: #6b8e23; font-weight: bold; }
.record-salary .down { color: #8b3a3a; font-weight: bold; }
.record-salary .same { color: var(--sand-dark); }
.record-time { font-size: 12px; color: var(--sand-dark); }
.record-arrow { font-size: 20px; color: var(--sand-border); }
.pagination-area { margin-top: 20px; display: flex; justify-content: center; }
</style>