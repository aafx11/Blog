<template>
  <div class="header-container">
        <div class="header-left">
          <div class="header-logo">
            <router-link :to="{name:'index'}">
              <span>Learning community</span>
            </router-link>
          </div>

          <div class="search-box">
    <!--        <a-input v-model:value="searchForm" placeholder="请输入相关的内容" class="search-input">-->
    <!--          <template #prefix>-->
    <!--            <SearchOutlined/>-->
    <!--          </template>-->
    <!--        </a-input>-->
            <my-selector @searchOptions="searchOptions" @setItemValue="setItemValue" class="search" placeholder="请输入相关的内容"></my-selector>
          </div>
          <div class="search-button">
    <!--        <a-button @click="pushSearchPage()">-->
    <!--          搜索-->
    <!--        </a-button>-->
            <my-button @click="pushSearchPage()" >搜索</my-button>
          </div>
        </div>
        <div class="header-right">
          <div class="nav-item">
            <span class="nav-item-title" @click="pushIndex">
              <HomeOutlined/>首页
            </span>
          </div>
          <div class="nav-item">
            <span class="nav-item-title" @click="pushIndex"><ReadOutlined/>文章<CaretDownOutlined/></span>
            <ul class="nav-item-ul">
              <li>
                <div class="li-div" @click="pushLatestArticle">最新文章</div>
              </li>
              <li>
                <div class="li-div" @click="pushHotArticle">热门文章</div>
              </li>
              <li>
                <div class="li-div" @click="pushFollowArticle">全部关注</div>
              </li>
            </ul>
          </div>
          <div class="nav-item">
            <span class="nav-item-title" @click="pushTagFile">
              <BarsOutlined/>文章分类
            </span>
          </div>
          <div class="nav-item">
            <span class="nav-item-title" @click="pushEdit">
              <EditOutlined/>发文章
            </span>
          </div>
          <div class="nav-item">
            <span class="nav-item-title" @click="pushRecommendedVideo">
              <VideoCameraOutlined/>视频<CaretDownOutlined/>
            </span>
            <ul class="nav-item-ul">
              <li>
                <div class="li-div" @click="pushRecommendedVideo">热门推荐</div>
              </li>
              <li>
                <div class="li-div" @click="pushVideoFollow">我的关注</div>
              </li>
              <li>
                <div class="li-div" @click="pushVideoFollow">我的收藏</div>
              </li>
              <li>
                <div class="li-div" @click="pushUploadVideo">上传视频</div>
              </li>
              <li>
                <div class="li-div" @click="pushManuscript">稿件管理</div>
              </li>
            </ul>
          </div>
          <div class="nav-item">
            <span class="nav-item-title" @click="pushPrivateLetter">
              <a-badge :count="unReadMessageCount">
                <MailOutlined style="font-size: 1rem" class="letter"/>
              </a-badge>
              私信
            </span>
          </div>
          <div class="nav-item" v-if="!isAuth('hasLogin')">
            <span class="nav-item-title" @click="pushLogin">
              <LoginOutlined/>登录
            </span>
            <span>/</span>
            <span class="nav-item-title" @click="pushRegister">
              注册
            </span>
          </div>
          <div class="nav-item" v-if="isAuth('hasLogin')">
            <span class="nav-item-title" @click="pushUserPage">
              <UserOutlined/>个人中心
            </span>
          </div>
          <div class="nav-item" v-if="isAuth('hasLogin')">
            <span class="nav-item-title" @click="pushUserPage">
              <a-avatar :src="'http://localhost:8081/static/avatar/'+userInfoData.avatar" v-if="showInfo" :size="38"></a-avatar>
    <!--          <a-avatar :src="'http://47.113.204.103:8081/static/avatar/'+userInfoData.avatar" v-if="showInfo"></a-avatar>-->
              <CaretDownOutlined/>
            </span>
            <ul class="nav-item-ul">
              <li>
                <div class="li-div">
                  <router-link :to="{name:'myHomePage'}">
                    个人主页
                  </router-link>
                </div>
              </li>
              <li>
                <div class="li-div">
                  <router-link :to="{name:'myAttention'}">
                    我的关注
                  </router-link>
                </div>
              </li>
              <li>
                <div class="li-div">
                  <router-link :to="{name:'myFanList'}">
                    我的粉丝
                  </router-link>
                </div>
              </li>
              <li>
                <div class="li-div">
                  <router-link :to="{name:'editProfile'}">
                    编辑资料
                  </router-link>
                </div>
              </li>
              <li>
                <div class="li-div" @click="logout">退出登录</div>
              </li>
            </ul>
          </div>
        </div>

  </div>
