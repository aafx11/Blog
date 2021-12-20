<template>
  <div class="manuscript-box">
    <div class="manuscript-header">
      <div>稿件管理</div>
      <div>
        <a-input-search v-model:value="searchContent" placeholder="搜索稿件" style=" border-radius: 5px;"/>
      </div>
    </div>
    <div class="manuscript-main">
      <videoList :videoList="videoData" v-if="showList"></videoList>
      <div>
        <a-pagination
            :total="total"
            v-model:current="current"
            v-model:pageSize="size"
            size="small"
            show-size-changer
            show-quick-jumper />
      </div>
    </div>

  </div>

</template>

<script>
import {getVideoListByUserId} from '../../../request/api/video.js'
import {getCurrentInstance, onMounted, reactive, ref, watch} from "vue";
import {message} from "ant-design-vue";
import {useRoute, useRouter} from "vue-router"
import videoList from './videoList.vue'

export default {
  name: "Manuscript",
  components:{
    videoList:videoList,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const current = ref(1);
    const size = ref(5);
    const total = ref(50);
    const searchContent = ref('');
    const videoData = reactive();
    const showList = ref(false);

    const getVideoDataByLike = () => {
      getVideoListByUserId(current.value, size.value, searchContent.value).then(res => {
        console.log("视频", res);
        const data = res.data.data;
        total.value = data.total;
        proxy.videoData = data.records;
        showList.value = false;
        showList.value = true;

      })
    }
    const getAllVideoData = () => {
      current.value = 1;
      size.value = 10;

      getVideoDataByLike();
    }

    watch(() => searchContent.value, () => {
      getAllVideoData();
    })

    watch(size, () => {
      getVideoDataByLike();
      console.log('pageSize', size.value);
    });
    watch(current, () => {
      getVideoDataByLike();
      console.log('current', current.value);
    });

    onMounted(() => {
      getAllVideoData();
    })
    return {
      videoData,
      showList,
      searchContent,
      current, size, total,
      getAllVideoData,
      getVideoDataByLike,
    }
  }
}
</script>

<style scoped>
.manuscript-box {
  width: 100%;
  height: 100%;
  padding: 10px 150px 10px 50px;
  background-color: #fff;
  overflow-y: scroll;
  -ms-overflow-style: none;
  overflow: -moz-scrollbars-none;
  margin-top: 10px;
}
.manuscript-box::-webkit-scrollbar{
  width: 0;
}

.manuscript-header {
  display: flex;
  flex-direction: row;
  /*justify-content: space-between;*/
  font-size: 1rem;
  font-weight: 600;

}

.manuscript-header div:last-child {
  width: 20%;
  margin-left: 20px;
}

.manuscript-header div:last-child input:focus {
  border: 1px solid #00a1d6;
}
.manuscript-main{
  width: 100%;
  height: 100%;
}
</style>