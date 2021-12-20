import {getUserProfileVOById} from '../../request/api/userList.js'
import {computed, defineComponent,toRefs, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';

export function getUserDataById(id){
    const user = reactive({})
    const {proxy} = getCurrentInstance();
    getUserProfileVOById(id).then(res=>{
        console.log("返回角色",res);
        proxy.user = res.data.data;
        return {
            user
        }
    })
}