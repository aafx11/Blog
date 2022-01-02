<template>
  <!--  <div class="container">-->
  <!--    <div class="aside-empty"></div>-->
  <!--    <div class="aside-container">123</div>-->
  <!--    <div class="main-container"></div>-->
  <!--  </div>-->
  <my-selector placeholder="测试" @setItemValue="setItemValue" @searchOptions="searchOptions" :data="data" ></my-selector>
  <input type="text" v-model="txt">
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
    let txt = ref()

    watch(()=>{
      return txt.value
    },()=>{
      console.log('watch',txt.value)
    })
    const data = reactive([

      {
        id: 1,
        value: 'vue123',
        text: 'Vue'
      },
      {
        id: 2,
        value: 'ES61123',
        text: 'ES6'
      },

    ])

    const setItemValue = (value) => {
      console.log('获取的值',value)
    }

    const searchOptions = (value)=>{
      console.log('输入',value)
      txt.value = value
    }
    return {
      txt,
      data,
      setItemValue,
      searchOptions,
    }
  }

}


</script>

<style lang="scss" scoped>
.container {
  min-height: 100vh;
  background-color: #f0f2f5;
  display: flex;
}

.aside-empty {
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

  width: 100px;
  /*min-width: 2000px;*/
  height: 100px;
  z-index: 100;
}


</style>