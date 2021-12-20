<template>
  <!--文章列表-->
  <div class="container-box" v-for="(item,index) in article" >
    <!--头部-->
    <div class="header">
      <div class="header-avatar">
        <!--头像-->
        <a-avatar :size="52" :src="item.avatar"></a-avatar>
      </div>
      <!--nickname-->
      <div class="header-nickname">
        <span v-text="item.nickname" class="nickname"></span>

      </div>
    </div>
    <!--main-->
    <div class="main-box">
      <div class="main-left">
        <!--文章标题-->
        <h1 v-text="item.title"></h1>
        <!--文章tags-->
        <span v-for="tag in item.tags">
          <a-tag color="cyan">{{tag.name}}</a-tag>
        </span>
        <!--分割线-->
        <div class="hr-line"></div>
        <!--文章简介-->
        <p v-text="item.introduction" class="article-introduction" ></p>
      </div>
      <div class="main-right">
<!--        <a-image :src="item.cover" :width="400" :height="300" class="article-cover"></a-image>-->
        <img :src="item.cover" class="article-cover">
      </div>
    </div>
    <div class="footer-box">
      <!--点赞-->
      <span class="like" v-if="item.hasLike" :key="item.userId">
            <LikeOutlined :style="{fontSize: '20px', color: '#08c'}" @click="unLike(item.id)"/>{{ item.likeCount }}
      </span>
      <span class="unLike" v-else :key="item.id">
            <LikeOutlined :style="{fontSize: '20px'}" @click="getLike(item.id)"/>{{ item.likeCount }}
      </span>
      <!--取消收藏-->
      <span class="collect" v-if="item.hasCollect" :key="item.userId">
            <StarOutlined :style="{fontSize: '20px', color: '#08c'}"
                          @click="unCollectArticle(item.id)"/>{{ item.collects }}
      </span>
      <!--收藏-->
      <span class="collect" v-else :key="item.id">
            <StarOutlined :style="{fontSize: '20px'}" @click="collectArticle(item.id)"/>{{ item.collects }}
      </span>
      <span>
        <MessageOutlined :style="{fontSize: '20px'}"/>
      </span>
    </div>
  </div>
</template>

<script>
import {StarOutlined, LikeOutlined, MessageOutlined} from '@ant-design/icons-vue';
import {likewise,unLikewise,collect,unCollect} from "../../../request/api/article";
import {getCurrentInstance} from "vue";
import { message } from 'ant-design-vue';


export default {
  name: "ArticleList",
  inject: ['getArticleList'],
  props:{
    article:{
      type:Object,
      default:null,
    }
  },
  components:{
    StarOutlined,
    LikeOutlined,
    MessageOutlined
  },
  setup(){
    const {proxy} = getCurrentInstance();
    const getLike = (articleId) => {
      likewise(articleId).then(res => {
        message.success(res.data.msg);
        proxy.getArticleList();
      })
    }

    const unLike = (articleId) =>{
      unLikewise(articleId).then(res => {
        proxy.getArticleList();

      })
    }

    const collectArticle = (articleId) => {
      collect(articleId).then(res => {
        message.success(res.data.msg);
        proxy.getArticleList();
      })
    }
    const unCollectArticle = (articleId) => {
      unCollect(articleId).then(res => {
        proxy.getArticleList();
      })
    }

    const test=()=>{
      proxy.getArticleList();

    }
    return{
      getLike,
      unLike,
      collectArticle,
      unCollectArticle,
      test,
    }
  }
}
</script>

<style scoped>
.container-box{
  height: 420px;
  /*min-width: 750px;*/
  width: 100%;
  margin-bottom: 10px;
  padding: 10px 10px 5px 10px;
  /*border: 1px solid blueviolet;*/
  background-color: #FFF;

}



.header{
  height: 60px;
  width: 100%;
}
.header-avatar{
  width: 60px;
  margin-left: 10px;
  float: left;
}
.header-nickname{
  margin-top: 5px;
  margin-left: 5px;
  float: left;
}
.nickname{
  font-family: system-ui;
  font-weight: 700;
}
.main-box{
  width: 100%;
  height: 300px;
  margin-bottom: 25px;
}
.main-left{
  width: 35%;
  float: left;

  /*border: 1px solid royalblue;*/
}


.main-left h1{
  font-size: 1rem;
  font-family: Roboto;
  font-weight: 700;
}
.hr-line{
  border-bottom: 1px solid rgb(195,195,195);
  margin-top: 10px;
  margin-bottom: 20px;
}
.article-introduction{
  font-size: 0.9rem;
  line-height: 1.5rem;
  text-align: justify;
  font-family: Roboto;

  width: 90%;
  overflow: hidden;
  display: -webkit-box;
  -webkit-box-orient:vertical;
  -webkit-line-clamp:7;
}
.article-cover{
  width: 100%;
  height: 300px;
  box-shadow: 10px 5px 35px 0px rgba(0, 0, 0, 0.75);
}
.main-right{
  margin-left: 2%;
  /*padding-top: 15px;*/
  /*width: 66%;*/
  float: right;
  width: 60%;
  /*height: 100%;*/
  /*border: 1px solid sienna;*/
}
.footer-box{
  /*margin-top: 10px;*/
  height: 10%;
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content:space-around;
}
</style>