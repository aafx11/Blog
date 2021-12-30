<template>
  <el-tabs v-model="editableTabsValue" type="card" closable @tab-remove="removeTab" @tab-click="clickTab">
    <el-tab-pane
        :key="item.name"
        v-for="(item, index) in editableTabs"
        :label="item.title"
        :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import {ref, reactive, getCurrentInstance, computed} from 'vue'
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import { ElMessageBox,ElMessage } from 'element-plus';

export default {
  name: "tabs",
  setup(){
    const { proxy }= getCurrentInstance();
    const store = useStore()
    const route = useRoute();
    const router = useRouter();


    const removeTab = (targetName)=> {
      let tabs = proxy.editableTabs;
      let activeName = proxy.editableTabsValue;
      /*首页不能删除*/
      if(targetName ==='welcome'){
        return
      }

      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      proxy.editableTabsValue = activeName;
      proxy.editableTabs = tabs.filter(tab => tab.name !== targetName);

      router.push({name:activeName})
    }

    const handleTabsEdit = (targetName, action)=>{
      if (action === 'add') {
        let newTabName = ++proxy.tabIndex + '';
        proxy.editableTabs.push({
          title: 'New Tab',
          name: newTabName,
          content: 'New Tab content'
        });
        proxy.editableTabsValue = newTabName;
      }
      if (action === 'remove') {
        let tabs = proxy.editableTabs;
        let activeName = proxy.editableTabsValue;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }

        proxy.editableTabsValue = activeName;
        proxy.editableTabs = tabs.filter(tab => tab.name !== targetName);

      }
    }

    const clickTab = (target)=>{
      console.log("name",target.props);

      router.push({name:target.props.name,query:{title:target.props.label}})
    }

    const editableTabsValue=computed({
      get:() => {
        return store.state.menus.editableTabsValue;
      },
      set:(val)=>{
        store.state.menus.editableTabsValue = val
      }
    })

    const editableTabs=computed({
      get:()=>{
        return store.state.menus.editableTabs;
    },
      set:(val)=>{
      store.state.menus.editableTabs = val
    }
    })




    return {editableTabsValue,editableTabs,handleTabsEdit,removeTab,clickTab};
  }
}
</script>

<style scoped>

</style>