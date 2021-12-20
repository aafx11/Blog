import {articleLists} from '../../request/api/article.js'

export function getArticleData(current,size,tab){
    articleLists(current,size,tab).then(res=>{
        console.log("返回文章",res);
    })

}
