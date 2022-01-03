<template>
  <!--搜索框,筛选,批量删除-->
  <el-form :inline="true" :model="searchForm" @submit.native.prevent class="top-container">
    <el-form-item>
      <el-input v-model="searchForm.searchContent" placeholder="请输入视频相关信息" clearable></el-input>
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
    <!--    <el-button type="danger" :disabled="delBtnState">批量删除</el-button>-->

    <el-form-item>
      <el-dropdown>
        <!--        <el-button type="primary">-->
        <!--          筛选<i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
        <!--        </el-button>-->
        <my-button type="primary">
          筛选
          <i class="el-icon-arrow-down el-icon&#45;&#45;right"></i>
        </my-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="tab='all'">全部</el-dropdown-item>
            <el-dropdown-item @click="tab='passed'">已通过审核</el-dropdown-item>
            <el-dropdown-item @click="tab = 'reviewed'">待审核</el-dropdown-item>
            <el-dropdown-item @click="tab = 'fail'">审核不通过</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-form-item>
  </el-form>

  <!--视频信息列表-->
  <el-table ref="videoTable" :data="videoData"
            @selection-change="handleSelectionChange" v-if="showVideoList"
            border stripe>
    <el-table-column type="selection"></el-table-column>
    <el-table-column prop="id" label="视频id" width="80"></el-table-column>
    <el-table-column prop="title" label="标题" width="150"></el-table-column>
    <el-table-column label="视频简介" width="200">
      <template #default="scope">
        <el-tooltip effect="dark" placement="top">
          <template #content>
            <div class="tooltip">{{ scope.row.introduction }}</div>
          </template>
          <div class="text-style">{{ scope.row.introduction }}</div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column label="作者" width="150">
      <template #default="scope">
        <div class="userInfo-box">
          <el-avatar :size="32" :src="scope.row.avatar" @error="errorHandler"></el-avatar>
          <span>{{ scope.row.nickname }}</span>
        </div>
      </template>
    </el-table-column>
    <!--    <el-table-column  label="点赞" width="70" >-->
    <!--      <template #default="scope">-->
    <!--        <div class="iconfont icon-dianzan icon-style">{{scope.row.likeCount}}</div>-->
    <!--      </template>-->
    <!--    </el-table-column>-->
    <!--    <el-table-column  label="收藏" width="70">-->
    <!--      <template #default="scope">-->
    <!--        <div class="iconfont icon-shoucang icon-style">{{scope.row.collects}}</div>-->
    <!--      </template>-->
    <!--    </el-table-column>-->
    <el-table-column label="视频封面" width="140">
      <template #default="scope">
        <el-image
            style="width: 100px; height: 100px"
            :src="'http://localhost:8081/static/videoCover/'+scope.row.coverUrl"
        >
        </el-image>
      </template>
    </el-table-column>
    <el-table-column label="视频内容" width="150">
      <template #default="scope">
        <router-link :to="{name:'videoDetail',params:{id:scope.row.id}}" target="_blank">
          <!--          <el-button type="primary" size="small">点击进入视频页</el-button>-->
          <my-button type="primary" size="mini">点击进入视频页</my-button>
        </router-link>
      </template>
    </el-table-column>
    <el-table-column label="标签" width="150">
      <template #default="scope">
        <div v-if="scope.row.tags">
          <el-tag v-for="item in scope.row.tags">{{ item.name }}</el-tag>
        </div>
        <span v-else>无标签</span>
      </template>
    </el-table-column>
    <el-table-column label="日期" width="140">
      <template #default="scope">
        <div class="iconfont icon-riqi1 icon-style">{{ scope.row.created }}</div>
      </template>
    </el-table-column>
    <el-table-column label="状态" width="100">
      <template #default="scope">
        <div v-if="scope.row.state == 0" class="under-review">
          <HourglassOutlined/>
          待审核
        </div>
        <div v-else-if="scope.row.state == 1" class="passed">
          <span class="iconfont icon-tongguo "></span>
          通过
        </div>
        <div v-else class="ban">
          <ExclamationCircleOutlined/>
          <span>审核不通过</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="审核评语" width="100">
      <template #default="scope">
        <el-tooltip effect="dark" placement="top">
          <template #content>
            <div class="tooltip">{{ scope.row.examine }}</div>
          </template>
          <div class="text-style">{{ scope.row.examine }}</div>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column label="操作">
      <template #default="scope">
        <div class="edit-box">
          <router-link :to="{name:'editVideo',params:{id:scope.row.id}}" class="edit-button">
            <!--            <div>-->
            <!--              <EditOutlined/>-->
            <!--              编辑-->
            <!--            </div>-->
            <my-button type="primary">
              编辑
              <EditOutlined/>
            </my-button>
          </router-link>
          <el-popconfirm
              title="确定删除吗" @confirm="deleteHandle(scope.row.id)"
          >
            <template #reference>

              <my-button type="danger">
                删除
                <DeleteOutlined/>
              </my-button>
            </template>
          </el-popconfirm>
        </div>
      </template>
    </el-table-column>
  </el-table>

  <!--              <div class="delete-button">-->
  <!--                <DeleteOutlined/>-->
  <!--                删除-->
  <!--              </div>-->

  <!--分页-->
  <div class="bottom-container">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
                   :page-sizes="[10, 20, 50, 100]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
                   :total="total"></el-pagination>
  </div>
