import { CREATE_MEMBER } from '@/store/shared/actionTypes'
import MemberService from '@/api/modules/member'

const actions = {
  async [CREATE_MEMBER]({ commit }, newMember) {
    return MemberService.create(newMember)
  }
}

export default {
  actions
}
