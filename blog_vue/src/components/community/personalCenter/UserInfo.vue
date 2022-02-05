<template>
  <a-row class="row">
    <a-col :lg="{span:2,offset:5}" :md="{span:5}" :xs="{span:0}" class="left">
      <div>
        <div class="title">
          <span class="title_font" v-if="showUserInfo">{{ userInfoData.nickname }}主页</span>
        </div>
        <a-menu
            mode="inline"

        >
          <a-menu-item key="myHomePage" @click="active=1">
            <UserOutlined/>
            <span class="nav-text">
              个人主页
            </span>
          </a-menu-item>
          <a-menu-item key="myAttention" @click="active=2">
            <UserSwitchOutlined/>
            <span class="nav-text">
              关注列表
            </span>
          </a-menu-item>
          <a-menu-item key="myFans" @click="active=3">
            <UsergroupAddOutlined/>
            <span class="nav-text">
             粉丝列表
            </span>
          </a-menu-item>
        </a-menu>
      </div>
    </a-col>
    <a-col :lg="{span:8}" :md="{span:17}" :xs="{span:24}" class="center">
      <div class="main-container" v-if="active==1" v-infinite-scroll="load">
        <div class="search">
          <a-input v-model:value="searchContent" placeholder="请输入与文章相关的内容"></a-input>
          <a-button @click="getArticleDataByLike" type="primary">搜索</a-button>
        </div>
        <ArticleList :articleListData="articleData" :is-order-user="true"
                     v-if="showArticle"></ArticleList>
      </div>
      <div class="main-container" v-if="active==2">
        <div class="user-list" v-if="followData">
          <div class="user-item" v-for="(item,index) in followData">
            <UserCard :user-info="item" :mode="1"></UserCard>
          </div>
        </div>
        <div class="pagination-box">
          <a-pagination
              show-size-changer
              v-model:current="followCurrent"
              v-model:pageSize="followSize"
              :total="followTotal"

          />
        </div>
      </div>
      <div class="main-container" v-if="active==3">
        <div class="user-list" v-if="fanData">
          <div class="user-item" v-for="(item,index) in fanData">
            <UserCard :user-info="item" :mode="1"></UserCard>
          </div>
        </div>
        <div class="pagination-box">
          <a-pagination
              show-size-changer
              v-model:current="fanCurrent"
              v-model:pageSize="fanSize"
              :total="fanTotal"

          />
        </div>
      </div>
    </a-col>
    <a-col :lg="{span:4}" :md="{span:0}" :xs="{span:1}" class="right">
      <!--个人简介卡片-->
      <ProfileCard
          :userInfoData="userInfoData"
          :articleListData="provideArticle"
          ref="RefChildren"
          v-if="showUserInfo"
      >

      </ProfileCard>
    </a-col>
  </a-row>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, toRaw, getCurrentInstance,watch} from 'vue';
import {useRouter, useRoute} from "vue-router"
import {getUserProfileVOById} from '../../../request/api/userList.js'
import {getArticleByUserId} from '../../../request/api/article.js'
import {getFollowListByUserId, getFansListByUserId} from '../../../request/api/follow.js'
import ProfileCard from './ProfileCard.vue'
import ArticleList from './ArticleList.vue'
import UserCard from './UserCard.vue'
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

