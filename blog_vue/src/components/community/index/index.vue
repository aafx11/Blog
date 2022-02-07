<template>
  <a-row class="row">
    <a-col :span="24" class="col-cover">
      <div class="cover-container">
        <h2>Learning makes you
        <div class="mask">
          <span data-show>stronger.</span>
          <span>happier.</span>
          <span>more focused.</span>
          <span>more leisurely.</span>
        </div>
        </h2>
      </div>
    </a-col>
    <a-col :lg="{span:2,offset:4}" :md="{span:5}" :xs="{span:0}" class="left">
      <div class="title">
        <span class="title_font">首页</span>
      </div>
      <a-menu
          mode="inline"
          v-model:selectedKeys="selectedKeys"
      >
        <a-menu-item key="latestArticles">

          <router-link to="/latestArticles" class="a-menu-item">
            <span ><CloudOutlined class="icon-style"/></span>
            <span class="nav-text">最新文章</span>
          </router-link>
        </a-menu-item>
        <a-menu-item key="popularArticles">

          <router-link to="/popularArticles">
            <span><FireOutlined class="icon-style"/></span>
            <span class="nav-text">7天热门</span>
          </router-link>

        </a-menu-item>
        <a-menu-item key="followArticles" @click="pushFollowPage">
                <span><ExceptionOutlined class="icon-style"/></span>
                <span class="nav-text">全部关注</span>
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col :lg="{span:10}" :md="{span:18}" :xs="{span:24}" class="center">
      <router-view></router-view>
    </a-col>
    <a-col :lg="{span:3}" :md="{span:0}" :xs="{span:0}" class="right">
      <!--用户简介-->
<!--      <ProfileCard-->
<!--          v-if="isAuth('hasLogin') && showUserInfo && showArticle"-->
<!--          ref="RefChild"-->
<!--          :userInfoData="userInfoData"-->
<!--          :articleListData="articleListData"-->
<!--      ></ProfileCard>-->
      <!--发布文章-->
<!--      <div-->
<!--          class="editArticle"-->
<!--          v-if="isAuth('hasLogin')"-->
<!--      >-->
<!--        <router-link to="/editArticle">-->
<!--          <a-button type="primary" shape="round" size="large" class="button-style">-->
<!--            <template #icon>-->
<!--              <EditOutlined/>-->
<!--              发表文章-->
<!--            </template>-->
<!--          </a-button>-->
<!--        </router-link>-->
<!--      </div>-->
      <!--登录表单-->
      <login-form v-if="!isAuth('hasLogin')"></login-form>
      <!--推荐用户-->
<!--      <div class="random-user-box">-->
<!--        <div class="title">推荐用户</div>-->
<!--        <div class="user-list" v-if="showUser">-->
<!--          <div class="user-item" @click="pushUserPage(item.username,item.id)" v-for="(item,index) in userData">-->
<!--            <UserCard :user-info="item" :mode="1"></UserCard>-->
<!--          </div>-->
<!--        </div>-->
<!--      </div>-->
      <!--作者榜-->
      <div class="author-list">
        <div class="list-header">
          <svg class="title icon" aria-hidden="true" >
            <use xlink:href="#icon-jiangbei"></use>
          </svg>
          <span class="title">作者榜</span>
        </div>
        <div class="list" v-if="showUser">
          <div class="item" v-for="(item,index) in userData" @click="pushUserPage(item.username,item.id)">
            <div class="item-img">
              <img class="avatar" :src="'http://localhost:8081/static/avatar/'+item.avatar">
            </div>
            <div class="left-box">
              <div class="left-nickname">
                <div class="name">{{item.nickname}}</div>
<!--                < div class="score-tag" v-if="item.score<200">Lv1</div>-->
                <a-tag color="success" v-if="item.score<200" class="score-tag">Lv1</a-tag>
                <a-tag color="processing" v-else class="score-tag">Lv2</a-tag>

              </div>
              <div class="left-briefintroduction">{{item.briefintroduction}}</div>
            </div>
          </div>
        </div>
      </div>
    </a-col>

  </a-row>
