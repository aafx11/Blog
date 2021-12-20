<template>
  <div class="hot-title">
    <div>我的关注</div>
  </div>
  <a-row class="row-container-new" :wrap="true" justify="space-around" :gutter="0" v-infinite-scroll="load">
    <a-col :xs="{ span: 20 }" :md="{ span: 10 }" :lg="{ span: 10}" :xl="{span:6}"
           class="col-container-new" v-for="(item,index) in videoData" v-if="show">
      <video-card :video-data="item"></video-card>
    </a-col>
  </a-row>
</template>

<script>
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {message} from "ant-design-vue";
import videoCard from './videoCard.vue'

import {getVideoByFollow} from '../../../request/api/video.js'
export default {
  name: "VideoFollow",
  components:{
    videoCard:videoCard,
  },
  setup(){
    const {proxy} = getCurrentInstance();
    const current = ref(1)
    const size = ref(10)
    const show = ref(false)
    const videoData = reactive()
    const getVideoFollowData = () => {
      getVideoByFollow(current.value,size.value).then(res=>{
        console.log("关注",res);
        const data = res.data.data;
        proxy.videoData = data.records;
        show.value = false;
        show.value = true;
      })
    }

    const load = () => {
      size.value = size.value + 3;
      getVideoFollowData();
    }

    onMounted(()=>{
      getVideoFollowData();
    })

    return{
      show,
      videoData,
      load,

    }
  }
}
</script>

<style scoped>
.hot-title {
  padding-top: 10px;
  padding-bottom: 10px;
  position: relative;
  font-size: 1.2rem;
  font-weight: 700;
}
</style>