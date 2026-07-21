<template>
  <div class="markdown-viewer" ref="viewerRef" v-html="html"></div>
</template>

<script setup>
import { ref, watch, nextTick } from "vue";
import { marked } from "marked";
import mermaid from "mermaid";

mermaid.initialize({ startOnLoad: false, theme: "base" });

const props = defineProps({
  content: { type: String, default: "" },
});

const viewerRef = ref(null);
const html = ref("");
let renderSeq = 0;

watch(
  () => props.content,
  async (text) => {
    if (!text) {
      html.value = "";
      return;
    }
    const seq = ++renderSeq;

    // 第一步：marked 解析 → v-html 插入真实 DOM
    html.value = await marked.parse(text);

    // 等待 v-html 生效后再在真实 DOM 上替换 mermaid
    await nextTick();
    if (renderSeq !== seq) return;

    const el = viewerRef.value;
    if (!el) return;

    const blocks = el.querySelectorAll("pre code.language-mermaid");
    for (const block of blocks) {
      if (renderSeq !== seq) return;
      const pre = block.parentElement;
      const code = block.textContent.trim();
      if (!code) continue;
      try {
        const { svg } = await mermaid.render(
          `mmd-${seq}-${Math.random().toString(36).slice(2)}`,
          code
        );
        const wrap = document.createElement("div");
        wrap.className = "mermaid-diagram";
        wrap.innerHTML = svg;
        pre.replaceWith(wrap);
      } catch {
        pre.classList.add("mermaid-error");
      }
    }
  },
  { immediate: true }
);
</script>

<style scoped>
/* 基础排版 */
.markdown-viewer :deep(h1),
.markdown-viewer :deep(h2),
.markdown-viewer :deep(h3) {
  color: var(--sand-darker);
  margin: 16px 0 8px;
}
.markdown-viewer :deep(p) {
  margin: 8px 0;
  line-height: 1.8;
}
.markdown-viewer :deep(ul),
.markdown-viewer :deep(ol) {
  padding-left: 20px;
  margin: 8px 0;
}
.markdown-viewer :deep(li) {
  line-height: 1.8;
}
.markdown-viewer :deep(code) {
  background: rgba(139, 115, 85, 0.12);
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 13px;
  font-family: "Courier New", monospace;
}
.markdown-viewer :deep(pre) {
  background: rgba(62, 39, 35, 0.06);
  border: 1px solid var(--sand-border);
  border-radius: 6px;
  padding: 12px 16px;
  overflow-x: auto;
  margin: 12px 0;
}
.markdown-viewer :deep(pre code) {
  background: transparent;
  padding: 0;
}
.markdown-viewer :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 12px 0;
}
.markdown-viewer :deep(th),
.markdown-viewer :deep(td) {
  border: 1px solid var(--sand-border);
  padding: 8px 12px;
  text-align: left;
}
.markdown-viewer :deep(th) {
  background: rgba(184, 134, 11, 0.1);
  font-weight: bold;
}
.markdown-viewer :deep(blockquote) {
  border-left: 3px solid var(--sand-accent);
  margin: 12px 0;
  padding: 4px 16px;
  color: var(--sand-dark);
  background: rgba(184, 134, 11, 0.05);
}

/* Mermaid 图表居中 */
.markdown-viewer :deep(.mermaid-diagram) {
  display: flex;
  justify-content: center;
  margin: 16px 0;
  overflow-x: auto;
}
.markdown-viewer :deep(.mermaid-diagram svg) {
  max-width: 100%;
}
</style>