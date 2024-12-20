import request from './request'

// 获取验证码
export function generateCaptcha() {
  return request({
    url: '/api/captcha/generate',
    method: 'get',
    responseType: 'blob',
    withCredentials: true
  })
}

// 验证验证码
export function validateCaptcha(captcha) {
  const params = new URLSearchParams()
  params.append('captcha', captcha)
  
  return request({
    url: '/api/captcha/validate',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data: params,
    withCredentials: true
  })
}

export function login(username, password) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    params: {
      username,
      password: String(password)
    },
    withCredentials: true
  })
}

export function register(username, password, role) {
  return request({
    url: '/api/user/register',
    method: 'post',
    params: {
      username,
      password: String(password),
      role: Number(role)
    }
  })
}

// 获取用户名
export function getUserName() {
  return request({
    url: '/api/user/name',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function getUserInfo() {
  return request({
    url: '/api/user/info',
    method: 'get',
    headers: {
      'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
    }
  })
}

export function test5() {
    return request({
        url: '/api/test/test5',
        method: 'get',
      headers: {
        'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
      }
    })
}