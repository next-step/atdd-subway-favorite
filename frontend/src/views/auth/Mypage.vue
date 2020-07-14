<template>
  <div class="d-flex flex-column justify-center height-100vh-65px">
    <div class="d-flex justify-center relative">
      <v-card width="400" class="card-border px-3 pt-3 pb-5">
        <v-card-title class="font-weight-bold justify-center">
          나의 정보
        </v-card-title>
        <v-card-text class="px-4 pt-4 pb-0">
          <v-row>
            <v-col cols="12">
              <label class="font-weight-regular">이름</label>
              <div class="text-left subtitle-1 text-dark font-weight-bold">{{ user.name }}</div>
            </v-col>
          </v-row>
          <v-row>
            <v-col cols="12">
              <label class="font-weight-regular">email</label>
              <div class="text-left subtitle-1 text-dark font-weight-bold">{{ user.email }}</div>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-btn to="/mypage/edit" color="amber" class="width-100">
            수정
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Mypage',
  data() {
    return {
      user: {
        name: 'eastjun',
        email: 'email@email.com'
      }
    }
  }
}
</script>

<style scoped></style>
