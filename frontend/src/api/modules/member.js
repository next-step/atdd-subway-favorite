import ApiService from '@/api'

const BASE_URL = '/members'

const MemberService = {
  get(stationId) {
    return ApiService.get(`${BASE_URL}/${stationId}`)
  },
  getAll() {
    return ApiService.get(`${BASE_URL}`)
  },
  create(newMember) {
    return ApiService.post(`${BASE_URL}`, newMember)
  },
  update(station) {
    return ApiService.put(`${BASE_URL}/${station.id}`, station)
  },
  delete(stationId) {
    return ApiService.delete(`${BASE_URL}/${stationId}`)
  }
}

export default MemberService
