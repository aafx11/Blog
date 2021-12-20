import service from "../http";

export const follow=(followerId) => {
    return service({
        url:`/follow/followOneUser/${followerId}`,
        method:'get',
    })
}

export const unFollow=(articleUserId) => {
    return service({
        url:`/follow/unFollowOneUser/${articleUserId}`,
        method:'get',
    })
}

//检测是否已经关注该用户
export const hasFollow=(articleUserId) => {
    return service({
        url:`/follow/hasFollow/${articleUserId}`,
        method:'get',
    })
}

export const getFollowListByPage=(current, size) => {
    return service({
        url:"/follow/getFollowListByPage",
        method:'get',
        params:{current: current, pageSize: size}
    })
}

export const getFollowList=() => {
    return service({
        url:"/follow/getFollowList",
        method:'get',
    })
}

export const getFansList=(current, size) => {
    return service({
        url:"/follow/getFansList",
        method:'get',
        params:{current: current, pageSize: size}
    })
}
//根据用户id获取关注列表
export const getFollowListByUserId=(current, size,userId) => {
    return service({
        url:"/follow/getFollowListByUserId",
        method:'get',
        params:{current: current, pageSize: size,userId:userId}

    })
}
//通过用户id获取粉丝列表(分页)
export const getFansListByUserId=(current, size,userId) => {
    return service({
        url:"/follow/getFansListByUserId",
        method:'get',
        params:{current: current, pageSize: size,userId:userId}

    })
}