</template>

<script>
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {getVideoByStateOrLike, deleteVideoByVideoId} from '../../../../../request/api/video.js'
import {ElMessageBox, ElMessage} from 'element-plus';
import myButton from "../../../../../view/myButton.vue";
import {HourglassOutlined, ExclamationCircleOutlined, EditOutlined, DeleteOutlined} from '@ant-design/icons-vue';

export default {
  name: "VideoList",
  components: {
    myButton,
    HourglassOutlined,
    ExclamationCircleOutlined,
    EditOutlined,
    DeleteOutlined
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const delBtnState = ref(true);
    const multipleSelection = reactive([]);

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
      deleteVideoByVideoId(ids).then(res => {
        ElMessage({
          showClose: true,
          message: '删除成功',
          type: 'success',
          duration: 1000,
          onClose: () => {
            getVideoData();
          }
        })
      })
    }

    const showVideoList = ref(false);
    const current = ref(1);
    const size = ref(10);
    const total = ref(50);
    const videoData = reactive();
    const tab = ref('all');
    const searchForm = reactive({
      searchContent: ''
    })

    const getVideoData = () => {
      getVideoByStateOrLike(current.value, size.value, proxy.searchForm.searchContent, tab.value).then(res => {
        console.log("返回视频", res);
        const data = res.data.data;
        proxy.videoData = data.records;
        total.value = data.total;

        showVideoList.value = false;
        showVideoList.value = true;
      })
    }

    watch(() => searchForm.searchContent, () => {
      current.value = 1;
      size.value = 10;
      tab.value = 'all';
      getVideoData();
    })
    watch(() => tab.value, () => {
      console.log("watch+++", tab.value);
      current.value = 1;
      size.value = 10;
      getVideoData();
    })

    const handleSizeChange = (val) => {
      console.log(`每页 ${val} 条`);
      size.value = val
      getVideoData();
    }

    const handleCurrentChange = (val) => {
      console.log(`当前页: ${val}`);
      current.value = val
      getVideoData();
    }

    const errorHandler = () => true;
    onMounted(() => {
      getVideoData();
    })
    return {
      delBtnState,
      showVideoList,
      multipleSelection,
      current, size, total,
      videoData, tab, searchForm,
      getVideoData,
      deleteHandle,
      errorHandler,
      handleSizeChange,
      handleCurrentChange,
      handleSelectionChange,

    }
  }
}
</script>

<style scoped>
.container-box {
  width: 100%;
  height: 100%;
  border: 1px solid black;
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

.passed {
  font-size: 0.9rem;
  color: #68bb8d;
}

.under-review {

  color: #00a1d6;
  font-size: 0.9rem;
}

.ban {
  font-size: 0.9rem;
  color: #fa5a57;

}

.edit-box {
  display: flex;
  justify-content: space-around;
}

.edit-button, .edit-button + div {
  display: inline-block;
  /*padding: 5px 15px;*/
  border: 1px solid rgb(231, 231, 231);
  border-radius: 5px;
}

.delete-button {
  margin-left: 20px;
}

.edit-button:hover, .edit-button + div:hover {
  border: 1px solid #00a1d6;
}

.problem {
  display: inline-block;

  text-decoration: underline;
}

a {
  color: black;
}

.top-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 15px 10px 0 10px;
}

.bottom-container {
  display: flex;
  justify-content: flex-end;
  padding: 10px 10px;
}
</style>