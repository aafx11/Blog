<template>
  <div class="container-box">
    <div class="left-container"></div>
    <div class="right-container">
      {{commentData}}
    </div>
  </div>
</template>

<script>
import {getLatestComments} from '../../../../request/api/information.js'
import {onBeforeMount, ref} from "vue";
export default {
  name: "welcome",
  setup(){

    // 获取最新评论
    let commentData = ref([])
    onBeforeMount(async ()=>{
      commentData.value = await getLatestComments()

    })

    return {
      commentData
    }
  }
}
</script>

<style lang="scss" scoped>
.container-box{
  width: 100%;
  //height: 100%;
  height: 100vh;
  background-color: rgb(240, 242, 245);
  border: 1px solid black;
  display: flex;

  .left-container{
    width: 100%;
    border: 1px solid blue;
  }

  .right-container{
    width: 500px;
    border: 1px solid rebeccapurple;
  }
}
</style>