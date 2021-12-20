import service from "../http";

export const getRoleById=(id) => {
    return service({
        url:"/user/UserInfo/" + id,
        method:'get',
    })
}

export const updateOrSave=(data) => {
    return service({
        url:'/user/' + (data.id ? 'update' : 'save'),
        method:'post',
        data
    })
}

export const deleteById=(data) => {
    return service({
        url:'/user/delete/',
        method:'post',
        data
    })
}

export const UserList=(data) => {
    return service({
        url:'/user/UserList',
        method:'get',
        params:data
    })
}

export const getOneUserInfo=() => {
    return service({
        url:'/getUserInfo',
        method:'get',
    })
}

export const rePassWord=(data) => {
    return service({
        url:'/user/resetPassword',
        method:'post',
        data
    })
}

export const getUserInfoById=(id) => {
    return service({
        url:"/user/UserInfo/" + id,
        method:'get',
    })
}

export const getUserInfo=() => {
    return service({
        url:"/user/getUserInfo",
        method:'get',
    })
}

export const updateUserNickname=(data) => {
    return service({
        url:"/user/updateUserNickname",
        method:'get',
        params:{data:data}

    })
}

export const updateUserIntroduction=(data) => {
    return service({
        url:"/user/updateUserIntroduction",
        method:'post',
        data
    })
}

export const getUserProfileVOById=(id) => {
    return service({
        url:"/user/getUserProfileVOById",
        method:'post',
        data:id

    })
}

//获取随机用户，通过积分排序，用于用户推荐
export const getRandomUsers=(current,size) => {
    return service({
        url:"/user/getRandomUsers",
        method:'get',
        params:{current:current,size:size}
    })
}