</template>

<script>
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';
import {useStore, mapGetters} from 'vuex'
import {hasAuth} from '../../../assets/js/global.js'
import {getUnReadMessageCount} from '../../../request/api/webSocket.js'
import {UserLogout} from '../../../request/api/common.js'
import {getUserInfo} from "../../../request/api/userList";
import {
  SearchOutlined, CaretDownOutlined,
  HomeOutlined, ReadOutlined,
  FireOutlined, BarsOutlined,
  MailOutlined, LoginOutlined,
  UserOutlined,
  AlertOutlined,
  EditOutlined,
  VideoCameraOutlined
} from '@ant-design/icons-vue';
import {message} from "ant-design-vue";
import {useRouter} from "vue-router";
import MyButton from "../../../view/myButton.vue";

export default defineComponent({
  name: "home_Nav",
  components: {
    MyButton,
    SearchOutlined, CaretDownOutlined,
    HomeOutlined, ReadOutlined,
    FireOutlined, BarsOutlined,
    MailOutlined, LoginOutlined,
    UserOutlined,
    AlertOutlined,
    EditOutlined,
    VideoCameraOutlined,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();
    const router = useRouter();
    const searchForm = ref('')
    const unReadMessageCount = ref()
    const getUnReadMessageCountData = () => {
      if (isAuth('hasLogin')) {
        getUnReadMessageCount().then(res => {
          const data = res.data.data;
          unReadMessageCount.value = data;
        })
      } else {
        return;
      }
    }

    const isAuth = (data) => {
      return hasAuth(data);
    }

    const pushSearchPage = () => {
      if (searchForm.value.split(" ").join("").length == 0) {
        message.warn("请输入需要搜索的内容");
        return;
      }
      router.push({name: 'searchPage', params: {content: searchForm.value}})

    }

    const logout = () => {
      UserLogout().then((res) => {
        localStorage.clear();
        sessionStorage.clear();
        store.commit("resetState");
        proxy.$router.push('/index');
      })
    }

    const pushIndex = () => {
      router.push({name: 'index'})
    }
    const pushLatestArticle = () => {
      router.push({name: 'latestArticle'})
    }
    const pushHotArticle = () => {
      router.push({name: 'hotArticle'})
    }
    const pushRecommendedVideo = () => {
      router.push({name: 'recommendedVideo'})
    }

    const flag = ref(false);
    const pushFollowArticle = () => {
      let perm = 'hasLogin'
      // const authority = store.getters.menuList;
      const authority = proxy.$store.state.menus.permList;
      console.log("权限", authority);
      if (authority.indexOf(perm) > -1) {
        flag.value = true;
      } else {
        flag.value = false;
      }

      if (flag.value) {
        router.push({name: 'followArticle'})
      } else {
        message.info("您尚未登录")
        return;
      }
    }
    //
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
    const pushUploadVideo = () => {
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
    const pushTagFile = () => {
      router.push({name: 'tagFile'})
    }

    const pushPrivateLetter = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'privateLetter'})

      } else {
        message.info("您尚未登录")
      }
    }
    const pushLogin = () => {
      router.push({name: 'login'})
    }

    const pushRegister = () => {
      router.push({name: 'register'})
    }

    const pushEdit = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'editArticle'})
      } else {
        message.info("您尚未登录")
      }
    }

    const pushUserPage = () => {
      router.push({name: 'myHomePage'})
    }
    const userInfoData = reactive()
    const showInfo = ref(false)
    const getUserInfoData = () => {
      getUserInfo().then(res => {
        const data = res.data.data;
        proxy.userInfoData = data;
        console.log("用户", res);
        showInfo.value = false;
        showInfo.value = true;
      })
    }

    const pushVideoFollow = () => {
      const flag = Auth();
      if (flag) {
        router.push({name: 'videoFollow'})
      } else {
        message.info("您尚未登录")
      }
      console.log('push-test')
      console.log('master')
      console.log('hot-fix')
      console.log('test')

    }

    // 输入框，输入监听
    const searchOptions = (value) => {
      console.log(value);
      searchForm.value = value
    }

    // 输入框菜单，获取选中的下拉菜单值
    const setItemValue = (item) => {
      console.log(item);
      searchForm.value = item.value
      pushSearchPage()
    }

    onMounted(() => {
      getUnReadMessageCountData();
      const flag = isAuth('hasLogin');
      if (flag) {
        getUserInfoData();
      }
    })


    return {
      flag,
      store,
      showInfo,
      searchForm,
      userInfoData,
      unReadMessageCount,
      logout,
      isAuth,
      pushEdit,
      pushIndex,
      pushLogin,
      pushTagFile,
      setItemValue,
      pushUserPage,
      pushRegister,
      searchOptions,
      pushSearchPage,
      pushManuscript,
      pushHotArticle,
      getUserInfoData,
      pushUploadVideo,
      pushPrivateLetter,
      pushVideoFollow,
      pushLatestArticle,
      pushFollowArticle,
      pushRecommendedVideo,
      getUnReadMessageCountData,
    };
  }

})
</script>

