<template>
  <!--  <div class="wrapper">-->
  <!--    <my-magnifier-->
  <!--      link="link"-->
  <!--      :blank="blank"-->
  <!--      :imgUrl="imgUrl"-->
  <!--      :imgWidth="imgWidth"-->
  <!--      :imgHeight="imgHeight"-->
  <!--      :magWidth="magWidth"-->
  <!--      :magHeight="magHeight"-->
  <!--      imgAlt="测试"-->
  <!--      ref="test"-->
  <!--    >-->

  <!--    </my-magnifier>-->
  <router-view></router-view>
  <!--  </div>-->
  <div id="mode">123</div>
  <a-popover>
    <template #content>
      <div class="test">内容</div>
    </template>
    <button>123</button>
  </a-popover>
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
      imgUrl: 'http://localhost:8081/static/videoCover/5f71b521-1903-404a-917f-cd3ec2a0ad1d.jpg',
      link: "https://www.baidu.com",
      blank: true,
      imgWidth: 375,
      imgHeight: 481,
      magWidth: 100,
      magHeight: 100
    })


    let test = () => {
      let aa = document.getElementById('mode')
      console.log(aa);
      console.log(123);
    }
    const self = getCurrentInstance()
    onMounted(() => {
      test()
      console.log('this1',self);
    })
    return {
      ...toRefs(state),
      test
    }
  },
  methods: {
    ss() {
      let aa = document.getElementById('mode')
      console.log(aa);
    }
  },
  beforeCreate() {

  },
  created() {
  },
  beforeMount() {

  },
  mounted() {
    console.log('this2',this)
  }

}


</script>

<style lang="scss" scoped>
.wrapper {
  margin: 100px auto;
}

.test-button:disabled {
  width: 50px;
  height: 50px;
}
.test{
  background-color: red;
}
</style>

