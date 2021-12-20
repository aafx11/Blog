<template>
  <a-card
      style="width: 100%"
      :tab-list="tabListNoTitle"
      :active-tab-key="noTitleKey"
      @tabChange="key => onTabChange(key, 'noTitleKey')"
      :key="author.id"
      class="profile-card"
  >
    <div v-if="noTitleKey === 'profile'">
      <div class="profile-title">
        <img :src="'http://localhost:8081/static/avatar/'+author.avatar" class="avatar-border"/>
<!--        <img :src="'http://47.113.204.103:8081/static/avatar/'+author.avatar" class="avatar-border"/>-->
        <h1 v-text="author.nickname"></h1>
        <div class="article-count">
          <div>
            <p>文章</p>
            <p>{{ author.articleCount }}</p>
          </div>
          <div>
            <p>粉丝</p>
            <p>{{ author.fansCount }}</p>
          </div>
        </div>
        <div class="about-me">
          <p>{{ author.briefintroduction }}</p>
        </div>

        <a-button class="hasFollow"
                  @click="unFollowHandle(author.userId)"
                  v-if="isFollow"
        >
          <PlusOutlined class="icon"/>
          已关注
        </a-button>
        <a-button class="unFollow"
                  @click="followHandle(author.userId)"
                  v-else
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
import {onMounted, reactive, getCurrentInstance, ref} from "vue";
import {useStore} from 'vuex'
import {follow, unFollow, hasFollow} from "../../../request/api/follow.js";
import {message}  from "ant-design-vue";
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
  name: "author",
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
  inject:['alive'],
  props: {
    author: {
      type: Object,
      default: null
    }
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();
    const isFollow = ref(false)

    //关注处理器
    const followHandle = (followerId) => {
      // console.log("获取的token",store.getters.getToken);
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        follow(followerId).then(res => {
          message.success(res.data.msg)
          proxy.alive();

        })
      }else {
        message.info("您尚未登录")
      }
    }

    const unFollowHandle = (articleUserId) => {
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        unFollow(articleUserId).then(res => {
          proxy.alive();

          // proxy.$forceUpdate();
        })
      }else {
        message.info("您尚未登录")
      }
    }

    const followInit = () => {
      const token = localStorage.getItem("token");
      if (token != null && token !== '') {
        hasFollow(proxy.author.userId).then(res => {
          isFollow.value = res.data.data.isFollow;
          console.log("关注",res);
          console.log("是否关注",isFollow.value);
        })
      }
    }

    const tabListNoTitle = [
      {
        key: 'profile',
        tab: '个人简介',
      },
      {
        key: 'workExperience',
        tab: '工作经历',
      },
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

    onMounted(() => {
      followInit();
    })
    return {
      isFollow,
      tabListNoTitle,
      noTitleKey,
      key,
      onTabChange,
      followHandle,
      unFollowHandle,
      followInit,
    }
  }
}
</script>

<style scoped>
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
.hasFollow{
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
.unFollow:hover{
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

.avatar-info {
  text-align: center;
  border: 1px solid rgb(240, 240, 240);
}
</style>