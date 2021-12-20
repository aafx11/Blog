import {getCurrentInstance} from "vue";
import {useRouter} from "vue-router"
import axios from "axios";
import {useStore} from 'vuex'

const router = useRouter()
// const {proxy} = getCurrentInstance()

import store from '../../store/index.js'
import ar from "element-plus/packages/locale/lang/ar";
import fa from "element-plus/packages/locale/lang/fa";

export const hasAuth = (perm) => {
    const store = useStore()


    const {proxy} = getCurrentInstance()
    const authority = proxy.$store.state.menus.permList


    /*判断perm权限是否在authority(数组)权限里面*/
    if (authority.indexOf(perm) > -1){
        return true
    }
    else {return false}

    // return true

}

export const judgeIdentity=(username) => {

    const token = localStorage.getItem('token');

    if (token == "" || token == null || token == "null"){
        console.log("没token，没登陆");
        return false
    } else {

        let n = token.split(".").length - 1;
        console.log("n的值",n);
        if (n == 2){
            let user = decodeURIComponent(escape(window.atob(token.split('.')[1].replace(/-/g, "+").replace(/_/g, "/"))));
            let res = JSON.parse(user).sub
            if (res == username){
                return true;
            } else {
                return false;
            }
        } else {
            console.log("token错误");
            return false;
        }

    }
}

//删除数组中一个指定元素
export const DeleteArrayElements=(str,arr) => {
    let index = arr.indexOf(str);//如果没有找到会返回-1
    if (index > -1) {
        arr.splice(index,1)//splice函数返回的是被删除的数组
        return arr;
    }

}

