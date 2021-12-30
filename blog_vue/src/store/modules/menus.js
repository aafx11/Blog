import { createStore } from 'vuex'
import {reactive, ref} from "vue";


export const menus={

    //数据存储属性(不能直接调用，只能通过mutations的方法调用)
    state: {
        menuList:[{}],
        permList:[],
        hasRoutes:false,

        editableTabsValue:'system:menu',
        editableTabs:[{
            title:'首页',
            name:'welcome',
        }]
    },
    //方法属性
    mutations: {
        setMenuList(state,menus){
            state.menuList = menus
        },

        setPermList(state,perms){
            state.permList = perms
        },

        changeRoutesStatus(state,hasRoutes){
            state.hasRoutes = hasRoutes

        },

        addTab(state,tab){
            console.log(tab)
            /*查找标签里有没相同的元素*/
            let index = state.editableTabs.findIndex(e => e.name ===tab.name)
            /*如果没有相同的元素，再创建新标签*/
            if(index === -1){
                state.editableTabs.push({
                    title: tab.title,
                    name: tab.name,
                });
            }
            state.editableTabsValue = tab.name;
        },

        resetState(state){
            state.menuList = [];
            state.permList = [];
            state.hasRoutes = false;
            state.editableTabsValue='welcome';
            state.editables = [{
                title:'首页',
                name:'welcome',
            }]

        }
    },
    //异步属性
    actions: {

    },
    //计算属性
    getters:{
        menuList(state){
            return state.menuList
        },
        hasRoutes(state){
            return state.hasRoutes
        },
        permList(state){
            return state.permList
        }
    },

}
