<template>
  <div class="container-box">
    <div class="inner">
      <login-form></login-form>
    </div>
  </div>
</template>

<script>
import loginForm from './loginForm.vue'
export default {
  name: "loginPage",
  components:{
    loginForm:loginForm
  }
}
</script>

<style scoped>
.container-box{
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.inner{
  height: 500px;
  width: 20%;

}
</style>