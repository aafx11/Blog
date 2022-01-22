<template>
  <!--  <el-popover placement="left-start" trigger="hover" width="300" popper-class="popper"-->
  <!--              v-for="(item,index) in articleData">-->
  <!--    <template #reference>-->
  <!--      <div class="container-box">-->
  <!--        <div class="container-inner">-->
  <!--          <img :src="'http://localhost:8081/static/articleCover/'+item.cover" class="cover"/>-->
  <!--&lt;!&ndash;          <img :src="'http://47.113.204.103:8081/static/articleCover/'+item.cover" class="cover"/>&ndash;&gt;-->
  <!--          <router-link :to="{name:'articleDetail',params: {id:item.id}}">-->
  <!--            <div class="text-box">-->
  <!--              <div class="title" v-text="item.title"></div>-->
  <!--              <div class="br-line"></div>-->
  <!--              <div class="introduction" v-text="item.introduction"></div>-->
  <!--            </div>-->
  <!--          </router-link>-->
  <!--          <div class="tag-box">-->
  <!--            <a-button @click="pushTagFile(tag.id)" v-for="tag in item.tags" class="buttonColor">{{-->
  <!--                tag.name-->
  <!--              }}-->
  <!--            </a-button>-->
  <!--          </div>-->
  <!--        </div>-->
  <!--      </div>-->
  <!--    </template>-->
  <!--    <span @click="pushAuthorPage(item.username,item.userId)" class="author-button"><UserOutlined/>作者:{{ item.nickname }}</span>-->
  <!--    <span class="Calendar"><CalendarOutlined/>{{ item.created }}</span>-->
  <!--    <div class="icon-box">-->
  <!--      &lt;!&ndash;已点赞&ndash;&gt;-->
  <!--      <span class="like" v-if="item.hasLike" :key="item.userId">-->
  <!--            <LikeOutlined :style="{fontSize: '20px', color: '#0060ff'}" @click="unLike(item.id)"/>{{ item.likeCount }}-->
  <!--      </span>-->
  <!--      &lt;!&ndash;未点赞&ndash;&gt;-->
  <!--      <span class="like" v-else><LikeOutlined @click="like(item.id)"/>{{ item.likeCount }}</span>-->
  <!--      &lt;!&ndash;取消收藏&ndash;&gt;-->
  <!--      <span class="collect" v-if="item.hasCollect" :key="item.userId">-->
  <!--            <StarOutlined :style="{fontSize: '20px', color: '#08c'}"-->
  <!--                          @click="unCollectArticle(item.id)"/>{{ item.collects }}-->
  <!--      </span>-->
  <!--      &lt;!&ndash;收藏&ndash;&gt;-->
  <!--      <span class="collect" v-else :key="item.id">-->
  <!--            <StarOutlined :style="{fontSize: '20px'}" @click="collectArticle(item.id)"/>{{ item.collects }}-->
  <!--    </span>-->
  <!--      <span class="share">-->
  <!--      <ShareAltOutlined/>-->
  <!--    </span>-->
  <!--    </div>-->
  <!--  </el-popover>-->
  <!--  <router-link :to="{name:'articleDetail',params: {id:item.id}}" v-for="(item,index) in articleData">-->
  <div class="item-container" @click="pushToDetail(item.id)" v-for="(item,index) in articleData">
    <div class="info-container">
      <div class="user-message" @click.stop="pushAuthorPage(item.username,item.userId)">{{ item.nickname }}</div>
      <div class="dividing"></div>
      <div class="date">{{ item.created }}</div>
      <div class="dividing"></div>
      <div class="tag-list">
        <div class="tag" v-for="tag in item.tags" @click.stop="pushTagFile(tag.id)">{{ tag.name }}</div>
      </div>
    </div>
    <div class="content-wrapper">
      <div class="info-box">
        <div class="title">{{ item.title }}</div>
        <div class="introduction-box">{{ item.introduction }}</div>
        <div class="bottom-box">
          <!--          <div class="iconfont icon-guankancishu item">{{ item.view }}</div>-->
          <span class="item">
            <EyeOutlined/>
            {{ item.view }}
          </span>
          <div class="item">
            <span v-if="item.hasLike" :key="item.userId">
              <LikeOutlined :style="{ color: '#0060ff'}" @click.stop="unLike(item.id)"/>
              {{ item.likeCount }}
            </span>
            <span v-else>
              <LikeOutlined @click.stop="like(item.id)"/>
              {{ item.likeCount }}
            </span>

          </div>
          <div class="item">
               <span v-if="item.hasCollect" :key="item.userId">
                <StarOutlined :style="{ color: '#08c'}"
                              @click.stop="unCollectArticle(item.id)"/>{{ item.collects }}
         </span>
            <span v-else :key="item.id">
                              <StarOutlined @click.stop="collectArticle(item.id)"/>{{ item.collects }}
                      </span>
          </div>
        </div>
      </div>
      <img :src="'http://localhost:8081/static/articleCover/'+item.cover" alt="加载失败">
    </div>
    <!--          <div class="icon-box">-->
    <!--            &lt;!&ndash;已点赞&ndash;&gt;-->
    <!--            <span class="like" v-if="item.hasLike" :key="item.userId">-->
    <!--                  <LikeOutlined :style="{fontSize: '20px', color: '#0060ff'}" @click="unLike(item.id)"/>{{ item.likeCount }}-->
    <!--            </span>-->
    <!--            &lt;!&ndash;未点赞&ndash;&gt;-->
    <!--            <span class="like" v-else><LikeOutlined @click="like(item.id)"/>{{ item.likeCount }}</span>-->
    <!--            &lt;!&ndash;取消收藏&ndash;&gt;-->
    <!--            <span class="collect" v-if="item.hasCollect" :key="item.userId">-->
    <!--                  <StarOutlined :style="{fontSize: '20px', color: '#08c'}"-->
    <!--                                @click="unCollectArticle(item.id)"/>{{ item.collects }}-->
    <!--            </span>-->
    <!--            &lt;!&ndash;收藏&ndash;&gt;-->
    <!--            <span class="collect" v-else :key="item.id">-->
    <!--                  <StarOutlined :style="{fontSize: '20px'}" @click="collectArticle(item.id)"/>{{ item.collects }}-->
    <!--          </span>-->
    <!--            <span class="share">-->
    <!--            <ShareAltOutlined/>-->
    <!--          </span>-->
    <!--          </div>-->

  </div>
  <!--  </router-link>-->
