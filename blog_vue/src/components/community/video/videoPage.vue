<template>
  <a-row class="contain-box" :gutter="24">
    <a-col :lg="{span:3,offset:4}" :md="{span:5}" :xs="{span:0}" class="side-col">
      <div class="side-box">
        <div class="leftNav-title">
          视频
        </div>
        <div>
          <router-link :to="{name:'recommendedVideo'}">
            <div class="leftNav-item" tabindex="1" @click="activeIndex=1">
              <span class="iconfont icon-shipin icon"></span>
              <span class="item-font">热门推荐</span>
            </div>
          </router-link>
          <div class="leftNav-item " tabindex="1" @click="pushVideoFollow">
            <span class="iconfont icon-guanzhu icon"></span>
            <span class="item-font">我的关注</span>
          </div>
        </div>
        <div class="leftNav-title-two">
          我的
        </div>
        <div>
          <div class="leftNav-item" tabindex="1" @click="pushVideoUpload">
            <span class="iconfont icon-yunshangchuan_o icon"></span>
            <span class="item-font">上传视频</span>
          </div>
          <div class="leftNav-item " tabindex="1" id="manuscript" @click="pushManuscript">
            <span class="iconfont icon-gaojian icon"></span>
            <span class="item-font">稿件管理</span>
          </div>
        </div>
        <div class="leftNav-title-two">
          精选频道
        </div>
        <div class="channels-box"  id="channel-box" v-infinite-scroll="more">
          <div v-for="(item,index) in tagsData" class="channels-item" v-if="showTag">
            <router-link :to="{name:'videoTagsPage',params:{id:item.id,name:item.name}}">
              <div class="item-inner" @click="set()" :class="{active:activeIndex==item.name}">
                <span v-text="item.name" class="item-text"></span>
              </div>
            </router-link>
          </div>
        </div>
      </div>
    </a-col>
    <a-col :lg="{span:17}" :md="{span:19}" :xs="{span:24}" class="main-col">
      <router-view></router-view>
    </a-col>
  </a-row>

</template>

<script>
import videoUpload from './videoUpload.vue'
import RecommendedVideo from './RecommendedVideo.vue'
import loginForm from '../common/loginForm.vue'
import Manuscript from './Manuscript.vue'
import {computed, getCurrentInstance, onMounted, reactive, ref} from "vue";
import {hasAuth} from '../../../assets/js/global.js'
import {useRouter,useRoute} from "vue-router";
import {message} from "ant-design-vue";
import {getVideoTags} from '../../../request/api/video.js'

export default {
  name: "videoPage",
  components: {
    loginForm: loginForm,
    videoUpload: videoUpload,
    RecommendedVideo: RecommendedVideo,
    Manuscript: Manuscript,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const route = useRoute();
    const pageControl = reactive({})


    const styleSwitch = () => {
      let div = document.querySelectorAll('.leftNav-item');

      for (let i = 0; i < div.length; i++) {
        div[i].addEventListener('click', () => {
          for (let i = 0; i < div.length; i++) {
            div[i].style.color = 'black'
          }

          div[i].style.color = '#ff8200';
        })
      }
    }
    //权限判断
    const isAuth = (data) => {
      return hasAuth(data)
    }
    const changeIndexFun = (val) => {
      let manuscript = document.getElementById('manuscript');
      manuscript.click();
      // activeIndex.value = val;
    }
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

    const pushVideoFollow = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'videoFollow'})
      } else {
        message.info("您尚未登录")
      }
    }
    const pushVideoUpload = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'videoUpload'})
      } else {
        message.info("您尚未登录")
      }
    }

    const pushManuscript = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'manuscript'})
      } else {
        message.info("您尚未登录")
      }
    }
    const showTag = ref(false)
    const tagCurrent = ref(1)
    const tagSize = ref(15)
    const tagTotal = ref(50)
    const tagsData = reactive()
    const getVideoTagsData = () => {
      getVideoTags(tagCurrent.value, tagSize.value).then(res => {
        const data = res.data.data;
        console.log(data);
        proxy.tagsData = data.records;
        showTag.value = false;
        showTag.value = true;


      })
    }

    const more = () => {
      tagSize.value = tagSize.value +3
      getVideoTagsData();
    }
    // const activeIndex = ref()
    const set = () => {
      let name = route.params.name
      localStorage.setItem('activeIndex',name)
      // activeIndex.value  = localStorage.getItem('activeIndex')
    }
    const activeIndex = computed({
      get(){
        return localStorage.getItem('activeIndex')
      }
    })


    onMounted(() => {
      getVideoTagsData();
      proxy.$nextTick(() => {
        styleSwitch();
        set();

      })
    })
    return {
      showTag,
      tagsData,
      activeIndex,
      set,
      more,
      isAuth,
      styleSwitch,
      changeIndexFun,
      pushVideoFollow,
      pushManuscript,
      pushVideoUpload
    }
  }

}
</script>

<style scoped>
.contain-box {
  height: 100%;
  width: 100%;
}

/*左侧导航栏-------------------------------start*/
a {
  list-style: none;
  color: #0c0c0c;
}

.side-col {
  height: 100%;
  padding: 0 0;
  /*background-color: #fff;*/
}

.side-box {
  height: 100%;
  width: 10%;
  padding: 5px 0px;
  position: fixed;
  background-color: #fff;
  overflow-y: scroll;
}

.leftNav-title {
  font-size: 1.3rem;
  padding: 0px 15px;
}

.leftNav-item {
  margin-top: 10px;
  padding: 5px 15px;
}

.leftNav-item:hover {
  background-color: rgb(242, 242, 242);
}

.icon {
}

.leftNav-item span {
  font-size: 0.9rem;
}

.item-font {
  margin-left: 20px;
}

.leftNav-title-two {
  font-size: 1.3rem;
  padding: 0px 15px;
  margin-top: 25px;
}

.channels-box {
  height: 100%;
  /*background-color: blueviolet;*/
  display: flex;
  flex-direction: column;
  align-items: center;
}

.channels-item {
  width: 95%;
  height: 40px;
  border-radius: 5px;
}

.channels-item:hover {
  background-color: rgb(242, 242, 242);
}

.item-inner {
  height: 100%;
  width: 100%;
  padding: 8px 16px;
  font-size: 0.9rem;
}
.active{
  color: rgb(255, 130, 0);
}
/*左侧导航栏-------------------------------end*/


/*右侧主要内容-------------------------------start*/

.main-col {
  width: 100%;
  height: 100%;
}

.main-box {
  width: 100%;
  height: 100%;
}

.login-box {
  width: 100%;
  height: 100%;
  background-color: #fff;
}

.login-box .login-form {
  width: 300px;
  height: 400px;
  margin: 0 auto;
}

/*右侧主要内容-------------------------------end*/
</style>