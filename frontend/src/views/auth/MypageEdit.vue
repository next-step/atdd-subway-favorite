<template>
  <div class="d-flex flex-column justify-center height-100vh-65px">
    <div class="d-flex justify-center relative">
      <v-card width="400" class="card-border px-3 pt-3 pb-5">
        <v-form ref="loginForm" v-model="valid" @submit.prevent>
          <v-card-title class="font-weight-bold justify-center">
            나의 정보 수정
          </v-card-title>
          <v-card-text class="px-4 pt-4 pb-0">
            <div class="d-flex">
              <v-text-field
                color="grey darken-1"
                label="이메일을 입력해주세요."
                v-model="user.email"
                prepend-inner-icon="mdi-email"
                dense
                outlined
                :rules="rules.user.email"
              ></v-text-field>
            </div>
            <div class="d-flex mt-2">
              <v-text-field
                color="grey darken-1"
                label="이름을 입력해주세요."
                v-model="user.email"
                prepend-inner-icon="mdi-account"
                dense
                outlined
                :rules="rules.user.email"
              ></v-text-field>
            </div>
            <div class="d-flex mt-2">
              <v-text-field
                color="grey darken-1"
                label="비밀번호를 입력해주세요."
                v-model="user.password"
                prepend-inner-icon="mdi-lock"
                type="password"
                dense
                outlined
                :rules="rules.user.password"
              ></v-text-field>
            </div>
            <div class="d-flex mt-2">
              <v-text-field
                color="grey darken-1"
                label="비밀번호를 한번 더 입력해주세요."
                type="password"
                prepend-inner-icon="mdi-lock"
                dense
                outlined
                v-model="user.confirmPassword"
                :rules="[(user.password && user.password === user.confirmPassword) || '비밀번호가 일치하지 않습니다.']"
              ></v-text-field>
            </div>
            <!--          <v-row>-->
            <!--            <v-col cols="12" class="py-0">-->
            <!--              <div class="text-left subtitle-1 text-dark font-weight-bold">이름</div>-->
            <!--              <v-text-field-->
            <!--                v-model="user.name"-->
            <!--                prepend-inner-icon="mdi-format-title"-->
            <!--                placeholder="세션 타이틀을 입력해주세요."-->
            <!--                color="grey darken-2"-->
            <!--                :value="user.name"-->
            <!--                class="pt-0"-->
            <!--                required-->
            <!--              ></v-text-field>-->
            <!--            </v-col>-->
            <!--          </v-row>-->
          </v-card-text>
        </v-form>
      </v-card>
    </div>
  </div>
</template>

<script>
import validator from '@/utils/validator'

export default {
  name: 'MypageEdit',
  data() {
    return {
      user: {
        name: 'eastjun',
        email: 'email',
        password: '',
        confirmPassword: ''
      },
      valid: false,
      rules: { ...validator }
    }
  }
}
</script>

<style scoped></style>
