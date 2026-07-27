import axios from 'axios'
import router from '../router'
import { useUserStore } from '../stores/user'

const request = axios.create({
  baseURL: '/api',
  timeout: 600000,
  withCredentials: true,
})

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data
    // 全局拦截未登录：清本地登录态 + 原已登录则跳登录页
    if (res.code === 40100) {
      const userStore = useUserStore()
      const wasLoggedIn = !!userStore.user
      userStore.clearUser()
      if (wasLoggedIn && router.currentRoute.value.path !== '/login') {
        router.push('/login')
      }
      return Promise.reject(new Error('登录已过期，请重新登录'))
    }
    if (res.code !== 0) {
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default request