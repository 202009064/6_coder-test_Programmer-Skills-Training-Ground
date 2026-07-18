<template>
  <div class="level-page">
    <div v-if="!userStore.isLoggedIn" class="no-login">
      <el-result icon="warning" title="请先登录" sub-title="登录后才能答题闯关">
        <template #extra>
          <el-button type="primary" @click="$router.push('/login')"
            >去登录</el-button
          >
        </template>
      </el-result>
    </div>
    <div v-else>
      <div v-if="!levelStore.currentLevel" class="generate-step">
        <el-card shadow="hover">
          <div class="generate-card">
            <el-icon :size="48" color="#cd9b1d"><Cpu /></el-icon>
            <h2>AI 智能生成关卡</h2>
            <p>
              根据您当前的薪资
              <strong>{{ formatSalary(userStore.salary) }}</strong
              >，AI 将为您生成对应难度的关卡
            </p>
            <el-button
              type="primary"
              size="large"
              :loading="generating"
              @click="generateLevel"
            >
              <el-icon><MagicStick /></el-icon>
              {{ generating ? "AI 正在生成关卡..." : "开始生成关卡" }}
            </el-button>
            <p v-if="generating" class="generating-hint">
              AI 正在模拟产品经理思考需求，请耐心等待...
            </p>
            <LoadingArena v-if="generating" class="loading-area" />
          </div>
        </el-card>
      </div>
      <div v-else class="answer-step">
        <el-card shadow="hover" class="level-info-card">
          <div class="level-header">
            <h2>{{ levelStore.currentLevel.levelName }}</h2>
            <el-tag :type="difficultyType" size="large">{{
              levelStore.currentLevel.difficulty
            }}</el-tag>
          </div>
          <div class="level-desc">{{ levelStore.currentLevel.levelDesc }}</div>
        </el-card>
        <el-row :gutter="20" class="answer-area">
          <el-col :span="12">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>可选选项（拖拽到右侧答题区）</span>
                  <el-tag>{{ availableOptions.length }} 个</el-tag>
                </div>
              </template>
              <div class="options-list">
                <div
                  v-for="(option, index) in availableOptions"
                  :key="index"
                  class="option-item"
                  draggable="true"
                  @click="addToAnswer(index)"
                  @dragstart="onDragStart(index)"
                  @dragend="onDragEnd"
                  :class="{ dragging: draggingIndex === index }"
                >
                  <span class="option-index">{{ index + 1 }}</span>
                  <span class="option-text">{{ option }}</span>
                  <el-icon class="option-arrow"><DArrowRight /></el-icon>
                </div>
                <el-empty
                  v-if="availableOptions.length === 0"
                  description="所有选项已移至答题区"
                />
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <template #header>
                <div class="card-header">
                  <span>你的答案（拖拽选项到此处）</span>
                  <el-tag type="success"
                    >{{ levelStore.selectedOptions.length }} 个</el-tag
                  >
                </div>
              </template>
              <div
                class="answer-drop-zone"
                @dragover.prevent="onDragOver"
                @dragleave="onDragLeave"
                @drop="onDrop"
                :class="{ 'drag-over': isDragOver }"
              >
                <div
                  v-for="(option, index) in levelStore.selectedOptions"
                  :key="index"
                  class="answer-item"
                >
                  <span class="answer-index">{{ index + 1 }}</span>
                  <span class="answer-text">{{ option }}</span>
                  <el-button
                    type="danger"
                    link
                    size="small"
                    @click="removeFromAnswer(index)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </div>
                <div
                  v-if="levelStore.selectedOptions.length === 0"
                  class="drop-hint"
                >
                  <el-icon :size="40" color="#bfb090"><Download /></el-icon>
                  <p>将左侧选项拖拽到此处，或点击箭头添加</p>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div class="submit-area">
          <el-button size="large" class="action-btn" @click="resetLevel">重新生成关卡</el-button>
          <el-button
            type="primary"
            size="large"
            class="action-btn"
            :loading="submitting"
            @click="submitAnswer"
            :disabled="levelStore.selectedOptions.length === 0"
          >
            <el-icon><Finished /></el-icon> 提交答案
          </el-button>
        </div>
        <div v-if="submitting" class="submit-loading" ref="submitLoadingRef">
          <p class="submit-hint">
            提交成功！AI 正在评估你的答案，请耐心等待...
          </p>
          <LoadingArena class="loading-area" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/user";
import { useLevelStore } from "../stores/level";
import { generateLevel as generateLevelApi, submitLevel } from "../api";
import { ElMessage } from "element-plus";
import LoadingArena from "../components/LoadingArena.vue";

const router = useRouter();
const userStore = useUserStore();
const levelStore = useLevelStore();
const submitLoadingRef = ref(null);

// 每次进入页面都清空上次的关卡状态，确保重新走「生成关卡」流程
onMounted(() => {
  levelStore.clearLevel();
});

const generating = ref(false);
const submitting = ref(false);
const draggingIndex = ref(null);
const isDragOver = ref(false);

const allOptions = computed(() => {
  try {
    return JSON.parse(levelStore.currentLevel?.options || "[]");
  } catch {
    return [];
  }
});

const availableOptions = computed(() => {
  return allOptions.value.filter(
    (o) => !levelStore.selectedOptions.includes(o)
  );
});

const difficultyType = computed(() => {
  const d = levelStore.currentLevel?.difficulty;
  if (d === "简单") return "success";
  if (d === "中等") return "warning";
  if (d === "困难") return "danger";
  return "";
});

