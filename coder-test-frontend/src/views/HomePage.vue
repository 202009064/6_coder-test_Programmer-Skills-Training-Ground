"<template>
  <div class="home-page">
    <!-- Banner 区域 -->
    <div class="banner-section">
      <div class="banner-overlay">
        <h1 class="banner-title">程序员技术练兵场</h1>
        <p class="banner-subtitle">
          千锤百炼，方成大器 · AI 驱动的编程技能闯关平台
        </p>
        <p class="banner-desc">
          大漠孤烟，烽火连天。在这片广袤的技术疆域中，以代码为刃，以逻辑为盾，<br />经历一次次实战洗礼，从士兵蜕变为统帅。
        </p>
        <div class="banner-actions">
          <el-button
            v-if="!userStore.isLoggedIn"
            type="primary"
            size="large"
            @click="$router.push('/register')"
          >
            <el-icon><Flag /></el-icon> 即刻入伍
          </el-button>
          <el-button
            v-else
            type="primary"
            size="large"
            @click="$router.push('/level')"
          >
            <el-icon><KnifeFork /></el-icon> 开疆拓土
          </el-button>
          <el-button
            size="large"
            @click="$router.push(userStore.isLoggedIn ? '/records' : '/login')"
          >
            <el-icon><Notebook /></el-icon>
            {{ userStore.isLoggedIn ? "战功簿" : "老兵归营" }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 用户信息区 -->
    <div v-if="userStore.isLoggedIn" class="user-info-section">
      <div class="troop-card">
        <div class="troop-item">
          <span class="troop-label">将士</span>
          <span class="troop-value">{{
            userStore.user?.nickname || userStore.user?.username
          }}</span>
        </div>
        <div class="troop-divider"></div>
        <div class="troop-item">
          <span class="troop-label">军饷</span>
          <span class="troop-value salary">{{
            formatSalary(userStore.salary)
          }}</span>
        </div>
        <div class="troop-divider"></div>
        <div class="troop-item">
          <span class="troop-label">军阶</span>
          <span class="troop-value rank" :style="{ color: rankColor }">{{
            difficultyLabel
          }}</span>
        </div>
      </div>
    </div>

    <!-- 平台特色 -->
    <h2 class="section-title">⚔ 练 兵 之 道 ⚔</h2>

    <div class="feature-cards">
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon-wrap">
              <el-icon :size="36" color="var(--sand-accent)"><Cpu /></el-icon>
            </div>
            <h3>AI 军师出题</h3>
            <p>
              AI
              模拟沙场点兵，依军饷定难度，生成真实战场需求与诸多谋略选项，静候将军抉择。
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon-wrap">
              <el-icon :size="36" color="#6b8e23"><DataAnalysis /></el-icon>
            </div>
            <h3>AI 军师点评</h3>
            <p>
              交令之后 AI
              即刻阅卷评分，给出详尽战报与标准谋略解析，助你查缺补漏、精进不休。
            </p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon-wrap">
              <el-icon :size="36" color="#8b3a3a"><TrendCharts /></el-icon>
            </div>
            <h3>军饷动态升降</h3>
            <p>
              每战之后依功过赏罚，军饷随之增减，关卡难度亦随之起伏，仿若真实的疆场晋升之路。
            </p>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 薪资排行榜 -->
    <h2 class="section-title">🏆 军 饷 排 行 榜 🏆</h2>
    <div class="ranking-section">
      <el-row :gutter="20">
        <el-col
          :xs="24"
          :sm="12"
          :md="8"
          v-for="(col, ci) in rankingCols"
          :key="ci"
        >
          <div class="ranking-col">
            <div
              v-for="(user, ui) in col"
              :key="user.id"
              class="ranking-item"
              :class="{ 'top-three': ci === 0 && ui < 3 }"
            >
              <span class="ranking-num" :class="'num-' + (ci * 10 + ui + 1)">{{
                ci * 10 + ui + 1
              }}</span>
              <img
                :src="user.avatar"
                class="ranking-avatar"
                @error="onAvatarError"
              />
              <span class="ranking-name">{{
                user.nickname || user.username
              }}</span>
              <span class="ranking-salary">{{
                formatSalary(user.salary)
              }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from "vue";
import { useUserStore } from "../stores/user";
import { getRankingList } from "../api";

const userStore = useUserStore();

const rankingList = ref([]);

// 将排行榜数据分成最多3列，每列最多10条，不展示空列
const rankingCols = computed(() => {
  const cols = [[], [], []]
  rankingList.value.forEach((user, i) => {
    const colIndex = Math.floor(i / 10)
    if (colIndex < 3) cols[colIndex].push(user)
  })
  return cols.filter(c => c.length > 0)
});

function fetchRanking() {
  getRankingList(30)
    .then((res) => {
      rankingList.value = res.data || [];
    })
    .catch(() => {});
}

function onAvatarError(e) {
  e.target.style.display = "none";
}

onMounted(() => {
  fetchRanking();
});

function formatSalary(salary) {
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + " 万/月";
  }
  return salary + " 元/月";
}

const difficultyLabel = computed(() => {
  const s = userStore.salary;
  if (s <= 6000) return "步卒";
  if (s <= 15000) return "伍长";
  if (s <= 30000) return "校尉";
  if (s <= 60000) return "将军";
  return "统帅";
});

const rankColor = computed(() => {
  const s = userStore.salary;
  if (s <= 6000) return "#8b7355";
  if (s <= 15000) return "#6b8e23";
  if (s <= 30000) return "#b8860b";
  if (s <= 60000) return "#c99718";
  return "#cd9b1d";
});
</script>

<style scoped>
.home-page {
}

/* ========== Banner 区域 ========== */
.banner-section {
  position: relative;
  height: 380px;
  border-radius: 8px;
  overflow: hidden;
  margin: -20px -20px 0 -20px;
  background: url("../assets/banner.png") center/cover no-repeat;
}

.banner-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba(62, 39, 35, 0.85) 0%,
    rgba(92, 64, 51, 0.6) 50%,
    rgba(139, 115, 85, 0.4) 100%
  );
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px;
  text-align: center;
}

