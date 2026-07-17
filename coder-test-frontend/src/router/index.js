import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '../views/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      { path: 'home', component: () => import('../views/HomePage.vue'), meta: { title: '首页' } },
      { path: 'level', component: () => import('../views/LevelPage.vue'), meta: { title: '答题闯关' } },
      { path: 'report/:id', component: () => import('../views/ReportPage.vue'), meta: { title: '成绩报告' } },
      { path: 'records', component: () => import('../views/RecordsPage.vue'), meta: { title: '闯关记录' } },
    ],
  },
  {
    path: '/login',
    component: () => import('../views/LoginPage.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/register',
    component: () => import('../views/RegisterPage.vue'),
    meta: { title: '注册' },
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

export default router