import Vue from 'vue'
import VueRouter from 'vue-router'
import stationRoutes from '@/router/modules/station'
import lineRoutes from '@/router/modules/line'
import mainRoutes from '@/router/modules/main'
import edgeRoutes from '@/router/modules/edge'
import mapRoutes from '@/router/modules/map'
import pathRoutes from '@/router/modules/path'

Vue.use(VueRouter)

export default new VueRouter({
  mode: 'history',
  routes: [...mapRoutes, ...pathRoutes, ...stationRoutes, ...lineRoutes, ...edgeRoutes, ...mainRoutes],
  scrollBehavior() {
    return { x: 0, y: 0 }
  }
})
