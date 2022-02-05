<template>
  <div class="login-container">
    <div class="form-container">
      <div class="form">
        <el-form ref="formRef" :model="form"  :rules="rules">
          <el-form-item label="账号" required prop="username">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" required prop="password">
            <el-input v-model="form.password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="验证码" required class="code-container">
            <el-input v-model="form.code" class="input"></el-input>
            <img :src="codeUrl" alt="加载失败" @click="getCodeUrl" class="img">
          </el-form-item>
        </el-form>
        <div class="control-container">
          <my-button @click="reset">重置</my-button>
          <my-button type="primary" @click="submitLoginForm">登录</my-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {onMounted, reactive, ref} from "vue";
import {useRoute, useRouter} from "vue-router"
import {useStore} from 'vuex'
import {VerificationCode, login} from '../../../request/api/common.js'
import {getOneUserInfo, getUserInfoById} from '../../../request/api/userList.js'
import {UserLogout} from '../../../request/api/common.js'
import myButton from '../../../view/myButton.vue'
import {message} from "ant-design-vue";

export default {
  name: "login",
  components: {
    myButton
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const route = useRoute();

    let form = reactive({
      username: '',
      password: '',
      code: '',
      token: ''
    })

    // 获取验证码
    const codeUrl = ref('')
    const getCodeUrl = () => {
      VerificationCode().then(res => {
        form.token = res.data.data.token
        codeUrl.value = res.data.data.codeImg
        form.code = ''
      })
    }
    onMounted(() => {
      getCodeUrl()
    })

    // 重置
    const reset = () => {
      form.username = ''
      form.password = ''
      form.code = ''
    }

    // 退出
    const logout = () => {
      UserLogout().then((res) => {
        localStorage.clear();
        sessionStorage.clear();
        store.commit("resetState");
        proxy.$router.push('/login');
      })
    }

    const formRef = ref()

    // 登录
    const submitLoginForm = () => {
      formRef.value.validate(res=>{
        if (!res){
          console.log('不通过');
          return;
        }else {
          if (form.code.length != 5) {
            message.warn("请输入正确的验证码");
            getCodeUrl();
            return;
          }
          login(form).then(res => {
            let jwt = ''
            jwt = res.headers['authorization']
            store.commit('setToken', jwt)
            return res.data.msg
            // router.push('/myHomePage')
          }, reason => {
            getCodeUrl();
          }).then(success => {
            console.log(success);
            if (success === '登录成功') {
              getOneUserInfo().then(user => {

                getUserInfoById(user.data.data.id).then(role => {

                  let data = role.data.data;
                  if (data.state != 1) {
                    message.warn("该账号已被封禁");
                    logout()
                  }
                  let admin = data.roles.some((item) => {
                    console.log(item.roleName);
                    return item.roleName === "超级管理员"
                  })
                  if (admin) {
                    router.push('/welcome')
                    return;
                  }

                  let normal = data.roles.some((item) => {
                    return item.roleName === "普通用户"
                  })
                  if (normal) {
                    router.push('/index')
                    return;
                  }
                })
              })
            }
          })
        }
      })


    }

    const rules = reactive({
      username: [
        {
          required: true,
          message: '账号不能为空',
          trigger: 'blur',
        }

      ],
      password: [
        {
          required: true,
          message: '密码不能为空',
          trigger: 'blur',
        }

      ],
    })

    return {
      form,
      codeUrl,
      rules,
      formRef,
      getCodeUrl,
      reset,
      submitLoginForm
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .form-container {
    width: 600px;
    height: 400px;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 5px 10px #cccc;
    display: flex;
    justify-content: center;
    align-items: center;

    .form {
      width: 50%;
      display: flex;
      flex-direction: column;
      justify-content: center;

      .code-container {
        display: flex;

        .input {
          width: 50%;

        }

        .img {
          width: 45%;
          margin-left: 10px;
          border: 1px solid #666;
        }
      }

      .control-container {
        display: flex;
        justify-content: flex-end;

        & > button {
          margin-right: 10px;
        }
      }
    }
  }
}
</style>