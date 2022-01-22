<template>
  <a-row class="row-container">
    <a-col :xs="{span:24}" :md="{span:24}" :lg="{span:19}" :xl="{span:13,offset:4}" class="col-left"
           v-if="showVideoDetail">
      <!--左侧头部视频标题，视频信息      -->
      <div class="header-left">
        <div class="header-video-title">
          {{ videoDetailData.title }}
        </div>
        <div class="video-data">
          <span>{{ videoDetailData.view }}播放</span>
          <span>{{ videoDetailData.created }}</span>
        </div>
      </div>
      <!--左侧播放器-->
      <div class="player-wrap">
        <div class="video-wrap">
          <video :src="'http://localhost:8081/static/video/'+videoDetailData.videoUrl"
                 class="video-player" controls="controls" autoplay="autoplay"
          >

          </video>

<!--          <video :src="'http://47.113.204.103:8081/static/video/'+videoDetailData.videoUrl"-->
<!--                 class="video-player" controls="controls" autoplay="autoplay"-->
<!--          >-->

<!--          </video>-->
        </div>
      </div>
      <!--左侧点赞收藏转发-->
      <div class="video-toolbar">
        <div class="operation">
          <div class="operation-icon" v-if="videoDetailData.hasLike" @click="unLike(videoDetailData.id)">
            <span class="iconfont icon-dianzan_kuai">{{ videoDetailData.likeCount }}</span>
          </div>
          <div class="operation-un-icon" v-else @click="like(videoDetailData.id)">
            <span class="iconfont icon-dianzan_kuai ">    {{ videoDetailData.likeCount }}</span>
          </div>
          <div class="operation-icon" v-if="videoDetailData.hasCollect" @click="opsUnCollect(videoDetailData.id)">
            <span class="iconfont icon-shoucangxiao">{{ videoDetailData.collects }}</span>
          </div>
          <div class="operation-un-icon" v-else @click="opsCollect(videoDetailData.id)">
            <span class="iconfont icon-shoucangxiao ">    {{ videoDetailData.collects }}</span>
          </div>
          <div class="operation-un-icon">
            <span class="iconfont icon-zhuanfa00 "></span>
          </div>
        </div>
      </div>
      <!--视频简介-->
      <div class="video-desc">
        <div class="tip-info">
          <span class="iconfont icon-ai55 ban-icon"></span>
          <span>未经作者授权，禁止转载</span>
        </div>
        <div class="video-desc-introduction">
          <p class="introduction-text">{{ videoDetailData.introduction }}</p>
        </div>
        <div class="video-tags">
          <router-link :to="{name:'videoTagsPage',params:{id:item.id,name:item.name}}" v-for="item in videoDetailData.tags">
            <a-tag class="iconfont icon-icon_biaoqian video-tags-style" >{{ item.name }}</a-tag>
          </router-link>
        </div>
      </div>
      <!--视频评论-->
      <div class="video-comment">
        <VideoComment :video="videoDetailData"></VideoComment>
      </div>
    </a-col>
    <a-col :xs="{span:0}" :md="{span:0}" :lg="{span:5}" :xl="{span:5}" class="col-right">
      <div v-if="showVideoDetail" class="author-card">
        <authorMinCard :videoData="videoDetailData"></authorMinCard>
      </div>
      <div class="video-list">
        <videoMinCard></videoMinCard>
      </div>
    </a-col>
  </a-row>
</template>