<style scoped>
.header-menu {
  position: fixed;
  z-index: 99;
  width: 100%;
  height: 53px;
  top: 0;
  background-color: white;
}

.font-size {
  font-size: 0.9rem;
}

.logo {
  font-size: 1.5rem;
}

a {
  color: black;
}

.header-container {
  position: fixed;
  z-index: 99;
  width: 100%;
  height: 45px;
  top: 0;
  background-color: #fff;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}


.header-left {
  /*padding: 0px 15px;*/
  width: 34%;
  display: flex;
  /*justify-content: flex-start;*/
  justify-content: space-around;
  align-items: center;


}

.header-logo {
  height: auto;
  /*margin-left: 2rem;*/
  font-size: 1.6rem;
  /*line-height: px;*/
  font-weight: 600;
  flex: 0 1 auto;
  padding-bottom: 8px;
  /*border: 1px solid red;*/
}



@media screen and (max-width: 768px) {
  .header-logo {
    font-size: 1rem;
    line-height: 15px;
  }
}

@media screen and (max-width: 769px) {
  .header-logo {
    margin-left: 0;
  }
}

.search-box {
  width: 27%;
}

.search {
  /*display: block;*/
  /*width: 100%;*/
}

.search-input {
  border: 1px solid black;
  border-radius: 10px;
}

.search-input:focus {
  border: 1px solid #00a1d6;

}

.search-button button {
  border-radius: 10px;
  border: 1px solid black;
  min-width: 60px;
}

.search-button button:hover {
  border: 1px solid #00a1d6;
}

.header-right {
  width: 40%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

@media screen and (max-width: 1550px) {
  .header-right {
    width: 50%;
  }
}

@media screen and (max-width: 1320px) {
  .header-right {
    width: 60%;
  }
}

@media screen and (max-width: 950px) {
  .header-right {
    width: 70%;
  }
}

ul {
  margin: 0;
}

.nav-item {
  position: relative;
  font-size: 1rem;
  height: 100%;
  display: flex;
  align-items: center;
}

@media screen and (max-width: 768px) {
  .nav-item {
    font-size: 0.8rem;
  }
}

.nav-item-title, .nav-item-ul li {
  cursor: pointer;
}

.nav-item-ul li {
  margin-bottom: 10px;
}

.nav-item-ul {
  display: none;
  position: absolute;
  right: 0;
  top: 40px;
  width: 120px;
  text-align: center;
  padding: 7px 5px;
  border-radius: 5px;
  background-color: rgb(255, 255, 255);
  box-shadow: 0 5px 40px rgba(0, 0, 0, 0.2);
}

.li-div:hover {
  background-color: rgb(242, 242, 242);
}

.nav-item:hover .nav-item-title, .nav-item:hover .letter {
  color: rgb(64, 147, 246);
}

.nav-item:hover .nav-item-ul {
  display: block;
}

/*.nav {*/
/*  flex: 1 0 auto;*/
/*  height: 100%;*/
/*}*/

/*.nav-list {*/
/*  display: flex;*/
/*  justify-content: space-between;*/
/*  align-items: center;*/
/*  height: 100%;*/
/*}*/

/*.main-nav-list {*/
/*  display: flex;*/
/*  height: 100%;*/
/*}*/


/*.phone-hide {*/
/*  display: flex;*/
/*  height: 100%;*/
/*}*/

/*.phone-hide li {*/
/*  font-size: 16px;*/
/*  cursor: pointer;*/
/*  margin: 0 10px;*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*}*/
</style>