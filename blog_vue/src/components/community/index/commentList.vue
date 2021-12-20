<template>
  <!--评论展示-->
  <div class="comment-list-box">
    <ul  v-for="(item,index) in list" class="comment-list">
      <li class="comment-item">
        <div class="author-box">
          <a-avatar
              :src="'http://localhost:8081/static/avatar/'+item.avatar"
              :size="56"
              @click="pushAuthPageById(item.username,item.userId)"
              class="avatar"
          >
          </a-avatar>
<!--          <a-avatar-->
<!--              :src="'http://47.113.204.103:8081/static/avatar/'+item.avatar"-->
<!--              :size="56"-->
<!--              @click="pushAuthPageById(item.username,item.userId)"-->
<!--              class="avatar"-->
<!--          >-->
<!--          </a-avatar>-->
          <div class="info-box">
            <div class="nickname-box">
              <span class="nickname" @click="pushAuthPageById(item.username,item.userId)">{{item.nickname}}</span>
              <p>{{item.created}}</p>
            </div>
            <div v-if="item.parentNickname" class="reply">
              <span>回复</span>
              <span @click="pushAuthPageById(item.username,item.userId)">@{{ item.parentNickname }}</span>
            </div>
          </div>

        </div>
        <div class="comment-content-box">
          <div v-text="item.content"
               class="comment-content"
          >

          </div>
<!--          <v-md-editor-->
<!--              v-model="item.content"-->
<!--              mode="preview"-->
<!--              height="50px"-->
<!--              weight="50px"-->
<!--          >-->
<!--          </v-md-editor>-->
          <button type="button"  @click="openReplyForm(item.id)" class="iconfont icon-huifu">
            回复
          </button>
        </div>

        <div :data-index="item.id" class="close" :key="item.id">
          <a-form
              :model="comment"
          >
              <a-textarea v-model:value="comment.content"
                          :placeholder="'@'+item.parentNickname"
                          class="commentInput"/>
          </a-form>
          <button class="submitButton" @click="replyComments(item.id)">
            <span>发表</span>
            <span>评论</span>
          </button>
        </div>
      </li>
      <div v-if="item.child" class="children-item">
        <comment-list :list="item.child"></comment-list>
      </div>
    </ul>
  </div>
</template>

<script>
import {useRoute,useRouter} from "vue-router";
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
import {submitComment} from "../../../request/api/information";
import {message} from "ant-design-vue";
import {judgeIdentity} from '../../../assets/js/global.js'

export default {
  name: "commentList",
  inject: ['alive'],
  props: {
    list: Array
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const comment = reactive({
      content: ''
    })

    const openReplyForm = (id) => {
      // console.log("前"+proxy.comment.content);
      comment.content = ''
      // console.log("后"+proxy.comment.content);

      const index = '\'' + id + '\''
      const form = document.querySelector("div[data-index=" + index + "]");
      const close = document.querySelectorAll('.close')

      for (let i = 0; i < close.length; i++) {
        close[i].style.display = 'none';
      }

      form.style.display = 'flex'

    }
    const replyComments = (id) => {

      if (comment.content.split(" ").join("").length == 0) {
        message.info("请输入评论");
        return;
      }

      if (comment.content.length > 50){
        message.info("评论不能超过50字");
        return;
      }
      console.log("长度",proxy.comment.content.length);
      // console.log("评论", proxy.comment.content);
      // console.log("评论id", id);

      let data = {};
      data['articleId'] = route.params.id;
      data['content'] = comment.content;
      data['parentId'] = id;
      submitComment(data).then(res => {
        console.log(res);
        if (res.data.code == 200) {
          comment.content = ''
          proxy.alive();
          message.success(res.data.msg);
        }

      })
    }

    const pushAuthPageById = (username, id) => {
      let flag = judgeIdentity(username);
      if (flag) {
        router.push({name: 'myHomePage'});
      } else {
        router.push({name: 'userInfo', params: {id: id}});
      }
    }
    onMounted(() => {
      console.log("路由id", route.params.id);
    })

    return {
      comment,
      pushAuthPageById,
      replyComments,
      openReplyForm,
    }
  }
}
</script>

<style scoped>
.comment-list-box{

}
.close {
  display: none;
  flex-direction: row;
  justify-content: flex-start;
}

.commentInput {
  width: 330px;
  height: 65px;
  padding: 5px 10px;
  resize: none;
}

.submitButton {
  margin-left: 5px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.children-item{
  margin-left: 8%;
}
.avatar {
  cursor: pointer;
}
.author-box {
  display: flex;
  flex-direction: row;
  /*justify-content: flex-start;*/
  /*margin-top: 20px;*/
  width: 90%;
}
.nickname{
  font-family: system-ui;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
}
.info-box{
  display: flex;
  flex-direction: row;
  margin-left: 10px;
}
.info-box div:nth-child(1){
  padding-top: 2px;
}
.reply{
  margin-left: 10px;
  display: inline-block;
}
.reply span:nth-child(2){
  margin-left: 10px;
  color:#00a1d6 ;
}
.reply span:nth-child(2):hover {
  cursor: pointer;
  color: rgb(242, 93, 142);
}
.reply span:nth-child(n){
  font-size: 1rem;
}
.comment-list{
  /*background-color: rgb(255, 254, 239);*/
  background-color: #fff;
}

.comment-content-box{
  display: flex;
  flex-direction: column;
}
.comment-content{
  width: 93%;
  color: rgb(75, 85, 99);
  font-size: 1rem;
}
.comment-content-box button{
  /*background-color: rgb(153, 153, 153);*/
  background-color: #fff;
  color: black;
  align-self: flex-end;
  /*height: 25px;*/
  border: 0;
  cursor: pointer;
}
.comment-item{
  /*border: 1px solid skyblue;*/
  /*background-color: rgb(255, 254, 233);*/
}
</style>
