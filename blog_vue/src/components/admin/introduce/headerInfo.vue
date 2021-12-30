<template>
  <el-row v-if="state.show">
    <el-col :span="3" :offset="10"><strong>BlogAdmin管理系统</strong></el-col>
    <el-col :span="1" :offset="8">
<!--      <el-avatar :src="'http://localhost:8081/static/avatar/'+ state.info.avatar" :size="42" class=""></el-avatar>-->
          <el-image :src="'http://localhost:8081/static/avatar/'+ state.info.avatar" class="header-avatar"></el-image>
    </el-col>
    <el-col :span="1">
      <el-dropdown>
              <span class="el-dropdown-link">
                {{ state.info.username }}<i class="el-icon-arrow-down el-icon--right"></i>
              </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>黄金糕</el-dropdown-item>
            <el-dropdown-item>狮子头</el-dropdown-item>
            <el-dropdown-item>螺蛳粉</el-dropdown-item>
            <el-dropdown-item disabled>双皮奶</el-dropdown-item>
            <el-dropdown-item divided>蚵仔煎</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-col>
    <el-col :span="1">
      <button @click="logout">退出</button>
    </el-col>
  </el-row>
</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted, onBeforeMount,} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import {useStore} from "vuex";
import {UserLogout} from '../../../request/api/common.js'
import {getOneUserInfo} from '../../../request/api/userList.js'
export default {
  name: "headerInfo",
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();
    /*顶部个人信息*/
    let state = reactive({
      info:{},
      show:false
    })
    /*获取个人信息*/
    const getInfo = () => {

          getOneUserInfo().then((res) => {
              state.info = res.data.data
              state.show = true
            console.log('获取个人信息',state.info);
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
    onBeforeMount(()=>{
      getInfo();
    })
    return {state, getInfo, logout}
  },

}
</script>

<style scoped>
.header-avatar{
  border-radius: 50%;
  width: 50px;
  height: 50px;
}
</style>