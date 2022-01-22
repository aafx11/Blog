import service from "../http";

export const getNotices = () => {
    return service({
        url: "/information/getNotices",
        method: 'get',
    })
}

// export const getCommentList=(articleId) => {
//     return service({
//         url:"/comment/getCommentList",
//         method:'get',
//         params:{
//             articleId:articleId
//         }
//     })
// }

export const postComment = (data) => {
    return service({
        url: "/comment/postComment",
        method: 'post',
        data
    })
}


//获取文章评论列表
export const getCommentList = (articleId) => {
    return service({
        url: "/commentform/getCommentList",
        method: 'get',
        params: {
            articleId: articleId
        }
    })
}

//获取文章最新评论
export const getLatestComments = () => {
    return service({
        url: "/commentform/getLatestComments",
        method: 'get',
    })
}

//获取所有评论
export const getCommentListByPage=(current, size) => {
    return service({
        url:"/commentform/getCommentListByPage",
        method:'get',
        params:{current: current, size: size}
    })
}

// 修改文章评论的状态
export const updateCommentState=(id) => {
    return service({
        url:"/commentform/updateCommentState",
        method:'post',
        data:id
    })
}

//提交文章评论
export const submitComment = (data) => {
    return service({
        url: "/commentform/submitComment",
        method: 'post',
        data
    })
}

//获取视频评论通过视频id
export const getVideoCommentByVideoId = (videoId) => {
    return service({
        url: "/videoComment/getVideoCommentByVideoId",
        method: 'get',
        params: {
            videoId: videoId
        }
    })
}

//提交视频评论
export const submitVideoComment = (data) => {
    return service({
        url: "/videoComment/submitComment",
        method: 'post',
        data: data
    })
}