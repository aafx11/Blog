<template>
  <div class="container">
    {{user.nickname}}
    <div>123</div>
  </div>

</template>

<script>
import test from './test.vue'
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref, watch, onBeforeMount} from 'vue';
import {getUserProfileVOById} from '../../request/api/userList.js'
import {getUserDataById} from '../../assets/js/user.js'
export default {
  name: "parent",
  components:{
    test:test
  },
  setup() {

  }
}
</script>

<style scoped>
.container{
  width: 500px;
  height: 500px;
  background-color: darkseagreen;
}
</style>