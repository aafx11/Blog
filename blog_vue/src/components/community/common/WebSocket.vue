<template>
  <div class="contain-box">
    <div class="chat-box">
      <div class="left-box">
        <div class="header">
          <a-avatar :src="userInfo.avatar" :size="58"></a-avatar>
          <span>{{ userInfo.nickname }}</span>
        </div>
        <div class="sessionList-box">
          <div @click="currentShow=!currentShow" class="current-session-title">当前会话</div>
          <div class="current-session" v-if="currentShow">
            <div v-for="(item,index) in SessionListAlreadyData" tabindex="1" class="current-session-item">
              <div @click="startSession(item.id,item.nickname)">
                <a-badge :count="item.unReadCount">
                  <a-avatar :src="'http://localhost:8081/static/avatar/'+item.avatar" :size="46"></a-avatar>
<!--                  <a-avatar :src="'http://47.113.204.103:8081/static/avatar/'+item.avatar" :size="46"></a-avatar>-->

                </a-badge>
              </div>
              <div class="info-box" @click="startSession(item.id,item.nickname)">
                <span>{{ item.nickname }}</span>
              </div>
              <div class="close-icon" @click="deleteSessionById(item.id)">
                <CloseOutlined/>
              </div>

            </div>
          </div>
          <div @click="itemShow=!itemShow" class="follow-list-title">关注列表</div>
          <div class="follow-box">
            <div v-for="(item,index) in followListData" class="follow-list-item" v-show="itemShow" tabindex="1"
                 @click="clickCreateSession(item.userId,item.nickname)">
              <div>
                <a-avatar :src="'http://localhost:8081/static/avatar/'+item.avatar" :size="46"
                          @click="clickCreateSession(item.userId,item.nickname)"></a-avatar>
<!--                <a-avatar :src="'http://47.113.204.103:8081/static/avatar/'+item.avatar" :size="46"-->
<!--                          @click="clickCreateSession(item.userId,item.nickname)"></a-avatar>-->
              </div>
              <div class="info-box">
                <span>{{ item.nickname }}</span>
              </div>
            </div>
          </div>

        </div>
      </div>
      <div class="right-box">
        <div v-if="show" style="height: 80%;">
          <div class="info-header">
            <span>正在与</span>
            <span>{{ otherNickname }}</span>
            <span>对话</span>
          </div>
          <div class="message-list">
            <div v-for="item in messageList"
                 :class="item.fromNickname==userInfo.nickname ? 'message-list-item-right':'message-list-item-left'">
              <div :class="item.fromNickname==userInfo.nickname ?'message-box-right':'message-box-left'">
                <a-avatar :src="'http://localhost:8081/static/avatar/'+item.fromAvatar" :size="42"></a-avatar>
              </div>
              <div :class="item.fromNickname==userInfo.nickname ?'triangle-right':'triangle-left'"></div>
              <div>
                <div :class="item.fromNickname==userInfo.nickname ?'message-right':'message-left'">{{
                    item.content
                  }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="message-list-blank" v-else></div>
        <div class="sendMessage-box" v-show="sendMessageShow">
          <a-textarea v-model:value="message" class="message-text"/>
          <button @click="sendMessage">发送</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getUserInfo} from '../../../request/api/userList.js'
import {DeleteArrayElements} from '../../../assets/js/global.js'
import {getFollowListByPage, getFollowList} from '../../../request/api/follow.js'
import {message} from "ant-design-vue";
import {createSession, getSessionListAlready, deleteSession, getMessageList} from "../../../request/api/webSocket";
import {onMounted, getCurrentInstance, reactive, ref, watch} from "vue";
import {CloseOutlined} from '@ant-design/icons-vue';

