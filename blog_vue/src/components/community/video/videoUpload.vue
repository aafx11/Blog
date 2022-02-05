<template>
  <div class="upload-box">
    <div class="box">
      <div>
        <div class="title">文件上传</div>
        <span>(推荐采用mp4，flv，avi等格式)</span>
        <el-upload
            ref="uploadVideo"
            :on-change="handleChange"
            :http-request="submitFile"
            :limit="1"
            :auto-upload="false"
            :multiple="false"
            action=""
            drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            <em>选取需要上传的视频</em>
          </div>
          <template #tip>
            <div class="progress">
              <el-progress :stroke-width="16" :percentage="progressPercent"></el-progress>
            </div>
          </template>
        </el-upload>
      </div>
      <div class="video-wrapper">
        <span class="title">视频预览</span>

        <video
            v-if="videoSrc!=''"
            controls="controls"
            :src="'http://localhost:8081/static/video/'+videoSrc"
            class="video"
            autoplay="autoplay"
        >
        </video>
        <div class="video-box" v-else>
          <span>请先上传视频</span>
        </div>
      </div>
    </div>

    <div class="info-box">
      <div class="title">基本信息</div>

      <div>
        <div class="item-title"><span style="color: red">*</span>视频封面设置</div>
        <el-upload
            ref="uploadImg"
            :on-change="coverChange"
            :on-preview="handlePictureCardPreview"
            :auto-upload="false"
            :multiple="false"
            action=""
            drag
            class="cover-upload"
            :show-file-list="false"
        >
          <div class="img-preview">
            <div v-if="coverSrc == ''">选择视频的封面</div>
            <img :src="'http://localhost:8081/static/videoCover/'+coverSrc" v-else class="cover">
          </div>
        </el-upload>
      </div>

      <div class="item">
        <div class="item-title"><span style="color: red">*</span>标题(最大长度为25字)</div>
        <div>
          <el-input v-model="title" placeholder="请输入标题" minlength="1" maxlength="25" class="input-style"></el-input>
        </div>
      </div>

      <div class="item">
        <div class="item-title"><span style="color: red">*</span>视频简介(最大长度为50字)</div>
        <div>
          <el-input type="textarea" :rows="5" v-model="introduction"
                    minlength="1" maxlength="100" placeholder="请输入视频简介"
                     class="input-style" resize="none">
          </el-input>
        </div>
      </div>

      <div class="item">
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
      </div>
<!--      <el-button @click="submitUpload">上传</el-button>-->
        <my-button type="primary" @click="submitUpload">上传</my-button>
    </div>

  </div>


</template>

<script>
import {getCurrentInstance, reactive, ref, nextTick} from "vue";
import service from "../../../request/http.js";
import {ElMessage} from 'element-plus';
import {videoUpload} from '../../../request/api/common.js'
import {PlusOutlined} from '@ant-design/icons-vue';
import {isImg,isVideo} from '../../../assets/js/upload.js'
import myButton from '../../../view/myButton.vue'

export default {
  name: "videoUpload",
  // props:{
  //   activeIndex:Number
  // },
  components: {
    myButton,
    PlusOutlined,
  },
  setup(props, context) {
    const {proxy} = getCurrentInstance();
    const state = reactive({
      inputVisible: false,
      inputValue: '',
    });
    const uploadVideo = ref();
    const uploadImg = ref();
    const inputRef = ref();
    const video = reactive({
      tags: []
    });
    const title = ref('');
    const introduction = ref('');
    const progressPercent = ref();
    const videoSrc = ref('')
    const coverSrc = ref('')
    const objectFile = reactive({})
    const objectCover = reactive({})
    const handleChange = (file) => {
      proxy.objectFile = file.raw;

    }
    const coverChange = (file) => {
      proxy.objectCover = file.raw;
      console.log(proxy.objectCover);
    }

    const handlePictureCardPreview = (file) => {
      console.log("预览", file.url);
      coverSrc.value = file.url
    }
    //删除标签
    const handleClose = removedTag => {
      const tags = video.tags.filter(tag => tag !== removedTag);
      video.tags = tags;

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


    const submitFile = () => {
      console.log("需要上传的图片", proxy.objectCover);



      //封装文件
      let formData = new FormData();
      formData.append("title", proxy.title);
      formData.append("introduction", proxy.introduction);
      formData.append("video", proxy.objectFile);
      formData.append("videoPath", 'D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/video');
      formData.append("cover", proxy.objectCover);
      formData.append("coverPath", 'D:/Student/Vue_student/Blog/fxBlog_SpringBoot/src/main/resources/static/videoCover');
      formData.append("tags", video.tags);


      service.post("/upload/videoUpload", formData, {
        headers: {'content-Type': 'multipart/form-data'},
        onUploadProgress: progressEvent => {
          proxy.progressPercent = (progressEvent.loaded / progressEvent.total * 100) | 0;
        }
      }).then(res => {
        console.log("返回", res);
        // videoSrc.value = res.data.data.videoUrl;
        if (res.data.code == 200) {
          const data = res.data.data;
          ElMessage.success(res.data.msg);
          videoSrc.value = data.videoUrl;
          coverSrc.value = data.coverUrl;
          console.log(context.emit);
          context.emit('changeIndex');
        }
      })

    }

    const submitUpload = () => {
      if (title.value.split(" ").join("").length == 0) {
        ElMessage.info("请输入视频标题");
        return;
      }
      if (introduction.value.split(" ").join("").length == 0) {
        ElMessage.info("请输入视频简介");
        return;
      }

      if (video.tags.length == 0){
        ElMessage.info('视频标签不能为空')
        return;
      }

      //判断图片大小和格式
      if (!isImg(proxy.objectCover)) {
        return;
      }
      //判断视频大小和格式
      if (!isVideo(proxy.objectFile)) {
        return;
      }

      proxy.$refs.uploadVideo.submit();
    }

    return {
      video,
      title,
      state,
      videoSrc,
      coverSrc,
      inputRef,
      objectFile,
      objectCover,
      introduction,
      progressPercent,
      showInput,
      submitFile,
      coverChange,
      handleClose,
      submitUpload,
      handleChange,
      handleInputConfirm,
      handlePictureCardPreview,
    }
  }
}
</script>

<style scoped>
.upload-box {
  display: flex;
  flex-direction: column;
  /*align-items: center;*/
  width: 100%;
  height: 100%;
  background-color: #fff;
  padding: 10px 50px;
}

.box {
  display: flex;
}

.video {
  width: 400px;
  height: 210px;
}

.title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 10px;
  display: inline-block;
}

.video-box {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 400px;
  height: 210px;
  font-size: 1rem;
  border: 1px solid black;
}

.progress {
  width: 400px;
}
.video-wrapper{
  display: flex;
  flex-direction: column;
}
.info-box {
  margin-top: 15px;
}

.cover-upload {
  /*height: 100px;*/
  /*width: 100px;*/
}

.img-preview {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-size: 1rem;
  width: 100%;
  height: 100%;
}

.item-title {
  font-size: 1rem;
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.input-style {
  width: 35%;
}
.item{
  margin-bottom: 15px;
}
</style>