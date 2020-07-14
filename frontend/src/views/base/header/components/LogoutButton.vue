<template>
  <MenuListItem>
    <!--    <a href="/logout" class="text-dark">-->
    <v-icon class="mr-1 button">mdi-logout-variant</v-icon>
    <span class="relative top-1">로그아웃</span>
    <!--    </a>-->
  </MenuListItem>
</template>

<script>
import MenuListItem from '@/components/menus/MenuListItem'

export default {
  name: 'LogoutButton',
  components: { MenuListItem }
}
</script>
