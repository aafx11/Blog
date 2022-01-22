<template>
  <ArticleListHorizontal
    :article-data="articleList"
  >

  </ArticleListHorizontal>
  <!--分页-->
  <!--  <a-pagination-->
  <!--      show-size-changer-->
  <!--      v-model:current="current"-->
  <!--      v-model:pageSize="pageSize"-->
  <!--      :total="total"-->
  <!--      @showSizeChange="onShowSizeChange"-->

  <!--  />-->
  <!--加载中-->
  <div v-if="loadingImg" class="indicator">
    <a-spin :indicator="indicator"/>
    正在加载中，请稍等...
  </div>
  <!--所有文章加载完-->
  <div v-if="loadLastText" class="indicator">
    已经到底了
  </div>
</template>

<script>
import {ref, reactive, watch, getCurrentInstance, onMounted, computed, h} from 'vue'
import {onBeforeRouteLeave, useRouter} from "vue-router";
import {useStore} from 'vuex'
import {StarOutlined, LikeOutlined, MessageOutlined, CheckCircleTwoTone} from '@ant-design/icons-vue';
import {articleLists, likewise, unLikewise, collect, unCollect} from '../../../request/api/article.js'
import {message} from 'ant-design-vue';
import {LoadingOutlined} from '@ant-design/icons-vue';
import ArticleList from './ArticleList.vue'
import ArticleListHorizontal from './ArticleListHorizontal.vue'


export default {
  name: "hot",
  provide(){
    return{
      getArticleList:this.getArticleList
    }
  },
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    CheckCircleTwoTone,
    ArticleList:ArticleList,
    ArticleListHorizontal:ArticleListHorizontal
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const store = useStore();
    const activeName = ref('latest')
    const articleList = reactive([])


    const pageSize = ref(7);
    const current = ref(1);
    const total = ref()
    const tab = ref('hot')


    // watch(pageSize, () => {
    //   console.log('pageSize', pageSize.value);
    //   getArticleList();
    //
    // });
    watch(current, () => {
      getArticleList();
    });

    const getArticleList = () => {
      articleLists(current.value, pageSize.value, tab.value).then(res => {
        current.value = res.data.data.current
        pageSize.value = res.data.data.size
        total.value = res.data.data.total
        proxy.articleList = res.data.data.records
        proxy.$forceUpdate();
      })
    }



    const onShowSizeChange = (current, pageSize) => {
      console.log(current, pageSize);
    };


    const getLike = (articleId) => {
      likewise(articleId).then(res => {
        message.success(res.data.msg);
        getArticleList();
      })
    }

    const unLike = (articleId) => {
      unLikewise(articleId).then(res => {
        getArticleList();

      })
    }

    const collectArticle = (articleId) => {
      collect(articleId).then(res => {
        message.success(res.data.msg);
        getArticleList();
      })
    }
    const unCollectArticle = (articleId) => {
      unCollect(articleId).then(res => {
        getArticleList();
      })
    }

    const indicator = h(LoadingOutlined, {
      style: {
        fontSize: '18px',
        color: 'rgb(242, 151, 101)'
      },
      spin: true,
    });
    let isMoreLoad = ref(true);//是否加载更多
    let loadingImg = ref(false);//loading图片
    let loadLastText = ref(false);//全部加载完

    const scrollLoadMore = () => {
      //load图片达到了节流的效果,loadingImg在第一次执行函数时为true，在定时器时间内无法再执行，只有等函数执行完，
      //loadingImg变false才能继续执行
      if (loadingImg.value) {
        return;
      }
      loadingImg.value = true;

      pageSize.value += 3;
      setTimeout(() => {
        articleLists(current.value, pageSize.value, tab.value).then(res => {

          if (res.data.code == 200) {
            current.value = res.data.data.current
            pageSize.value = res.data.data.size
            total.value = res.data.data.total
            proxy.articleList = res.data.data.records

            proxy.$forceUpdate();
            if (total.value - current.value * pageSize.value > 0) {

              isMoreLoad.value = true;
            } else {
              isMoreLoad.value = false;
              loadLastText.value = true;
            }
            loadingImg.value = false;
          }

        })
      }, 300)


    }
    function articleScroll(){
      let scr = document.documentElement.scrollTop || document.body.scrollTop;//屏幕滑动高度
      let clientHeight = document.documentElement.clientHeight;//页面静止高度
      let scrHeight = document.documentElement.scrollHeight || document.body.scrollHeight;//整个网页的高度
      if (scr + clientHeight + 10 >= scrHeight) {

        if (isMoreLoad.value) { //this.isMoreLoad控制滚动是否加载更多
          scrollLoadMore();
        } else {
          return;
        }
      }
    }

    const scrollRoadMoreArticle = () => {
      window.addEventListener('scroll', articleScroll)
    }
    onBeforeRouteLeave(()=>{
      window.removeEventListener('scroll',articleScroll)
    })

    onMounted(() => {
      getArticleList();
      scrollRoadMoreArticle();

    })

    return {
      activeName,
      articleList,
      pageSize,
      current,
      total,
      tab,
      indicator,
      loadLastText,
      loadingImg,
      onShowSizeChange,
      getArticleList,
      getLike,
      unLike,
      collectArticle,
      unCollectArticle,
    }
  }
}
</script>

<style scoped>
.indicator {
  width: 100%;
  text-align: center;
  color: rgb(242, 151, 101);
  background-color: rgb(255, 253, 236);
  /*border: 1px solid red;*/
  padding: 4px 0px;
}
</style>