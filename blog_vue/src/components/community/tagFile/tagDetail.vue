<template>
<!--  <div>{{ tag }}:</div>-->
  <a-list item-layout="horizontal" :data-source="articleList">
    <template #renderItem="{ item ,index}">
      <a-list-item>
        <a-list-item-meta :description="item.content">
          <template #title>
            <router-link :to="{name:'articleDetail',params:{id:item.id}}">{{ item.title }}</router-link>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
  <a-pagination
      show-size-changer
      v-model:current="current"
      v-model:pageSize="pageSize"
      :total="total"
      @showSizeChange="onShowSizeChange"

  />
</template>

<script>
import {ref, reactive, watch, getCurrentInstance, onMounted, computed} from 'vue'
import {articleListByTag} from '../../../request/api/article.js'
import {onBeforeRouteUpdate, useRouter, useRoute} from "vue-router";
import {useStore} from 'vuex'


export default {
  name: "tagDetail",
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const route = useRoute();
    const store = useStore();
    const articleList = reactive([])


    const pageSize = ref(10);
    const current = ref(1);
    const total = ref()
    // const tag = route.params.tagName;

    watch(pageSize, () => {
      console.log('pageSize', pageSize.value);
      getArticleList();

    });
    watch(current, () => {
      console.log('current', current.value);
      getArticleList();
    });

    const getArticleList = () => {
      articleListByTag(current.value, pageSize.value, route.params.id).then(res => {
        current.value = res.data.data.current
        pageSize.value = res.data.data.size
        total.value = res.data.data.total
        proxy.articleList = res.data.data.records
        console.log("获取的文章", proxy.articleList);
        proxy.$forceUpdate();
      })
    }


    const onShowSizeChange = (current, pageSize) => {
      console.log(current, pageSize);
    };


    onMounted(() => {
      getArticleList();
    })


    return {
      articleList,
      pageSize,
      current,
      total,
      // tag,
      onShowSizeChange,
      getArticleList,
    }
  },
  activated() {
    this.getArticleList();
  },
  methods:{
    test(){
      console.log(this.$store.getters['getToken']);
    }
  }
}
</script>

<style scoped>

</style>