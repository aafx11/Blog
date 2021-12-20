<template>
  <div>
    <span>账号信息设置</span>
    <div>
      <a-avatar
          :src="'http://localhost:8081/static/avatar/'+oldUser.avatar"
          :size="{ xs: 24, sm: 32, md: 40, lg: 64, xl: 80, xxl: 100 }">
      </a-avatar>
<!--      <a-avatar-->
<!--          :src="'http://47.113.204.103:8081/static/avatar/'+oldUser.avatar"-->
<!--          :size="{ xs: 24, sm: 32, md: 40, lg: 64, xl: 80, xxl: 100 }">-->
<!--      </a-avatar>-->
    </div>
    <div>
      <a-upload
          v-model:file-list="fileList"
          :customRequest="uploadAvatar"
          @change="handleChange"
      >
        <a-button>

          更换头像
        </a-button>
      </a-upload>
    </div>
    <div>
      <a-form
          :model="NewUser"
          ref="formRef"

          @finish="handleFinish"
          @finishFailed="handleFinishFailed"
      >
        <a-form-item name="nickname" class="form">
          <div class="input">
            <span>昵称</span>
            <a-input v-model:value="NewUser.nickname" size="large"
                     style="width: 90%;" :maxlength="8"
                     @change="oldUser.nickname==NewUser.nickname ? nicknameButton=false : nicknameButton=true"></a-input>
          </div>
          <div class="towButton">
            <a-button size="small" v-show="nicknameButton"
                      @click="NewUser.nickname = oldUser.nickname,nicknameButton=false">取消
            </a-button>
            <a-button size="small" v-show="nicknameButton"
                      @click="update('nickname')">保存
            </a-button>
          </div>
        </a-form-item>
        <a-form-item name="briefintroduction" class="form">
          <div class="input">
            <span>简介</span>
            <a-input v-model:value="NewUser.briefintroduction"
                     size="large" style="width: 90%;" :maxlength="100"
                     @change="oldUser.briefintroduction==NewUser.briefintroduction ? fintroductionButton=false : fintroductionButton=true"></a-input>
          </div>
          <div class="towButton">
            <a-button size="small" v-show="fintroductionButton"
                      @click="NewUser.briefintroduction = oldUser.briefintroduction,fintroductionButton=false">取消
            </a-button>
            <a-button size="small" v-show="fintroductionButton"
                      @click="update('introduction')">保存
            </a-button>
          </div>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script>
import {ref, reactive, watch, getCurrentInstance, onMounted, readonly} from 'vue'
import {AvatarUpload, AvatarUploadBase64,saveAvatarToLocal} from '../../../request/api/common.js'
import {message} from "ant-design-vue";
import {getUserInfo, updateUserNickname, updateUserIntroduction} from '../../../request/api/userList.js'