<!--<div class="main-container">-->
<!--  <div class="navigation"></div>-->
<!--  <div class="content-container">-->
<!--    <div class="content">-->
<!--      -->
<!--    </div>-->
<!--    <div class="aside"></div>-->
<!--  </div>-->
<!--</div>-->
</template>

<script>
import {onMounted, reactive, getCurrentInstance, ref} from "vue";
import {useRouter,onBeforeRouteLeave} from "vue-router"
import {useStore} from 'vuex'
import {getNotices} from "../../../request/api/information.js"
import {hasAuth,judgeIdentity} from '../../../assets/js/global.js'
import {VerificationCode, login} from "../../../request/api/common.js"
import {FireOutlined, CloudOutlined, ExceptionOutlined, EditOutlined} from '@ant-design/icons-vue';
import {getUserInfo,getRandomUsers} from '../../../request/api/userList.js'
import {getPersonalArticle} from '../../../request/api/article.js'
import ProfileCard from '../personalCenter/ProfileCard.vue'
import loginForm from '../common/loginForm.vue'
import UserCard from '../personalCenter/UserCard.vue'
import {message} from "ant-design-vue";
export default {
  name: "index",
  components: {
    FireOutlined,
    CloudOutlined,
    ExceptionOutlined,
    EditOutlined,
    ProfileCard: ProfileCard,
    loginForm: loginForm,
    UserCard:UserCard,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore()
    const router = useRouter()
    const loginFormRef = ref()
    const selectedKeys = ref(['myHomePage']);
    const card_title = reactive({
      "padding-left": "10px",

      "font-weight": '700',
      "font-size": '18px',
    })
    const card_body = reactive({
      "padding": "5px 10px",
      // "height": "98px",
      "font-size": '16px',
    })

    const rule = {
      code: [
        {required: true, message: '请输入验证码', trigger: 'blur',},
        {min: 5, max: 5, message: '验证码错误', trigger: 'blur',},
      ]
    }

    const form = reactive({
      username: '',
      password: '',
      code: '',
      token: ''
    })

    //提交登录表单
    const submitForm = () => {
      loginFormRef.value.validate().then(() => {
        userLogin();
      })
          .catch(error => {
            console.log('error', error);
          })
    }

    const jwt = ref();
    //用户登录
    const userLogin = () => {
      login(proxy.form).then(res => {
        jwt.value = res.headers['authorization']
        /*将token存入*/
        store.commit('setToken', jwt.value)
        router.push('/personal')
        //  router.push('/index')
      })
    }


    const isAuth = (data) => {
      return hasAuth(data);
    }

    let codeUrl = ref('')
    const getCode = () => {
      VerificationCode().then(res => {
        form.token = res.data.data.token;
        codeUrl.value = res.data.data.codeImg;
        form.code = '';
      })
    }

    const showUserInfo = ref(false)
    const userInfoData = reactive();
    const getUserInfoData = () => {
      getUserInfo().then(res => {

        proxy.userInfoData = res.data.data;
        proxy.$forceUpdate();
        showUserInfo.value = false;
        showUserInfo.value = true;

        console.log("用户个人信息", proxy.userInfoData);
      })
    }

    const articleListData = reactive();
    const showArticle = ref(false)
    const getArticleListData = () => {
      getPersonalArticle().then(res => {
        proxy.articleListData = res.data.data;
        // articleTotal.value = res.total;
        // proxy.articleShow();
        showArticle.value= false
        showArticle.value= true

        proxy.$forceUpdate();
      })
    }
    const RefChild = ref()
    // const userShow = () => {
    //   RefChild.value.UserInfoShow();
    // }
    // const articleShow = () => {
    //   RefChild.value.ArticleDataShow();
    // }

    let switchText = setInterval(function () {
        const show = document.querySelector('span[data-show]')
        const next = show.nextElementSibling || document.querySelector('.mask span:first-child')
        const up = document.querySelector('span[data-up]')
        if (up) {
          up.removeAttribute('data-up')
        }

        show.removeAttribute('data-show')
        show.setAttribute('data-up', '')

        next.setAttribute('data-show', '')
      }, 2000)

    const userCurrent = ref(1)
    const userSize = ref(5)
    const userData = reactive({
      userId:''
    })
    const  showUser = ref(false)
    const getRandomUserData = () => {
      getRandomUsers(userCurrent.value,userSize.value).then(res=>{
        const data = res.data.data;
        proxy.userData = data.records;
        proxy.userData.forEach(user=>{
          user.userId = user.id;
        })
        showUser.value = false;
        showUser.value = true;
        console.log("返回的用户",proxy.userData);

      })
    }
    const pushUserPage = (username, id) => {
      if (judgeIdentity(username)) {
        router.push({name: 'myHomePage'})
      } else {
        router.push({name: 'userInfo', params: {id: id}})
      }
    }

    const Auth = () => {
      let perm = 'hasLogin';
      const authority = proxy.$store.state.menus.permList;
      console.log("权限", authority);

      if (authority.indexOf(perm) > -1) {
        return true;
      } else {
        return false;
      }
    }

    const pushFollowPage = () => {
      let flag = Auth();
      console.log("判断",flag);
      if (flag){
        router.push({name:'followArticle'})
      } else {
        message.info("您尚未登录")
      }
    }
    onMounted(
        () => {
          getCode();
          getRandomUserData();
          if (isAuth('hasLogin')) {
            proxy.getUserInfoData();
            proxy.getArticleListData();
          }
        }
    )


    onBeforeRouteLeave(()=>{
      clearInterval(switchText)
    })
    return {
      selectedKeys,
      loginFormRef,
      card_title,
      card_body,
      rule,
      form,
      userData,
      codeUrl,
      RefChild,
      switchText,
      userInfoData,
      showUserInfo,
      showArticle,
      showUser,
      articleListData,
      submitForm,
      isAuth,
      getCode,
      // userShow,
      // articleShow,
      pushUserPage,
      getRandomUserData,
      getUserInfoData,
      getArticleListData,
      pushFollowPage,
    }
  },
  beforeUnmount(){
    clearInterval(this.switchText);
  }
}

