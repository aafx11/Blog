<template>
  <div class="container-box" v-if="showDetail">
    <div class="container-video-box">

      <div class="container-video-left">
        <div class="left-box">
          <div class="container-video-title">
            <div>视频文件</div>
            <a-upload
                :customRequest="reUploadByVideoId"
                :showUploadList="false"
            >
              <div id="upload-button">重新上传</div>
            </a-upload>
            <div class="upload-progress-container">
              <el-progress v-if="progressPercent!=0" :stroke-width="16" :percentage="progressPercent"
                           class="upload-progress"></el-progress>
            </div>
          </div>
          <video
              :src="'http://localhost:8081/static/video/'+videoDetailData.videoUrl"
              class="video-player"
              controls="controls"
          >
          </video>
<!--          <video-->
<!--              :src="'http://47.113.204.103:8081/static/video/'+videoDetailData.videoUrl"-->
<!--              class="video-player"-->
<!--              controls="controls"-->
<!--          >-->
<!--          </video>-->
        </div>
        <div v-if="isAuth('video:examine')">
          <div class="right-title">视频审核</div>
          <div class="item-title"><span style="color: red">*</span>修改视频状态</div>
          <div>
            <el-radio-group v-model="videoState" @change="stateChange" >
              <el-radio :label="0" border>审核中</el-radio>
              <el-radio :label="1" border>审核通过</el-radio>
              <el-radio :label="2" border>审核不通过</el-radio>
            </el-radio-group>
          </div>
          <div class="item-title"><span style="color: red">*</span>审核评语</div>
          <div>
            <el-input type="textarea" :rows="6" v-model="videoInfo.examine"
                      minlength="1" maxlength="100" placeholder="请输入审核评语"
                      class="input-style" resize="none">
            </el-input>
          </div>
          <div class="examine-button">
            <a-button type="primary" @click="setVideoState">修改视频审核状态</a-button>
          </div>
        </div>
      </div>
      <div class="video-info-container">
        <div>
          <div class="info-title">基本信息</div>
          <div>
            <div class="item-title"><span style="color: red">*</span>视频封面设置</div>
            <a-upload
                :customRequest="reUploadCoverByVideoId"
                :showUploadList="false"
            >
              <div id="cover-button">重新选择视频封面</div>
            </a-upload>
            <div>
