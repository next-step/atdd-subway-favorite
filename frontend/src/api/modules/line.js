import ApiService from '@/api'

const BASE_URL = '/lines'

const LineService = {
  get(lineId) {
    return ApiService.get(`${BASE_URL}/${lineId}`)
  },
  getAll() {
    return ApiService.get(`${BASE_URL}`)
  },
  create(newLine) {
    return ApiService.post(`${BASE_URL}`, newLine)
  },
  update(editingLine) {
    return ApiService.update(`${BASE_URL}/${editingLine.lineId}`, editingLine.line)
  },
  delete(lineId) {
    return ApiService.delete(`${BASE_URL}/${lineId}`)
  },
  deleteStation({ lineId, stationId }) {
    return ApiService.delete(`${BASE_URL}/${lineId}/stations/${stationId}`)
  },
  createEdge({ lineId, edge }) {
    return ApiService.post(`${BASE_URL}/${lineId}/stations`, edge)
  }
}

export default LineService
