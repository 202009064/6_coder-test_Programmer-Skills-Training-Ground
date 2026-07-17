import request from './request'

// ==================== 用户接口 ====================

// 注册
export function register(data) {
  return request.post('/user/register', data)
}

// 登录
export function login(data) {
  return request.post('/user/login', data)
}

// 登出
export function logout() {
  return request.post('/user/logout')
}

// 获取当前用户
export function getCurrentUser() {
  return request.get('/user/current')
}

// ==================== 关卡接口 ====================

// AI 生成关卡（根据薪资动态调整难度）
export function generateLevel(salary) {
  return request.post('/level/generate', { salary })
}

// 查询关卡详情
export function getLevelDetail(id) {
  return request.get('/level/detail', { params: { id } })
}

// ==================== 用户关卡接口 ====================

// 提交关卡答案（AI 评估）
export function submitLevel(data) {
  return request.post('/user-level/submit', data)
}

// 查询一条答题记录详情
export function getUserLevelDetail(id) {
  return request.get('/user-level/get', { params: { id } })
}

// 分页查询当前用户的闯关记录
export function listUserLevels(params) {
  return request.get('/user-level/list', { params })
}