</script>

<style lang="scss" scoped>
/*封面---------------------------------------start*/
:root{
  --offset:6px;
}
.col-cover{
  height: 350px;
  width: 100%;
  background-image: url("../../../assets/svg/undraw_absorbed_in_xahs.svg");
  background-size: 170% 100%;
  background-repeat: no-repeat;
}
.cover-container{
  z-index: 99;
  display: flex;
  /*justify-content: flex-end;*/
  border-bottom: 2px solid black;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-image: url("../../../assets/svg/undraw_reading_time_gvg0.svg");
  /*background-image: url("../../../assets/svg/undraw_reading_time_gvg0.png");*/
  background-repeat: no-repeat;
  background-size: contain;
}
@media screen and (max-width: 1530px){
  .col-cover{
    background-image: none;
  }
  .cover-container{
    background-image: none;
  }
}
.cover-container h2 {
  width: 980px;
  /*font-size: 100px;*/
  font-size: 5.4rem;
  font-family: Helvetica;
  line-height: 1.06;
  letter-spacing: -0.02em;
  color: #1d1d1f;
}
.mask{
  height: 106px;
  position: relative;
  overflow: hidden;
  margin-top: var(--offset);
}
.mask span{
  display: block;
  box-sizing: border-box;
  position: absolute;
  padding: 10px 0px 6px 0px;
  top: 100px;
  /*padding-bottom: var(--offset);*/

  background-size: 100% 100%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-repeat: no-repeat;
}
.mask span[data-show] {
  transform: translateY(-100%);
  transition: .5s transform ease-in-out;
}
.mask span[data-up] {
  transform: translateY(-200%);
  transition: .5s transform ease-in-out;
}
.mask span:nth-child(1) {
  background-image: linear-gradient(45deg, #0ecffe 50%, #07a6f1);
}

.mask span:nth-child(2) {
  background-image: linear-gradient(45deg, #18e198 50%, #0ec15d);
}

.mask span:nth-child(3) {
  background-image: linear-gradient(45deg, #8a7cfb 50%, #633e9c);
}
.mask span:nth-child(4) {
  background-image: linear-gradient(45deg, #fa7671 50%, #f45f7f);
}

/*封面---------------------------------------end*/
/*整体布局------------------------------------start*/
.row {
  margin-top: 7px;
  height: 1200px;
}

.left {
  /*border: 1px solid red;*/
  margin-right: 10px;
  background-color: rgb(255, 255, 255);
}

.center {
  /*border: 1px solid black;*/
  margin-right: 10px;
  background-color: rgb(255, 255, 255);
}

.right {
  /*border: 1px solid cornflowerblue;*/
  /*background-color: rgb(255, 255, 255);*/
  width: 100%;
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

.a-menu-item {
  /*display: flex;*/
  /*flex-direction: row;*/
}
.icon-style{
  font-size: 1.1rem;
}
.nav-text {
  margin-left: 20px;
  font-size: 1rem;
}

/*左侧导航------------------------------------end*/

/**/
::v-deep(.ant-card-head-title) {
  padding: 0;
}

/*登录表单*/
.loginForm {
  /*border: 1px dashed gray;*/
  margin-bottom: 5px;
}

/*发布文章*/
.editArticle {
  margin-top: 10px;
  width: 100%;
}

.button-style {
  width: 100%;
}

.codeInpuut {
  width: 60%;
  float: left;
}


/*封面----------------------------------------start*/
/*封面-----------------------------------------end*/



.random-user-box{
  width: 100%;
  height: 500px;

  background-color: #fff;
  .title{
    font-size: 1rem;
    font-weight: bolder;
    text-align: center;
  }
}
.user-list {
  height: 95%;
  .user-item {
    height: 92px;
    width: 100%;
    cursor: pointer;
    padding: 18px 35px 18px 5px;
    border-bottom: 1px solid rgb(249, 249, 249);

    &:hover {
      background-color: rgb(242, 242, 242);
    }
  }
}


// 新布局
//.main-container{
//  position: relative;
//  max-width: 960px;
//  width: 100%;
//  height: auto;
//  margin: 0 auto;
//  display: flex;
//  flex-direction: column;
//
//  .navigation{
//    position: sticky;
//    top: 5%;
//    max-width: 960px;
//    width: 100%;
//    height: 50px;
//    //background-color: red;
//    border: 1px solid black;
//    margin: 0 auto;
//    z-index: 100;
//  }
//
//  .content-container{
//    display: flex;
//  }
//
//  .content{
//    width: 700px;
//    min-height: 1920px;
//    background-color: #fff;
//    border-radius: 2px;
//  }
//  .aside{
//    position: sticky;
//    top: 5%;
//    min-width: 250px;
//    height: 200px;
//    background-color: #fff;
//    border-radius: 2px;
//    margin-left: 10px;
//  }
//}
//作者榜单
.author-list{
  height: 390px;
  width: 100%;
  background-color: white;
  .list-header{
    padding: 0 10px;
    border-bottom: 1px solid hsla(0,0%,59.2%,.1);;
    display: flex;
    align-items: center;
  }
  .list-header title{
    font-size: 18px;
  }
  .list{
    width: 100%;
    height: 100%;

    .item{
      width: 100%;
      padding: 12px 15px;
      display: flex;
      align-items: center;
      cursor: pointer;
      
      .item-img{
        width: 40px;
        height: 40px;
      }

       img{
        object-fit: cover;
        width: 40px;
        height: 40px;
        border-radius: 50%;

       }

      .left-box{
        width: 80%;
        padding-left: 5px;
        flex: 1;
        display: flex;
        flex-direction: column;

        .left-nickname,.left-briefintroduction{
          width: 100%;
          overflow:hidden;
          text-overflow:ellipsis;
          white-space:nowrap;
        }
        .left-nickname{
          width: 100%;
          display: flex;
          justify-content: space-between;

          .name{
            flex: 1;
            max-width: 100px;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .score-tag{
          }
        }
        .left-briefintroduction{
          color: #909090;
        }
      }
    }

    .item:hover{
      background-color: #f6f6f6;
    }

  }
}

.icon {
  width: 2em;
  height: 2em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>