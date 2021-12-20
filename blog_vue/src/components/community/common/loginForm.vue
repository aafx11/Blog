<template>
  <div class="login-box">
    <div class="login-header">
      <div @click="selectUserNameForm" class="userForm" :class="{active: show==1}">
        <span class="iconfont icon-yonghu">账号登录</span>
      </div>
      <div @click="selectPhoneForm" class="userForm" :class="{active: show==2}">
        <span class="iconfont icon-shouji1">手机登录</span>
      </div>
    </div>
    <div class="login-form-username" v-if="show == 1">
      <a-input placeholder="用户名/账号" v-model:value="userNameForm.username" class="username">
        <template #prefix>
          <user-outlined type="user" />
        </template>
      </a-input>
      <a-input-password placeholder="请输入密码" v-model:value="userNameForm.password" class="username">
        <template #prefix>
          <UnlockOutlined/>
        </template>
      </a-input-password>
      <div class="code">
        <a-input v-model:value="userNameForm.code" placeholder="请输入验证码" class="codeInput"></a-input>
        <img :src="codeUrl" alt="加载失败" @click="getCodeUrl" class="codeImg">
      </div>
      <a-button class="login-button" @click="submitLoginForm(userNameForm)">登录</a-button>
      <router-link :to="{name:'register'}"  class="register-span">
        <span>立即注册!</span>
      </router-link>
    </div>
    <div class="login-form-phone" v-else-if="show == 2">

    </div>
  </div>
</template>

<script>
import {onMounted, reactive, ref} from "vue";
import {VerificationCode, login} from '../../../request/api/common.js'
import {UserOutlined,UnlockOutlined,} from '@ant-design/icons-vue';
import {message} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router"
import {useStore} from 'vuex'
export default {
  name: "login",
  components:{
    UserOutlined,
    UnlockOutlined
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const route = useRoute();
    const userNameForm = reactive({
      username: '',
      password: '',
      code: '',
      token: '',//验证码的token
    })

    const show = ref(1)
    const selectUserNameForm = () => {
      show.value = 1;
    }
    const selectPhoneForm = () => {
      show.value = 2;
    }

    const codeUrl = ref()
    const getCodeUrl = () => {
      VerificationCode().then(res => {
        userNameForm.token = res.data.data.token;
        codeUrl.value = res.data.data.codeImg;
        userNameForm.code = '';
      })
    }

    const jwt = ref();
    const submitLoginForm = (loginForm) => {
      if (loginForm.code.length != 5 ){
        console.log("boolean",route.meta.requireAuth);


        message.warn("请输入正确的验证码");
        getCodeUrl();
        return;
      }
      login(loginForm).then(res => {
        jwt.value = res.headers['authorization']
        /*将token存入*/
        store.commit('setToken', jwt.value)
        router.push('/myHomePage')
      })
    }
    onMounted(()=>{
      getCodeUrl();
    })
    return {
      show,
      userNameForm,
      codeUrl,
      selectUserNameForm,
      selectPhoneForm,
      getCodeUrl,
      submitLoginForm,
    }
  }

}
</script>

<style scoped>
.login-box {
  position: relative;
  width: 100%;
  height: 290px;
  background: #ffffff;
  margin: 0px auto 15px;
  border-bottom: 1px solid #c4c4c4;
  border-radius: 5px;
  box-shadow: 0 1px 5px rgba(0, 0, 0, 0.25);
}

.login-header {
  display: flex;
  flex-direction: row;
  justify-content:space-around;
  align-items:center;
  padding: 0px 10px;
  height: 23%;
}

.userForm{
  text-align: center;
  width: 50%;
  font-weight: 700;
  font-size: 0.9rem;
  border-bottom: 1px solid gray;
}

.active{
  border-bottom: 4px solid #fa7d3c;
}


.login-form-username {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 75%;
  padding: 0px 13px;
}
.username{
  height: 43px;
  width: 95%;
  margin-bottom: 15px;
  box-shadow: inset 0 0 0 2px black,
  inset 6px 6px 0 rgba(30, 144, 255, 0.2),
  3px 3px 0 rgba(30, 144, 255, 0.2);
}
.code{
  display: flex;
  justify-content: center;
  width: 100%;
  height: 43px;
}
.codeInput{
  /*height: 90%;*/
  height: 100%;
  width: 48%;
  margin-bottom: 15px;
  /*margin-right: 10px;*/
}
.codeImg{
  /*height: 90%;*/
  width: 48%;
}
.login-button{
  background-color: rgb(255, 129, 64);
  color: white;
  margin-top: 5px;
  width: 95%;
  height: 17%;
}
.login-button:hover{
  background-color: rgb(247, 103, 29);
}
.register-span{
  color:#eb7350;
  margin-left: 3%;
  align-self: flex-start;
}
.login-form-phone {
  height: 75%;
}
</style>