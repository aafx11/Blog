<template>

  <div class="search">
    <a-input v-model:value="searchContent" placeholder="请输入与文章相关的内容"></a-input>
    <a-button @click="getArticleDataByLike" type="primary">搜索</a-button>
  </div>
  <div v-infinite-scroll="load"  class="contain-box" v-if="showArticle">
    <div v-for="(item,index) in articleListData" class="item-box">
      <div class="title-header">
        <router-link :to="{name:'articleDetail',params: {id:item.id}}">
          <div>{{item.title}}</div>
        </router-link>
        <div class="button-style">
        <button>点赞{{item.likeCount}}</button>
        <button>收藏{{item.collects}}</button>
        <button>观看{{item.view}}</button>
        <a-popconfirm
          title="是否删除该文章"
          ok-text="确定"
          cancel-text="取消"
          @confirm="deleteArticle(item.id)"
        >
        <button >删除</button>
        </a-popconfirm>
        </div>
      </div>
      <div class="content-box">
        <router-link :to="{name:'articleDetail',params: {id:item.id}}">
        <span>{{item.introduction}}</span>
        </router-link>
        <div>
          <router-link :to="{name:'tagDetail',params:{id:tag.id}}" v-for="tag in item.tags">
            <a-tag color="blue" >{{tag.name}}</a-tag>
          </router-link>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import {ref, reactive, watch, getCurrentInstance, onMounted, readonly} from 'vue'
import {getUserInfo } from '../../../request/api/userList.js'
import {getPersonalArticle,getListByIdAndLike,deleteArticleByIds} from '../../../request/api/article.js'

export default {
  name: "myHomePage",
  setup(){
    const {proxy} = getCurrentInstance();
    const size = ref(10);
    const current = ref(1);
    const total = ref(50);
    const searchContent = ref('');


    const userInfoData = reactive();
    const getUserInfoData = () => {
      getUserInfo().then(res => {
        proxy.userInfoData = res.data.data;
        proxy.$forceUpdate();
        console.log("用户个人信息",proxy.userInfoData);
      })
    }
    const articleListData = reactive();
    const getArticleListData = () => {
      getPersonalArticle().then(res => {
        proxy.articleListData = res.data.data;
        proxy.$forceUpdate();
        console.log("文章信息",proxy.articleListData);
      })
    }

    const showEmpty = ref(false);
    const showArticle = ref(false);

    const articleList = () =>{
      if (searchContent.value.split(" ").join("").length == 0){
        console.log("纸张大小",size.value);
        getListByIdAndLike(current.value,size.value).then(res => {
          proxy.articleListData = res.data.data.records;

          console.log("模糊查询",res);
          if (proxy.articleListData.length == 0 ){
            showEmpty.value = true;
          } else {
            console.log("不为空");
            showArticle.value = false;
            showArticle.value = true;
            proxy.$forceUpdate();
          }
        })
      } else {
        getListByIdAndLike(current.value,size.value,searchContent.value).then(res => {
          proxy.articleListData = res.data.data.records;
          proxy.$forceUpdate();
          console.log("模糊查询",res);
          if (proxy.articleListData.length!=0){
            showArticle.value=false;
            showArticle.value=true;
          }
        })
      }
    }

    //获取个人文章列表，模糊查询
    const  getArticleDataByLike = () =>{
      current.value = 1;
      size.value = 10;
      articleList();
    }
    const cancel = e => {
      console.log(e);
    };

    const load = () => {
      console.log("滚动加载");
      size.value = size.value +3;
      articleList();
    }
    const deleteArticle = (id) => {
      const ids = [];
      ids.push(id);
      deleteArticleByIds(ids).then(res => {
        console.log("删除",res);
        getArticleDataByLike()
      })

    }
    onMounted(()=>{
      getUserInfoData();
      getArticleDataByLike();
    })
    return{
      showArticle,
      userInfoData,
      articleListData,
      searchContent,
      size,current,total,
      getUserInfoData,
      getArticleListData,
      getArticleDataByLike,
      articleList,
      load,
      cancel,
      deleteArticle,
    }
  }
}
</script>

<style scoped>

.search{
  display: flex;
  background-color: #fff;
}
.search input{
  width: 50%;
  border-right: 0px;
  border-top: 1px solid black;
  border-left: 1px solid black;
  border-bottom: 1px solid black;
}
.contain-box{
  width: 100%;
  height: 100%;
  /*border: 1px solid black;*/
  padding: 15px 10px;
  background-color: #fff;

}
.item-box{
  padding: 0px 5px;
  /*background-color: #fff;*/
  margin-bottom: 10px;
  border-radius: 7px;
  /*background-color: #fff;*/
  border: 1px solid black;

}
.item-box:hover{
  transform: translate(2px, 2px);

}
.title-header{
  height: 50px;
  padding: 10px 5px;
  font-size: 1rem;
  font-weight: 500;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  border-bottom: 1px solid grey;
}
.title-header div{
  align-self: center;
}

.title-header button {
  margin-left: 5px;
  color: white;
  border-radius: 5px;
  font-size: 0.7rem;
  padding: 0px 13px;
}
.button-style button:nth-child(4){
  border: 1px solid rgb(217, 83, 79);
  background-color: rgb(217, 83, 79);
}
.button-style button:nth-child(3){
  border: 1px solid #87d068;
  background-color: #87d068;
}
.button-style button:nth-child(2){
  border: 1px solid #5742b6;
  background-color: #5742b6;
}
.button-style button:nth-child(1){
  border: 1px solid #337ab7;
  background-color: #337ab7;
}

.button-style button:nth-child(4):hover{
  background-color: #c9302c;
  border: 1px solid black;
}
.button-style button:nth-child(3):hover{
  background-color: #72d04c;
  border: 1px solid black;
}
.button-style button:nth-child(2):hover{
  background-color: #502890;
  border: 1px solid black;
}
.button-style button:nth-child(1):hover{
  background-color: #286090;
  border: 1px solid black;
}
.content-box{
  padding: 5px 10px;

}
.content-box span:nth-child(1){
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}
.content-box div{
  display: flex;
}
a{
  color: #0c0c0c;
}
</style>