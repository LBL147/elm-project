/**
 * 权限管理工具
 */
export const Auth = {
  // 保存用户信息
  setUser(userInfo) {
    localStorage.setItem('username', userInfo.username || '')
    localStorage.setItem('token', userInfo.token || '')
    localStorage.setItem('userType', userInfo.type || '')
    localStorage.setItem('userId', userInfo.userId || '')
  },

  // 获取用户信息
  getUser() {
    return {
      username: localStorage.getItem('username') || '',
      token: localStorage.getItem('token') || '',
      userType: localStorage.getItem('userType') || '',
      userId: localStorage.getItem('userId') || ''
    }
  },

  // 清除用户信息
  clearUser() {
    localStorage.removeItem('username')
    localStorage.removeItem('token')
    localStorage.removeItem('userType')
    localStorage.removeItem('userId')
  },

  // 检查是否已登录
  isLoggedIn() {
    return !!localStorage.getItem('token')
  },

  // 检查是否为管理员
  isAdmin() {
    return localStorage.getItem('userType') === 'admin'
  },

  // 检查是否为商家
  isBusiness() {
    return localStorage.getItem('userType') === 'business'
  }
}


