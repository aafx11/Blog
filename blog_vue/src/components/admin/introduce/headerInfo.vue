<template>
  <el-row>
    <el-col :span="3" :offset="10"><strong>BlogAdmin管理系统</strong></el-col>
    <el-col :span="1" :offset="8">
      <el-avatar :src="'http://'+Info.avatar" :size="50" class="header-avatar"></el-avatar>
    </el-col>
    <el-col :span="1">
      <el-dropdown>
              <span class="el-dropdown-link">
                {{ Info.username }}<i class="el-icon-arrow-down el-icon--right"></i>
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
import {ref, reactive, getCurrentInstance, onMounted,} from 'vue'
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
    const Info = reactive({
      id: '',
      username: '',
      avatar: ''
    })
    /*获取个人信息*/
    const getInfo = () => {
          getOneUserInfo().then((res) => {
        proxy.Info = res.data.data;
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

    onMounted(
        () => {
          getInfo();
          proxy.$forceUpdate();
        }
    )
    return {Info, getInfo, logout}
  },

}
</script>

<style scoped>

</style>