</template>

<script>
import {ref, reactive, watch, getCurrentInstance, onMounted, computed, h} from 'vue'
import {
  StarOutlined,
  LikeOutlined,
  MessageOutlined,
  CheckCircleTwoTone,
  UserOutlined,
  CalendarOutlined,
  ShareAltOutlined,
  EyeOutlined

} from '@ant-design/icons-vue';
import {message} from 'ant-design-vue';
import {useRouter} from "vue-router";
import {judgeIdentity} from '../../../assets/js/global.js'
import {likewise, unLikewise, collect, unCollect} from '../../../request/api/article.js'

export default {
  name: "ArticleListHorizontal",
  inject: ['getArticleList'],
  props: {
    articleData: {
      type: Object,
      default: null
    }
  },
  components: {
    StarOutlined,
    LikeOutlined,
    MessageOutlined,
    CheckCircleTwoTone,
    UserOutlined,
    CalendarOutlined,
    ShareAltOutlined,
    EyeOutlined

  },
  setup() {
    const router = useRouter();
    const {proxy} = getCurrentInstance();
    const tooltipStyle = reactive({
      'color': 'blue',
      border: '1px solid red'
    })

    const pushTagFile = (id) => {
      router.push({name: 'tagDetail', params: {id: id}})
    }

    const pushAuthorPage = (username, id) => {
      let flag = judgeIdentity(username);
      if (flag) {
        router.push({name: 'myHomePage'});
      } else {
        router.push({name: 'userInfo', params: {id: id}});
      }
    }

    const unLike = (id) => {
      unLikewise(id).then(res => {
        proxy.getArticleList();
      })
    }
    const like = (id) => {
      likewise(id).then(res => {
        if (res.data.code == 200) {
          message.success(res.data.msg);
        }
        proxy.getArticleList();
      })
    }

    const unCollectArticle = (id) => {
      unCollect(id).then(res => {
        proxy.getArticleList();

      })
    }

    const collectArticle = (id) => {
      collect(id).then(res => {
        if (res.data.code == 200) {
          message.success(res.data.msg);
        }
        proxy.getArticleList();

      })
    }

    const pushToDetail = (id) => {
      router.push({name: 'articleDetail', params: {id: id}})
    }
    return {
      tooltipStyle,
      pushTagFile,
      pushAuthorPage,
      unLike,
      like,
      unCollectArticle,
      collectArticle,
      pushToDetail,
    }
  }
}
</script>

