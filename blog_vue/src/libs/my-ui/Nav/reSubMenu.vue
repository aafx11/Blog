<template>

    <my-sub-menu>
      <template #title>{{ data.title }}</template>


      <template v-for="(c,index) of data.children">

          <my-menu-item
              v-if="!c.children"
              :path="c.path"
          >
            {{ c.title }}
          </my-menu-item>
          <my-reSub-menu v-else :data="c"></my-reSub-menu>

      </template>


    </my-sub-menu>

</template>

<script>
import {toRefs,inject,computed} from "vue";
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
  inject: ['mode'],
  props: {
    data: Object,

  },
  setup(props) {
    let {data} = toRefs(props)
    console.log(data);
    let mode = inject('mode')


    let itemClass = computed(() => {
      let mode = inject('mode')
      if (mode === 'horizontal') return 'horizontal-item'
      else if (mode === 'vertical') return 'vertical-item'
      else if (mode === 'inline') return 'inline-item'
    })

    return {
      mode,
      itemClass
    }
  }
}
</script>

<style scoped>
.horizontal-item{
  position: absolute;
  top: 100%;
  left: 10%;
  background-color: #333;
  width: 100px;
}
.horizontal-item-next{
  position: absolute;
  top: 100%;
  left: 100%;
  background-color: #333;
  width: 100px;
}

.vertical-item {
  position: absolute;
  left: 100%;
  top: 0;
  width: 100%;
  background-color: #333;
}
</style>