import service from "../http";
import ar from "element-plus/packages/locale/lang/ar";

//获取文章列表
export const articleLists = (current, size, tab) => {
    return service({
        url: "/article/articleList",
        method: 'get',
        params: {current: current, size: size, tab: tab}
    })
}

//获取文章列表,模糊查询
export const ArticleListByLikeSearch = (current, size, searchContent) => {
    return service({
        url: "/article/ArticleListByLikeSearch",
        method: 'get',
        params: {current: current, size: size, searchContent: searchContent}
    })
}

//获取个人文章列表，模糊查询
export const getListByIdAndLike=(current, size, searchContent) => {
    return service({
        url:"/article/getListByIdAndLike",
        method:'get',
        params: {current: current, size: size, searchContent: searchContent}
    })
}

//发布文章
export const editArticle = (article) => {
    return service({
        url: "/article/editArticle",
        method: 'post',
        data: article
    })
}

export const articleListByTag=(current, size, tagId) => {
    return service({
        url:"/article/articleListByTag",
        method:'get',
        params:{current: current, size: size,tagId:tagId}
    })
}
//获取文章详情以及作者的信息
export const getArticleById = (id) => {
    return service({
        url: "/article/getArticleById",
        method: 'get',
        params:{
            id:id
        }
    })
}


export const getArticleByUserId=(current,size,searchContent,userId) => {
    return service({
        url:"/article/getArticleByUserId",
        method:'get',
        params: {current: current, size: size, searchContent: searchContent,userId:userId}

    })
}

export const getFollowArticle=(current, size) => {
    return service({
        url:"/article/getFollowArticle",
        method:'get',
        params: {current: current, size: size}
    })
}

export const getPersonalArticle=(current, size) => {
    return service({
        url:"/article/getPersonalArticle",
        method:'get',
        params: {current: current, size: size}

    })
}

export const likewise=(articleId) => {
    return service({
        url:"/articleLike/likewise",
        method:'post',
        data:articleId

    })
}

export const unLikewise=(articleId) => {
    return service({
        url:"/articleLike/unLikewise",
        method:'post',
        data:articleId

    })
}

//收藏文章
export const collect=(articleId) => {
    return service({
        url:"/articleCollect/collect",
        method:'post',
        data:articleId
    })
}

//取消收藏
export const unCollect=(articleId) => {
    return service({
        url:"/articleCollect/unCollect",
        method:'post',
        data:articleId
    })
}

export const deleteArticleByIds=(data) => {
    return service({
        url:"/article/deleteArticleByIds",
        method:'post',
        data,
    })
}