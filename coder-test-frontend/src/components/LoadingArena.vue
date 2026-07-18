<template>
  <div class="loading-arena">
    <div class="loading-roam" ref="arenaRef">
      <img :src="loadingImg" class="loading-mover" ref="moverRef" alt="加载中" />
    </div>
    <div class="loading-progress">
      <el-progress
        :percentage="displayPercent"
        :stroke-width="8"
        :color="progressColor"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import loadingImg from '../assets/loading.png'

const arenaRef = ref(null)
const moverRef = ref(null)

// ========== 移动图标参数 ==========
const SPEED_MIN = 0.8
const SPEED_MAX = 1.8
const JITTER = 0.06

let rafId = null
let x = 0
let y = 0
let vx = 0
let vy = 0
let started = false

function rand(min, max) {
  return Math.random() * (max - min) + min
}

function tick() {
  const arena = arenaRef.value
  const mover = moverRef.value
  if (arena && mover) {
    const aw = arena.clientWidth
    const ah = arena.clientHeight
    const mw = mover.offsetWidth || 48
    const mh = mover.offsetHeight || 48
    const maxX = Math.max(0, aw - mw)
    const maxY = Math.max(0, ah - mh)

    if (!started) {
      x = rand(0, maxX)
      y = rand(0, maxY)
      const ang = rand(0, Math.PI * 2)
      const sp = rand(SPEED_MIN, SPEED_MAX)
      vx = Math.cos(ang) * sp
      vy = Math.sin(ang) * sp
      started = true
    }

    vx += rand(-JITTER, JITTER)
    vy += rand(-JITTER, JITTER)
    let sp = Math.hypot(vx, vy) || SPEED_MIN
    if (sp > SPEED_MAX) {
      vx = (vx / sp) * SPEED_MAX
      vy = (vy / sp) * SPEED_MAX
      sp = SPEED_MAX
    } else if (sp < SPEED_MIN) {
      vx = (vx / sp) * SPEED_MIN
      vy = (vy / sp) * SPEED_MIN
      sp = SPEED_MIN
    }

    x += vx
    y += vy

    if (x <= 0) { x = 0; vx = Math.abs(vx) }
    else if (x >= maxX) { x = maxX; vx = -Math.abs(vx) }
    if (y <= 0) { y = 0; vy = Math.abs(vy) }
    else if (y >= maxY) { y = maxY; vy = -Math.abs(vy) }

    mover.style.transform = `translate(${x}px, ${y}px)`
  }
  rafId = requestAnimationFrame(tick)
}

// ========== 模拟进度条 ==========
// 渐近逼近 99%，无法获知 AI 真实进度，故用「快后慢、永不触顶」的假进度：
// progress += (99 - progress) * rate，rate 带随机抖动，让推进不机械。
// 父组件在请求完成时卸载本组件，进度条随之消失。
const progress = ref(0)
let progressTimer = null
const PROGRESS_STEP = 160 // ms
const RATE_MIN = 0.06
const RATE_MAX = 0.09
const CEIL = 99

const displayPercent = computed(() => Math.round(progress.value))

// 进度条颜色随阶段变化：青褐 → 沙金 → 墨绿
const progressColor = computed(() => {
  const p = progress.value
  if (p < 50) return '#8b7355'
  if (p < 85) return '#b8860b'
  return '#6b8e23'
})

function advanceProgress() {
  const rate = rand(RATE_MIN, RATE_MAX)
  progress.value = Math.min(CEIL, progress.value + (CEIL - progress.value) * rate)
}

onMounted(() => {
  rafId = requestAnimationFrame(tick)
  progressTimer = setInterval(advanceProgress, PROGRESS_STEP)
})

onBeforeUnmount(() => {
  if (rafId) cancelAnimationFrame(rafId)
  if (progressTimer) clearInterval(progressTimer)
})
</script>

<style scoped>
.loading-arena {
  width: 100%;
  border: 1px dashed var(--sand-border);
  border-radius: 8px;
  background: rgba(245, 230, 211, 0.4);
  padding: 8px;
}

.loading-roam {
  position: relative;
  width: 100%;
  height: 120px;
  overflow: hidden;
  border-radius: 4px;
}

.loading-mover {
  position: absolute;
  top: 0;
  left: 0;
  width: 48px;
  height: 48px;
  user-select: none;
  pointer-events: none;
  will-change: transform;
}

.loading-progress {
  margin-top: 10px;
  padding: 0 6px;
}
</style>