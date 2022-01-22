<template>
  <el-table ref="articleTable" border stripe :data="state.commentData" tooltip-effect="dark" style="width: 100%"
            v-if="state.show">
    <el-table-column prop="id" label="评论id" width="70"></el-table-column>
    <el-table-column label="用户" width="200">
      <template #default="scope">
        <div class="userInfo-box">
          <img :src="'http://localhost:8081/static/avatar/'+scope.row.avatar"/>
          <span>{{ scope.row.nickname }}</span>
        </div>
      </template>
    </el-table-column>
    <!--    <el-table-column  label="评论内容" width="800">-->
    <!--      <template #default="scope">-->
    <!--        <el-tooltip effect="dark" placement="top">-->
    <!--          <template #content>-->
    <!--            <div class="tooltip">{{ scope.row.content }}</div>-->
    <!--          </template>-->
    <!--          <div class="text-style">{{ scope.row.content }}</div>-->
    <!--        </el-tooltip>-->
    <!--      </template>-->
    <!--    </el-table-column>-->
    <el-table-column prop="content" label="评论内容" width="850"></el-table-column>
    <el-table-column label="状态" width="70">
      <template #default="scope">
        <!--        <span v-if="scope.row.state==1">正常</span>-->
        <my-button v-if="scope.row.state==1" type="success" size="mini">正常</my-button>

        <!--        <span v-else>封禁</span>-->
        <my-button v-else type="danger" size="mini">封禁</my-button>
      </template>
    </el-table-column>
    <el-table-column label="日期" width="150">
      <template #default="scope">
        <div class="iconfont icon-riqi1 icon-style">{{ scope.row.created }}</div>
      </template>
    </el-table-column>

    <el-table-column label="进入文章" width="130">
      <template #default="scope">
        <my-button type="primary" @click="pushArticle(scope.row.articleId)">进入文章</my-button>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="140">
      <template #default="scope">
        <el-popconfirm
            title="确定修改评论状态吗？"
            @confirm="modifyState(scope.row.id)"

        >
          <template #reference>
            <div class="control-container">
              <my-button type="danger" letter-spacing v-if="scope.row.state==1" >
                封禁该评论
              </my-button>
              <my-button type="success" letter-spacing v-else >解除封禁</my-button>

            </div>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <div class="bottom-container">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page.current"
                   :page-sizes="[10, 20, 50, 100]" :page-size="page.size"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="page.total"></el-pagination>
  </div>
</template>

<script>
import {getCommentListByPage,updateCommentState} from '../../../../../request/api/information.js'
import {onBeforeMount, onMounted, reactive} from "vue";
import {useRouter} from "vue-router";
import {ElMessage} from 'element-plus';
import myButton from '../../../../../view/myButton.vue'

export default {
  name: "comment",
  components: {
    myButton
  },
  setup() {
    const router = useRouter()

    // 获取全部评论
    let page = reactive({
      size: 15,
      current: 1,
      total: 100
    })
    let state = reactive({
      commentData: {},
      show: false
    })
    const getCommentData = () => {
      getCommentListByPage(page.current, page.size).then(res => {
        console.log('评论', res);
        state.commentData = res.data.data.records;
        page.total = res.data.data.total;
        state.show = false;
        state.show = true;
      })
    }

    onBeforeMount(() => {
      getCommentData()
    })

    // 修改页码和页数
    const handleSizeChange = (val) => {
      console.log(`每页 ${val} 条`);
      page.size = val
      getCommentData();
    }

    const handleCurrentChange = (val) => {
      console.log(`当前页: ${val}`);
      page.current = val
      getCommentData();
    }

    // 跳转至文章详情页点击事件
    const pushArticle = (articleId) => {
      router.push({name: 'articleDetail', params: {id: articleId}})
    }

    // 修改文章状态点击事件
    const modifyState = (id) => {
      console.log(id);
      updateCommentState(id).then(res=>{
        console.log(res);
        if (res.data.code == 200){
          ElMessage.success({
            showClose: true,
            message: '修改成功',
            // type: 'success',
            duration: 1000,
            /*操作成功后的回调函数,更新数据*/
            onClose: () => {
              getCommentData();
            }
          })
        } else {
          ElMessage.info({
            showClose: true,
            message: '修改失败',
            // type: 'success',
            duration: 1000,
            /*操作成功后的回调函数,更新数据*/
            onClose: () => {
              getCommentData();
            }
          })
        }

        getCommentData();
      })
    }
    return {
      state,
      page,
      pushArticle,
      modifyState,
      handleSizeChange,
      handleCurrentChange
    }
  }

}
</script>

<style lang="scss" scoped>
.userInfo-box {
  font-size: 16px;
  display: flex;
  flex-direction: row;
  //justify-content: space-around;
  justify-content: flex-start;
  align-items: center;

  img {
    width: 35px;
    height: 35px;
    border-radius: 50%;
    object-fit: cover;
  }
}

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

.bottom-container {
  display: flex;
  justify-content: flex-end;
  padding: 10px 10px;
}
</style>