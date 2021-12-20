import { createStore } from "vuex";
import {menus} from "./modules/menus";
import ru from "element-plus/packages/locale/lang/ru";

/*
* 1.state:数据存储属性(不能直接调用，只能通过mutations的方法调用)
* 2.mutations://方法属性
*
* */
export default createStore({
    state: {
        token:'',
        userId:''
    },
    mutations:{
        setToken(state,token){
            state.token = token;
            localStorage.setItem("token",token);
        },
        setUserInfo(state,userId){
            state.userId = userId;
        }
    },
    actions:{

    },
    getters:{
        getUserId(state){
            return state.userId
        },
        getToken(state){
            return state.token
        }
    },
    modules:{
        menus
    }
});
