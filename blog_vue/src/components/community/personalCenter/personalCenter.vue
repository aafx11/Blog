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
    <a-col :lg="{span:3}" :md="{span:0}" :xs="{span:0}" class="right">
      <!--个人简介卡片-->
<!--      <ProfileCard-->
<!--          :userInfoData="userInfoData"-->
<!--          :articleListData="articleListData"-->
<!--          ref="RefChildren"-->
<!--          v-if="showUserInfo && showArticle"-->

<!--      >-->

<!--      </ProfileCard>-->
      <div class="panel-wrapper"  v-if="showUserInfo">
        <userPanel :user-info="userInfoData"></userPanel>
      </div>
      <!--个人成就-->
      <div class="panel-wrapper achievement" v-if="showArticle">
        <div class="panel-title">
          <div class="panel-txt">个人成就</div>
        </div>
        <div class="content">
          <div class="item">
            <svg class="item-icon" aria-hidden="true" >
              <use xlink:href="#icon-tuijian"></use>
            </svg>
            <div class="txt">
              文章被点赞
              <span>{{articleListData.like}}</span>
            </div>
          </div>
          <div class="item">
            <svg class="item-icon" aria-hidden="true" >
              <use xlink:href="#icon-qiyewenhua"></use>
            </svg>
            <div class="txt">
              文章被收藏
              <span>{{articleListData.collect}}</span>
            </div>
          </div>
          <div class="item">
            <svg class="item-icon" aria-hidden="true" >
              <use xlink:href="#icon-liuchengguancha"></use>
            </svg>
            <div class="txt">
              文章被阅读
              <span>{{articleListData.view}}</span>
            </div>
          </div>
        </div>
      </div>
    <!--关注和粉丝数量-->
      <div class="panel-wrapper column" v-if="showUserInfo">
        <div class="count-item" @click="pushToFollow">
          <p>关注了</p>
          <p>{{userInfoData.followCount}}</p>
        </div>
        <div class="count-item" @click="pushToFans">
          <p>关注者</p>
          <p>{{userInfoData.fansCount}}</p>
        </div>
      </div>
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
import userPanel from '../view/userPanel.vue'
import router from "../../../router";

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
    userPanel,
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
        proxy.userInfoData = res.data.data;
        proxy.$forceUpdate();
        showUserInfo.value = false;
        showUserInfo.value = true;
        console.log("用户个人信息", proxy.userInfoData);
      })
    }
    const count = ref(false)
    const articleListData = reactive({});

    const showArticle = ref(false)
    const  getArticleListData =  () => {
      getPersonalArticle(1,50).then(res => {
        proxy.articleListData = res.data.data;
        proxy.articleListData.like = 0
        proxy.articleListData.view = 0
        proxy.articleListData.collect = 0
        for(let i of proxy.articleListData.records){
          console.log("文章",i);
          proxy.articleListData.like = proxy.articleListData.like + i.likeCount
          proxy.articleListData.view = proxy.articleListData.view + i.view
          proxy.articleListData.collect = proxy.articleListData.collect + i.collects

        }
        showArticle.value = false
        showArticle.value = true
        proxy.$forceUpdate();
        console.log("文章信息1", proxy.articleListData);
      })
    }

    const RefChildren = ref();
    // const userInfoShow= () => {
    //   RefChildren.value.UserInfoShow();
    // }
    // const articleDataShow = () => {
    //   RefChildren.value.ArticleDataShow();
    // }

    const pushToFollow = () =>{
      router.push('/myAttention')
    }

    const pushToFans = () =>{
      router.push('/myFanList')
    }

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
      pushToFollow,
      pushToFans
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

.panel-wrapper{
  width: 100%;
  min-height: 165px;
  background-color: #fff;
  border-radius: 2px;
  margin-bottom: 5px;

}

/*个人成就*/
.achievement{
  height: 190px;
  margin-top: 10px;
}
.panel-title{
  width: 100%;
  height: 50px;
  font-size: 18px;
  font-weight: 600;
  line-height: 50px;
  border-bottom: 1px solid rgb(228, 230, 235);;
}
.panel-txt{
  padding: 0 10px;
}
.item {
  padding: 0 12px 0 20px;
  margin-top: 15px;
  display: flex;
  align-items: center;
}
.item-icon{
  width: 2em;
  height: 2em;
  fill: currentColor;
  overflow: hidden;
}
.item .txt{
  margin-left: 8px;
}

/*关注和粉丝数量*/
p{
  margin: 0;
  padding: 0;
}
.column{
  display: flex;
  min-height: 100px!important;
}
.column .count-item:first-child{
  border-right: 1px solid rgb(228, 230, 235);;

}
.count-item{
  font-size: 18px;
  /*font-weight: 600;*/
  flex: 1;
  display: flex;
  flex-direction:column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.count-item:hover{
  color: #868686;
}
</style>