function formatSalary(salary) {
  if (salary >= 10000) return (salary / 10000).toFixed(1) + " 万/月";
  return salary + " 元/月";
}

function onDragStart(index) {
  draggingIndex.value = index;
}
function onDragEnd() {
  draggingIndex.value = null;
  isDragOver.value = false;
}
function onDragOver(e) {
  e.dataTransfer.dropEffect = "move";
  isDragOver.value = true;
}
function onDragLeave() {
  isDragOver.value = false;
}
function onDrop() {
  isDragOver.value = false;
  if (draggingIndex.value !== null) {
    const option = availableOptions.value[draggingIndex.value];
    if (option) levelStore.addOption(option);
    draggingIndex.value = null;
  }
}
function addToAnswer(index) {
  const option = availableOptions.value[index];
  if (option) levelStore.addOption(option);
}
function removeFromAnswer(index) {
  levelStore.removeOption(levelStore.selectedOptions[index]);
}

async function generateLevel() {
  generating.value = true;
  try {
    const res = await generateLevelApi(userStore.salary);
    levelStore.setLevel(res.data);
    ElMessage.success("关卡生成成功！");
  } catch (e) {
    ElMessage.error(e.message || "关卡生成失败");
  } finally {
    generating.value = false;
  }
}

function resetLevel() {
  levelStore.clearLevel();
}

async function submitAnswer() {
  if (levelStore.selectedOptions.length === 0) {
    ElMessage.warning("请至少选择一个选项");
    return;
  }
  submitting.value = true;
  // 等待加载区域渲染后滚动到可视区，确保用户能看到提示和进度条
  await nextTick();
  submitLoadingRef.value?.scrollIntoView({ behavior: "smooth", block: "center" });
  try {
    const res = await submitLevel({
      levelId: levelStore.currentLevel.id,
      userOptions: JSON.stringify(levelStore.selectedOptions),
    });
    // 刷新用户信息，获取最新薪资
    await userStore.fetchCurrentUser();
    router.push("/report/" + res.data.id);
  } catch (e) {
    ElMessage.error(e.message || "提交失败");
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.level-page {
  max-width: 1100px;
  margin: 0 auto;
}
.no-login {
  padding: 60px 0;
}
.generate-step {
  margin-top: 40px;
}
.generate-card {
  text-align: center;
  padding: 40px 20px;
}
.generate-card h2 {
  margin: 16px 0 8px;
  color: var(--sand-darker);
}
.generate-card p {
  color: var(--sand-dark);
  margin-bottom: 24px;
  font-size: 15px;
}
.generating-hint {
  margin-top: 16px;
  color: var(--sand-accent);
  font-size: 14px;
}
.submit-loading {
  margin-top: 24px;
  text-align: center;
}
.submit-hint {
  margin: 0 0 12px;
  color: var(--sand-accent);
  font-size: 15px;
  letter-spacing: 1px;
}
.loading-area {
  margin: 16px auto 0;
  max-width: 420px;
}
.level-info-card {
  margin-bottom: 20px;
}
.level-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}
.level-header h2 {
  margin: 0;
  color: var(--sand-darker);
}
.level-desc {
  color: var(--el-text-color-regular);
  font-size: 15px;
  line-height: 1.8;
  white-space: pre-wrap;
}
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
/* 两栏等高：el-row 拉伸子项 → el-col 等高 → card 撑满 col → 内容区撑满 body */
.answer-area {
  align-items: stretch;
}
.answer-area .el-col {
  display: flex;
}
.answer-area .el-card {
  width: 100%;
  display: flex;
  flex-direction: column;
}
.answer-area :deep(.el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.options-list {
  flex: 1;
  overflow-y: auto;
}
.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 10px;
  border: 1px solid var(--sand-border);
  border-radius: 6px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.2s;
  background: var(--el-fill-color-blank);
}
.option-item:hover {
  border-color: var(--sand-accent);
  background: rgba(184, 134, 11, 0.08);
}
.option-item:active {
  cursor: grabbing;
}
.option-item.dragging {
  opacity: 0.5;
  border-color: var(--sand-accent);
  background: rgba(184, 134, 11, 0.15);
}
.option-arrow {
  flex-shrink: 0;
  color: var(--sand-accent);
  font-size: 16px;
}
.option-index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(184, 134, 11, 0.15);
  color: var(--sand-accent-hover);
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}
.option-text {
  flex: 1;
  font-size: 14px;
  color: var(--el-text-color-regular);
  word-break: break-all;
}
.answer-drop-zone {
  flex: 1;
  min-height: 300px;
  border: 2px dashed var(--sand-border);
  border-radius: 8px;
  padding: 16px;
  transition: all 0.2s;
  background: rgba(245, 230, 211, 0.5);
}
.answer-drop-zone.drag-over {
  border-color: var(--sand-accent);
  background: rgba(184, 134, 11, 0.08);
}
.answer-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: rgba(107, 142, 35, 0.1);
  border: 1px solid rgba(107, 142, 35, 0.4);
  border-radius: 6px;
  margin-bottom: 8px;
}
.answer-index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #6b8e23;
  color: #fff;
  font-size: 12px;
  font-weight: bold;
  flex-shrink: 0;
}
.answer-text {
  flex: 1;
  font-size: 14px;
  color: var(--el-text-color-regular);
  word-break: break-all;
}
.drop-hint {
  text-align: center;
  padding: 60px 20px;
  color: #bfb090;
}
.drop-hint p {
  margin-top: 10px;
  font-size: 14px;
}
.submit-area {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: center;
}
.action-btn {
  width: 180px;
}
</style>