export default {
  name: 'HelloWorld',
  components: {
    CloseOutlined,
  },
  data() {
    return {
      websock: null,
      show: false,
      followListData: {},
      message: '',
      userInfo: {},
      SessionListAlreadyData: {},//已经建立的会话
      messageList: {},//消息列表
      collapse: '',
      otherAvatar: '',
      otherNickname: '',
      otherSessionId: 0,
      itemShow: true,
      currentShow: true,
      sendMessageShow:false,
    }
  },
  methods: {
    initWebSocket(userId, sessionId) {
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      this.websock = new WebSocket('ws://localhost:8081/webSocket/' + userId + '/' + sessionId)
      this.websock.onopen = this.websocketOnopen
      this.websock.onerror = this.websocketOnerror
      this.websock.onmessage = this.websocketOnmessage
      this.websock.onclose = this.websocketClose
    },
    websocketOnopen() {
      console.log('WebSocket连接成功')
    },
    websocketOnerror(e) {
      console.log('WebSocket连接发生错误')
    },
    //接受到服务端推送的消息后触发
    websocketOnmessage(e) {

      let data = JSON.parse(e.data);
      if (data instanceof Array) {
        console.log("收到消息的时候", data);
        this.SessionListAlreadyData = data;
      } else {
        this.messageList.push(data);
        this.pushToMessageListBottom();
      }

      /*     let str = e.data;
           //将字符串转化为json
           let res = JSON.parse(str);

           //系统发送消息
           if (res.system) {
             console.log("获取的系统信息",res.message);
             let nicknames = res.message
             nicknames = DeleteArrayElements(this.userNickname, nicknames)//所有在线用户，不包括自己
             console.log("在线列表", nicknames);
           } else {//发送给指定用户的消息
             console.log(this.userNickname+"收到的消息",res.message);
               // this.windowMessage.join(res.message)
             console.log(this.windowMessage);
           }*/
    },
    websocketClose(e) {
      console.log('connection closed', e);

    },
    //创建会话
    clickCreateSession(toUserId, toNickname) {
      for (let i = 0; i < this.SessionListAlreadyData.length; i++) {
        if (toUserId == this.SessionListAlreadyData[i].toUserId) {
          return;
        }
      }
      console.log("fromuserid", this.userInfo.userId);
      console.log("touserid", toUserId);
      let data = {}
      console.log("用户id", this.userInfo.userId);
      data['fromUserId'] = this.userInfo.userId;
      data['toUserId'] = toUserId;
      createSession(data).then(res => {
        console.log("创建会话", res);
        this.startSession(res.data.data.id, toNickname)
        this.getSessionListAlreadyData(this.userInfo.userId);

      })
    },
    //获取已经创建的会话列表
    getSessionListAlreadyData(fromUserId) {
      getSessionListAlready(fromUserId).then(res => {
        this.SessionListAlreadyData = res.data.data;
        console.log("当前会话", res);
      })
    },
    //删除会话
    deleteSessionById(id) {
      console.log("otherSessionId", this.otherSessionId);
      console.log("id是", id);
      if (this.otherSessionId == id) {
        this.show = false;
      }
      deleteSession(id).then(res => {

        console.log("删除会话", res);
        message.success(res.data.msg);
        this.getSessionListAlreadyData(this.userInfo.userId);
        this.sendMessageShow=false;

      })
    },
    //获取消息列表
    getMessageListData(sessionId) {
      getMessageList(sessionId).then(res => {
        console.log("获取的消息", res);
        this.messageList = res.data.data;
        this.getSessionListAlreadyData(this.userInfo.userId);

        setTimeout(this.pushToMessageListBottom(),2000)

      })
    },
    //点击一个已创建的会话，来开始会话
    startSession(sessionId, nickname) {
      this.otherNickname = nickname;
      this.show = true;
      this.otherSessionId = sessionId;
      console.log(this.websock);
      //在开启一个新的websock连接前，确保关闭上一个连接
      if (this.websock != undefined) {
        this.websock.close();
      }
      //建立websocket连接，并获取聊天记录
      this.initWebSocket(this.userInfo.userId, sessionId);
      this.getMessageListData(sessionId);
      this.sendMessageShow=true;
      // this.pushToMessageListBottom();
    },
    //开始会话
    startSessionList(sessionId, nickname){
      this.startSession(sessionId, nickname).then(res=>{
        this.pushToMessageListBottom();
      })
    },
    //发送消息
    sendMessage() {
      let data = {
        "fromUserId": this.userInfo.userId,
        "fromNickname": this.userInfo.nickname,
        "content": this.message,
        "fromAvatar": this.userInfo.avatar,
      }
      this.messageList.push(data);
      console.log("聊天", this.messageList);
      this.websock.send(this.message);
      this.message = '';
      this.pushToMessageListBottom();
    },
    //跳转到聊天页面底部
    pushToMessageListBottom() {
      this.$nextTick(()=>{
        let list = document.querySelector('.message-list');
        let scrollHeight = list.scrollHeight;



        list.scrollTop = scrollHeight-list.clientHeight;
        console.log("滚动距离",list.scrollTop);
        if (list.scrollTop + list.clientHeight !=list.scrollHeight){
          list.scrollTop = list.scrollHeight;
        }
      })

      }

  },
  created() {
    getUserInfo().then(res => {
      this.userInfo = res.data.data
      console.log("用户信息", this.userInfo);
      console.log("用户id", this.userInfo.userId);
      this.initWebSocket(this.userInfo.userId, 99999999)
      this.getSessionListAlreadyData(this.userInfo.userId);//获取已建立的会话
    })
    getFollowList().then(res => {
      this.followListData = res.data.data;
      console.log("关注列表", this.followListData);
    })
  },
  destroyed() { // 离开页面生命周期函数
    this.websocketClose()
  },
  updated() {


  },
  setup() {
    const activeKey = ref(['1']);
    watch(activeKey, val => {
      console.log(val);
    });
    return {
      activeKey,
    }
  }
}
</script>

<style scoped>
.contain-box {
  width: 100%;
  height: 1080px;
  /*background-image: url('../../../assets/images/start.png');*/
  background-color: #E5ECEF;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;

}

.chat-box {
  height: 95%;
  width: 1150px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  /*border: 1px solid blue;*/


}

/*left-box------------------------------start*/
.left-box {
  width: 23%;
  height: 100%;
  /*border: 1px solid red;*/
  /*background-color: rgb(220, 221, 222);*/
  /*background-color: #444753;*/
  background-color: #FFFFFF;
}

