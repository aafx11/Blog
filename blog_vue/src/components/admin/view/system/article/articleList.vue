<template>
  <!--搜索框,批量删除-->
  <el-form :inline="true" :model="searchForm" @submit.native.prevent class="top-container">
    <el-form-item>
      <el-input v-model="searchForm.articleTitle" placeholder="请输入文章相关信息" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <!--      <el-button @click="searchArticleListData">搜索</el-button>-->
      <my-button @click="searchArticleListData">搜索</my-button>
    </el-form-item>
    <el-form-item>
      <el-popconfirm
          title="确定删除吗？"
          @confirm="deleteHandle(null)"

      >
        <template #reference>
          <my-button type="danger" :disabled="delBtnState">批量删除</my-button>
        </template>
      </el-popconfirm>
    </el-form-item>
  </el-form>
  <!--  <el-button type="danger" :disabled="delBtnState">批量删除</el-button>-->
  <!--文章信息列表-->
  <el-table ref="articleTable" border stripe :data="articleListData" tooltip-effect="dark" style="width: 100%"
            @selection-change="handleSelectionChange" v-if="showArticleList">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="文章id" width="100"></el-table-column>
    <el-table-column prop="title" label="标题" width="170"></el-table-column>
    <el-table-column label="文章简介" width="230">
      <template #default="scope">
        <el-tooltip effect="dark" placement="top">
          <template #content>
            <div class="tooltip">{{ scope.row.introduction }}</div>
          </template>
          <div class="text-style">{{ scope.row.introduction }}</div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column label="作者" width="200">
      <template #default="scope">
        <div class="userInfo-box">
          <el-avatar :size="32" :src="'http://localhost:8081/static/avatar/'+scope.row.avatar"
                     @error="errorHandler"></el-avatar>
          <span>{{ scope.row.nickname }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="点赞" width="70">
      <template #default="scope">
        <div class="iconfont icon-dianzan icon-style">{{ scope.row.likeCount }}</div>
      </template>
    </el-table-column>
    <el-table-column label="收藏" width="70">
      <template #default="scope">
        <div class="iconfont icon-shoucang icon-style">{{ scope.row.collects }}</div>
      </template>
    </el-table-column>
    <el-table-column label="文章封面" width="140">
      <template #default="scope">
        <el-image
            style="width: 100px; height: 100px"
            :src="'http://localhost:8081/static/articleCover/'+scope.row.cover"
        >
        </el-image>
      </template>
    </el-table-column>
    <el-table-column label="文章内容" width="140">
      <template #default="scope">
        <router-link :to="{name:'articleDetail',params:{id:scope.row.id}}" target="_blank">
          <!--          <el-button type="primary">点击进入文章</el-button>-->
          <div class="link-box">
            <my-button type="primary" class="link-button">进入文章</my-button>
          </div>
        </router-link>
      </template>
    </el-table-column>
    <el-table-column label="标签">
      <template #default="scope">
        <div v-if="scope.row.tags">
          <el-tag v-for="item in scope.row.tags">{{ item.name }}</el-tag>
        </div>
        <span v-else>无标签</span>
      </template>
    </el-table-column>
    <el-table-column label="日期" width="160">
      <template #default="scope">
        <div class="iconfont icon-riqi1 icon-style">{{ scope.row.created }}</div>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <el-popconfirm
            title="确定删除吗？"
            @confirm="deleteHandle(scope.row.id)"

        >
          <template #reference>
            <div class="control-container">
              <my-button type="danger" letter-spacing>删除</my-button>
            </div>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
<!--  <el-button type="danger">删除</el-button>-->
  <!--分页-->
  <div class="bottom-container">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
                   :page-sizes="[10, 20, 50, 100]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
                   :total="total"></el-pagination>
  </div>
</template>

<script>
import {articleLists, deleteArticleByIds, ArticleListByLikeSearch} from '../../../../../request/api/article.js'
import {ref, reactive, getCurrentInstance, onMounted} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import myButton from "../../../../../view/myButton.vue";

export default {
  name: "articleList",
  components: {
    myButton: myButton
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const showArticleList = ref(false);
    const size = ref(10);
    const current = ref(1);
    const total = ref(100);
    const tab = ref('latest');
    const articleListData = reactive({});
    const multipleSelection = reactive([]);
    const delBtnState = ref(true)
    const searchForm = reactive({
      articleTitle: ''
    });

    const handleSizeChange = (val) => {
      console.log(`每页 ${val} 条`);
      size.value = val
      proxy.getArticleListData();
    }

    const handleCurrentChange = (val) => {
      console.log(`当前页: ${val}`);
      current.value = val
      proxy.getArticleListData();
    }

    const errorHandler = () => true;
    //获取文章列表
    const getArticleListData = () => {
      articleLists(current.value, size.value, tab.value).then(res => {
        const data = res.data.data;
        total.value = data.total;
        console.log("获取文章", res);
        proxy.articleListData = data.records;
        showArticleList.value = false;
        showArticleList.value = true;
      })
    }

    const searchArticleListData = () => {
      if (searchForm.articleTitle.split(" ").join("").length == 0) {
        proxy.getArticleListData();
      } else {
        ArticleListByLikeSearch(current.value, size.value, searchForm.articleTitle).then(res => {
          const data = res.data.data;
          total.value = data.total;

          proxy.articleListData = data.records;
          showArticleList.value = false;
          showArticleList.value = true;
        })
      }

    }
    const handleSelectionChange = (rows) => {
      proxy.multipleSelection = rows;
      console.log(proxy.multipleSelection);
      delBtnState.value = rows.length == 0
    }

    const deleteHandle = (id) => {
      const ids = [];

      if (id) {
        //单个删除
        ids.push(id);
      } else {
        proxy.multipleSelection.forEach(row => {
          ids.push(row.id)
        })
      }

      deleteArticleByIds(ids).then(res => {
        ElMessage({
          showClose: true,
          message: '删除成功',
          type: 'success',
          duration: 100,
          /*操作成功后的回调函数,更新数据*/
          onClose: () => {
            proxy.getArticleListData();
            proxy.$forceUpdate();
          }
        })
      })
    }


    onMounted(() => {
      getArticleListData();
    })
    return {
      delBtnState,
      searchForm,
      articleListData,
      showArticleList,
      multipleSelection,
      size, current, total, tab,
      errorHandler,
      handleSizeChange,
      handleCurrentChange,
      searchArticleListData,
      getArticleListData,
      deleteHandle,
      handleSelectionChange,
    }
  }
}
</script>

<style scoped>
.text-style {
  /*text-overflow: ellipsis;*/
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.tooltip {
  width: 200px;
}

.userInfo-box {
  display: flex;
  flex-direction: row;
  /*justify-content: space-around;*/
  align-items: center;
}

.userInfo-box span {
  margin-left: 10px;
}

.icon-style {
  font-size: 1.1rem;
}

.top-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 15px 10px 0 10px;
}

.control-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.link-box{
  display: flex;
  justify-content: center;
}
.bottom-container{
  display: flex;
  justify-content: flex-end;
  padding: 10px 10px;
}
</style>