<template>
  <div class="hot-title">
    <div>{{tagName}}</div>
  </div>
  <a-row class="row-container-new" :wrap="true" justify="space-around" :gutter="0" v-infinite-scroll="getMoreVideo">
    <a-col :xs="{ span: 20 }" :md="{ span: 10 }" :lg="{ span: 10}" :xl="{span:6}"
           class="col-container-new" v-for="(item,index) in videoData" v-if="showVideo">
      <video-card :video-data="item"></video-card>
    </a-col>
  </a-row>
</template>

<script>
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
import {useRouter, useRoute} from "vue-router";
import {getVideoByTag} from '../../../request/api/video.js'
import videoCard from './videoCard.vue'

export default {
  name: "VideoTagsPage",
  components:{
    videoCard: videoCard,

  },
  setup() {
    const {proxy} = getCurrentInstance();
    const route = useRoute();
    const showVideo = ref(false)
    const current = ref(1)
    const size = ref(8)
    const videoData = reactive()
    const getVideoDataByTag = () => {
      const tagId = route.params.id
      getVideoByTag(current.value, size.value, tagId).then(res => {
        console.log("标签视频", res);
        const data = res.data.data;
        proxy.videoData = data.records;
        showVideo.value=false;
        showVideo.value=true;

      })
    }
    const tagName = ref()
    const getMoreVideo = () =>{
      size.value = size.value + 4;
      getVideoDataByTag();
    }
    onMounted(() => {
      getVideoDataByTag();
      tagName.value = route.params.name
    })
    return {
      showVideo,
      videoData,
      tagName,
      getMoreVideo,
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