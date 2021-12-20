<template>
  <el-container>
    <!--顶部区域-->
    <el-header>
      <header-info></header-info>
    </el-header>
    <el-container>
      <!--左侧菜单区域-->
      <el-aside width="210px">
        <asideMenu></asideMenu>
      </el-aside>
      <!--中间主要内容区域-->
      <el-main>
        <tabs></tabs>
        <div style="margin: 0 15px">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import asideMenu from "../../introduce/asideMenu.vue";
import headerInfo from "../../introduce/headerInfo.vue";
import tabs from "../../introduce/tabs.vue";
import { ElMessageBox,ElMessage } from 'element-plus';
export default {
  name: "manage",
  components:{
    asideMenu,headerInfo,tabs
  }
}
</script>

<style scoped>
/*主容器布局样式-----------------------------start*/
.el-container{
  height: 100%;
  padding: 0;
  margin: 0;
}
.el-header {
  padding: 0;
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: left;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  line-height: 160px;
}
body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
/*主容器布局样式-----------------------------end*/

/*顶部信息----------------------------------start*/
.el-row{
  height: 100%;
  padding: 0;
  margin: 0;
}
.el-col{
  height: 100%;
}
/*顶部信息------------------------------------end*/

/*左侧菜单栏------------------------------------start*/

/*左侧菜单栏------------------------------------start*/

</style>