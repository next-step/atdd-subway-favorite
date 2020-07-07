<template>
  <div class="d-flex justify-center align-center app-container">
    <div class="text-center">
      <img src="/images/main_logo.png" class="main-logo mx-auto" />
      <p class="mt-2 text-gray-700">
        RunningMap Service 페이지입니다. <br />
        제공되는 템플릿을 활용해 프로젝트를 완성해 보세요!
      </p>
      <ul class="text-blue-700">
        <li class="inline-block p-1">
          <router-link to="/stations">
            <v-btn text>역 관리</v-btn>
          </router-link>
        </li>
        <li class="inline-block p-1">
          <router-link to="/lines">
            <v-btn text>노선 관리</v-btn>
          </router-link>
        </li>
        <li class="inline-block p-1">
          <router-link to="/edges">
            <v-btn text>구간 관리</v-btn>
          </router-link>
        </li>
        <li class="inline-block p-1">
          <router-link to="/maps">
            <v-btn text>노선도 페이지</v-btn>
          </router-link>
        </li>
        <li class="inline-block p-1">
          <router-link to="/path">
            <v-btn text>경로 검색 페이지</v-btn>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainPage'
}
</script>

<style lang="scss" scoped>
.app-container {
  height: calc(100vh - 100px);
}

.main-logo {
  width: 150px;
}
</style>