export default {
  name: "editProfile",
  inject: ['switchShow'],
  data() {
    return {
      NewUser: {
        articleCount: 0,
        avatar: '',
        briefintroduction: '',
        commentCount: 0,
        email: '',
        columns: '',
        fansCount: 0,
        followCount: 0,
        gender: 0,
        nickname: '',
        phone: '',
        userId: 0,
        username: '',
        avatarBase64: ''
      },
      oldUser: {
        articleCount: 0,
        avatar: '',
        briefintroduction: '',
        commentCount: 0,
        email: '',
        columns: '',
        fansCount: 0,
        followCount: 0,
        gender: 0,
        nickname: '',
        phone: '',
        userId: 0,
        username: '',
        avatarBase64: ''
      }
    }
  },
  setup() {

    let checkNickname = async (rule, value) => {
      if (!value) {
        return Promise.reject('Please input the age');
      }

      if (value.trim().length < 2 || value.trim().length > 8) {
        return Promise.reject();
      }
      return Promise.resolve();

    }


    const rules = {
      nickname: [{required: true, validator: checkNickname, trigger: 'change'}],
      briefintroduction: [{required: true, message: 'Please input Activity name', trigger: 'blur',},
        {min: 5, max: 40, message: 'Length should be 3 to 5', trigger: 'blur',}],
    }
    const {proxy} = getCurrentInstance();
    const fileList = ref([]);
    const formRef = ref();
    let base64 = ref()
    let flag = ref(false)
    const uploadAvatar = (data) => {

      //头像保存到本地
      const formData = new FormData();
      formData.append("image", data.file)
      saveAvatarToLocal(formData).then(res=>{
              if (res.data.code == 200) {
                userInfo();
                proxy.switchShow();
                message.success(res.data.msg)

              } else {
                message.warn(res.data.msg)
              }
      })

      //转为base64上传
      // let reader = new FileReader();
      // reader.readAsDataURL(data.file)
      // reader.onload = () => {
      //
      //   base64.value = reader.result;
      //
      //   const formData = new FormData();
      //   formData.append("image",base64.value )
      //   AvatarUploadBase64(formData).then(res => {
      //
      //       if (res.data.code == 200) {
      //         userInfo();
      //         proxy.switchShow();
      //         message.success(res.data.msg)
      //
      //       } else {
      //         message.warn(res.data.msg)
      //       }
      //   })
      // }


      //上传七牛云
      // formData.append("image", data.file)
      // formData.append("buckets", "community-avatar-image")
      // formData.append("cdn", "qy8a1g6l5.hn-bkt.clouddn.com")

      // AvatarUpload(formData).then(res => {
      //   if (res.data.code == 200) {
      //     userInfo();
      //     proxy.switchShow();
      //     message.success(res.data.msg)
      //
      //   } else {
      //     message.warn(res.data.msg)
      //   }
      // })
    }
    const handleChange = info => {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }

      if (info.file.status === 'done') {
        message.success(`${info.file.name} file uploaded successfully`);
      } else if (info.file.status === 'error') {
        message.error(`${info.file.name} file upload failed.`);
      }
    };

    const userInfo = () => {
      getUserInfo().then(res => {
        proxy.oldUser.nickname = res.data.data.nickname;
        proxy.oldUser.articleCount = res.data.data.articleCount;
        proxy.oldUser.avatar = res.data.data.avatar;
        proxy.oldUser.briefintroduction = res.data.data.briefintroduction;
        proxy.oldUser.columns = res.data.data.columns;
        proxy.oldUser.commentCount = res.data.data.commentCount;
        proxy.oldUser.email = res.data.data.email;
        proxy.oldUser.fansCount = res.data.data.fansCount;
        proxy.oldUser.followCount = res.data.data.followCount;
        proxy.oldUser.gender = res.data.data.gender;
        proxy.oldUser.phone = res.data.data.phone;
        proxy.oldUser.userId = res.data.data.userId;
        proxy.oldUser.username = res.data.data.username;
        proxy.oldUser.avatarBase64 = res.data.data.avatarBase64;
        proxy.NewUser = res.data.data;
        proxy.$forceUpdate();
      })
    }

    //昵称输入框改变事件
    const nicknameButton = ref(false)
    const fintroductionButton = ref(false)


    const update = (data) => {
      if (data == 'nickname') {
        if (proxy.NewUser.nickname.length > 8 || proxy.NewUser.nickname.length < 2) {
          message.info("昵称长度应为2到8位字符")
          return
        } else if (proxy.NewUser.nickname.indexOf(" ") > -1) {
          //""代表空，" "代表空格
          message.info("昵称中不能存在空格")
        } else {
          updateUserNickname(proxy.NewUser.nickname).then(res => {
            console.log(res);
            if (res.data.code == 200) {
              proxy.oldUser.nickname = res.data.data;
              nicknameButton.value = false
              proxy.switchShow();
              proxy.$forceUpdate();

            } else {
              return
            }
          })
        }
      }
      if (data == 'introduction') {
        if (proxy.NewUser.briefintroduction.length > 140) {
          message.info("个人简介字数最多为100个字")
        } else {
          updateUserIntroduction(proxy.NewUser.briefintroduction).then(res => {
            console.log(res);
            if (res.data.code == 200) {
              proxy.oldUser.briefintroduction = res.data.data;
              fintroductionButton.value = false
              proxy.switchShow();
              proxy.$forceUpdate();
            } else {
              return
            }
          })
        }
      }

    }

    const handleFinish = values => {
      console.log(values, proxy.NewUser);
    };

    const handleFinishFailed = errors => {
      console.log(errors);
    };
    onMounted(() => {
      userInfo();
    })
    return {
      fileList,
      rules,
      nicknameButton,
      fintroductionButton,
      base64,
      checkNickname,
      uploadAvatar,
      handleChange,
      userInfo,
      handleFinish,
      handleFinishFailed,
      update,
    }
  }
}
</script>

<style scoped>
/*表单*/
.form {
  height: 60px;

}

.input {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

.towButton {
  float: right;
}
</style>