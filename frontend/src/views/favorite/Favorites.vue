<template>
  <div class="d-flex flex-column justify-center height-100vh-65px">
    <div class="d-flex justify-center relative">
      <v-card width="500" max-width="600" max-height="600" class="card-border">
        <v-card-title class="font-weight-bold justify-center relative">
          즐겨 찾기
        </v-card-title>
        <v-card-text class="relative px-0 pb-0 line-list-container d-flex flex-column">
          <div class="mt-4 overflow-y-auto">
            <v-list-item-group color="grey darken-3">
              <template v-for="(favorite, index) in favorites">
                <v-list-item :key="index">
                  <v-list-item-content>
                    <v-list-item-title>
                      <v-chip class="ma-2" color="amber">
                        <v-avatar left>
                          <v-icon>mdi-subway</v-icon>
                        </v-avatar>
                        {{ favorite.sourceStationName }}
                      </v-chip>
                      <v-icon>mdi-arrow-right-bold</v-icon>
                      <v-chip class="ma-2" color="amber">
                        <v-avatar left>
                          <v-icon>mdi-subway</v-icon>
                        </v-avatar>
                        {{ favorite.targetStationName }}
                      </v-chip>
                    </v-list-item-title>
                  </v-list-item-content>
                  <v-list-item-action class="flex-row">
                    <FavoriteDeleteButton :favorite="favorite" />
                  </v-list-item-action>
                </v-list-item>
                <v-divider v-if="index !== favorites.length - 1" />
              </template>
            </v-list-item-group>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </div>
</template>

<script>
import FavoriteDeleteButton from '@/views/favorite/components/FavoriteDeleteButton'

export default {
  name: 'Favorites',
  components: { FavoriteDeleteButton },
  data() {
    return {
      favorites: [
        {
          id: '1',
          memberId: 'eastjun',
          sourceStationId: '1',
          sourceStationName: '출발역',
          targetStationId: '2',
          targetStationName: '도착역'
        },
        {
          id: '2',
          memberId: 'westjun',
          sourceStationId: '3',
          sourceStationName: '출발역2',
          targetStationId: '4',
          targetStationName: '도착역2'
        }
      ]
    }
  }
}
</script>

<style scoped></style>
