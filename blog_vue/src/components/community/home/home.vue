<template>
  <Nav></Nav>
  <div class="content">
    <router-view :key="key"></router-view>
  </div>




</template>

<script>
import home_Nav from './home_Nav.vue'
import home_footer from './home_footer.vue'
export default {
  name: "home",
  components:{
    Nav:home_Nav,
    footer:home_footer,
  },
  computed:{
    key () {
      console.log("key值",this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date());
      return this.$route.name!== undefined? this.$route.name+ +new Date(): this.$route + +new Date()
    }
  }
}
</script>

<style scoped>
.content{
  padding: 0 0;
  margin-top:62px ;
  height: 100%;
  /*height: auto;*/
  width: 100%;
}

</style>