<!--              <a-image :src="'http://localhost:8081/static/videoCover/'+videoDetailData.coverUrl"-->
<!--                       alt="加载失败" :width="360" :height="180"/>-->
              <img :src="'http://localhost:8081/static/videoCover/'+videoDetailData.coverUrl"  alt="加载失败" class="video-img"/>
            </div>
          </div>
        </div>
        <div class="video-info-box">
          <div class="item-title"><span style="color: red">*</span>标题(最大长度为25字)</div>
          <div>
            <el-input v-model="videoInfo.title" placeholder="请输入标题" minlength="1" maxlength="25"
                      class="input-style"></el-input>
          </div>
          <div class="item-title"><span style="color: red">*</span>视频简介(最大长度为50字</div>
          <div>
            <el-input type="textarea" :rows="5" v-model="videoInfo.introduction"
                      minlength="1" maxlength="100" placeholder="请输入视频简介"
                      class="input-style" resize="none">
            </el-input>
          </div>
          <div class="item-title"><span style="color: red">*</span>标签(可自定义标签)</div>
          <div>
            <span>给你的文章创建标签：</span>
            <template v-for="(tag, index) in video.tags" :key="index">
              <a-tooltip v-if="tag.length > 20" :title="tag">
                <a-tag :key="tag" :closable="index !== 0" @close="handleClose(tag)">
                  {{ `${tag.slice(0, 20)}...` }}
                </a-tag>
              </a-tooltip>
              <a-tag v-else :closable="index !== 0" @close="handleClose(tag)">
                {{ tag }}
              </a-tag>
            </template>
            <a-input
                v-if="state.inputVisible"
                ref="inputRef"
                type="text"
                size="small"
                :style="{ width: '78px' }"
                v-model:value="state.inputValue"
                @blur="handleInputConfirm"
                @keyup.enter="handleInputConfirm"
            />
            <a-tag v-else @click="showInput" style="background: #fff; border-style: dashed">
              <plus-outlined/>
              点击创建新的标签
            </a-tag>
          </div>
          <div id="info-button" @click="uploadVideoInfo">修改视频信息</div>
        </div>


      </div>
    </div>
  </div>
</template>

<script>
import {getCurrentInstance, reactive, ref, nextTick, onMounted} from "vue";
import {getVideoDetailByVideoId, reUploadVideoAndCover, setVideoInfoByVideoId,setVideoExamine} from '../../../request/api/video.js'
import {useRoute} from "vue-router";
import {hasAuth} from "../../../assets/js/global.js";
import service from "../../../request/http.js";
import {ElMessage} from "element-plus";
import {PlusOutlined} from '@ant-design/icons-vue';

export default {
  name: "EdieVideo",
  components: {
    PlusOutlined
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const route = useRoute();
    const showDetail = ref(false);
    const videoState = ref(0);
    const videoDetailData = reactive({});
    const inputRef = ref();
    const videoInfo = reactive({
      title: '',
      introduction: '',
      examine:'',
    })
    const video = reactive({
      tags: []
    });
    const state = reactive({
      inputVisible: false,
      inputValue: '',
    })

    //删除标签
    const handleClose = removedTag => {
      const tags = video.tags.filter(tag => tag !== removedTag);
      video.tags = tags;
      console.log(video.tags, "标签");
    };
    //写入标签
    const showInput = () => {
      state.inputVisible = true;
      nextTick(() => {
        inputRef.value.focus();
      });
    };
    const handleInputConfirm = () => {
      const inputValue = state.inputValue;
      let tags = video.tags;

      if (inputValue && tags.indexOf(inputValue) === -1) {
        tags = [...tags, inputValue];
      }


      Object.assign(state, {
        inputVisible: false,
        inputValue: '',
      });

      Object.assign(video, {
        tags
      });

      video.tags = tags;
      console.log(video.tags);
    };

    let progressPercent = ref(0)
    const getVideoDetailData = () => {
      getVideoDetailByVideoId(route.params.id).then(res => {
        const data = res.data.data;
        proxy.videoDetailData = data;
        videoInfo.title = data.title;
        videoInfo.introduction = data.introduction;
        videoInfo.examine = data.examine;
        videoState.value = data.state;
        console.log(videoState.value);
        console.log("信息", proxy.videoDetailData);
        if (data.tags !== null) {
          data.tags.forEach(tag => {
            video.tags.push(tag.name)
          })
        }

        console.log("标签", video.tags);
        showDetail.value = false;
        showDetail.value = true;
      })
    }
    //重新上传视频
    const reUploadByVideoId = (data) => {
      console.log("id", proxy.videoDetailData.id);
      const formData = new FormData();
      formData.append("video", data.file);
      formData.append("videoPath", "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/video");
      formData.append("videoId", proxy.videoDetailData.id)

      service.post("/upload/reUploadByVideoId", formData, {
        headers: {'content-Type': 'multipart/form-data'},
        onUploadProgress: progressEvent => {
          proxy.progressPercent = (progressEvent.loaded / progressEvent.total * 100) | 0;
        }
      }).then(res => {
        console.log("返回", res);
        proxy.videoDetailData.videoUrl = res.data.data;
        showDetail.value = false;
        showDetail.value = true;
      })
    }

    //重新上传视频封面
    const reUploadCoverByVideoId = (data) => {
      const formData = new FormData();
      formData.append("cover", data.file);
      formData.append("coverPath", "D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/videoCover");
      formData.append("videoId", proxy.videoDetailData.id)

      reUploadVideoAndCover(formData).then(res => {
        const data = res.data.data;
        console.log("返回", data);
        proxy.videoDetailData.coverUrl = data;
        showDetail.value = false;
        showDetail.value = true;

      })
    }

    const uploadVideoInfo = () => {
      if (videoInfo.title.split(" ").join("").length == 0) {
        ElMessage.info("请输入视频标题");
        return;
      }
      if (videoInfo.introduction.split(" ").join("").length == 0) {
        ElMessage.info("请输入视频简介");
        return;
      }
      let formData = new FormData();
      formData.append("videoId", proxy.videoDetailData.id);
      formData.append("title", videoInfo.title);
      formData.append("introduction", videoInfo.introduction);
      formData.append("tags", video.tags);

      setVideoInfoByVideoId(formData).then(res => {
        console.log("信息修改", res);
        ElMessage.success(res.data.msg);
      })
    }


    const isAuth = (data) => {
      return hasAuth(data);
    }

    const setVideoState = () => {
      const formData = new FormData();
      formData.append("videoId",proxy.videoDetailData.id);
      formData.append("state",videoState.value)
      formData.append("examine",videoInfo.examine);
      setVideoExamine(formData).then(res => {
        console.log("返回",res);
        ElMessage.success(res.data.msg);
      })
    }
    const stateChange = (val) => {
      console.log(val);
      videoState.value = val;
    }
    onMounted(() => {
      getVideoDetailData();
    })

    return {
      video,
      state,
      inputRef,
      videoInfo,
      showDetail,
      videoState,
      videoDetailData,
      progressPercent,
      isAuth,
      showInput,
      handleClose,
      stateChange,
      uploadVideoInfo,
      setVideoState,
      reUploadByVideoId,
      handleInputConfirm,
      reUploadCoverByVideoId,
    }
  }
}
</script>

<style scoped>
.container-box {
  width: 100%;
  height: 100%;
  padding: 10px 50px;
  background-color: #fff;
}

.container-video-box {
  height: 360px;
}

.container-video-left {
  height: 100%;
  display: flex;
}

.video-player {
  width: 80%;
}

.left-box {
  width: 50%;
}

.container-video-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 10px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.container-video-title div {
  display: inline-block;
}

.right-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 10px;
}

#upload-button, #cover-button {
  margin-left: 15px;
  font-size: 1rem;
  font-weight: normal;
  border: 1px solid rgb(231, 231, 231);
  color: white;
  padding: 5px 15px;
  border-radius: 5px;
  background-color: #1890ff;
  cursor: pointer;
}

#upload-button:hover, #cover-button:hover {
  border: 1px solid #00a1d6;
  background-color: #40a9ff;
}

#info-button {
  text-align: center;
  width: 40%;
  font-size: 1rem;
  font-weight: normal;
  border: 1px solid rgb(231, 231, 231);
  color: white;
  padding: 5px 15px;
  border-radius: 5px;
  background-color: #1890ff;
  cursor: pointer;
  float: left;
  margin-top: 15px;
}

#info-button:hover {
  border: 1px solid #00a1d6;
  background-color: #40a9ff;
}

.upload-progress-container {
  margin-left: 10px;
}

.upload-progress {
  width: 300px;
}

.info-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 5px;
}

.item-title {
  font-size: 1rem;
  display: inline-block;
}

.video-info-container {
  display: flex;
  flex-direction: row;
}

.video-info-box {
  width: 40%;
  margin-top: 3%;
  margin-left: 15%;
}
.examine-button button{
  margin-top: 15px;
  float: right;
  border-radius: 5px;
}
.video-img{
  width: 360px;
  height: 270px;
  object-fit: cover;
}
</style>