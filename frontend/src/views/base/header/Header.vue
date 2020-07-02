<template>
  <v-app-bar flat app color="amber" clipped-left :height="65">
    <v-toolbar-title class="pl-0 mr-12 align-center relative bottom-2">
      <v-btn to="/" text>
        <div class="title">
          <img class="header-logo relative top-5" src="/images/logo_small.png" />
          <span>RUNNINGMAP</span>
        </div>
      </v-btn>
    </v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn to="/stations" text>역 관리</v-btn>
    <v-btn to="/lines" text>노선 관리</v-btn>
    <v-btn to="/edges" text>구간 관리</v-btn>
  </v-app-bar>
</template>

<script>
export default {
  name: 'Header'
}
</script>
<style lang="scss" scoped>
.header-logo {
  width: 25px;
}
</style>
