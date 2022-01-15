import service from "../http";

export const getNotices = ()=>{
    return service({
        url:"/information/getNotices",
        method:'get',
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

export const postComment=(data) => {
    return service({
        url:"/comment/postComment",
        method:'post',
        data
    })
}


//获取文章评论列表
export const getCommentList=(articleId) => {
    return service({
        url:"/commentform/getCommentList",
        method:'get',
        params:{
            articleId:articleId
        }
    })
}

//获取最新评论
export const getLatestComments=() => {
    return service({
        url:"/commentform/getLatestComments",
        method:'get',
    })
}

//提交文章评论
export const submitComment=(data) => {
    return service({
        url:"/commentform/submitComment",
        method:'post',
        data
    })
}

//获取视频评论通过视频id
export const getVideoCommentByVideoId=(videoId) => {
    return service({
        url:"/videoComment/getVideoCommentByVideoId",
        method:'get',
        params:{
            videoId:videoId
        }
    })
}

//提交视频评论
export const submitVideoComment=(data) => {
    return service({
        url:"/videoComment/submitComment",
        method:'post',
        data:data
    })
}