import axios from "axios";
import qs from "qs";
import router from "../../router";
import ElementPlus, {ElMessage} from 'element-plus';
import da from "element-plus/packages/locale/lang/da";


axios.defaults.baseURL = "http://localhost:8081"
// let protocol = window.location.protocol; //协议
// let host = window.location.host; //主机
// let reg = /^localhost+/;
// if(reg.test(host)) {
//     //若本地项目调试使用
//     axios.defaults.baseURL = 'http://localhost:8081';
// } else {
//     //动态请求地址             协议               主机
//     axios.defaults.baseURL = protocol + "//" + host  +":5000";
// }



//post请求头
axios.defaults.headers.post["Content-Type"] = "application/json; charset=utf-8";
//设置超时
axios.defaults.timeout = 10000;
//设置请求拦截
axios.interceptors.request.use(
    config => {
        /*每次请求都要带请求头*/
        config.headers['Authorization'] = localStorage.getItem("token");

        return config;
    });

axios.interceptors.response.use(
    response => {
        let res =response.data
        if (res.code === 200) {
            return response;
        } else {
            ElMessage.error(!res.msg? '系统异常': res.msg)
            return Promise.reject(response.data.msg)
        }
    },
    error => {
        /*如果返回的数据不为空*/
        if(error.response.data){
            error.massage = error.response.data.msg;
        }

        //权限不足，跳转到登录页面
        if(error.response.status === 401) {
            router.push("/login")
        }
        //消息显示3秒
        ElMessage.error(error.massage)
        return Promise.reject(error);
    }
);
export default {
    post(url, data) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'post',
                url,
                data:data,
            })
                .then(res => {
                    resolve(res)
                })
                .catch(err => {
                    reject(err)
                });
        })
    },

    get(url, data) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url,
                params:data

            })
                .then(res => {
                    resolve(res)
                })
                .catch(err => {
                    reject(err)
                })
        })
    }
};
