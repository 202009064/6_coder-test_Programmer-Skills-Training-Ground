<template>
  <div class="loading-arena" ref="arenaRef">
    <img :src="loadingImg" class="loading-mover" ref="moverRef" alt="加载中" />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import loadingImg from '../assets/loading.png'

const arenaRef = ref(null)
const moverRef = ref(null)

// 运动参数
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

    // 首帧：随机起点 + 随机方向
    if (!started) {
      x = rand(0, maxX)
      y = rand(0, maxY)
      const ang = rand(0, Math.PI * 2)
      const sp = rand(SPEED_MIN, SPEED_MAX)
      vx = Math.cos(ang) * sp
      vy = Math.sin(ang) * sp
      started = true
    }

    // 随机扰动，让轨迹更自然
    vx += rand(-JITTER, JITTER)
    vy += rand(-JITTER, JITTER)
    // 限速
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

    // 撞墙反弹
    if (x <= 0) {
      x = 0
      vx = Math.abs(vx)
    } else if (x >= maxX) {
      x = maxX
      vx = -Math.abs(vx)
    }
    if (y <= 0) {
      y = 0
      vy = Math.abs(vy)
    } else if (y >= maxY) {
      y = maxY
      vy = -Math.abs(vy)
    }

    mover.style.transform = `translate(${x}px, ${y}px)`
  }
  rafId = requestAnimationFrame(tick)
}

onMounted(() => {
  rafId = requestAnimationFrame(tick)
})

onBeforeUnmount(() => {
  if (rafId) cancelAnimationFrame(rafId)
})
</script>

<style scoped>
.loading-arena {
  position: relative;
  width: 100%;
  height: 120px;
  overflow: hidden;
  border: 1px dashed var(--sand-border);
  border-radius: 8px;
  background: rgba(245, 230, 211, 0.4);
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
</style>