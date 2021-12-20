<template>
  <a-row class="row">
    <a-col :lg="{span:2,offset:5}" :md="{span:5}" :xs="{span:0}" class="left">
      <div>
        <div class="title">
          <span class="title_font">个人主页</span>
        </div>
        <a-menu
            mode="inline"

        >
          <a-menu-item key="myHomePage">
            <UserOutlined/>
            <span class="nav-text">
              <router-link to="/myHomePage">个人主页</router-link>
            </span>
          </a-menu-item>
          <a-menu-item key="myAttention">
            <UserSwitchOutlined/>
            <span class="nav-text">
              <router-link to="/myAttention">我的关注</router-link>
            </span>
          </a-menu-item>
          <a-menu-item key="myFans">
            <UsergroupAddOutlined/>
            <span class="nav-text">
              <router-link to="/myFanList">我的粉丝</router-link>
            </span>
          </a-menu-item>
          <a-menu-item key="editProfile">
            <SettingOutlined />
            <span class="nav-text">
              <router-link to="/editProfile">编辑资料</router-link>
            </span>
          </a-menu-item>
        </a-menu>
      </div>
    </a-col>
    <a-col :lg="{span:8}" :md="{span:17}" :xs="{span:24}" class="center">
      <router-view></router-view>
    </a-col>
    <a-col :lg="{span:4}" :md="{span:0}" :xs="{span:0}" class="right">
      <!--个人简介卡片-->
      <ProfileCard
          :userInfoData="userInfoData"
          :articleListData="articleListData"
          ref="RefChildren"
          v-if="showUserInfo && showArticle"

      >

      </ProfileCard>
    </a-col>
  </a-row>
</template>

<script>
import {defineComponent, getCurrentInstance, onMounted, reactive, ref, onBeforeUpdate} from 'vue';
import {
  UserOutlined,
  UserSwitchOutlined,
  UsergroupAddOutlined,
  PlusOutlined,
  WechatOutlined,
  WeiboOutlined,
  GithubOutlined,
  QqOutlined,
  SettingOutlined
} from '@ant-design/icons-vue';
import {getUserInfo} from '../../../request/api/userList.js'
import {follow} from "../../../request/api/follow";
import {getPersonalArticle} from '../../../request/api/article.js'
import {test} from '../../../assets/js/iconStyleSwitch.js'
import message from "ant-design-vue";
import ProfileCard from './ProfileCard.vue'
export default defineComponent({
  name: "personalCenter",
  provide() {
    return {
      switchShow: this.getUserInfoData
    }
  },
  components: {
    UserOutlined,
    UserSwitchOutlined,
    UsergroupAddOutlined,
    PlusOutlined,
    WechatOutlined,
    WeiboOutlined,
    GithubOutlined,
    QqOutlined,
    SettingOutlined,
    ProfileCard:ProfileCard,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const card_title = reactive({
      "padding-left": "10px",
      // "height":"36px",
      "font-weight": '700',
      "font-size": '18px',
    })
    const card_body = reactive({
      "padding": "5px 10px",
      "height": "98px",
      "font-size": '16px',
    })


    const show = ref(false)
    const userInfoData = reactive();
    const showUserInfo = ref(false)
    const getUserInfoData = () => {
      getUserInfo().then(res => {
        console.log("重新获取");
        proxy.userInfoData = res.data.data;
        proxy.$forceUpdate();
        // proxy.userInfoShow();
        showUserInfo.value = false;
        showUserInfo.value = true;
        console.log("用户个人信息", proxy.userInfoData);
      })
    }
    const count = ref(false)
    const articleListData = reactive();

    const showArticle = ref(false)
    const  getArticleListData =  () => {
      getPersonalArticle().then(res => {
        proxy.articleListData = res.data.data;
        console.log("文章总数", proxy.articleListData);
        // articleTotal.value = res.total;
        // proxy.articleDataShow();
        showArticle.value = false
        showArticle.value = true
        proxy.$forceUpdate();
        console.log("文章信息", proxy.articleListData);
      })
    }

    const RefChildren = ref();
    // const userInfoShow= () => {
    //   RefChildren.value.UserInfoShow();
    // }
    // const articleDataShow = () => {
    //   RefChildren.value.ArticleDataShow();
    // }





    onMounted(() => {
      getArticleListData();
      getUserInfoData();
      // iconSwitch();
    })

    return {
      card_title,
      card_body,
      userInfoData,
      show,
      articleListData,
      count,
      RefChildren,
      showArticle,
      showUserInfo,
      getUserInfoData,
      getArticleListData,
      // userInfoShow,
      // articleDataShow,
    }
  },
})
</script>

<style scoped>
/*整体布局------------------------------------start*/
.row {
  margin-top: 7px;
  height: 100%;
}

.left {
  /*border: 1px solid red;*/
  margin-right: 10px;
  min-width: 40px !important;
  background-color: rgb(255, 255, 255);
}

.center {
  /*border: 1px solid black;*/
  margin-right: 10px;
  /*background-color: rgb(255, 255, 255);*/
}

.right {
  /*border: 1px solid cornflowerblue;*/
  /*background-color: rgb(255, 255, 255);*/
}

/*整体布局------------------------------------end*/

/*左侧导航------------------------------------start*/
.title {
  margin: 10px 0;
}

.title_font {
  padding: 10px 16px;
  font-weight: 500;
  font-size: 1.4rem;
}

/*左侧导航------------------------------------end*/

::v-deep(.ant-card-head-title) {
  padding: 0;
}

.notice {
  /*border: 1px dashed gray;*/
  margin-bottom: 5px;
}

.nav-text{
  font-size: 0.9rem;
}



</style>