.header {
  height: 80px;
  background-color: #ffffff;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;

}

.header span {
  margin-left: 20px;
}

.sessionList-box {
  background-color: rgb(220, 221, 222);
  overflow: hidden;
}

.follow-list {
  overflow: scroll;
}

.follow-list::-webkit-scrollbar {
  display: none;
}

.content {
  overflow: scroll;
}

.item-box {
  width: 100%;
  height: 70px;
  padding: 13px 10px;
  /*border-bottom: 1px solid rgba(43, 41, 41, 0.8);*/
  background-color: rgb(220, 221, 222);
  display: flex;
  /*align-items: center;*/
}

.item-box:hover {
  background-color: rgb(209, 209, 210);
}

.item-box:focus {
  background-color: rgb(209, 209, 210);
}

.info-box {
  width: 65%;
  height: 100%;
  display: flex;
  align-items: center;
}

.info-box span {
  margin-left: 15px;
  font-size: 1rem;
  font-weight: 500;
}

.text-box {
  overflow: hidden;
  text-overflow: ellipsis;
}

.close-icon {
  cursor: pointer;
}


.follow-box {
  height: 450px;
  overflow-y: scroll;
  background-color: rgb(255, 255, 255);
  cursor: pointer;


}

.current-session::-webkit-scrollbar {
  width: 0;
}

.follow-box::-webkit-scrollbar {
  width: 0;
}

.current-session {
  height: 440px;
  overflow-y: scroll;
  background-color: rgb(255, 255, 255);
  cursor: pointer;
}

.current-session-title, .follow-list-title {
  height: 30px;
  font-size: 1rem;
  background-color: rgb(250, 250, 250);
  border: 1px solid rgb(217, 217, 217);
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
}

.current-session-title:hover, .follow-list-title:hover {
  transform: translate(2px, 2px);
}

.current-session-item {
  height: 70px;
  display: flex;
  flex-direction: row;
  align-items: center;

}

.follow-list-item {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.follow-list-item:hover ,.follow-list-item:hover .info-box span{
  background-color: rgb(75, 79, 88);
  color: white;
}

.follow-list-item:focus,.follow-list-item:focus .info-box span {
  background-color: rgb(75, 79, 88);
  color: white;
}

.current-session-item:hover,.current-session-item:hover .info-box span{
  background-color: rgb(75, 79, 88);
  color: white;
}

.current-session-item:focus,.current-session-item:focus .info-box span {
  background-color: rgb(75, 79, 88);
  color: white;
}

/*left-box------------------------------end*/

/*right-box------------------------------start*/
.right-box {
  width: 76%;
  height: 100%;
  background-color: #f5f5f5;
  /*border: 1px solid saddlebrown;*/
}

.info-header {
  height: 5%;
  background-color: rgb(255, 255, 255);
  display: flex;
  align-items: center;
}

.info-header span {
  margin-left: 20px;
  font-size: 1rem;
  font-weight: 600;

}

.message-list {
  width: 100%;
  height: 95%;
  background-color: rgb(245, 245, 245);
  overflow: hidden;
  overflow-y: scroll;
  /*position: relative;*/
  /*border: 1px solid blue;*/
}

.message-list-item-left {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin-bottom: 10px;
}

.message-list-item-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 10px;

}

.message-list-blank {
  width: 100%;
  height: 80%;
  background-color: rgb(245, 245, 245);
}

.sendMessage-box {
  width: 100%;
  height: 20%;
  background-color: rgb(255, 255, 255);
  /*border: 1px solid saddlebrown;*/
}

.sendMessage-box button {
  float: right;
  background-color: rgb(245, 245, 245);
  width: 70px;
  border: 1px solid gray;
}

.sendMessage-box button:hover {
  background-color: rgb(18, 150, 17);
  color: white;
}

.message-box-left {
  width: auto;
}

.message-box-right {
  width: auto;
  order: 2;
}


.message-right {
  text-align: right;
  max-width: 500px;
}

.message-left {
  text-align: left;
  max-width: 500px;

}

.message-text {
  resize: none;
  height: 80%;
}

.message-right {
  background-color: rgb(255, 255, 255);
  color: black;
  padding: 13px 14px;
  border-radius: 6px;
}

/*.message-left::before {*/
/*  content: "";*/
/*  position: absolute;*/
/*  top: 19px;*/
/*  display: block;*/
/*  width: 8px;*/
/*  height: 6px;*/
/*  transform: rotate(29deg) skew(-35deg);*/
/*  background-color: rgb(0, 176, 255);*/
/*}*/

.message-left {
  background-color: rgb(0, 176, 255);
  color: white;
  padding: 13px 14px;
  border-radius: 6px;

}
.triangle-left{
  width: 0;
  height: 0;
  border-top: 5px solid transparent;
  border-right: 10px solid rgb(0, 176, 255);
  border-bottom: 5px solid transparent;
}
.triangle-right{
  width: 0;
  height: 0;
  border-top: 5px solid transparent;
  border-left: 10px solid rgb(255, 255, 255);
  border-bottom: 5px solid transparent;
  order: 1;
}


/*right-box------------------------------end*/

</style>