<style scoped>

.author-button {
  display: block;
  background: none;
  color: white;
  border: 0px;
  text-decoration: dotted underline;
  font-size: 1rem;
}

.Calendar {
  display: block;
  color: white;
  border: 0px;
  font-size: 1rem;
}

.icon-box {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.like {
  float: left;
  display: block;
  color: white;
  border: 0px;
  font-size: 1rem;
}

.collect {
  display: block;
  color: white;
  border: 0px;
  font-size: 1rem;
  margin-top: 1px;
}

.share {
  display: block;
  color: white;
  border: 0px;
  font-size: 1rem;
}

.author-button:hover {
  color: rgb(96, 203, 101);
  cursor: pointer;
}

.container-box {
  position: relative;
  height: 300px;
  width: 100%;
  border: 2px solid #ddd;
  background-color: #fff;
  border-radius: 15px;
  overflow: hidden;
  /*cursor: pointer;*/
  box-shadow: 0 4px 21px -12px rgba(0, 0, 0, .66);
  transition: box-shadow 0.2s ease, transform 0.2s ease;
  margin-bottom: 5%;
}


.container-box:hover {
  box-shadow: 0 34px 32px -33px rgba(0, 0, 0, .18);
  transform: translate(-10px, -10px);
}

.container-box::before {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  /*background-image: linear-gradient(-70deg, #F5AF41, transparent 50%);*/
  background-image: linear-gradient(-70deg, #0088FF, transparent 50%);
  opacity: 0.07;
}

.container-box:nth-child(2n)::before {
  background-image: linear-gradient(-250deg, #ff0026, transparent 50%);

  /*background-image: linear-gradient(-250deg, #0d10dc, transparent 50%);*/
}

.container-inner {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.cover {
  position: absolute;
  height: 300px;
  width: 47%;
  top: 0;
  left: 0;
  transition: transform 0.2s ease;
}

.container-box:nth-child(2n) .cover {
  left: initial;
  right: 0;
}

.container-box:hover .cover {
  transform: scale(1.05) rotate(1deg);
}

.text-box {
  position: absolute;
  top: 7%;
  bottom: 7%;
  left: 50%;
  width: calc(100% - 470px);
  font-size: 17px;
  width: 45%;

}

.tag-box {
  position: absolute;
  bottom: 5%;
  left: 50%;
}

.container-box:nth-child(2n) .tag-box {
  left: initial;
  left: 5%;
}

.tag-box button {

}

.tag-box button:hover {
  transform: translate(-5px, -5px);
}

.container-box:nth-child(2n) .text-box {
  left: initial;
  right: 50%;
}

.text-box::before,
.text-box::after {
  content: "";
  position: absolute;
  display: block;
  background: #ff0000bb;
  background: #fff;
  top: -20%;
  left: -15%;
  height: 140%;
  width: 17%;
  transform: rotate(8deg);
}

.container-box:nth-child(2n) .text-box::before,
.container-box:nth-child(2n) .text-box::after {
  content: "";
  position: absolute;
  display: block;
  background: #ff0000bb;
  background: #fff;
  top: -20%;
  right: -15%;
  height: 140%;
  width: 17%;
  transform: rotate(8deg);
}

@media screen and (max-width: 576px) {
  .container-box:nth-child(2n) .text-box::before,
  .container-box:nth-child(2n) .text-box::after {
    content: "";
    position: absolute;
    display: block;
    background: #ff0000bb;
    background: #fff;
    top: -20%;
    right: -20%;
    height: 140%;
    width: 26%;
    transform: rotate(8deg);
  }
}

@media screen and (max-width: 576px) {
  .text-box::before,
  .text-box::after {
    content: "";
    position: absolute;
    display: block;
    background: #ff0000bb;
    background: #fff;
    top: -20%;
    right: -18%;
    height: 140%;
    width: 26%;
    transform: rotate(8deg);
  }
}


@media screen and (max-width: 768px) {
  .text-box::before,
  .text-box::after {
    content: "";
    position: absolute;
    display: block;
    background: #ff0000bb;
    background: #fff;
    top: -20%;
    left: -20%;
    height: 140%;
    width: 23%;
    transform: rotate(8deg);
  }
}

.container-box:nth-child(2n) .text-box::before {
  display: none;
}

.text-box::after {
  display: none;
  left: initial;
  right: -55px;
}

.container-box:nth-child(2n) .text-box::after {
  display: block;
}

.text-box * {
  position: relative;
}

.title {
  font-family: 'Voces', 'Open Sans', arial, sans-serif;
  font-size: 1.3rem;
  /*text-align: center;*/
}


.br-line {
  left: -2px;
  width: 13%;
  height: 5px;
  margin: 5% 0;
  border-radius: 5px;
  background-color: #0088FF;
  transition: width 0.2s ease;
}

.container-box:nth-child(2n) .br-line {
  background-color: #D62F1F;
}

.container-box:hover .br-line {
  width: 23%;
}

.introduction {
  z-index: 10;
  font-size: 15px;
  color: #424242;
  height: 125px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.buttonColor {
  margin-right: 5px;
}

.container-box:nth-child(odd) .tag-box .buttonColor {
  color: rgb(9, 109, 217);
  background: rgb(230, 247, 255);
  border-color: rgb(145, 213, 255);
  font-size: 0.8rem;
}

.container-box:nth-child(even) .tag-box .buttonColor {
  color: rgb(207, 19, 34);
  background: rgb(255, 241, 240);
  border-color: rgb(255, 163, 158);
  font-size: 0.8rem;
}

a {
  color: #0c0c0c;
}

/*新布局*/
.item-container {
  width: 100%;
  padding: 12px 20px 0 20px;
  cursor: pointer;
  border-bottom: 1px solid #e5e6eb;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.item-container:hover {
  background-color: #fafafa;
}

.info-container {
  display: flex;
  align-items: center;
  font-size: 16px;

}

.user-message {
  color: rgb(78, 89, 105);

}

.user-message:hover {
  color: #1d7dfa;
}

.dividing {
  width: 2px;
  height: 14px;
  background-color: #e5e6eb;
  margin: 0 8px;
}

.date {
  color: #86909c;
}

.content-wrapper {
  display: flex;
  margin-top: 8px;
  width: 100%;
  padding-bottom: 12px;
}

.info-box {
  flex-grow: 1;
}

.info-box .title {
  color: rgb(29, 33, 41);
  font-size: 18px;
  font-weight: 700;
}

.info-box .introduction-box {
  padding-top: 10px;
  padding-bottom: 8px;
  color: #86909c;

}

.content-wrapper img {
  width: 120px;
  height: 80px;
  margin-left: 24px;
  border-radius: 2px;
  flex: 0 0 auto;
  object-fit: cover;
}

.bottom-box {
  display: flex;
  align-items: center;
  color: #86909c;
  font-size: 18px;
}

.bottom-box .item {
  height: 100%;
  margin-right: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tag-list {
  display: flex;
  align-items: center;
}

.tag-list .tag:not(:last-child):after {
  position: absolute;
  right: -1px;
  top: 50%;
  display: block;
  content: " ";
  width: 2px;
  height: 2px;
  border-radius: 50%;
  background: #4e5969;
}

.tag {
  position: relative;
  color: #86909c;
  display: flex;
  align-items: center;
  padding: 0 8px;
}

.tag:hover {
  color: #1d7dfa;

}
</style>