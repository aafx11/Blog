<template>
  <div class="video-min-list" v-infinite-scroll="load" v-if="showHotVideo">
    <div v-for="(item,index) in hotVideoData" class="video-min-card">
      <div class="video-card-left">
        <router-link :to="{name:'videoDetail',params:{id:item.id}}">
          <img :src="'http://localhost:8081/static/videoCover/'+item.coverUrl" alt="">
<!--          <img :src="'http://47.113.204.103:8081/static/videoCover/'+item.coverUrl" alt="">-->
        </router-link>
      </div>
      <div class="video-card-right">
        <div>
          <router-link :to="{name:'videoDetail',params:{id:item.id}}">
            <div class="video-card-title">{{ item.title }}</div>
          </router-link>
        </div>
        <div class="video-card-info" @click="pushAuthorPage(item.username,item.userId)">
          <span>{{ item.nickname }}</span>
        </div>
        <div class="video-card-info">
          <span>{{item.view}}播放</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getVideoOrderByView} from '../../../request/api/video.js'
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {message} from "ant-design-vue";
import {judgeIdentity} from '../../../assets/js/global.js'
import {useRouter} from "vue-router";

export default {
  name: "videoMinCard",
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
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

    const load = () => {
      hotSize.value = hotSize.value + 4;
      getHotVideoData();
    }

    const pushAuthorPage = (username,id) => {
      let flag = judgeIdentity(username);
      if (flag) {
        router.push({name: 'myHomePage'});
      } else {
        router.push({name: 'userInfo', params: {id: id}});
      }
    }

    onMounted(() => {
      getHotVideoData();
    })
    return {
      showHotVideo,
      hotVideoData,
      load,
      pushAuthorPage,
    }
  }
}
</script>

<style scoped>
a{
  color: black;
}
.video-min-list {
  height: 100%;
  overflow-y: scroll;
}

.video-min-list::-webkit-scrollbar {
  width: 0;
}

.video-min-card {
  width: 100%;
  height: 100px;
  margin-bottom: 15px;
}

.video-card-left {
  float: left;
  width: 45%;
  height: 100%;
  overflow: hidden;
}

.video-card-left img {
  width: 100%;
  height: 100%;
  border-radius: 5px;
  transition: all 0.3s ease-out;
  /* 过度 所有css属性 1秒过度 结束慢*/
  -moz-transition: all 0.3s ease-out;
  /* Firefox 4 */
  -webkit-transition: all 0.3s ease-out;
  /* Safari 和 Chrome */
  -o-transition: all 0.3s ease-out;
  /* Opera */
}
.video-card-left img:hover{
  width: 100%;
  height: 100%;
  border-radius: 5px;
  transform: scale(1.2, 1.2);
}

.video-card-right {
  padding-left: 10px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-around;
}
.video-card-title{
  line-height:18px;
  font-size: 0.9rem;
}
.video-card-title:hover{
  color: #00a1d6;
}
.video-card-info span{
  cursor: pointer;
  color: #999;
  font-size: 0.8rem;
}
</style>