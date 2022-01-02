<template>
  <div class="content-box"
       :style="{'background-image':'url(http://localhost:8081/static/articleCover/'+article.cover+')','background-size':'cover',}"
       @mouseenter="mouse(article.id)" @mouseleave="leave(article.cover,article.id)" :data-index="article.id"
        @click="pushArticleDetail(article.id)"
  >
    <div class="date">
      <span>{{article.created}}</span>
    </div>
    <div class="article-info">
      <span class="title">{{article.title}}</span>
      <div class="introduction">{{article.introduction}}</div>
      <div>
        <span v-for="tag in article.tags"  class="tag">{{tag.name}}</span>
      </div>
    </div>
  </div>

</template>

<script>
import {useRoute, useRouter} from "vue-router";
export default {
  name: "articleCard",
  props:{
    article:{
      type:Object,
      default:null
    }
  },
  setup(){
    const router = useRouter();

    const mouse = (id) =>{
      const index = '\'' + id + '\'';
      let contain = document.querySelector("div[data-index=" + index + "]");
      contain.style = 'none'
      contain.style.backgroundColor = '#7d7d7d'
      console.log(contain.style);
    }
    const leave = (cover,id) =>{
      const index = '\'' + id + '\'';
      let contain = document.querySelector("div[data-index=" + index + "]");

      contain.style.backgroundImage="url(http://localhost:8081/static/articleCover/"+cover+")";
      contain.style.backgroundSize='cover';

      console.log(contain.style);
    }

    const pushArticleDetail = (articleId) => {
      router.push({name:'articleDetail',params:{id:articleId}})
    }

    return {
      mouse,
      leave,
      pushArticleDetail,
    }
  }
}
</script>

<style scoped>
.content-box{
  position: relative;
  height: 410px;
  width: 85%;
  cursor: pointer;
  padding: 1rem;
  border-radius: 1rem;
  border: 10px solid #ccc;
  overflow: hidden;
}

.content-box:hover {
  transform: rotate(0);
}

.content-box:hover .title,.content-box:hover .introduction
,.content-box:hover .date span{
  color: black;
}
.content-box:hover .tag{
  background-color: #3c3163;
  color: white;
}
.content-box:before, .content-box:after {
  content: '';
  transform: scale(0);
  transform-origin: top left;
  border-radius: 50%;
  position: absolute;
  left: -50%;
  top: -50%;
  z-index: -5;
  transition: all, 0.8s;
  background-color: white;
  transition-timing-function: ease-in-out;
}

.content-box:before {
  background: #ddd;
  width: 250%;
  height: 250%;
}

.content-box:after {
  background: white;
  width: 200%;
  height: 200%;
}

.content-box:hover:before, .content-box:hover:after {
  transform: scale(1);
}

.date{
  position: absolute;
  top: 3%;
  right: 5%;
}
.date span{
  color: white;
  font-size: 0.9rem;
  /*font-weight: 600;*/
  font-family: 'Heebo';
}
.article-info{
  position: absolute;
  bottom: 10%;

}
.title{
  color: white;
  font-size: 1.5rem;
  font-family: 'Heebo';
}

.introduction{
  color: white;
  font-family: 'Heebo';
  font-size: 1rem;
  font-weight: 600;
  line-height: 2rem;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}
.tag{
  padding: 3px 7px;
  font-size: 0.9rem;
  border-radius: 7px;
  width: 50px;
  background-color: #7e7e7e;
  text-align: center;
  margin-right: 5px;
  color: white;
}

</style>