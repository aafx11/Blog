<template>

  <div class="video-card">
    <router-link :to="{name:'videoDetail',params:{id:videoData.id}}">
    <div class="image-box">
      <img :src="'http://localhost:8081/static/videoCover/'+videoData.coverUrl" alt="">
<!--      <img :src="'http://47.113.204.103:8081/static/videoCover/'+videoData.coverUrl" alt="">-->
    </div>
    </router-link>
    <div class="video-info-box">
      <router-link :to="{name:'videoDetail',params:{id:videoData.id}}">
        <div class="video-info-title">{{videoData.title}}</div>
      </router-link>
      <div class="video-info-author">
        <a-avatar :src="'http://localhost:8081/static/avatar/'+videoData.avatar" class="avatar"></a-avatar>
<!--        <a-avatar :src="'http://47.113.204.103:8081/static/avatar/'+videoData.avatar" class="avatar"></a-avatar>-->
        <div class="info-box">
          <div class="nickname-box"><span>@</span>{{videoData.nickname}}</div>
          <div>
            <span v-for="tag in videoData.tags" class="tags-style">{{tag.name}}</span>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "videoCard",
  props:{
    videoData:{
      type:Object,
      default:null
    }
  }
}
</script>

<style scoped>
a{
  color: #000;
}
.video-card{
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 10px;
}
.image-box{
  height: 70%;
  width: 100%;
  overflow: hidden;
  border-radius: 10px;
}

.image-box img{
  width: 100%;
  height: 100%;
  transition: all 0.3s ease-out;
  /* 过度 所有css属性 1秒过度 结束慢*/
  -moz-transition: all 0.3s ease-out;
  /* Firefox 4 */
  -webkit-transition: all 0.3s ease-out;
  /* Safari 和 Chrome */
  -o-transition: all 0.3s ease-out;
  /* Opera */
  overflow: hidden;
}
.image-box img:hover{
  width: 100%;
  height: 100%;
  transform: scale(1.2, 1.2);
  overflow: hidden;
}
.video-info-box{
  padding: 0px 5px;
  height: 30%;

}
.video-info-title{
  font-size: 0.8rem;
  font-weight: 700;
  height: 40px;
}
.video-info-title:hover{
  color: rgb(255, 130, 0);
}
.video-info-author{
  /*height: 100%;*/
  display: flex;
  flex-direction: row;
  /*justify-content: center;*/
  align-items: center;
  padding-top: 10px;
}
.tags-style{
  border: 0px;
}
.nickname-box{
  color: #939393;
}
.nickname-box:hover{
  color: rgb(255, 130, 0);
  cursor: pointer;
}
.info-box{
  margin-left: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  /*align-items: center;*/
  line-height: 0.9rem;
}
.avatar{
  cursor: pointer;
}
.tags-style{
  color: rgb(255, 135, 14);
  cursor: pointer;
  font-size: 0.7rem;
  background-color: #fff5ef;
  margin-right: 5px;

}
</style>