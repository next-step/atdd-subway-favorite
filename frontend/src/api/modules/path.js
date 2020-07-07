import ApiService from '@/api'

const BASE_URL = '/paths'

const PathService = {
  get({ source, target, type }) {
    return ApiService.get(`${BASE_URL}/?source=${source}&target=${target}&type=${type}`)
  }
}

export default PathService
