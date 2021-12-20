<template>
  <div class="user-card-container"  v-if="stateChange" >
    <div class="user-card-left">
      <div class="avatar-box">
        <img :src="'http://localhost:8081/static/avatar/'+userInfo.avatar" >
<!--        <img :src="'http://47.113.204.103:8081/static/avatar/'+userInfo.avatar" >-->
      </div>
      <div class="info-box">
        <div class="nickname">{{userInfo.nickname}}</div>
        <div class="briefintroduction">{{userInfo.briefintroduction}}</div>
      </div>
    </div>
    <div class="user-card-right" v-if="mode==1">
      <div class="follow-button" v-if="isFollow" @click.stop="cancelAttention">
        已关注
      </div>
      <div class="unFollow-button" @click.stop="attention" v-else>
        +关注
      </div>
    </div>
  </div>
</template>

<script>
import {ref, onMounted, reactive, watch, getCurrentInstance} from "vue";
import {hasFollow,unFollow,follow} from "../../../request/api/follow";
import {message}  from "ant-design-vue";
import {judgeIdentity} from '../../../assets/js/global.js'
import {useRouter} from "vue-router";

export default {
  name: "UserCard",
  props: {
    userInfo: {
      type: Object,
      default: null
    },
    mode: {
      type: Number,
      default: null,
      request:false
    }
  },
  setup(){
    const {proxy} = getCurrentInstance();
    const router = useRouter()

    const stateChange = ref(false)

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
    //是否关注，初始话
    let isFollow = ref()
    const followInit = () =>{
      if (proxy.mode ==1 ){
        hasFollow(proxy.userInfo.userId).then(res=>{
          isFollow.value = res.data.data.isFollow;
          stateChange.value = false;
          stateChange.value = true;
        })
      } else return;
    }

    //取消关注
    const cancelAttention = () => {
      unFollow(proxy.userInfo.userId).then(res=>{
        message.success(res.data.msg)
        followInit();
      })
    }

    //关注用户
    const attention = () => {
      let login = Auth();
      // const token = localStorage.getItem("token");
      if (login) {
        follow(proxy.userInfo.userId).then(res => {
          message.success(res.data.msg)
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
      stateChange,
      attention,
      cancelAttention,
    }
  }
}
</script>

<style lang="scss" scoped>
@mixin bor($color){
  border: 1px solid $color;
}
.user-card-container{
  width: 100%;
  height: 100%;
  display: flex;
  .user-card-left{
    width: 90%;
    height: 100%;
    display: flex;
    flex-direction: row;
    justify-content: flex-start;

  }
  .user-card-right{
    width: 10%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .follow-button{
      text-align: center;
      border: 1px solid black;
      &:hover{
        background-color: rgb(222, 222, 222);
      }
    }
    .unFollow-button{
      text-align: center;
      color: #ff8200;
      border: 1px solid #ff8200;
      &:hover{
        background-color: rgb(243, 230, 217);
      }
    }
    .follow-button,.unFollow-button{
      width: 57px;
      min-width: 46px;
      border-radius: 2.25rem;

    }
  }
}

.user-card-left{
  .avatar-box{
    flex: 0 1 53px;
    img{
      height: 100%;
      width: 100%;
      min-width: 53px;
      border-radius: 100%;
      display: block;
    }
  }
  .info-box{
    display: flex;
    flex-direction: column;
    margin-left: 1rem;
    flex: 1 1 auto;

    .nickname{
      font-size: 0.7rem;
      font-weight: bolder;
    }
    .briefintroduction{
      font-size: 0.7rem;
      color: grey;
    }
  }
}


</style>