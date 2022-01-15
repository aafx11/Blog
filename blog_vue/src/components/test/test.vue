<template>
  <div class="wrapper">
    <my-magnifier
      link="link"
      :blank="blank"
      :imgUrl="imgUrl"
      :imgWidth="imgWidth"
      :imgHeight="imgHeight"
      :magWidth="magWidth"
      :magHeight="magHeight"
      imgAlt="测试"
      ref="test"
    >

    </my-magnifier>
    <router-view></router-view>
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
  onBeforeMount,
    nextTick,
    getTransitionRawChildren
} from 'vue';


export default {
  name: "test",
  setup() {
    const state = reactive({
      imgUrl:'http://localhost:8081/static/videoCover/5f71b521-1903-404a-917f-cd3ec2a0ad1d.jpg',
      link:"https://www.baidu.com",
      blank:true,
      imgWidth:375,
      imgHeight:481,
      magWidth:100,
      magHeight:100
    })


    let test = ref(null)
    console.log(test);
    return {
      ...toRefs(state),
      test
    }
  }

}


</script>

<style lang="scss" scoped>
.wrapper{
  margin: 100px auto;
}
</style>

