import service from "../http";

//获取个人视频列表,模糊查询
export const getVideoListByUserId=(current,size,searchContent) => {
    return service({
        url:"/videoInfo/getVideoListByUserId",
        method:'get',
        params:{current:current,size:size,searchContent:searchContent}
    })
}
//获取视频列表通过审核状态或模糊查询
export const getVideoByStateOrLike=(current,size,searchContent,tab) => {
    return service({
        url:"/videoInfo/getVideoByStateOrLike",
        method:'get',
        params:{current:current,size:size,
                searchContent:searchContent,
                tab:tab
        }

    })
}

//获取关注的用户已过审的视频
export const getVideoByFollow=(current,size) => {
    return service({
        url:"/videoInfo/getVideoByFollow",
        method:'get',
        params:{current:current,size:size}
    })
}

//获取所有已过审视频，通过观看数排序
export const getVideoOrderByView=(current,size) => {
    return service({
        url:"/videoInfo/getVideoOrderByView",
        method:'get',
        params:{current:current,size:size}
    })
}

//获取所有已过审视频，通过日期排序
export const getVideoOrderByCreated=(current,size) => {
    return service({
        url:"/videoInfo/getVideoOrderByCreated",
        method:'get',
        params:{current:current,size:size}
    })
}

//模糊查询已过审的视频
export const getVideoOrderByLike=(current,size,searchContent) => {
    return service({
        url:"/videoInfo/getVideoOrderByLike",
        method:'get',
        params:{current:current,size:size,searchContent:searchContent}
    })
}

//根据用户关注获取视频
export const getVideoByTag=(current, size, tagId) => {
    return service({
        url:"/videoInfo/getVideoByTag",
        method:'get',
        params:{current: current, size: size,tagId:tagId}
    })
}

//获取视频详情信息
export const getVideoDetailByVideoId=(videoId) => {
    return service({
        url:"/videoInfo/getVideoDetailByVideoId",
        method:'get',
        params: {videoId:videoId}
    })
}

//获取所有视频标签
export const getVideoTags=(current,size) => {
    return service({
        url:"/tagsVideo/getVideoTags",
        method:'get',
        params:{current: current, size: size}
    })
}

//重新上传视频或者封面文件
export const reUploadVideoAndCover=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/reUploadByVideoId",
        method:'post',
        data:formData,

    })
}

//修改视频信息
export const setVideoInfoByVideoId=(formData) => {
    return service({
        url:"/videoInfo/setVideoInfoByVideoId",
        method:'post',
        data:formData
    })
}

//修改视频审核状态
export const setVideoExamine=(formData) => {
    return service({
        url:"/videoInfo/setVideoExamine",
        method:'post',
        data:formData
    })
}

//删除视频信息和文件
export const deleteVideoByVideoId=(videoId) => {
    return service({
        url:"/videoInfo/deleteVideoByVideoId",
        method:'post',
        data:videoId
    })
}

//收藏功能
export const collect=(videoId) => {
    return service({
        url:"/videoCollect/collect",
        method:'post',
        data:videoId
    })
}

//取消收藏
export const unCollect=(videoId) => {
    return service({
        url:"/videoCollect/unCollect",
        method:'post',
        data:videoId
    })
}

//点赞功能
export const likewise=(videoId) => {
    return service({
        url:"/videoLike/likewise",
        method:'post',
        data:videoId
    })
}

//取消点赞
export const unLikewise=(videoId) => {
    return service({
        url:"/videoLike/unLikewise",
        method:'post',
        data:videoId
    })
}


