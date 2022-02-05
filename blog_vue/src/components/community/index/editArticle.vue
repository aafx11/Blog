<template>
  <div class="title-wrapper">
    <div class="input-wrapper">

      <a-input v-model:value="article.title" placeholder="请输入文章标题" class="title"></a-input>
      <a-input v-model:value="article.introduction" placeholder="请输入文章简介，用于封面介绍" class="title"></a-input>
    </div>
    <div class="upload-wrapper">
      <img :src="'http://localhost:8081/static/articleCover/'+article.cover" alt="" v-if="article.cover"
           class="upload-img"/>
      <div v-else class="upload-img">请选择封面</div>
      <a-upload
          v-model:file-list="fileList"
          :customRequest="uploadCover"
          @change="handleChange"
          :showUploadList="false"
      >
        <div class="select-button">
          选择文章封面
        </div>
      </a-upload>
    </div>
  </div>
  <v-md-editor
      v-model="article.content"
      :disabled-menus="[]"
      @upload-image="handleUploadImage"
      height="400px"
  >

  </v-md-editor>

  <div class="bottom-container">
    <div class="tag-wrapper">
      <span>给你的文章创建标签：</span>
      <template v-for="(tag, index) in article.tags" :key="index">
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
          v-if="inputVisible"
          ref="inputRef"
          type="text"
          size="small"
          :style="{ width: '78px' }"
          v-model:value="inputValue"
          @blur="handleInputConfirm"
          @keyup.enter="handleInputConfirm"
      />
      <a-tag v-else @click="showInput" style="background: #fff; border-style: dashed">
        <plus-outlined/>
        点击创建新的标签
      </a-tag>
    </div>
    <!--  <a-button @click="submitArticle">发表文章</a-button>-->
    <div class="select-button" @click="submitArticle">发布文章</div>
  </div>
</template>

<script>
import {defineComponent, ref, reactive, toRefs, nextTick, getCurrentInstance} from 'vue';
import {useRouter} from "vue-router"
import {PlusOutlined} from '@ant-design/icons-vue';
import {editArticle} from '../../../request/api/article.js'
import {upload, uploadArticleImgToLocal, uploadArticleCoverToLocal} from '../../../request/api/common.js'
import {message} from "ant-design-vue";

