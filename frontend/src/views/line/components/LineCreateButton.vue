<template>
  <Dialog :width="500" :close="close">
    <template slot="trigger">
      <v-btn class="mx-2 absolute right-30 z-1 line-create-button" fab color="amber">
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
    <template slot="title">
      <div class="width-100 text-center mt-6">노선 생성</div>
    </template>
    <template slot="text">
      <v-form ref="lineForm" v-model="valid" @submit.prevent>
        <v-text-field
          v-model="lineForm.name"
          :rules="rules.line.name"
          color="grey darken-1"
          label="노선 이름"
          placeholder="노선 이름"
          outlined
        ></v-text-field>
        <v-row>
          <v-col cols="4">
            <v-text-field
              v-model="lineForm.startTime"
              :rules="rules.line.startTime"
              color="grey darken-1"
              label="첫차 시간"
              placeholder="첫차 시간"
              outlined
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <v-text-field
              v-model="lineForm.endTime"
              :rules="rules.line.endTime"
              color="grey darken-1"
              label="막차 시간"
              placeholder="막차 시간"
              outlined
            ></v-text-field>
          </v-col>
          <v-col cols="4">
            <v-text-field
              v-model="lineForm.intervalTime"
              :rules="rules.line.intervalTime"
              color="grey darken-1"
              label="간격"
              placeholder="간격"
              outlined
            ></v-text-field>
          </v-col>
        </v-row>
        <div>
          <v-text-field v-model="lineForm.color" :rules="rules.line.color" :value="lineForm.color" label="노선 색상" filled disabled></v-text-field>
          <p>
            노선의 색상을 아래 팔레트에서 선택해주세요.
          </p>
          <div class="d-flex">
            <div>
              <template v-for="(color, index) in lineColors">
                <v-btn :key="index" small class="color-button ma-1" depressed :color="color" @click="setLineColor(color)"></v-btn>
                <template v-if="index === 0"></template>
                <br v-if="index === 8 || index % 9 === 8" />
              </template>
            </div>
          </div>
        </div>
      </v-form>
    </template>
    <template slot="action">
      <v-btn :disabled="!valid" @click.prevent="onCreateLine" color="amber">확인</v-btn>
    </template>
  </Dialog>
</template>

<script>
import Dialog from '@/components/dialogs/Dialog'
import { SNACKBAR_MESSAGES } from '@/utils/constants'
import dialog from '@/mixins/dialog'
import validator from '@/utils/validator'
import { mapActions, mapMutations } from 'vuex'
import { SHOW_SNACKBAR } from '@/store/shared/mutationTypes'
import { CREATE_LINE, DELETE_LINE, EDIT_LINE, FETCH_LINES } from '@/store/shared/actionTypes'

export default {
  name: 'LineCreateButton',
  mixins: [dialog],
  components: { Dialog },
  methods: {
    ...mapMutations([SHOW_SNACKBAR]),
    ...mapActions([FETCH_LINES, CREATE_LINE, DELETE_LINE, EDIT_LINE]),
    setLineColor(color) {
      this.lineForm.color = color
    },
    isValid() {
      return this.$refs.lineForm.validate()
    },
    async onCreateLine() {
      if (!this.isValid()) {
        return
      }
      try {
        await this.createLine(this.lineForm)
        await this.fetchLines()
        this.lineForm = {
          name: '',
          color: '',
          startTime: '',
          endTime: '',
          intervalTime: ''
        }
        this.$refs.lineForm.resetValidation()
        this.closeDialog()
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.SUCCESS)
      } catch (e) {
        this.showSnackbar(SNACKBAR_MESSAGES.COMMON.FAIL)
      }
    }
  },
  data() {
    return {
      rules: { ...validator },
      lineForm: {
        name: '',
        color: '',
        startTime: '',
        endTime: '',
        intervalTime: ''
      },
      valid: false,
      lineColors: [
        'grey lighten-5',
        'grey lighten-4',
        'grey lighten-3',
        'grey lighten-2',
        'grey lighten-1',
        'grey darken-1',
        'grey darken-2',
        'grey darken-3',
        'grey darken-4',

        'red lighten-5',
        'red lighten-4',
        'red lighten-3',
        'red lighten-2',
        'red lighten-1',
        'red darken-1',
        'red darken-2',
        'red darken-3',
        'red darken-4',

        'orange lighten-5',
        'orange lighten-4',
        'orange lighten-3',
        'orange lighten-2',
        'orange lighten-1',
        'orange darken-1',
        'orange darken-2',
        'orange darken-3',
        'orange darken-4',

        'yellow lighten-5',
        'yellow lighten-4',
        'yellow lighten-3',
        'yellow lighten-2',
        'yellow lighten-1',
        'yellow darken-1',
        'yellow darken-2',
        'yellow darken-3',
        'yellow darken-4',

        'green lighten-5',
        'green lighten-4',
        'green lighten-3',
        'green lighten-2',
        'green lighten-1',
        'green darken-1',
        'green darken-2',
        'green darken-3',
        'green darken-4',

        'teal lighten-5',
        'teal lighten-4',
        'teal lighten-3',
        'teal lighten-2',
        'teal lighten-1',
        'teal darken-1',
        'teal darken-2',
        'teal darken-3',
        'teal darken-4',

        'blue lighten-5',
        'blue lighten-4',
        'blue lighten-3',
        'blue lighten-2',
        'blue lighten-1',
        'blue darken-1',
        'blue darken-2',
        'blue darken-3',
        'blue darken-4',

        'indigo lighten-5',
        'indigo lighten-4',
        'indigo lighten-3',
        'indigo lighten-2',
        'indigo lighten-1',
        'indigo darken-1',
        'indigo darken-2',
        'indigo darken-3',
        'indigo darken-4',

        'purple lighten-5',
        'purple lighten-4',
        'purple lighten-3',
        'purple lighten-2',
        'purple lighten-1',
        'purple darken-1',
        'purple darken-2',
        'purple darken-3',
        'purple darken-4',

        'pink lighten-5',
        'pink lighten-4',
        'pink lighten-3',
        'pink lighten-2',
        'pink lighten-1',
        'pink darken-1',
        'pink darken-2',
        'pink darken-3',
        'pink darken-4'
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.color-button {
  min-width: 30px !important;
}

.line-create-button {
  bottom: -25px;
}
</style>
