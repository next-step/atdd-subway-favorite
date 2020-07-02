import Vue from 'vue'
import Vuex from 'vuex'
import station from '@/store/modules/station'
import line from '@/store/modules/line'
import snackbar from '@/store/modules/snackbar'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    station,
    line,
    snackbar
  }
})
