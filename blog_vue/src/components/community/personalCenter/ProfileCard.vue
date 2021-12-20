<template>
  <a-card
      style="width: 100%"
      :tab-list="tabListNoTitle"
      :active-tab-key="noTitleKey"
      @tabChange="key => onTabChange(key, 'noTitleKey')"
      :key="userInfoData.id"
      class="profile-card"
      v-if="show"
  >
    <div v-if="noTitleKey === 'profile'">
      <div class="profile-title">
        <router-link to="/myHomePage">
        <img :src="'http://localhost:8081/static/avatar/'+userInfoData.avatar" class="avatar-border"/>
<!--        <img :src="'http://47.113.204.103:8081/static/avatar/'+userInfoData.avatar" class="avatar-border"/>-->
        </router-link>
        <span>{{userInfoData.nickname}}</span>
        <div class="article-count">
          <router-link to="/myHomePage">
          <div>
            <p>文章</p>
            <p v-if="count">{{ articleListData.total }}</p>
          </div>
          </router-link>
          <router-link to="/myFanList">
          <div>
            <p>粉丝</p>
            <p v-if="count">{{ userInfoData.fansCount }}</p>
          </div>
          </router-link>
        </div>
        <div class="about-me">
          <p>{{ userInfoData.briefintroduction }}</p>
        </div>
        <a-button class="unFollow" v-if="isFollow"
                  @click="unFollowHandle(userInfoData.userId)"
        >
          <PlusOutlined class="icon"/>
          已关注
        </a-button>
        <a-button class="unFollow" v-else
                  @click="followHandle(userInfoData.userId)"
        >
          <PlusOutlined class="icon"/>
          关注
        </a-button>


      </div>
      <div class="contact-icon">
        <a href="">
          <WechatOutlined class="icon-style"/>
        </a>
        <a href="">
          <WeiboOutlined class="icon-style"/>
        </a>
        <a href="">
          <GithubOutlined class="icon-style"/>
        </a>
        <a href="">
          <QqOutlined class="icon-style"/>
        </a>
      </div>
    </div>

<!--    <div v-else>project content</div>-->
  </a-card>
</template>

<script>
import {defineComponent, getCurrentInstance, onMounted, reactive, ref, onBeforeUpdate} from 'vue';
import {message} from "ant-design-vue";
import {hasFollow,follow,unFollow} from "../../../request/api/follow";
import {
  UserOutlined,
  UserSwitchOutlined,
  UsergroupAddOutlined,
  PlusOutlined,
  WechatOutlined,
  WeiboOutlined,
  GithubOutlined,
  QqOutlined
} from '@ant-design/icons-vue';

export default {
  name: "ProfileCard",
  components: {
    UserOutlined,
    UserSwitchOutlined,
    UsergroupAddOutlined,
    PlusOutlined,
    WechatOutlined,
    WeiboOutlined,
    GithubOutlined,
    QqOutlined,
  },
  props: {
    userInfoData: {
      type: Object,
      default: null
    },
    articleListData: {
      type: Object,
      default: null,
    },
  },
  setup() {
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

    //关注处理器
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
    const isFollow = ref()
    const followInit = () => {
      hasFollow(proxy.userInfoData.userId).then(res=>{
        console.log("是否关注",res);
        isFollow.value = res.data.data.isFollow;
        show.value = false;
        show.value = true;
      })
    }


    const tabListNoTitle = [
      {
        key: 'profile',
        tab: '个人简介',
      },
      // {
      //   key: 'workExperience',
      //   tab: '工作经历',
      // },
    ];

    const key = ref('profile');
    const noTitleKey = ref('profile');
    const onTabChange = (value, type) => {
      console.log(value, type);

      if (type === 'key') {
        key.value = value;
      } else if (type === 'noTitleKey') {
        noTitleKey.value = value;
      }
    };
    const buttonStyleSwitch = () => {
      const button = document.querySelector('.unFollow');
      button.style.background = '#3eaf7c'
    }

    const buttonStyleInit = () => {
      const button = document.querySelector('.unFollow');
      button.style.background = '#000000'
    }

    const show = ref(false);
    const count = ref(false);
    const UserInfoShow = () => {
      show.value = false;
      show.value = true;
    }
    const ArticleDataShow = () => {
      count.value = false;
      count.value = true;
    }
    onMounted(()=>{
      followInit();
    })
    onBeforeUpdate(() => {
      proxy.$nextTick().then(() => {
      })
    })
    return {
      key,
      show,
      count,
      isFollow,
      noTitleKey,
      tabListNoTitle,
      followInit,
      onTabChange,
      followHandle,
      UserInfoShow,
      buttonStyleInit,
      unFollowHandle,
      ArticleDataShow,
      buttonStyleSwitch,
    }
  }
}
</script>

<style  scoped>
/*个人简历*/
.profile-card {
  box-shadow: 0px 0px 22px 3px rgba(0, 0, 0, 0.18);
}

.profile-title h1 {
  display: inline-block;
}

.profile-title {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.unFollow {
  margin-bottom: 15px;
  float: right;
  height: 30px;
  background: rgb(51, 51, 51);
  border: none;
  color: white;
  padding: 0 27px;
  width: 80%;
  font-weight: bold;
  border-radius: 10px;
  line-height: 30px;

}

.hasFollow {
  margin-bottom: 15px;
  float: right;
  height: 30px;
  background-color: #3eaf7c;
  border: none;
  color: white;
  padding: 0 27px;
  width: 80%;
  font-weight: bold;
  border-radius: 10px;
  line-height: 30px;
}

.unFollow:hover {
  background-color: #3eaf7c;
}

.icon {
  margin-left: -10px;
  margin-right: 10px;
}

.icon-style {
  font-size: 28px;
  color: #0c0c0c;
}

.icon-style:hover {
  color: #3eaf7c;
}

.article-count {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-self: stretch;
  text-align: center;
}


.about-me {
  font-weight: 300;
  text-align: justify;
  color: #5a6f7c;
}

.contact-icon {
  display: flex;
  justify-content: space-around;
}

.avatar-border {
  text-align: center;
  width: 80px;
  height: 80px;
  line-height: 80px;
  border-radius: 50%;
}


.profile-title span:nth-child(2){
  font-size: 1.5rem;
}

.avatar-info {
  text-align: center;
  border: 1px solid rgb(240, 240, 240);
}
a{
  color: black;
  /*list-style: none;*/
}
</style>