<template>
  <!--  <el-row v-if="state.show">-->
  <!--    <el-col :span="3" :offset="10"><strong>BlogAdmin管理系统</strong></el-col>-->
  <!--    <el-col :span="1" :offset="8">-->
  <!--&lt;!&ndash;      <el-avatar :src="'http://localhost:8081/static/avatar/'+ state.info.avatar" :size="42" class=""></el-avatar>&ndash;&gt;-->
  <!--          <el-image :src="'http://localhost:8081/static/avatar/'+ state.info.avatar" class="header-avatar"></el-image>-->
  <!--    </el-col>-->
  <!--    <el-col :span="1">-->
  <!--      <el-dropdown>-->
  <!--              <span class="el-dropdown-link">-->
  <!--                {{ state.info.username }}<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
  <!--              </span>-->
  <!--        <template #dropdown>-->
  <!--          <el-dropdown-menu>-->
  <!--            <el-dropdown-item>黄金糕</el-dropdown-item>-->
  <!--            <el-dropdown-item>狮子头</el-dropdown-item>-->
  <!--            <el-dropdown-item>螺蛳粉</el-dropdown-item>-->
  <!--            <el-dropdown-item disabled>双皮奶</el-dropdown-item>-->
  <!--            <el-dropdown-item divided>蚵仔煎</el-dropdown-item>-->
  <!--          </el-dropdown-menu>-->
  <!--        </template>-->
  <!--      </el-dropdown>-->
  <!--    </el-col>-->
  <!--    <el-col :span="1">-->
  <!--      <button @click="logout">退出</button>-->
  <!--    </el-col>-->
  <!--  </el-row>-->
  <div class="header">
    <div class="title-wrapper">多人论坛管理系统</div>
    <div class="user-info" v-if="state.show">
      <img :src="'http://localhost:8081/static/avatar/'+ state.info.avatar" alt="加载失败" class="user-avatar">
      <div>{{state.info.username}}</div>
      <my-button size="mini" @click="logout">退出</my-button>
    </div>
  </div>
</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted, onBeforeMount,} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import {useStore} from "vuex";
import {UserLogout} from '../../../request/api/common.js'
import {getOneUserInfo} from '../../../request/api/userList.js'
import myButton from '../../../view/myButton.vue'

export default {
  name: "headerInfo",
  components:{
    myButton
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();
    /*顶部个人信息*/
    let state = reactive({
      info: {},
      show: false
    })
    /*获取个人信息*/
    const getInfo = () => {

      getOneUserInfo().then((res) => {
        state.info = res.data.data
        state.show = true
        console.log('获取个人信息', state.info);
      })
    }

    /*退出登录*/
    const logout = () => {
      UserLogout().then((res) => {
        localStorage.clear();
        sessionStorage.clear();
        store.commit("resetState");
        proxy.$router.push('/index');
      })
    }

    // onMounted(
    //     () => {
    //       getInfo();
    //       proxy.$forceUpdate();
    //     }
    // )
    onBeforeMount(() => {
      getInfo();
    })
    return {state, getInfo, logout}
  },

}
</script>

<style lang="scss" scoped>
.header-avatar {
  border-radius: 50%;
  width: 50px;
  height: 50px;
}

.header {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;

  .title-wrapper {
    font-size: 20px;
    letter-spacing:3px;
    flex: 1;
    display: flex;
    justify-content: center;
  }

  .user-info {
    width: 200px;
    padding-right:20px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    font-size: 16px;

    .user-avatar{
      width: 46px;
      height: 46px;
      object-fit: cover;
      border-radius: 50%;

    }
  }
}
</style>