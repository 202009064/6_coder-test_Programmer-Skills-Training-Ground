<template>
  <div class="footer-container">
    <div class="footer-content">
      <el-row :gutter="20">
        <el-col :xs="12" :sm="12" :md="8" :lg="6">
          <div class="footer-brand">
            <el-icon :size="48" color="#cd9b1d"><Flag /></el-icon>
            <h5 class="footer-site-name">程序员技术练兵场</h5>
          </div>
          <div class="footer-links" style="padding-left: 60px">
            <a href="#" class="footer-link">《用户协议》</a>
            <a href="#" class="footer-link">《隐私政策》</a>
          </div>
        </el-col>

        <el-col :xs="12" :sm="12" :md="8" :lg="6">
          <h5 class="footer-col-title">友情链接</h5>
          <div class="footer-link-list">
            <a href="https://www.codefather.cn/" target="_blank" class="footer-link">编程导航</a>
            <a href="https://www.laoyujianli.com/" target="_blank" class="footer-link">老鱼简历</a>
            <a href="https://www.codecopy.cn/" target="_blank" class="footer-link">代码小抄</a>
            <a href="https://jianqiezhushou.com/" target="_blank" class="footer-link">剪切助手</a>
            <a href="https://mianshiya.com/" target="_blank" class="footer-link">面试鸭</a>
          </div>
        </el-col>

        <el-col :xs="12" :sm="12" :md="8" :lg="6">
          <h5 class="footer-col-title">联系我们</h5>
          <div class="footer-link-list">
            <a href="#" target="_blank" class="footer-link">商务合作</a>
            <a href="https://space.bilibili.com/12890453" target="_blank" class="footer-link">统帅：程序员烨</a>
          </div>
        </el-col>

        <el-col :xs="12" :sm="12" :md="8" :lg="6">
          <h5 class="footer-col-title">关注我们</h5>
          <div class="footer-qrcode">
            <img
              :src="qrcodeImg"
              alt="微信联系方式"
              width="65"
              height="65"
              class="qrcode-img"
            />
            <span class="qrcode-text">扫码关注<br/>微信号</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <footer class="footer-bottom">
      <span style="margin-right: 20px">2026 程序员技术练兵场</span>
      <a class="footer-bottom-link" href="#" target="_blank" rel="noreferrer"
         style="display: inline-flex; align-items: center; gap: 4px">
        <span style="font-size: 12px">&#9878;</span>练兵备案号
      </a>
      <span>&nbsp;&nbsp;</span>
      <a class="footer-bottom-link" href="#" target="_blank">关于本营</a>
    </footer>
  </div>
</template>

<script setup>
import qrcodeImg from '../assets/微信联系方式.jpg'
</script>

<style scoped>
.footer-container {
  background: var(--sand-footer);
  color: #d4c5a9;
  border-top: 2px solid var(--sand-accent);
}

.footer-content {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 20px 16px;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.footer-site-name {
  margin: 0;
  font-size: 18px;
  color: #edd678;
}

.footer-col-title {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #edd678;
}

.footer-links {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.footer-link-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.footer-link {
  color: #d4c5a9;
  text-decoration: none;
  font-size: 14px;
}

.footer-link:hover {
  color: var(--sand-accent-hover);
}

.footer-qrcode {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.qrcode-img {
  border-radius: 4px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.3), 0 2px 4px -2px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.qrcode-img:hover {
  transform: scale(1.25);
}

.qrcode-text {
  color: #8b7355;
  font-size: 12px;
  line-height: 1.6;
  margin-bottom: 6px;
}

.footer-bottom {
  text-align: center;
  padding: 16px 20px 30px;
  border-top: 1px solid var(--sand-accent);
  font-size: 14px;
  color: #8b7355;
}

.footer-bottom-link {
  color: #8b7355;
  text-decoration: none;
  margin-right: 20px;
}

.footer-bottom-link:hover {
  color: var(--sand-accent-hover);
}
</style>