<template>
  <a-row style="height: 100%;">
    <a-col :lg="{span:11}" :md="{span:8}" :sm="{span:24}" :xs="{span:24}">
      <h2 class="h2">
        <span  class="menu-article1">Popular</span>
        <span  class="menu-article2">articles</span>
      </h2>
    </a-col>
    <!--文章列表-->
    <a-col :lg="{span:13}" :md="{span:16}" :sm="{span:24}" :xs="{span:24}"  class="articleList-border">
      <p class="para">
        More of my design and development work on platforms like dribbble, behance and codepen.
      </p>
      <a v-for="item in articleList" class="articleList-item" @mouseenter="enter($event)" @mouseleave="leave($event)">
        <div style="display: none">
          <img :src="item.cover" alt="">
        </div>
        <div>
          <div class="article-title">
            <h3>{{item.title}}</h3>
          </div>
          <div class="article-tag">
            <span>{{item.nickname}}</span>
            <span>{{item.introduction}}</span>
          </div>
        </div>
      </a>
      <a-pagination
          show-size-changer
          v-model:current="current"
          v-model:pageSize="pageSize"
          :total="total"
          @showSizeChange="onShowSizeChange"

      />
    </a-col>
  </a-row>


</template>

<script>
import {ref, reactive,watch, getCurrentInstance, onMounted,computed} from 'vue'
import {articleLists} from '../../../request/api/article.js'
import { onBeforeRouteUpdate,useRouter } from "vue-router";
import {useStore} from 'vuex'

export default {
  name: "hotArticle",
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const store = useStore();
    const activeName = ref('latest')
    const articleList = reactive([])


    const pageSize = ref(10);
    const current = ref(1);
    const total = ref()
    const tab = ref('hot')



    watch(pageSize, () => {
      console.log('pageSize', pageSize.value);
      getArticleList();

    });
    watch(current, () => {
      console.log('current', current.value);
      getArticleList();
    });

    const getArticleList = () => {
      articleLists(current.value, pageSize.value,tab.value).then(res => {
        current.value = res.data.data.current
        pageSize.value = res.data.data.size
        total.value = res.data.data.total
        proxy.articleList = res.data.data.records
        console.log("获取的文章",proxy.articleList);
        proxy.$forceUpdate();
      })
    }



    const onShowSizeChange = (current, pageSize) => {
      console.log(current, pageSize);
    };


    const enter = (e)=>{
      const pic = document.querySelector('.outer-yuan');
      pic.classList.add('expand');
      const imageSrc = e.currentTarget.querySelector('img').src;
      pic.style.backgroundImage = `url(${imageSrc})`;
    }
    const leave = (e)=>{
      const pic = document.querySelector('.outer-yuan');
      pic.classList.remove('expand');
      console.log("离开");
      pic.style.backgroundImage = '';
    }

    onMounted(() => {
      getArticleList();
    })

    return {
      activeName,
      articleList,
      pageSize,
      current,
      total,
      tab,
      onShowSizeChange,
      getArticleList,
      enter,
      leave,
    }
  }
}
</script>

<style scoped>
.articleList-border{
  margin-top: 70px;
  z-index: 100;
  /*height: 100vh;*/
  /*border: 1px solid blue;*/
}
.articleList-item{
  padding: 45px 0;
  width: 75%;
  height: 185px;
  display: block;
  border-bottom: 1px solid #ddd;
}
.article-title h3 {
  font-size: 2.1rem;
  font-weight: 400;
  line-height: 1.28;
  display: block;
  /*font-family: "TT Hoves",-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Oxygen,Ubuntu,Cantarell,"Open Sans","Helvetica Neue",sans-serif;*/
  font-family: "TT Hoves", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}
.article-tag span{
  /*color: rgb(85,85,85);*/
  color: #555;
  font-size: 18px;
  display: inline-block;
  /*font-size: 1.6rem;*/
  font-weight: 400;
  margin-right: 15px;
}

.h2{
  /*display: flex;*/
  font-size: 81px;
  font-weight: 400;
  font-family: "TT Hoves", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  margin-top: 50px;
}
.h2 span{
  display: block;
  /*margin-left: 25%;*/

}
.menu-article1{
  /*margin-left: 50px;*/
  margin-left: 25%;
}
.menu-article2{
  /*margin-left: 50px;*/
  margin-left: 35%;
}

.para{
  font-family: "TT Hoves", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  font-size: 22px;
}

</style>