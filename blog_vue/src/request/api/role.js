import service from "../http";
import permForm from "../../components/admin/view/system/sys/role.vue"


export const updateOrSave=(data) => {
    return service({
        url:'/role/' + (data.id ? 'update' : 'save'),
        method:'post',
        data
    })
}

export const getRoleInfoById=(id) => {
    return service({
        url:'/role/RoleInfo/' + id,
        method:'get',
    })
}
//因为params是添加到url的请求字符串中的，用于get请求。
//而data是添加到请求体（body）中的， 用于post请求。
export const roleList=(data) => {
    return service({
        url:'/role/RoleList',
        method:'get',
        params:data
    })
}

export const deleteById=(data) => {
    return service({
        url:'/role/delete/',
        method:'post',
        data
    })
}