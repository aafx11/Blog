import axios from 'axios';
import { ElMessage } from 'element-plus';
import { emitter } from '../utils/mitt.js'
import router from "../router";
import {message} from "ant-design-vue";

const service = axios.create({
    baseURL: "http://localhost:8081",
    // baseURL: "http://47.113.204.103:8081",

    timeout: 10000
})

let activeAxios = 0
let timer

const showLoading = () => {
    activeAxios++
    if (timer) {
        clearTimeout(timer)
    }
    timer = setTimeout(() => {
        if (activeAxios > 0) {
            emitter.emit("showLoading")
        }
    }, 400);
}

const closeLoading = () => {
    activeAxios--
    if (activeAxios <= 0) {
        clearTimeout(timer)
        emitter.emit("closeLoading")
    }
}

service.defaults.headers.post["Content-Type"] = "application/json; charset=utf-8";

//request拦截器
service.interceptors.request.use(
    config => {
        /*每次请求都要带请求头*/
        config.headers['Authorization'] = localStorage.getItem("token");

        return config;
    },
    error =>{
        closeLoading()
        ElMessage({
            showClose: true,
            message: error,
            type: 'error'
        })
        return error;
    }
    );

//request拦截器
service.interceptors.response.use(
    response => {
        closeLoading()
        let res =response.data
        if (res.code === 200) {
            return response;
        } else {
            console.log("info");
            // ElMessage.info(!res.msg? '系统异常': res.msg)
            message.info(!res.msg?"系统异常":res.msg);
            return Promise.reject(response.data.msg)
        }
    },
    error => {
        /*如果返回的数据不为空*/
        if(error.response.data){
            // error.massage = error.response.data.msg;
            console.log("错误",error.response);
        }


        //权限不足，跳转到登录页面
        if(error.response.status === 401) {
            localStorage.removeItem('token')
            // ElMessage.info("您的登录已超时,请重新登录")
            router.push("/login").then()

        }
        //消息显示3秒
        ElMessage.info(error.massage)
        return Promise.reject(error);
    }
);
export default service