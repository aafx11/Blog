<template>
  <div class="comment-box">
    <span class="title-span">评论</span>
    <!--输入评论-->
    <div class="comment" >
      <div >
    <textarea
        v-model="comment"
        placeholder="请输入评论"
        class="comment-text"
    >
    </textarea>
      </div>
      <div class="comment-button">
        <a-button @click="submitCommentForm" v-if="isAuth('hasLogin')" class="public-comment">评论</a-button>
        <a-button @click="showModal" v-else class="login-button">请先登录</a-button>
        <a-button @click="cancelComment" class="cancel-button">取消</a-button>
      </div>
    </div>

    <hr class="bottom-hr">
  </div>
  <!--评论展示,必须是数组，列表才能循环-->
  <VideoCommentList :list="commentDetail" v-if="showCommentList"></VideoCommentList>
  <a-modal
      v-model:visible="visible"
      :centered="true"
      width="460px"
      :body-style="{padding:'0'}"
  >
    <login-form></login-form>
  </a-modal>

</template>

<script>
import {defineComponent, onMounted, reactive, ref, getCurrentInstance} from 'vue';
import {getVideoCommentByVideoId,submitVideoComment} from '../../../request/api/information.js'
import {message} from "ant-design-vue";
import {useRoute} from "vue-router";
import {hasAuth} from '../../../assets/js/global.js'
import loginForm from '../common/loginForm.vue'
import VideoCommentList from './VideoCommentList.vue'

export default {
  name: "videoComment",
  provide(){
    return {
      getVideoComment:this.commentInit
    }
  },
  components: {
    VideoCommentList:VideoCommentList,
    loginForm:loginForm,
  },
  props: {
    video: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      commentDetail: [],
      showCommentList:false,
    }
  },
  setup() {
    const route = useRoute();
    const {proxy} = getCurrentInstance();
    const visible = ref(false);

    const commentForm = reactive({
      comment: ''
    })
    const comment = ref('')
    const submitCommentForm = () => {
      console.log(comment.value);

      if (comment.value.split(" ").join("").length == 0) {
        message.info("请输入评论")
        return;
      }
      if (comment.value.length > 50) {
        message.info("评论不能超过50字");
        return;
      }

      let data = {}
      data['videoId'] = proxy.video.id
      data['content'] = comment.value
      data['parentId'] = null;
      console.log("评论", data);
      submitVideoComment(data).then(res => {
        if (res.data.code == 200) {
          comment.value = '';
          proxy.commentInit(proxy.video.is);
          message.success(res.data.msg)
        }
      })
    }

    const cancelComment = () => {
      comment.value = '';
    }

    const isAuth = (data) => {
      return hasAuth(data);
    }

    const showModal = () => {
      visible.value = true;
    };
    return {
      route,
      comment,
      visible,
      commentForm,
      submitCommentForm,
      cancelComment,
      isAuth,
      showModal,

    }
  },
  methods: {
    async commentInit(videoId) {
      getVideoCommentByVideoId(this.video.id).then(res => {
        this.commentDetail = res.data.data;
        console.log("评论", this.commentDetail);
        this.showCommentList = false;
        this.showCommentList = true;

      })
    }
  },
  async mounted() {
    console.log("文章", this.video);
    await this.commentInit(this.video.id)
  }
}
</script>

<style scoped>
.comment-box {

  width: 100%;
  /*margin-top: 1px;*/
  padding: 0px 15px;
  background-color: #fff;
}

.title-span {
  font-size: 2rem;
  display: block;
}

.comment-text {
  margin-top: 10px;
  width: 100%;
  height: 80px;
  border-radius: 5px;
  resize: none;

}

.comment {
  display: flex;
  flex-direction: column;
}
.comment-button{
  align-self: flex-end;
}
.public-comment{
  background-color: rgb(47, 90, 223);
  color: white;
  border-radius: 5px;
  width: 75px;
  margin-right: 10px;
}
.login-button{
  background-color: rgb(47, 90, 223);
  color: white;
  border-radius: 5px;
  width: 90px;
  margin-right: 10px;
}
.cancel-button{
  color: black;
  border-radius: 5px;
  width: 75px;
}
.bottom-hr {
  margin-top: 20px;
  border-color: rgba(0, 0, 0, 0.12);
  /*margin-bottom: 25px;*/
}
</style>