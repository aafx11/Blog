<template>
  <div class="videoList-box" v-for="(item,index) in videoList">
    <router-link :to="{name:'videoDetail',params:{id:item.id}}">
      <div class="video-cover">
        <img :src="'http://localhost:8081/static/videoCover/'+item.coverUrl" alt="" class="video-img">
      </div>
    </router-link>
    <div class="video-info">
      <div class="info-box">
        <div>
          <router-link :to="{name:'videoDetail',params:{id:item.id}}">
            <div class="video-title">{{ item.title }}</div>
          </router-link>
        </div>
        <div class="video-date">
          <div class="date">{{item.created}}</div>
          <div v-if="item.state == 0" class="under-review">
            <HourglassOutlined  />
            待审核
          </div>
          <div v-else-if="item.state == 1" class="passed">
            <span class="iconfont icon-tongguo "></span>
            通过
          </div>
          <div v-else class="ban">
            <ExclamationCircleOutlined />
            <span>审核不通过,稿件中发现违规问题。</span>
            <a-popover placement="bottom">
              <template #content>
                <p>{{item.examine}}</p>
              </template>
              <template #title>
                <span>退回理由</span>
              </template>
              <span class="problem">查看问题</span>
            </a-popover>
          </div>
          <div class="edit-box">
            <router-link :to="{name:'editVideo',params:{id:item.id}}" class="edit-button">
              <div><EditOutlined />编辑</div>
            </router-link>
            <el-popconfirm
                title="确定删除吗？"
                @confirm="deleteVideo(item.id)"

            >
              <template #reference>
                <div class="delete-button"><DeleteOutlined />删除</div>
              </template>
            </el-popconfirm>
          </div>
        </div>
        <div class="bottom-icon">
          <div class="iconfont icon-shipinzhibo icon-item">
            {{item.view}}
          </div>
          <div class="iconfont icon-dianzan icon-item">
            {{item.likeCount}}
          </div>
          <div class="iconfont icon-shoucang icon-item">
            {{item.collects}}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getCurrentInstance, onMounted} from "vue";
import {HourglassOutlined,ExclamationCircleOutlined,EditOutlined,DeleteOutlined} from '@ant-design/icons-vue';
import {deleteVideoByVideoId} from '../../../request/api/video.js'
import {message}  from "ant-design-vue";
export default {
  name: "videoList",
  components:{
    HourglassOutlined,
    ExclamationCircleOutlined,
    EditOutlined,
    DeleteOutlined
  },
  props: {
    videoList: {
      type: Object,
      default: null
    }
  },
  setup(props,ctx) {
    const {proxy} = getCurrentInstance();

    const deleteVideo = (id) => {
      let ids = []
      ids.push(id)
      deleteVideoByVideoId(ids).then(res=>{
        console.log(res);
        if (res.data.code == 200){
          message.success('删除成功')
          ctx.emit('update')
        }
      })
    }

    onMounted(() => {
      console.log(proxy.videoList);
    })

    return{
      deleteVideo
    }
  }
}
</script>

<style scoped>
a {
  list-style: none;
  color: #0c0c0c;
}

.videoList-box {
  width: 100%;
  height: 160px;
  padding: 24px 15px;
  display: flex;
  flex-direction: row;
  border-bottom: 1px solid rgb(231, 231, 231);
}

.video-cover {
  width: 170px;
  height: 100%;
}

.video-cover img {
  width: 100%;
  height: 100%;
}

.video-info {
  margin-left: 20px;
  width: 100%;
}

.video-title {
  font-size: 1.3rem;
}

.info-box {
  height: 100%;
  width: 100%;
  /*display: flex;*/
  /*flex-direction: column;*/
  /*justify-content: space-between;*/
}
.video-date{
  padding: 13px 0px 17px 0px;
  position: relative;
  /*display: flex;*/
  /*flex-direction: row;*/
}
.date{
  padding-right: 24px;
  border-right: 2px solid #e7e7e7;
  display: inline-block;
}

.passed{
  display: inline-block;
  padding-left: 24px;
  font-size: 0.9rem;
  color: #68bb8d;
}
.under-review{
  display: inline-block;

  padding-left: 24px;
  color: #00a1d6;
  font-size: 0.9rem;
}
.ban{
  display: inline-block;
  padding-left: 24px;
  font-size: 0.9rem;
  color: #fa5a57;

}
.problem{
  display: inline-block;

  text-decoration:underline;
}
.bottom-icon{
  display: flex;
}
.icon-item{
  margin-right: 15px;
}
.edit-box{
  display: inline-block;
  position: absolute;
  right: 0;
}
.edit-button,.edit-button + div{
  display: inline-block;
  padding: 5px 15px;
  border: 1px solid rgb(231, 231, 231);
  border-radius: 5px;
}
.delete-button{
  margin-left: 20px;
}
.edit-button:hover,.edit-button + div:hover{
  border: 1px solid #00a1d6;
}
.video-img{
  object-fit: cover;
}
</style>