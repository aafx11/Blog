<template>
  <div class="hot-title">
    <div>热门</div>
    <div @click="pushHotVideoPage">更多></div>
<!--    <div @click="changeHotVideo">-->
<!--      <RedoOutlined/>-->
<!--      换一换-->
<!--    </div>-->
  </div>
  <a-row class="row-container" :wrap="true" justify="space-around" :gutter="0">
    <a-col :xs="{ span: 20 }" :md="{ span: 10 }" :lg="{ span: 10}" :xl="{span:6}"
           class="col-container" v-for="(item,index) in hotVideoData" v-if="showHotVideo">
      <video-card :video-data="item"></video-card>
    </a-col>
  </a-row>
  <div class="recommend-title">推荐视频</div>
  <a-row class="row-container-new" :wrap="true" justify="space-around" :gutter="0" v-infinite-scroll="load">
    <a-col :xs="{ span: 20 }" :md="{ span: 10 }" :lg="{ span: 10}" :xl="{span:6}"
           class="col-container-new" v-for="(item,index) in newVideoData" v-if="showNewVideo">
      <video-card :video-data="item"></video-card>
    </a-col>
  </a-row>
</template>

<script>
import {getVideoOrderByView, getVideoOrderByCreated, getVideoOrderByLike} from '../../../request/api/video.js'
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';
import {RedoOutlined} from '@ant-design/icons-vue';

import videoCard from './videoCard.vue'
import router from "../../../router";

export default {
  name: "RecommendedVideo",
  components: {
    videoCard: videoCard,
    RedoOutlined,
  },
  setup() {
    const {proxy} = getCurrentInstance();


    const showHotVideo = ref(false);
    const hotVideoData = reactive({})
    const hotCurrent = ref(1)
    const hotSize = ref(4)
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

    const showNewVideo = ref(false)
    const newVideoData = reactive({})
    const newCurrent = ref(1)
    const newSize = ref(8)
    const newTotal = ref(50)
    const getNewVideoData = () => {
      getVideoOrderByCreated(newCurrent.value, newSize.value).then(res => {
        const data = res.data.data;
        proxy.newVideoData = data.records;
        showNewVideo.value = false;
        showNewVideo.value = true;
      })
    }

    const load = () => {
      newSize.value = newSize.value + 4;
      getNewVideoData();
    }

    // const changeHotVideo = () => {
    //   const min = 1;
    //   const max = Math.floor(hotTotal.value / 4) + 1;
    //   console.log(max);
    //   let randomCurrent = Math.floor(min + Math.random() * (max - min));
    //   //
    //   while (randomCurrent == hotCurrent.value) {
    //     randomCurrent = Math.floor(min + Math.random() * (max - min));
    //   }
    //   console.log("随机数2", randomCurrent);
      //
      // hotCurrent.value = randomCurrent;
      // getHotVideoData();
    // }

    const pushHotVideoPage = () => {
      router.push({name:'hotVideoPage'})
    }
    onMounted(() => {
      getHotVideoData();
      getNewVideoData();
    })
    return {
      showHotVideo,
      showNewVideo,
      hotVideoData,
      newVideoData,
      load,
      // changeHotVideo,
      getHotVideoData,
      pushHotVideoPage,
    }
  }
}
</script>

<style scoped>
.row-container {
  width: 100%;
  /*height: 100%;*/
  /*border: 1px solid black;*/
}

.row-container-new {
  width: 100%;
  /*height: auto;*/
  /*overflow-y: scroll;*/
}

.hot-title {
  padding-top: 10px;
  padding-bottom: 10px;
  position: relative;
}

.hot-title div {
  display: inline-block;
}

.hot-title div:nth-child(1) {
  font-size: 1.2rem;
  font-weight: 700;
}

.hot-title div:nth-child(2) {
  cursor: pointer;
  color: rgb(147, 147, 147);
}

.hot-title div:nth-child(3) {
  /*float: right;*/
  position: absolute;
  right: 0;
  top: 40%;
  cursor: pointer;
  color: rgb(147, 147, 147);

}

.col-container {
  width: 100%;
  height: 310px;

}

.col-container-new {
  width: 100%;
  height: 310px;
  margin-top: 25px;
}

@media screen and (min-width: 992px ) {
  .col-container, .col-container-new {
    padding-right: 20px;
  }
}

.recommend-title {
  padding-top: 15px;
  /*padding-bottom: 10px;*/
  margin-bottom: -10px;
  font-size: 1.2rem;
  font-weight: 700;
}

</style>