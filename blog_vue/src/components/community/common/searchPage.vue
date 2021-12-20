<template>
  <a-row class="contain-box" justify="center" :wrap="true" v-if="showEmpty" v-infinite-scroll="load">
    <a-col :xs="{ span: 20 }" :md="{ span: 10, offset: 1 }" :lg="{ span: 5, offset: 0 }"
           v-for="(item,index) in articleData" class="col-style">
      <div class="">
        <div class="header">
          <div v-if="index+1<10">
            <span>0</span>{{index+1}}
          </div>
          <div v-else>
            {{index+1}}
          </div>
        </div>
        <articleCard :article="item"></articleCard>
      </div>
    </a-col>
  </a-row>
    <div v-else><a-empty description="暂无搜索结果，换一个关键词吧 "/></div>

</template>

<script>
import {computed, defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';
import {ArticleListByLikeSearch} from '../../../request/api/article.js'
import {useRoute, useRouter} from "vue-router";
import home_Nav from '../home/home_Nav.vue'
import articleCard from '../index/articleCard.vue'
export default {
  name: "searchPage",
  components:{
    articleCard:articleCard,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const route = useRoute();
    const current = ref(1);
    const size = ref(10);
    const total = ref(0);
    const showEmpty = ref(false);

    const articleData = reactive({});
    const getArticleListByLikeSearch = () => {
      const content = route.params.content;
      ArticleListByLikeSearch(current.value, size.value, content).then(res => {
        console.log(res);
        const data = res.data.data;
        proxy.articleData = data.records;
        if (proxy.articleData.length !=0){
          console.log("不为空");
          showEmpty.value = false;
          showEmpty.value = true;
        }
        console.log("文章数据",proxy.articleData);
      })
    }

    const load = () => {
      console.log("滚动加载");
      size.value = size.value+5;
      getArticleListByLikeSearch();
      proxy.$forceUpdate();
    }
    onMounted(() => {

      console.log("路由参数", route.params.content);
      getArticleListByLikeSearch();

    })

    return {
      showEmpty,
      articleData,
      load,
      ArticleListByLikeSearch,
    }
  }
}
</script>

<style scoped>
.col-style{
  /*height: 560px;*/
}
.contain-box{
  width: 100%;
  height: 100%;
  /*border: 1px solid blue;*/
}
.header{
  /*height: 15%;*/
  /*border: 1px solid red;*/
}
.header div{
  font-size: 2.8rem;
  margin-left: 0.9rem;
  font-family: 'Open Sans';
  /*margin-bottom: 0.5rem;*/
}

</style>