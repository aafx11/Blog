<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import {useRouter} from "vue-router"
import {useStore} from 'vuex'
import {reactive, getCurrentInstance, onMounted, ref,} from 'vue'


export default {
  name: 'App',
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore()
    const route = useRouter()
    const isRouterAlive =ref(true)

    const reload = () => {
      isRouterAlive.value =false;
      proxy.$nextTick(()=>{
        isRouterAlive.value =true;
      })
    }

    const mouse = () => {


      const pic = document.querySelector('.outer')
      window.addEventListener('mousemove',function (e){
        let x = e.clientX;
        let y = e.clientY;
        let imgWight = 0;
        let imgHeight =0;
        pic.style.left = x + 'px';
        pic.style.top = y + 'px';
      })
    }

    onMounted(() => {
      // mouse();
      route.beforeEach((to, from) => {
        if (to.path != '/login') {
          let obj = {
            name: to.name,
            title: to.meta.title
          }
          store.commit("addTab", obj)
        }
      })
    })
    return {
      store,
      route,
      isRouterAlive,
      reload,

    }
  },
  provide(){
    return {
      reload:this.reload
    }
  }
}


</script>
<style>
.outer{
  position: fixed;
  top: 0;
  left: 0;
  z-index: 99;
  pointer-events: none;
}

.outer-yuan{
  border: 1px solid #fba754;
  border-radius: 50%;
  width: 30px;
  height: 30px;
}
.expand{
  position: absolute;
  top:-100px;
  left: -150px;
  border: 1px solid rgba(255, 255, 255, 1);
  /*background-position: -100px -100px;*/
  background-repeat: no-repeat;
  background-size: cover;
  /*border-color: rgba(255, 255, 255, .2);*/
  border-radius: 0;
  height: 200px;
  width: 300px;
  z-index: 1;
}

/*@import "./assets/css/reset.css";*/
#app{
  height: 100%;


}


</style>