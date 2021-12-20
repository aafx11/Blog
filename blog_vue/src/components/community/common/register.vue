<template>
  <div class="formBody">
    <a-row>
      <a-col :span="24" style="width: 100vh;">
        <!--        <img src="src/assets/images/registBG.jpg" alt="加载失败" class="Image">-->
        <!--        <img src="../../../assets/images/registBG.jpg" alt="加载失败" class="Image">-->
      </a-col>
    </a-row>
    <a-row class="row-container">
      <a-col :lg="{span:24}" class="Form">
        <div class="container-box">
          <a-form
              ref="formRef"
              :model="registerForm"
              :rules="rules"
              class="registerForm"
              @finish="handleFinish"
              @finishFailed="handleFinishFailed"
          >
            <a-form-item>
              <div class="title">
                <span>
                注册
                </span>
              </div>
            </a-form-item>
            <a-form-item name="username">
              <a-input v-model:value="registerForm.username" size="large" placeholder="用户名"></a-input>
            </a-form-item>
            <a-form-item name="nickname">
              <a-input v-model:value="registerForm.nickname" size="large" placeholder="昵称"></a-input>
            </a-form-item>
            <a-form-item name="password">
              <a-input-password v-model:value="registerForm.password" size="large" placeholder="密码" class="password"></a-input-password>
            </a-form-item>
            <a-form-item name="email">
              <a-input v-model:value="registerForm.email" size="large" placeholder="邮箱地址"></a-input>
            </a-form-item>
            <a-form-item name="code">
              <a-input v-model:value="registerForm.code" size="large" placeholder="请输入验证码" class="codeInput"></a-input>
              <img :src="codeImgUrl" @click="getCodeImg" alt="加载失败" style="float: right; ">
            </a-form-item>
            <a-form-item>
              <a-button type="primary" size="large" block @click="submitForm" class="register-button">注册</a-button>
            </a-form-item>
            <a-form-item>
              <a-button @click="router.push('/index')">返回首页</a-button>
              <a-button>忘记密码</a-button>
              <a-button @click="router.push('/login')">立即登录</a-button>
            </a-form-item>
          </a-form>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {defineComponent, onMounted, reactive, ref, toRaw, getCurrentInstance} from 'vue';
import {VerificationCode, register} from '../../../request/api/common.js';
import {useRouter} from "vue-router"
import {ElMessage} from 'element-plus';

export default defineComponent({
  name: "register",
  setup() {
    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const formRef = ref();
    const registerForm = reactive({
      username: '',
      nickname: '',
      password: '',
      email: '',
      code: '',
      token: '',
    })

    let isEmail = async (rule, value) => {
      const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
      if (value == '' || value == undefined || value == null) {
        return Promise.reject('请输入邮箱');
      } else {
        if (!reg.test(value)) {
          return Promise.reject('请输入正确的邮箱');
        } else {
          return Promise.resolve();
        }
      }
    }
    const rules = {
      username: [
        {required: true, message: '请输入用户名', trigger: 'blur',},
        {min: 4, max: 10, message: '用户名位数在4到10位', trigger: 'blur',},
      ],
      nickname: [
        {required: true, message: '请输入昵称', trigger: 'blur',},
        {min: 2, max: 8, message: '昵称位数在2到6位', trigger: 'blur',},
      ],
      password: [
        {required: true, message: '请输入密码', trigger: 'blur',},
        {min: 5, max: 10, message: '当前密码太简单了，请输入5到10位密码', trigger: 'blur',},
      ],
      email: [{required: true, validator: isEmail, trigger: 'change',}],
      code: [
        {required: true, message: '请输入验证码', trigger: 'blur',},
        {min: 5, max: 5, message: '验证码错误', trigger: 'blur',},
      ],
    }


    const handleFinish = values => {
      console.log(values, registerForm);
    };

    const handleFinishFailed = errors => {
      console.log(errors);
    };

    let code = ref('')
    let token = ref('')
    //用户注册
    const userRegister = () => {
      console.log("code", code.value);
      console.log("token", token.value);
      register(proxy.registerForm).then(res => {

        ElMessage.success({
          showClose: true,
          message: "注册成功",
          type: 'success',
          onClose: () => {
            proxy.registerForm = ''
            router.push('/login')
          }
        })

      })
    }

    //提交注册表单
    const submitForm = () => {
      formRef.value.validate().then(() => {
        console.log("登录");
        userRegister();
      })
          .catch(error => {
            console.log('error', error);
          })
    }

    //获取验证码
    let codeImgUrl = ref('');
    const getCodeImg = () => {
      VerificationCode().then(res => {
        registerForm.token = res.data.data.token;
        codeImgUrl.value = res.data.data.codeImg;
        registerForm.code = '';
      })
    }
    onMounted(
        () => {
          getCodeImg();
        }
    )
    return {
      router,
      formRef,
      registerForm,
      rules,
      codeImgUrl,
      code,
      token,
      isEmail,
      handleFinish,
      handleFinishFailed,
      userRegister,
      submitForm,
      getCodeImg,
    }
  }

})
</script>

<style scoped>
.formBody {
  height: 100%;
  width: 100%;
  background-color: #fff;
}

.Image {
  position: relative;
  z-index: 0;
  height: 100%;
  width: 100%;
}
.row-container{
  width: 100%;
  height: 100%;
}
/*注册表单---------------start*/
.registerForm {
  margin: 0 20px;
}

.codeInput {
  width: 60%;
  float: left;
}

.Form {
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: 50px;
}

.container-box {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgb(235, 235, 235);
  border: 1px solid #cbc9c9;
  box-shadow: 1px 2px 5px rgba(0,0,0,.31);

  border-radius: 5px;
}
.title{
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  font-weight: 600;
}
input,.password{
  border-radius: 10px;
}
.register-button{
  border-radius: 7px;
}
/*注册表单---------------end*/
</style>