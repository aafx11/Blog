<template>
  <div class="user-wrapper">
    <template v-if="type == '1'">
    <router-link :to="{name:'myHomePage'}">
    <div class="user-header">
      <img :src="'http://localhost:8081/static/avatar/' + userInfo.avatar " alt="" class="avatar-img">
      <div class="user-info">
        <div class="user-title">
          <div class="user-nickname">{{userInfo.nickname}}</div>
          <div class="user-tag">
            <a-tag color="success" v-if="userInfo.score<200" class="score-tag">Lv1</a-tag>
            <a-tag color="processing" v-else class="score-tag">Lv2</a-tag>
          </div>
        </div>
        <div class="user-introduction">{{userInfo.briefintroduction}}</div>
      </div>
    </div>
    </router-link>
    </template>
    <template v-if="type == '2'">
        <div class="user-header">
          <img :src="'http://localhost:8081/static/avatar/' + userInfo.avatar " alt="" class="avatar-img">
          <div class="user-info">
            <div class="user-title">
              <div class="user">
                <div class="user-nickname name">{{userInfo.nickname}}</div>
                <div class="user-tag">
                  <a-tag color="success" v-if="userInfo.score<200" class="score-tag">Lv1</a-tag>
                  <a-tag color="processing" v-else class="score-tag">Lv2</a-tag>
                </div>
              </div>
              <div class="follow-wrapper" v-if="state.show">
                <div v-if="!state.follow" @click="followHandle(userInfo.userId)">关注</div>
                <div v-else @click="unFollowHandle(userInfo.userId)">已关注</div>
              </div>
            </div>
          </div>
        </div>
    </template>
    <div class="item">
      <svg class="item-icon" aria-hidden="true" >
        <use xlink:href="#icon-suoyouwenzhang"></use>
      </svg>
      <div class="txt">
        文章数量
        <span>{{userInfo.articleCount}}</span>
      </div>
    </div>
    <div class="item">
      <svg class="item-icon" aria-hidden="true" >
        <use xlink:href="#icon-fensi2"></use>
      </svg>
      <div class="txt">
        粉丝数量
        <span>{{userInfo.fansCount}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import {onBeforeMount, reactive, ref, toRefs,getCurrentInstance} from "vue";
import {hasFollow,follow,unFollow} from "../../../request/api/follow.js";
import {message} from "ant-design-vue";

export default {
  name: "userPanel",
  props:{
    userInfo:{
      type:Object,
      require:true
    },
    type:{
      type:String,
      default:'1'
    }
  },
  setup(props){

    // 是否关注初始化
    const state = reactive({
        follow:false,
        show:false
  })
    const followInit = () => {
      hasFollow(props.userInfo.userId).then(res=>{
        console.log("是否关注1",res);
        state.follow = res.data.data.isFollow;
        state.show = false;
        state.show = true;
      })
    }
    onBeforeMount(()=>{
      followInit();
    })

    // 是否登录
    const {proxy} = getCurrentInstance();
    const Auth = () => {
      let perm = 'hasLogin';
      const authority = proxy.$store.state.menus.permList;
      console.log("权限", authority);

      if (authority.indexOf(perm) > -1) {
        return true
      } else {
        return false
      }
    }

    // 关注点击事件
    const followHandle = (followerId) => {
      let flag = Auth()
      if (flag) {
        follow(followerId).then(res => {
          console.log(res);
          followInit();
          message.success(res.data.msg)
        })
      } else {
        message.info("您尚未登录")
      }
    }
    // 取消关注处理
    const unFollowHandle = (followId) => {
      let flag = Auth()
      if (flag){
        unFollow(followId).then(res=> {
          followInit();
          message.info(res.data.msg)
        })
      } else {
        message.info("您尚未登录")
      }
    }

    return {
      state,
      followHandle,
      unFollowHandle
    }
  }
}
</script>

<style lang="scss" scoped>
a{
  color: black;
}
.user-wrapper{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}
.user-header{
  width: 100%;
  padding: 10px 15px;
  border-bottom: 1px solid rgb(228, 230, 235);
  display: flex;
  align-items: center;

  .avatar-img{
    width: 50px;
    height: 50px;
    border-radius: 50%;
    object-fit: cover;
  }

  .user-info{
    flex: 1;
    //padding-left: 10px;
    padding: 0 0px 0 11px;
    .user-title{
      display: flex;
      align-items: center;
      .user-nickname{
        font-size: 18px;
        flex: 1;
      }
      .user{
        display: flex;
        align-items: center;
      .name{
        margin-right: 10px;
      }
      }

    }

    .user-introduction{
      color: #909090;
    }
  }

  // 关注
  .follow-wrapper{
    width: 53px;
    height: 24px;
    border: 1px solid #6cbd45;
    color: #6cbd45;
    border-radius: 2px;
    text-align: center;
    margin: 0 0 0 auto;
    cursor: pointer;
  }
}

.item{
  padding: 0 12px 0 20px;
  margin-top: 15px;
  display: flex;
  align-items: center;


  .item-icon{
    width: 1.8em;
    height: 1.8em;
    //vertical-align: -0.15em;
    fill: currentColor;
    overflow: hidden;
  }
  .txt{
    margin-left: 8px;
  }
}
</style>