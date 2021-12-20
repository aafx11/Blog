<template>
  <div class="title-box">
    <div @click="pushRecommendedVideo" class="hot-title"><span v-text="'<'"></span>返回</div>
    <div >热门推荐</div>
  </div>
  <a-row class="row-container" :wrap="true" justify="space-around" :gutter="0" v-infinite-scroll="load">
    <a-col :xs="{ span: 20 }" :md="{ span: 10 }" :lg="{ span: 10}" :xl="{span:6}"
           class="col-container" v-for="(item,index) in hotVideoData" v-if="showHotVideo">
      <video-card :video-data="item"></video-card>
    </a-col>
  </a-row>
</template>

<script>
import {getVideoOrderByView} from '../../../request/api/video.js'
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';
import videoCard from './videoCard.vue'
import {useRouter} from "vue-router";


export default {
  name: "HotVideoPage",
  components:{
    videoCard:videoCard,
  },
  setup(){
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const showHotVideo = ref(false);
    const hotVideoData = reactive({})
    const hotCurrent = ref(1)
    const hotSize = ref(12)
    const hotTotal = ref(50)
    //获取热门视频（按播放量排序）
    const getHotVideoData = () => {
      getVideoOrderByView(hotCurrent.value, hotSize.value).then(res => {
        console.log("热门视频", res);
        const data = res.data.data;
        proxy.hotVideoData = data.records;
        hotTotal.value = data.total;
        showHotVideo.value = false;
        showHotVideo.value = true;
      })
    }
    const load = () => {
      hotSize.value = hotSize.value + 4;
      getHotVideoData();
    }
    const pushRecommendedVideo = () => {
      router.push({name:'recommendedVideo'})
    }
    onMounted(()=>{
      getHotVideoData();
    })
    return{
      showHotVideo,
      hotVideoData,
      load,
      getHotVideoData,
      pushRecommendedVideo,
    }
  }
}
</script>

<style scoped>
.title-box{
  position: relative;
}
.hot-title {
  font-size: 1rem;
  padding-top: 10px;
  font-weight: 700;
  cursor: pointer;
  display: inline-block;
}
.hot-title + div{
  display: inline-block;
  font-size: 1.2rem;
  font-weight: 700;
  position: absolute;
  right: 50%;
  line-height: 50px;
}
.row-container {
  width: 100%;
}
.col-container {
  width: 100%;
  height: 310px;
  margin-top: 25px;

}
@media screen and (min-width: 992px ) {
  .col-container, .col-container-new {
    padding-right: 20px;

  }
}

</style>