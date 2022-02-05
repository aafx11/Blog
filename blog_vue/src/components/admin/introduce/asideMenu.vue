<template>
  <div>
    <router-link :to="{path:'/welcome',query:{title:'首页'}}" @click="selectMenu(itemIndex)">
      <div class="index-txt">首页</div>
    </router-link>
    <el-menu
        :default-active="editableTabsValue"
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
    >


      <!--动态菜单栏-->
      <el-submenu :index="menu.name" v-for="menu in menuList">
        <template #title>
          <i :class="menu.icon"></i>
          <span>{{ menu.title }}</span>
        </template>
        <!--      -->
        <router-link :to="{path:item.path,query:{title:item.title}}" v-for="(item,index) in menu.children">
          <el-menu-item :index="item.name" @click="selectMenu(item)">
            <template #title>
              <i :class="item.icon"></i>
              <span>{{ item.title }}</span>
            </template>
          </el-menu-item>
        </router-link>

      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, computed, onMounted, isReactive} from 'vue'
import {useStore} from 'vuex'

export default {
  name: "asideMenu",
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore()
    const state = store.state

    let itemIndex = reactive({
      name: 'welcome',
      title: '首页'
    })

    const menuList = computed(() => {
      // console.log("在vuex中获取的菜单menu:",store.state.menus.menuList);
      return store.state.menus.menuList;
      console.log(menuList)
    })


    const editableTabsValue = computed(() => {
      return store.state.menus.editableTabsValue;
    })

    const selectMenu = (item) => {
      store.commit("addTab", item)
    }

    return {menuList, editableTabsValue, itemIndex, selectMenu};
  }
}
</script>

<style scoped>
.index-txt {
  height: 40px;
  /*color: black;*/
  color: #fff;
  background-color: #545c64;
  padding: 10px 0 0 20px;
  border-right: 1px solid rgba(189, 187, 187, 0.5);

}
</style>