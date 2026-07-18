import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getCurrentUser, login as loginApi, register as registerApi, logout as logoutApi } from '../api'

export const useUserStore = defineStore('user', () => {
  const user = ref(null)
  const isLoggedIn = computed(() => user.value !== null)
  const salary = computed(() => user.value?.salary ?? 0)

  // 尝试获取当前登录用户
  async function fetchCurrentUser() {
    try {
      const res = await getCurrentUser()
      user.value = res.data
    } catch {
      user.value = null
    }
  }

  // 登录
  async function doLogin(data) {
    const res = await loginApi(data)
    user.value = res.data
    return res
  }

  // 注册
  async function doRegister(data) {
    const res = await registerApi(data)
    user.value = res.data
    return res
  }

  // 登出
  async function doLogout() {
    await logoutApi()
    user.value = null
  }

  // 清空本地登录态（不调登出接口，供拦截器在 session 失效时调用）
  function clearUser() {
    user.value = null
  }

  // 更新薪资（本地更新，不调接口）
  function updateSalary(newSalary) {
    if (user.value) {
      user.value.salary = newSalary
    }
  }

  return {
    user,
    isLoggedIn,
    salary,
    fetchCurrentUser,
    doLogin,
    doRegister,
    doLogout,
    clearUser,
    updateSalary,
  }
})