<script>
import {getVideoDetailByVideoId, collect, unCollect, likewise, unLikewise} from '../../../request/api/video.js'
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {message} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router"
import authorMinCard from './authorMinCard.vue'
import videoMinCard from './videoMinCard.vue'
import VideoComment from './VideoComment.vue'
export default {
  name: "VideoDetail",
  components:{
    authorMinCard:authorMinCard,
    videoMinCard:videoMinCard,
    VideoComment:VideoComment,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const route = useRoute();

    const videoDetailData = reactive({})
    const showVideoDetail = ref(false)
    const getVideoDetailData = () => {
      getVideoDetailByVideoId(route.params.id).then(res => {
        console.log(res);
        const data = res.data.data;
        proxy.videoDetailData = data;
        console.log("标签",proxy.videoDetailData.tags);
        showVideoDetail.value = false;
        showVideoDetail.value = true;

      })
    }

    //点赞
    const like = (videoId) => {
      likewise(videoId).then(res => {
        console.log("点赞", res);
        getVideoDetailData();
        message.success(res.data.msg);
      })
    }
    //取消点赞
    const unLike = (videoId) => {
      unLikewise(videoId).then(res => {
        console.log("取消点赞", res);
        getVideoDetailData();

      })
    }

    //收藏
    const opsCollect = (videoId) => {
      collect(videoId).then(res => {
        console.log("收藏", res);
        getVideoDetailData();
        message.success(res.data.msg);


      })
    }

    //取消收藏
    const opsUnCollect = (videoId) => {
      unCollect(videoId).then(res => {
        console.log("取消收藏", res);
        getVideoDetailData();

      })
    }

    onMounted(() => {
      getVideoDetailData();
      console.log("123", route.params.id);
    })

    return {
      videoDetailData,
      showVideoDetail,
      like,
      unLike,
      opsCollect,
      opsUnCollect,
    }
  }
}
</script>

<style scoped>
.row-container {
  width: 100%;
  /*height: 100%;*/
  height: auto;
  padding: 0px;
}

/*左侧---------------------------------------start*/
.col-left {
  width: 100%;
  height: 100%;
  background: #fff;

  padding: 0 20px;
  /*border: 1px solid black;*/
}

.header-left {
  padding-top: 16px;
  margin-bottom: 15px;
}

.header-video-title {
  font-size: 1rem;
  font-weight: 500;
  margin-bottom: 8px;
}

.video-data span {
  color: rgb(153, 153, 153);
  margin-right: 20px;
}

.player-wrap, .video-wrap, .video-player {
  width: 100%;
}

.player-wrap {
  height: 560px;
}
@media screen and (max-width: 576px){
  .player-wrap {
    height: 300px;
  }
}


.video-wrap {
  display: flex;
  align-items: flex-end;
  background-color: black;
  height: 100%;
}

.video-player {
  width: 100%;
  height: 95%;
}

.video-toolbar {
  margin-top: 15px;
  border-bottom: 1px solid rgb(229, 233, 240);
}

.operation span {
  font-size: 1.4rem;
  color: rgb(117, 117, 117);
  cursor: pointer;
  margin-right: 25px;
}

.operation-icon, .operation-un-icon {
  display: inline-block;
}

.operation-icon span {
  color: #00a1d6;
  cursor: pointer;
}

.video-desc {
  margin-top: 16px;
}

.ban-icon {
  color: red;
}

.tip-info span:nth-child(2) {
  color: rgb(153, 153, 153);
}

.tip-info {
  margin-bottom: 15px;
}

.introduction-text {
  width: 90%;
}
@media screen and (max-width: 576px){
  .introduction-text {
    width: 100%;
  }
}

.video-tags {
  padding: 10px 0px;
}
.video-tags-style{
  border-radius: 100px;
  font-size: 0.8rem;
  transition: all .3s;
  margin: 0px 10px 8px 0px;
  background: rgb(244, 244, 244);
  padding: 0 12px;
  height: 22px;
  line-height: 22px;
  transition: all .3s;
  color: #00a1d6;
}
.video-tags-style:hover{
  border: 1px solid #00a1d6;
}

.video-comment{
  width: 100%;
  /*padding: 0 20px;*/
  /*box-sizing: content-box;*/
}
/*左侧---------------------------------------end*/

/*右侧---------------------------------------start*/

.col-right {
  width: 100%;
  height: 100%;
  /*background-color: #fff;*/
  padding: 15px 15px;
  /*border: 1px solid crimson;*/
}
.author-card{
  height: 80px;
  padding: 12px 0px;
}
.video-list{
  height: 100%;
  /*border: 1px solid red;*/
}

/*右侧---------------------------------------end*/

</style>