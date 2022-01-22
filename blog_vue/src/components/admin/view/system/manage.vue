<template>
  <!--  <el-container>-->
  <!--    &lt;!&ndash;顶部区域&ndash;&gt;-->
  <!--    <el-header>-->
  <!--      <header-info></header-info>-->
  <!--    </el-header>-->
  <!--    <el-container>-->
  <!--      &lt;!&ndash;左侧菜单区域&ndash;&gt;-->
  <!--      <el-aside width="210px">-->
  <!--        <asideMenu></asideMenu>-->
  <!--      </el-aside>-->
  <!--      &lt;!&ndash;中间主要内容区域&ndash;&gt;-->
  <!--      <el-main class="main-container">-->
  <!--        <tabs></tabs>-->
  <!--        <div style="margin: 0 15px">-->
  <!--          <router-view></router-view>-->
  <!--        </div>-->
  <!--      </el-main>-->
  <!--    </el-container>-->
  <!--  </el-container>-->
  <div class="container">
    <div class="aside-empty"></div>
    <div class="aside-container">
      <asideMenu class="menu-container"></asideMenu>
    </div>
    <div class="main-container">
      <div class="main">
<!--&lt;!&ndash;        <div class="header-empty"></div>&ndash;&gt;-->

        <div class="title">
          <div>{{title.txt?title.txt:'首页'}}</div>
          <div class="tabs-container">
            <tabs></tabs>
          </div>
        </div>
        <div class="content">
          <router-view></router-view>

        </div>
      </div>
    </div>
  </div>
  <div class="header">
    <header-info></header-info>
  </div>
</template>

<script>
import asideMenu from "../../introduce/asideMenu.vue";
import headerInfo from "../../introduce/headerInfo.vue";
import tabs from "../../introduce/tabs.vue";
import {ElMessageBox, ElMessage} from 'element-plus';
import {useRoute,onBeforeRouteUpdate} from "vue-router";
import {onBeforeMount, reactive, ref} from "vue";

export default {
  name: "manage",
  components: {
    asideMenu, headerInfo, tabs
  },
  setup(){
    let route = useRoute()
    let title = reactive({
      txt:'',
      state:false
    })
    // onBeforeMount(()=>{
    //   console.log('路由',route)
    // })
    onBeforeRouteUpdate((to)=>{
      console.log(to.query.title)
      title.txt = to.query.title
      title.state = false
      title.state = true

    })

    return{
      title,
    }
  }
}
</script>

<style scoped>
/*主容器布局样式-----------------------------start*/
.main-container::-webkit-scrollbar {
  width: 0 !important
}

.el-container {
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
.el-row {
  height: 100%;
  padding: 0;
  margin: 0;
}

.el-col {
  height: 100%;
}

/*顶部信息------------------------------------end*/

/*左侧菜单栏------------------------------------start*/

/*左侧菜单栏------------------------------------start*/


/*新容器*/
.container {
  /*width: 100vw;*/
  /*min-height: 100vh;*/
  background-color: #f0f2f5;
  display: flex;
}

.aside-empty {
  width: 248px;
  max-width: 248px;
  min-width: 248px;
  height: 100vh;
}

.aside-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 248px;
  max-width: 248px;
  min-width: 248px;

  height: 100vh;
  background-color: #545c64;
  /*background-color: #fff;*/
  flex: 1;
  padding-top: 64px;

}

.main-container {
  /*background-color: hotpink;*/
  width: 86%;
  /*max-width: 1600px;*/
  /*min-width: 1600px;*/
  /*height: 100vh;*/
  z-index: 100;
  /*display: flex;*/
  display: block;
  margin-top: 64px;
}

.header, .header-empty {
  height: 64px;
  line-height: 64px;
  width: 100%;
}
.header-empty{
  background-color: blue;
}
.header {
  position: fixed;
  top: 0;
  width: 100vw;
  background: linear-gradient(90deg,#536976,#588aa3);
  z-index: 100;
}
.main{
  /*width: 95%;*/
  /*height: 100%;*/
  /*background-color: #588aa3;*/
}
.title{
  width: 101%;
  /*width: auto;*/
  /*max-width: 600px;*/

  /*width: 100vw;*/
  height: 100px;
  font-size: 20px;
  font-weight: 600;
  background-color: #fff;
  padding: 16px 24px;
}
.tabs-container{
  padding: 10px 0 0 0 ;
}
.content{
  /*max-width: 1600px;*/
  /*width: 100%;*/
  /*width: 1600px;*/
  min-height: 100vh;
  background-color: #fff;
  margin: 15px 0 0 15px;
  /*padding: 15px 0 0 15px;*/
  /*overflow-x: scroll;*/
  /*overflow: hidden;*/
}
.test{
  max-width: 1650px;
  /*min-height: 1000px;*/
  /*height: 2000px;*/
  background-color: purple;
}
</style>