<template>
  <div>
    <el-row type="flex" class="row-bg" justify="center">
      <!--左侧图片文字区域-->
      <el-col :xs="24" :md="6" :xl="6" :lg="7">
        <h2>欢迎来到BlogAdmin管理系统</h2>
        <el-image :src="src" style="height: 180px;  width: 180px;"></el-image>
        <p>请先登录喔</p>
        <p>请先登录喔</p>
      </el-col>

      <!--中间分割线-->
      <el-col :xs="0" :md="1" :xl="1" :lg="1">
        <el-divider direction="vertical"></el-divider>
      </el-col>


      <!--右侧登录表单区域-->
      <el-col :xs="24" :md="6" :xl="6" :lg="7">
        <!--v-model和ref不能同名,否则v-model会失效,v-model获取的是对象，只跟值有关，而ref获取到的是为了操作Dom对象，可以使用$refs来操作-->
        <el-form :model="loginForm" ref="submitForm" :rules="loginRules" label-width="100px" style="width: 100%;">
          <el-form-item label="账号" prop="username" style="width: 100%;">
            <el-input v-model="loginForm.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" style="width: 100%;">
            <el-input v-model="loginForm.password" type="password"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code" style="width: 100%;">
            <el-input v-model="loginForm.code" style="width: 70%; float: left"></el-input>
            <el-image class="codeImg" :src="codeImgUrl" @click="getCodeImg"></el-image>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitLoginForm">提交</el-button>
            <el-button @click="resetForm('Form')">重置</el-button> <!--这里填的是ref-->
<!--            <router-link to="/test">-->
              <el-button type="primary" @click="testButton">test</el-button>
<!--            </router-link>-->
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import {useRouter} from "vue-router"
import {useStore} from 'vuex'
import * as qs from "qs";
import {VerificationCode, login} from "../../../../request/api/common.js"
import service from "../../../../request/http.js";
import {articleLists} from '../../../../request/api/article.js'

const checkUsername = (rule, value, callback) => {
  if (value.length < 4 || value.length > 12) {
    return callback(new Error("请输入正确的用户名"));
  } else {
    callback();
  }
};
const checkPassword = (rule, value, callback) => {
  if (value.length < 6 || value.length > 12) {
    return callback(new Error("请输入正确的密码"));
  } else {
    callback();
  }
};
const checkCode = (rule, value, callback) => {
  if (value.length < 5 || value.length > 5) {
    return callback(new Error("请输入正确的验证码"));
  } else {
    callback();
  }
};
export default {
  name: "Login",
  setup() {
    const src = ref('../../src/assets/images/minTV.jpg')
    const store = useStore()
    const router = useRouter()
    const {proxy} = getCurrentInstance();

    //登录表单
    const loginForm = reactive({
      username: '',
      password: '',
      code: '',
      token: '',
    })

    //登录表单验证规则
    const loginRules = reactive({
      username: [{validator: checkUsername, trigger: "blur"}],
      password: [{validator: checkPassword, trigger: "blur"}],
      code: [{validator: checkCode, trigger: "blur"}],
    })


    // 提交登录表单
    const submitLoginForm = () => {
      proxy.$refs.submitForm.validate((valid) => {
        if (valid) {
          userLogin();
        } else {
          console.log('登录失败');
          return false;
        }
      });
    };

    const jwt = ref();
    //用户登录
    const userLogin = () => {
      login(proxy.loginForm).then(res => {
        jwt.value = res.headers['authorization']
        /*将token存入*/
        store.commit('setToken', jwt.value)
        router.push('/index')
      })
    }

    //表单重置
    const resetForm = (Form) => {
      proxy.$refs[Form].resetFields();
    }

    //验证码图片路径
    const codeImgUrl = ref('');
    const getCodeImg = () => {
      VerificationCode().then(res => {
        loginForm.token = res.data.data.token;
        codeImgUrl.value = res.data.data.codeImg;
        loginForm.code = '';
      })
    }

    const testButton = () => {
      articleLists().then((res) => {
        // console.log("测试进入test");
        console.log(res);
      })
    }

    onMounted(
        () => {
          getCodeImg();
        }
    )


    return {src, loginForm, loginRules, userLogin, resetForm, codeImgUrl, jwt, getCodeImg, testButton, submitLoginForm};
  }
}
</script>

<style scoped>
.el-row {
  background-color: #fafafa;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.el-divider {
  height: 200px;
}

.codeImg {
  float: left;
  margin-left: 10px;
  border-radius: 4px;
}
</style>