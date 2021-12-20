<template>
  <el-menu
      :default-active="editableTabsValue"
      class="el-menu-vertical-demo"
  >
    <!--动态菜单栏-->
    <el-submenu :index="menu.name" v-for="menu in menuList">
      <template #title>
        <i :class="menu.icon"></i>
        <span>{{menu.title}}</span>
      </template>
      <!--      -->
      <router-link :to="item.path" v-for="(item,index) in menu.children">
        <el-menu-item :index="item.name" @click="selectMenu(item)">
          <template #title>
            <i :class="item.icon"></i>
            <span>{{item.title}}</span>
          </template>
        </el-menu-item>
      </router-link>

    </el-submenu>
  </el-menu>
</template>

<script>
import {getCurrentInstance, reactive,computed,onMounted,isReactive} from 'vue'
import { useStore } from 'vuex'
export default {
  name: "asideMenu",
  setup(){
    const { proxy }= getCurrentInstance();
    const store = useStore()
    const state = store.state


    const menuList = computed(()=>{
      // console.log("在vuex中获取的菜单menu:",store.state.menus.menuList);
      return store.state.menus.menuList;

    })


    const editableTabsValue=computed( ()=>{
      return store.state.menus.editableTabsValue;
    })

    const selectMenu = (item)=>{
      store.commit("addTab",item)
    }

    return {menuList,editableTabsValue,selectMenu};
  }
}
</script>

<style scoped>

</style>