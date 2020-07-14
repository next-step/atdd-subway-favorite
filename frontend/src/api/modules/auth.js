import ApiService from '@/api'

const AuthService = {
  login(userInfo) {
    const { email, password } = userInfo
    const username = email
    return ApiService.post(`/login/token`, {
      auth: {
        username,
        password
      }
    })
  }
}

export default AuthService
