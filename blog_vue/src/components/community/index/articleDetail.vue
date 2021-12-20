<template>
  <Nav></Nav>
  <a-row :gutter="16" class="content">
    <!--文章主体-->
    <a-col :lg="{span:10,offset:5}" class="article-box">
      <div>
        <img :src="'http://localhost:8081/static/articleCover/'+article.cover" class="cover"/>
<!--        <img :src="'http://47.113.204.103:8081/static/articleCover/'+article.cover" class="cover"/>-->
      </div>
      <div class="article-content">
        <div class="title">
          <span>{{ article.title }}</span>
        </div>
        <router-link
            v-for="tag in tags"
            :to="{name:'tagDetail',params:{id : tag.id}}"
        >
          <a-tag color="blue">
            {{ tag.name }}
          </a-tag>
        </router-link>
        <div class="author-box">
          <a-avatar
              :src="'http://localhost:8081/static/avatar/'+author.avatar"
              :size="64"
              @click="pushAuthPageById(author.username,author.userId)"
              class="avatar"
          >
          </a-avatar>
<!--          <a-avatar-->
<!--              :src="'http://47.113.204.103:8081/static/avatar/'+author.avatar"-->
<!--              :size="64"-->
<!--              @click="pushAuthPageById(author.username,author.userId)"-->
<!--              class="avatar"-->
<!--          >-->
<!--          </a-avatar>-->
          <div class="info-box">
            <p @click="pushAuthPageById(author.username,author.userId)" class="nickname">{{ author.nickname }}</p>
            <p>{{ article.created }}</p>
          </div>
          <div class="view">
            <div class="iconfont icon-guankancishu eye-icon"></div>
            <div>{{ article.view }}</div>
          </div>
        </div>
        <v-md-preview :text="article.content"></v-md-preview>
        <hr class="bottom-hr">
      </div>
        <comment
            :article="article"
            v-if="show"
        ></comment>

    </a-col>
    <!--关于作者-->
    <a-col :lg="{span:4}">
      <author
          :author="author"
          v-if="show"
      >

      </author>

    </a-col>
  </a-row>
</template>

<script>
import home_Nav from '../home/home_Nav.vue'
import author from './author.vue'
import comment from './comment.vue'
import {useRouter, useRoute} from "vue-router"
import {getArticleById} from '../../../request/api/article.js'
import {onMounted, getCurrentInstance, reactive, ref} from "vue";
import ProfileCard from '../personalCenter/ProfileCard.vue'
import {judgeIdentity} from '../../../assets/js/global.js'

export default {
  name: "articleDetail",
  provide() {
    return {
      alive: this.alive
    }
  },
  components: {
    Nav: home_Nav,
    author: author,
    comment: comment,
    ProfileCard: ProfileCard,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const route = useRoute();
    const router = useRouter();
    const tags = reactive([])
    const author = reactive({})
    const article = reactive({})
    const articleId = ref()
    const show = ref(false)
    const getArticle = async () => {
      getArticleById(route.params.id).then(res => {
        console.log("获取的数据", res.data.data);
        proxy.article = res.data.data.article;
        articleId.value = res.data.data.article.id;
        proxy.tags = res.data.data.tags;
        proxy.author = res.data.data.userProfile
        console.log(proxy.author);
        proxy.$forceUpdate();
        console.log("文章内容",proxy.article.content);
        show.value = true
      })
    }

    const alive = () => {
      show.value = false;
      console.log(show.value);
      proxy.$nextTick(() => {
        show.value = true
        console.log(show.value);

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
      getArticle();
    })
    return {
      tags,
      author,
      article,
      show,
      articleId,
      getArticle,
      alive,
      pushAuthPageById,
    }
  }
}
</script>

<style scoped>
body {
  background-color: #f5f5f5 !important;

}

.content {
  padding: 0 0;
  margin-top: 65px;
  height: 100%;
  width: 100%;
}

.article-box {
  /*background-color: #fff;*/
}

.article-content {
  padding: 0px 10px;
  background-color: #fff;

}

.preview {
  border: 1px solid red;
  margin-top: 50px;
}

.cover {
  width: 100%;
  height: 400px;
}

.title {
  text-align: center;
  font-size: 2rem;
}

.author-box {
  display: flex;
  flex-direction: row;
  /*justify-content: flex-start;*/
  margin-top: 20px;
  width: 90%;
}

.avatar {
  cursor: pointer;
}

.info-box {
  margin-left: 10px;
}

.nickname {
  font-weight: 700;
  font-size: 1rem;
  margin-top: 5px;
  margin-bottom: 5px;
  cursor: pointer;
}

.view {
  margin-left: auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.eye-icon {
  font-size: 2rem;
}

.eye-icon + div {
  font-size: 1.3rem;

}

.bottom-hr {
  border-color: rgba(0, 0, 0, 0.12);;
}
.article-foot {
  /*position: relative;*/
  /*left: 0;*/
  margin:0 auto;
  float: right;
}
</style>