export default {
  name: "UserInfo",
  components: {
    UserOutlined, PlusOutlined,
    UserSwitchOutlined, WechatOutlined,
    UsergroupAddOutlined, WeiboOutlined,
    GithubOutlined, QqOutlined,
    SettingOutlined,
    ProfileCard: ProfileCard,
    ArticleList: ArticleList,
    UserCard: UserCard,
  },
  setup() {
    const route = useRoute();
    const {proxy} = getCurrentInstance();
    const userId = ref()
    const active = ref(1)

    const RefChildren = ref();
    const userInfoShow = () => {
      RefChildren.value.UserInfoShow();
    }
    const articleDataShow = () => {
      RefChildren.value.ArticleDataShow();
    }
    const showUserInfo = ref(false)
    const userInfoData = reactive()
    const provideArticle = reactive()
    const getUserProfileData = () => {
      getUserProfileVOById(userId.value).then(res => {
        const data = res.data.data;
        proxy.userInfoData = data;
        showUserInfo.value = false;
        showUserInfo.value = true;

      }).then(() => {
        getArticleByUserId(current.value, size.value, "", userId.value).then(res => {
          const data = res.data.data;
          proxy.provideArticle = data;
          RefChildren.value.UserInfoShow();
          RefChildren.value.ArticleDataShow();
          proxy.$forceUpdate();
        })
      })
    }

    const showProfile = ref(false)
    const getArticleTotal = () => {
      getArticleByUserId(current.value, size.value, "", userId.value).then(res => {
        const data = res.data.data;
        proxy.provideArticle = data;
        showProfile.value = false;
        showProfile.value = true;
      })
    }


    const showArticle = ref(false)
    const searchContent = ref("")
    const current = ref(1)
    const size = ref(10)
    const articleData = reactive()
    const getArticleDataByUserId = () => {
      getArticleByUserId(current.value, size.value, searchContent.value, userId.value).then(res => {
        const data = res.data.data;
        proxy.articleData = data.records;
        showArticle.value = false;
        showArticle.value = true;
      })
    }

    const getArticleDataByLike = () => {
      current.value = 1;
      size.value = 10;
      getArticleDataByUserId();
    }

    const load = () => {
      size.value = size.value + 4
      getArticleDataByUserId();

    }
    const followCurrent = ref(1)
    const followSize = ref(10)
    const followTotal = ref(50)
    const followData = reactive()
    const getFollowData = () => {
      getFollowListByUserId(followCurrent.value, followSize.value, userId.value).then(res => {
        const data = res.data.data;
        proxy.followData = data.profileVOList;
        followTotal.value = data.total;
      })
    }

    watch(()=>followCurrent.value,()=>{
      getFollowData();
    })
    watch(()=>followSize.value,()=>{
      getFollowData();
    })

    const showFan = ref(false);
    const fanCurrent = ref(1)
    const fanSize = ref(10)
    const fanTotal = ref(50)
    const fanData = reactive()
    const getFansData = () => {
      getFansListByUserId(fanCurrent.value,fanSize.value,userId.value).then(res=>{
        proxy.fanData = res.data.data;

        showFan.value = false;
        showFan.value = true;
      })
    }

    onMounted(() => {
      userId.value = route.params.id
      getUserProfileData();
      getArticleDataByUserId();
      getFollowData();
      getFansData();
    })


    return {
      userId,
      active,
      showFan,
      showProfile,
      articleData,
      showArticle,
      showUserInfo,
      userInfoData,
      searchContent,
      provideArticle,
      RefChildren,
      followData,
      followCurrent,
      followSize,
      followTotal,
      fanSize,
      fanCurrent,
      fanData,
      fanTotal,
      load,
      userInfoShow,
      articleDataShow,
      getArticleDataByLike,
    }
  }
}
</script>

<style lang="scss" scoped>
/*整体布局------------------------------------start*/
.row {
  margin-top: 7px;
  height: 100%;
}

.left {
  margin-right: 10px;
  min-width: 40px !important;
  background-color: rgb(255, 255, 255);
}

.center {
  margin-right: 10px;
}

.right {

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
.main-container {
  width: 100%;
  height: 100%;
}

.user-list {
  height: 95%;

  .user-item {
    height: 92px;
    width: 100%;
    cursor: pointer;
    padding: 18px 20px;
    border-bottom: 1px solid rgb(249, 249, 249);

    &:hover {
      background-color: rgb(242, 242, 242);
    }
  }
}
.search {
  display: flex;
  background-color: #fff;
}

.search input {
  width: 50%;
  border-right: 0px;
  border-top: 1px solid black;
  border-left: 1px solid black;
  border-bottom: 1px solid black;
}
</style>