.banner-title {
  font-size: 42px;
  font-weight: 900;
  color: #edd678;
  margin: 0 0 8px;
  letter-spacing: 8px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

.banner-subtitle {
  font-size: 18px;
  color: #d4c5a9;
  margin: 0 0 10px;
  letter-spacing: 4px;
}

.banner-desc {
  font-size: 14px;
  color: #bfb090;
  line-height: 1.8;
  margin: 0 0 24px;
}

.banner-actions {
  display: flex;
  gap: 16px;
}

/* ========== 用户军阶卡片 ========== */
.user-info-section {
  max-width: 600px;
  margin: 24px auto 0;
}

.troop-card {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--sand-card);
  border: 1px solid var(--sand-border);
  border-radius: 8px;
  padding: 16px 32px;
  box-shadow: 0 2px 12px rgba(139, 115, 85, 0.15);
}

.troop-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.troop-label {
  font-size: 12px;
  color: #8b7355;
}

.troop-value {
  font-size: 18px;
  font-weight: 700;
  color: #5c4033;
}

.troop-value.salary {
  color: var(--sand-accent);
}

.troop-divider {
  width: 1px;
  height: 40px;
  background: var(--sand-border);
  margin: 0 32px;
}

/* ========== 平台特色 ========== */
.section-title {
  text-align: center;
  font-size: 22px;
  font-weight: 700;
  color: var(--sand-darker);
  margin: 36px 0 20px;
  letter-spacing: 4px;
}

.feature-cards {
  max-width: 960px;
  margin: 0 auto 40px;
}

.feature-card {
  background: var(--sand-card);
  border: 1px solid var(--sand-border);
  border-radius: 8px;
  padding: 28px 20px;
  text-align: center;
  transition: transform 0.2s, box-shadow 0.2s;
  box-shadow: 0 2px 8px rgba(139, 115, 85, 0.08);
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(139, 115, 85, 0.18);
}

.feature-icon-wrap {
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: linear-gradient(
    135deg,
    rgba(184, 134, 11, 0.12),
    rgba(139, 115, 85, 0.08)
  );
}

.feature-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: var(--sand-darker);
  margin: 0 0 8px;
}

.feature-card p {
  font-size: 14px;
  color: var(--sand-dark);
  line-height: 1.7;
  margin: 0;
}

/* ========== 排行榜 ========== */
.ranking-section {
  max-width: 960px;
  margin: 0 auto 48px;
}

.ranking-col {
  background: var(--sand-card);
  border: 1px solid var(--sand-border);
  border-radius: 8px;
  padding: 12px 16px;
  box-shadow: 0 2px 8px rgba(139, 115, 85, 0.08);
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 6px;
  border-bottom: 1px dashed var(--sand-border);
  transition: background 0.2s;
}

.ranking-item:last-child {
  border-bottom: none;
}

.ranking-item:hover {
  background: rgba(184, 134, 11, 0.06);
}

.ranking-item.top-three {
  background: linear-gradient(90deg, rgba(205, 155, 29, 0.12), transparent);
}

.ranking-num {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 26px;
  height: 26px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 700;
  color: #8b7355;
  background: rgba(139, 115, 85, 0.12);
  flex-shrink: 0;
}

.ranking-num.num-1,
.ranking-num.num-2,
.ranking-num.num-3 {
  background: linear-gradient(135deg, #cd9b1d, #e8b730);
  color: #fff;
  box-shadow: 0 2px 6px rgba(205, 155, 29, 0.3);
}

.ranking-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.ranking-name {
  flex: 1;
  font-size: 14px;
  font-weight: 600;
  color: var(--sand-darker);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ranking-salary {
  font-size: 13px;
  font-weight: 700;
  color: var(--sand-accent);
  flex-shrink: 0;
}
</style>", "file_path": "e:\devlop\work\6_coder-test\coder-test-frontend\src\views\HomePage.vue"}