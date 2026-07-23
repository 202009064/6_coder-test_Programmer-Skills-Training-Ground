<template>
  <div class="admin-page">
    <div class="page-header">
      <h2 class="page-title">
        <el-icon :size="24"><Setting /></el-icon>
        关卡管理
      </h2>
      <div class="header-actions">
        <el-button type="primary" @click="$router.push('/level')">
          <el-icon><Plus /></el-icon>AI 生成新关卡
        </el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchForm.keyword"
        placeholder="关卡名称关键字"
        clearable
        style="width: 220px"
        @keyup.enter="handleSearch"
      />
      <el-select
        v-model="searchForm.difficulty"
        placeholder="难度等级"
        clearable
        style="width: 130px"
      >
        <el-option label="简单" value="简单" />
        <el-option label="中等" value="中等" />
        <el-option label="困难" value="困难" />
      </el-select>
      <el-select
        v-model="searchForm.priority"
        placeholder="优先级"
        clearable
        style="width: 130px"
      >
        <el-option label="普通" :value="0" />
        <el-option label="推荐" :value="99" />
        <el-option label="精选" :value="999" />
        <el-option label="置顶" :value="9999" />
      </el-select>
      <el-button type="primary" @click="handleSearch">
        <el-icon><Search /></el-icon>搜索
      </el-button>
      <el-button @click="handleReset">
        <el-icon><Refresh /></el-icon>重置
      </el-button>
    </div>

    <!-- 关卡列表 -->
    <el-table
      :data="levels"
      v-loading="loading"
      border
      stripe
      class="level-table"
      empty-text="暂无关卡数据"
    >
      <el-table-column prop="levelName" label="关卡名称" min-width="180">
        <template #default="{ row }">
          <el-tooltip :content="row.levelName" placement="top">
            <span class="level-name">{{ row.levelName }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="difficulty" label="难度" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="difficultyType(row.difficulty)" size="small">{{
            row.difficulty
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="targetSalary"
        label="目标薪资"
        width="110"
        align="center"
      >
        <template #default="{ row }">
          {{ formatSalary(row.targetSalary) }}
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="priorityTagType(row.priority)" size="small">{{
            priorityLabel(row.priority)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            link
            @click="openEditDialog(row)"
          >
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-button
            type="warning"
            size="small"
            link
            @click="handleSetFeatured(row)"
            v-if="row.priority < 999"
          >
            <el-icon><Star /></el-icon>设为精选
          </el-button>
          <el-button
            type="success"
            size="small"
            link
            @click="handleSetPriority(row, 0)"
            v-if="row.priority >= 999"
          >
            <el-icon><Remove /></el-icon>取消精选
          </el-button>
          <el-popconfirm
            title="确定要删除该关卡吗？"
            confirm-button-text="确定"
            cancel-button-text="取消"
            @confirm="handleDelete(row.id)"
          >
            <template #reference>
              <el-button type="danger" size="small" link>
                <el-icon><Delete /></el-icon>删除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="fetchLevels"
        @current-change="fetchLevels"
      />
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑关卡"
      width="700px"
      destroy-on-close
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editFormRules"
        label-width="100px"
      >
        <el-form-item label="关卡名称" prop="levelName">
          <el-input v-model="editForm.levelName" placeholder="请输入关卡名称" />
        </el-form-item>
        <el-form-item label="关卡难度" prop="difficulty">
          <el-select v-model="editForm.difficulty" placeholder="请选择难度">
            <el-option label="简单" value="简单" />
            <el-option label="中等" value="中等" />
            <el-option label="困难" value="困难" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标薪资" prop="targetSalary">
          <el-input-number
            v-model="editForm.targetSalary"
            :min="1000"
            :step="1000"
            placeholder="目标薪资（元）"
          />
        </el-form-item>
        <el-form-item label="关卡描述" prop="levelDesc">
          <el-input
            v-model="editForm.levelDesc"
            type="textarea"
            :rows="4"
            placeholder="请输入关卡描述"
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-radio-group v-model="editForm.priority">
            <el-radio :value="0">普通</el-radio>
            <el-radio :value="99">推荐</el-radio>
            <el-radio :value="999">精选</el-radio>
            <el-radio :value="9999">置顶</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave"
          >保存</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
  listAllLevels,
  updateLevel,
  deleteLevel,
  setLevelPriority,
} from "../api";

const router = useRouter();

const levels = ref([]); // 表格数据源
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const loading = ref(false);
const saving = ref(false);

// 搜索表单
const searchForm = reactive({
  keyword: "",
  difficulty: "",
  priority: null,
});

// 编辑对话框
const editDialogVisible = ref(false);
const editFormRef = ref(null);
const editForm = ref({
  id: null,
  levelName: "",
  difficulty: "",
  targetSalary: 10000,
  levelDesc: "",
  priority: 0,
});

const editFormRules = {
  levelName: [{ required: true, message: "请输入关卡名称", trigger: "blur" }],
  difficulty: [{ required: true, message: "请选择难度", trigger: "change" }],
  targetSalary: [
    { required: true, message: "请输入目标薪资", trigger: "blur" },
  ],
  levelDesc: [{ required: true, message: "请输入关卡描述", trigger: "blur" }],
};

// 格式化薪资
function formatSalary(salary) {
  if (!salary) return "-";
  if (salary >= 10000) {
    return (salary / 10000).toFixed(1) + " 万/月";
  }
  return salary + " 元/月";
}

// 难度标签颜色
function difficultyType(difficulty) {
  const map = { 简单: "success", 中等: "warning", 困难: "danger" };
  return map[difficulty] || "info";
}

// 优先级标签文字
function priorityLabel(priority) {
  const map = { 0: "普通", 99: "推荐", 999: "精选", 9999: "置顶" };
  return map[priority] || "普通";
}

// 优先级标签颜色
function priorityTagType(priority) {
  if (priority >= 9999) return "danger";
  if (priority >= 999) return "warning";
  if (priority >= 99) return "success";
  return "info";
}

// 获取关卡列表
async function fetchLevels() {
  loading.value = true;
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value,
    };
    // 仅传递非空的筛选条件
    if (searchForm.keyword) params.keyword = searchForm.keyword;
    if (searchForm.difficulty) params.difficulty = searchForm.difficulty;
    if (searchForm.priority !== null && searchForm.priority !== "")
      params.priority = searchForm.priority;
    const res = await listAllLevels(params);
    levels.value = res.data.records || [];
    total.value = res.data.total || 0;
  } catch (e) {
    ElMessage.error(e.message || "获取关卡列表失败");
  } finally {
    loading.value = false;
  }
}

// 搜索（重置到第1页）
function handleSearch() {
  currentPage.value = 1;
  fetchLevels();
}

// 重置搜索条件
function handleReset() {
  searchForm.keyword = "";
  searchForm.difficulty = "";
  searchForm.priority = null;
  currentPage.value = 1;
  fetchLevels();
}

// 打开编辑对话框
function openEditDialog(row) {
  editForm.value = {
    id: row.id,
    levelName: row.levelName,
    difficulty: row.difficulty,
    targetSalary: row.targetSalary,
    levelDesc: row.levelDesc,
    priority: row.priority || 0,
  };
  editDialogVisible.value = true;
}

// 保存编辑
async function handleSave() {
  const valid = await editFormRef.value.validate().catch(() => false);
  if (!valid) return;
  saving.value = true;
  try {
    await updateLevel(editForm.value);
    ElMessage.success("关卡更新成功");
    editDialogVisible.value = false;
    fetchLevels();
  } catch (e) {
    ElMessage.error(e.message || "更新失败");
  } finally {
    saving.value = false;
  }
}

// 设为精选
async function handleSetFeatured(row) {
  try {
    await setLevelPriority(row.id, 999);
    ElMessage.success("已设为精选关卡");
    fetchLevels();
  } catch (e) {
    ElMessage.error(e.message || "操作失败");
  }
}

// 设置优先级
async function handleSetPriority(row, priority) {
  const label = priorityLabel(priority);
  try {
    await setLevelPriority(row.id, priority);
    ElMessage.success(`已设为${label}关卡`);
    fetchLevels();
  } catch (e) {
    ElMessage.error(e.message || "操作失败");
  }
}

// 删除关卡
async function handleDelete(id) {
  try {
    await deleteLevel(id);
    ElMessage.success("关卡已删除");
    fetchLevels();
  } catch (e) {
    ElMessage.error(e.message || "删除失败");
  }
}

onMounted(() => {
  fetchLevels();
});
</script>

<style scoped>
.admin-page {
  max-width: 1100px;
  margin: 32px auto;
  padding: 0 20px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--sand-accent);
  font-size: 22px;
  letter-spacing: 2px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.level-table {
  border-radius: 6px;
  overflow: hidden;
}

.level-name {
  color: var(--sand-accent);
  cursor: default;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
