<template>
  <div class="author-info">
      <a-avatar :src="'http://localhost:8081/static/avatar/'+videoData.avatar" :size="52"  class="author-avatar"
                @click="pushAuthorPage(videoData.username,videoData.userId)"></a-avatar>

<!--    <a-avatar :src="'http://47.113.204.103:8081/static/avatar/'+videoData.avatar" :size="52"  class="author-avatar"-->
<!--              @click="pushAuthorPage(videoData.username,videoData.userId)"></a-avatar>-->
      <div class="author-info-right" v-if="showFollow">
        <div class="author-nickname" @click="pushAuthorPage(videoData.username,videoData.userId)">
          {{videoData.nickname}}
        </div>
        <div class="author-hasFollow" @click="unFollowHandle(videoData.userId)" v-if="isFollow">
          <span class="iconfont icon-31yiguanzhudianpu">已关注</span>
        </div>
        <div class="author-follow" @click="followHandle(videoData.userId)" v-else>
          <span class="iconfont icon-jiahao1">关注</span>
        </div>

      </div>
  </div>
</template>

<script>
import {judgeIdentity} from '../../../assets/js/global.js'
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {message} from "ant-design-vue";
import {hasFollow,follow,unFollow} from "../../../request/api/follow";
import {useRoute, useRouter} from "vue-router"
export default {
  name: "authorMinCard",
  props:{
    videoData:{
      type:Object,
      default:null
    }
  },
  setup(){
    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const pushAuthorPage = (username,id) => {
      let flag = judgeIdentity(username);
      if (flag) {
        router.push({name: 'myHomePage'});
      } else {
        router.push({name: 'userInfo', params: {id: id}});
      }
    }

    const isFollow = ref(false);
    const showFollow = ref(false);
    const followInit = () => {
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        hasFollow(proxy.videoData.userId).then(res => {
          isFollow.value = res.data.data.isFollow;
          showFollow.value=false;
          showFollow.value=true;
          console.log("关注",res);
          console.log("是否关注",isFollow.value);
        })
      }
    }
    //关注
    const followHandle = (followId) => {
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        follow(followId).then(res => {
          console.log("关注",res);
          message.success(res.data.msg)
          followInit();

        })
      }else {
        message.info("您尚未登录")
      }
    }

    const unFollowHandle = (followId) => {
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        unFollow(followId).then(res => {
          followInit();
        })
      }else {
        message.info("您尚未登录")
      }
    }

    onMounted(()=>{
      followInit();
    })
    return{
      isFollow,
      showFollow,
      followInit,
      followHandle,
      pushAuthorPage,
      unFollowHandle,
    }
  }
}
</script>

<style scoped>
.author-info{
  width: 100%;
  height: 100%;
  display: flex;
}
.author-avatar{
  cursor: pointer;
}
.author-info-right{
  display: inline-block;
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.author-nickname{
  color:#FB7299 ;
  font-size: 1rem;
  cursor: pointer;
}
.author-follow{
  box-sizing: border-box;
  padding: 0;
  line-height: 27px;
  height: 27px;
  border-radius: 2px;
  font-size: 0.8rem;
  cursor: pointer;
  width: 120px;
  background: #00b5e5;
  color: white;
  text-align: center;
}
.author-hasFollow{
  box-sizing: border-box;
  padding: 0;
  line-height: 27px;
  height: 27px;
  border-radius: 2px;
  font-size: 0.8rem;
  cursor: pointer;
  width: 120px;
  background: rgb(231, 231, 231);
  color: black;
  text-align: center;
}
</style>