<template>
  <div class="container">
    <div class="aside-empty"></div>
    <div class="aside-container">123</div>
    <div class="main-container"></div>
  </div>

</template>

<script>
import {
  computed,
  defineComponent,
  toRefs,
  getCurrentInstance,
  onMounted,
  reactive,
  ref,
  watch,
  onBeforeMount
} from 'vue';
import {articleLists} from '../../request/api/article.js'
import {getArticleData} from '../../assets/js/article.js'


export default {
  name: "test",
  setup() {

  }

}


</script>

<style scoped>
.container{
  min-height: 100vh;
  background-color: #f0f2f5;
  display: flex;
}
.aside-empty{
  width: 248px;
  max-width: 248px;
  height: 100vh;
}
.aside-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 248px;
  max-width: 248px;
  height: 100vh;
  background-color: rgb(120, 125, 222);
  flex: 1;
}

.main-container {
  background-color: hotpink;
  /*width: 100px;*/
  /*min-width: 2000px;*/
  height: 100px;
  z-index: 100;
}
</style>