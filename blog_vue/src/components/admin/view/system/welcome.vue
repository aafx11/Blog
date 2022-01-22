<template>
  <div class="container-box">
    <div class="left-container"></div>
    <div class="messages-section">
      <div class="title">
        <p>最新评论</p>
      </div>
      <div class="message">
        <template v-if="commentData.length > 0">
          <div class="message-box" v-for="item in commentData">
            <img :src="'http://localhost:8081/static/avatar/'+item.avatar" alt="未加载">
            <div class="message-content">
              <div class="message-header">
                <div class="nickname">{{ item.nickname }}</div>
                <div class="message-info">
                  <p>{{item.content}}</p>
                </div>
                <div class="message-info message-date">
                  {{item.created}}
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import {getLatestComments} from '../../../../request/api/information.js'
import {onBeforeMount, ref} from "vue";

export default {
  name: "welcome",
  setup() {

    // 获取最新评论
    let commentData = ref([])
    onBeforeMount(() => {
      getLatestComments().then(res => {
        commentData.value = res.data.data
        console.log(commentData.value);
      })

    })

    return {
      commentData
    }
  }
}
</script>

<style lang="scss" scoped>
.container-box {
  width: 100%;
  //height: 100%;
  height: 100vh;
  background-color: rgb(240, 242, 245);
  display: flex;

  .left-container {
    width: 100%;
    border: 1px solid blue;
    flex: 2;
  }

  .messages-section {
    position: relative;
    width: 100%;
    background-color: #fff;
    border-radius: 30px;
    flex: 0.9;
    overflow: auto;

    .title {
      position: sticky;
      top: 0;
      font-size: 24px;
      padding: 10px 24px 0 24px;
      background-color: #fff;
      z-index: 10;
      
    }

    .message-box {
      padding: 16px;
      border-top: 1px solid #cccdd2;
      display: flex;
      align-items: flex-start;

      img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
      }

      .message-content {
        padding-left: 16px;
        width: 100%;

        .message-header {

          .nickname {
            font-size: 18px;
            line-height: 24px;
            font-weight: 700;

          }
        }

        .message-info{
          font-size: 16px;
          color: rgb(74, 74, 74);
          margin: 8px 0;
          //opacity: .9;
        }
        .message-date{
          text-align: right;
        }
      }
    }
  }
}
</style>