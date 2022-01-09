<template>

  <my-sub-menu :index="index">
    <template #title>
      <span>{{ data.title }}</span>
    </template>


    <template #content >
      <template v-for="(c,index) of data.children">
      <my-menu-item
          v-if="!c.children"
          :path="c.path"
      >
        {{ c.title }}
      </my-menu-item>
      <my-reSub-menu v-else :data="c"></my-reSub-menu>
      </template>
    </template>


  </my-sub-menu>

</template>

<script>
import {toRefs, inject, computed} from "vue";
/*
* data:
* {
*   id:1,
*   title:'标题',
*   path:{name:'test'} //path属性接收一个router-link :to的对象
* }
*
* */
export default {
  name: "my-reSub-menu",
  inject: ['mode','theme'],
  props: {
    data: Object,
    index: Number,

  },
  setup(props) {
    let {data} = toRefs(props)
    console.log(data);
    let mode = inject('mode')


    return {
      mode,

    }
  }
}
</script>

<style scoped>

</style>