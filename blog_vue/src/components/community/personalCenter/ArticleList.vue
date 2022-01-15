<template>

  <div   class="contain-box">
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
              v-if="!isOrderUser"
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
export default {
  name: "ArticleList",
  props:{
    articleListData:{
      type:Object,
      default:null
    },
    isOrderUser:{
      type:Boolean,
      required: false
    }

  },
  setup(){

  }
}
</script>

<style scoped>
.search{
  display: flex;
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

}
.item-box{
  padding: 0px 5px;
  background-color: #fff;
  margin-bottom: 10px;
  border-radius: 7px;
}
.item-box:hover{
  transform: translate(2px, 2px);

}
.title-header{
  height: 50px;
  padding: 10px 15px;
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