export default defineComponent({
  name: "editArticle",
  components: {
    PlusOutlined,
  },
  data() {
    return {
      text: '',
    }
  },
  methods: {
    handleUploadImage(event, insertImage, files) {
      console.log(files);
      const formData = new FormData();
      formData.append("img", files[0])

      uploadArticleImgToLocal(formData).then(res => {
        if (res.data.code == 200) {
          console.log("文章图片", res);
          const imgUrl = res.data.data
          insertImage({
            url: "http://localhost:8081/static/articleImage/" + imgUrl,
            desc: '',
            width: '450',
            height: '470',
          })
          message.success(res.data.msg)
        } else {
          message.warn(res.data.msg)
        }
      })


      //上传七牛云
      // const formData = new FormData();
      //
      // formData.append("image", files[0])
      // formData.append("buckets", "community-video-page")
      // formData.append("cdn", "r00x9rl7z.hn-bkt.clouddn.com")
      //
      // console.log("获取的文件", formData.get('image'));
      //
      //
      // upload(formData).then(res => {
      //   if (res.data.code == 200) {
      //     console.log(res);
      //     const imgUrl = res.data.data
      //     insertImage({
      //       url:  imgUrl,
      //       desc: '',
      //       width: '450',
      //       height: '400',
      //     })
      //     message.success(res.data.msg)
      //
      //   } else {
      //     message.warn(res.data.msg)
      //   }
      // })
    }
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const fileList = ref([]);
    const article = reactive({
      title: '',
      introduction: '',
      content: '',
      tags: [],
      cover: ''
    })

    /*标签相关*/
    const inputRef = ref();
    const state = reactive({
      inputVisible: false,
      inputValue: '',
    });

    //删除标签
    const handleClose = removedTag => {
      const tags = article.tags.filter(tag => tag !== removedTag);
      article.tags = tags;

    };

    const showInput = () => {
      state.inputVisible = true;
      nextTick(() => {
        inputRef.value.focus();
      });
    };

    const handleInputConfirm = () => {
      const inputValue = state.inputValue;
      let tags = article.tags;

      if (inputValue && tags.indexOf(inputValue) === -1) {
        tags = [...tags, inputValue];
      }


      Object.assign(state, {
        inputVisible: false,
        inputValue: '',
      });

      Object.assign(article, {
        tags
      });

      article.tags = tags;
      console.log(article.tags);
    };

    //上传文章封面
    let base64 = ref()
    const uploadCover = (data) => {
      const formData = new FormData();
      formData.append("image", data.file)

      uploadArticleCoverToLocal(formData).then(res => {
        if (res.data.code == 200) {
          article.cover = res.data.data
          console.log("封面地址", article.cover);
          message.success(res.data.msg)

        } else {
          message.warn(res.data.msg)
        }
      })


      // let reader = new FileReader();
      // reader.readAsDataURL(data.file)
      // reader.onload = () => {
      //   proxy.article.coverBase64 = reader.result;
      //
      // }

      // const formData = new FormData();
      //上传七牛云
      // formData.append("image", data.file)
      // formData.append("buckets", "article-cover-image")
      // formData.append("cdn", "qyy8fvdax.hn-bkt.clouddn.com")

      // upload(formData).then(res => {
      //   if (res.data.code == 200) {
      //     article.cover = res.data.data
      //     console.log("封面地址",article.cover);
      //     message.success(res.data.msg)
      //
      //   } else {
      //     message.warn(res.data.msg)
      //   }
      // })
    }

    //发布文章
    const submitArticle = () => {
      console.log(article.title);
      if (article.title.length == 0) {
        message.info('请输入文章标题')
        return
      }
      if (article.title.length > 20){
        message.info('文章标题字数最大为20')
        return
      }

      if (article.title.indexOf(" ") > -1){
        message.info('文章标题不能含有空格')
        return;
      }

      if (article.introduction.length == 0){
        message.info('请输入文章简介')
        return;
      }

      if (article.introduction.length > 100){
        message.info('文章简介最大字数为100')
        return;
      }

      if (article.cover.length == 0){
        message.info('请上传文章封面')
        return;
      }

      if (article.content.length == 0){
        message.info('请输入文章正文内容')
        return;
      }

      if (article.tags.length == 0){
        message.info('请输入文章标签')
        return;
      }

      editArticle(proxy.article).then(res => {
        console.log("返回的数据", res);
        setTimeout(() => {
          router.push({
            name: 'articleDetail',
            params: {id: res.data.data.id}
          })
        }, 800)

      })
    }

    const handleChange = info => {
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }

      if (info.file.status === 'done') {
        message.success(`${info.file.name} file uploaded successfully`);
      } else if (info.file.status === 'error') {
        message.error(`${info.file.name} file upload failed.`);
      }
    };

    return {
      article,
      fileList,
      ...toRefs(state),
      inputRef,
      handleClose,
      showInput,
      handleInputConfirm,
      submitArticle,
      handleChange,
      uploadCover,
    }
  }
})
</script>

<style scoped>
.title-wrapper {
  display: flex;
}

.title {
  height: 40px;
  width: 100%;
  margin-bottom: 10px;
  margin-right: 10px;
}

.input-wrapper, .upload-wrapper {
  flex: 1;
}

.select-button {
  height: 40px;
  width: 110px;
  color: #FFFFFF;
  background-color: #388eff;
  border: 1px solid rgb(29, 125, 250);
  border-radius: 2px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}

.select-button:hover {
  opacity: 0.9;
}

.bottom-container {
  width: 100%;
  margin-top: 8px;
  padding: 0 15px;
  display: flex;
}

.tag-wrapper {
  flex: 1;
}

.upload-wrapper {
  display: flex;
  align-items: flex-end;
}

.upload-img {
  height: 100px;
  width: 120px;
  border: 1px solid rgb(134, 144, 156);
  margin: 0 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  object-fit: cover;
}
</style>