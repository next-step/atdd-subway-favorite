import Vue from 'vue'
import axios from 'axios'
import VueAxios from 'vue-axios'

const ApiService = {
  init() {
    Vue.use(VueAxios, axios)
  },
  query(uri, params) {
    return Vue.axios.get(uri, params).catch(error => {
      throw new Error(`ApiService ${error}`)
    })
  },
  get(uri) {
    return Vue.axios.get(`${uri}`)
  },
  post(uri, params) {
    return Vue.axios.post(`${uri}`, params)
  },
  update(uri, params) {
    return Vue.axios.put(uri, params)
  },
  delete(uri) {
    return Vue.axios.delete(uri)
  }
}

ApiService.init()

export default ApiService
