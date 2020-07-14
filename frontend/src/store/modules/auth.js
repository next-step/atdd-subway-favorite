import { SET_MEMBER } from '@/store/shared/mutationTypes'
import { LOGIN } from '@/store/shared/actionTypes'
import AuthService from '@/api/modules/auth'

const state = {
  member: {}
}

const getters = {
  member(state) {
    return state.member
  }
}

const mutations = {
  [SET_MEMBER](state, member) {
    state.member = member
  }
}

const actions = {
  async [LOGIN]({ commit }, loginInfo) {
    return AuthService.login(loginInfo).then(({ data }) => {
      console.log(data)
      commit(SET_MEMBER, data)
    })
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
