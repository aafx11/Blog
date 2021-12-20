<template>
  <a-row class="row">
    <a-col :lg="{span:2,offset:5}" :md="{span:5}" :xs="{span:0}" class="left">
      <div class="title">
        <span class="title_font">文章分类</span>
      </div>
      <a-menu
          mode="inline"
          v-model:selectedKeys="selectedKeys"
      >

        <a-menu-item  v-for="(item,index) in tags.slice(0,size)" :key="item.name" >
          <UserOutlined/>
          <span class="nav-text">
              <router-link :to="{name:'tagDetail',params:{id:item.id}}" >
                {{item.name}}({{item.articleCount}})
              </router-link>
            </span>
        </a-menu-item>

      </a-menu>
      <a-button  v-if="size < total" @click='loadMore' >
        点击加载更多
      </a-button>
      <div v-else >没有更多了</div>
    </a-col>
    <a-col :lg="{span:12}" :md="{span:17}" :xs="{span:24}" class="center">
      <router-view v-if="show" :key="$route.path" ></router-view>
    </a-col>
  </a-row>
</template>

<script>
import {defineComponent, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import {UserOutlined, UserSwitchOutlined, UsergroupAddOutlined} from '@ant-design/icons-vue';
import {getTagList} from '../../../request/api/tag.js'
import tagDetail from './tagDetail.vue'
export default defineComponent({
  name: "tag",
  components: {
    UserOutlined,
    UserSwitchOutlined,
    UsergroupAddOutlined,
    tagDetail:tagDetail,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const selectedKeys = ref(['']);
    const tags = reactive([]);
    const show = ref(true)
    const size = ref(6)
    const total = ref();
    const tagList = () => {
      getTagList(size.value).then(res => {
        proxy.tags= res.data.data.records;
        total.value = res.data.data.total;
        proxy.$forceUpdate();
        console.log(res);

      })
    }

    const loadMore = () => {
      size.value = size.value +3;
      proxy.tagList();
    }

    onMounted(()=>{
      tagList();
    })
    return {
      selectedKeys,
      tags,
      size,
      total,
      show,
      tagList,
      loadMore,
    }
  }
})
</script>

<style scoped>
/*整体布局------------------------------------start*/
.row {
  margin-top: 7px;
  height: 100vh%;
}

.left {
  /*border: 1px solid red;*/
  margin-right: 10px;
  min-width: 40px!important;
  background-color: rgb(255, 255, 255);
}

.center {
  /*border: 1px solid black;*/
  margin-right: 10px;
  background-color: rgb(255, 255, 255);
}

/*整体布局------------------------------------end*/

/*左侧导航------------------------------------start*/
.title{
  margin: 10px 0;
}
.title_font{
  padding: 10px 16px;
  font-weight: 500;
  font-size: 22px;
}
/*左侧导航------------------------------------end*/


</style>