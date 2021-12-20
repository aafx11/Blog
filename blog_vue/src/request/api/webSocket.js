import service from "../http";

//创建会话
export const createSession=(data) => {
    return service({
        url:"/sessionList/createSession",
        method:'post',
        data
    })
}

//获取所有该用户与其他用户已经创建的会话
export const getSessionListAlready=(fromUserId) => {
    return service({
        url:"/sessionList/getSessionListAlready/"+fromUserId,
        method:'get',
    })
}

//删除会话
export const deleteSession=(data) => {
    return service({
        url:"/sessionList/deleteSession",
        method:'post',
        data
    })
}

//获取消息列表
export const getMessageList=(sessionId) => {
    return service({
        url:"message/getMessageList/"+sessionId,
        method:'get',
    })
}

export const getUnReadMessageCount=() => {
    return service({
        url:"/sessionList/getUnReadMessageCount",
        method:'get',
    })
}