<template>
  <v-app v-cloak>
    <Header />
    <v-main>
      <router-view />
    </v-main>
    <Snackbar />
  </v-app>
</template>

<script>
import Header from '@/views/base/header/Header'
import Snackbar from '@/components/snackbars/Snackbar'

export default {
  name: 'App',
  components: {
    Header,
    Snackbar
  },
  data() {
    return {}
  }
}
</script>
