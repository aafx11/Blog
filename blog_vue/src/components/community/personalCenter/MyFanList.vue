<template>
  <!--  <a-list item-layout="horizontal" :data-source="userProfile">-->
  <!--    <template #renderItem="{ item ,index}">-->
  <!--      <a-list-item>-->
  <!--        <a-list-item-meta :description="item.content">-->
  <!--          <template #title>-->
  <!--            <router-link :to="{name:'userInfo',params:{id:item.userId}}">{{item.nickname}}</router-link>-->
  <!--          </template>-->

  <!--        </a-list-item-meta>-->
  <!--      </a-list-item>-->
  <!--    </template>-->
  <!--  </a-list>-->
  <div class="attention-container">
    <div class="title">我的粉丝</div>
    <div class="user-list" v-if="userProfile">
      <div class="user-item" @click="pushUserPage(item.username,item.userId)" v-for="(item,index) in userProfile" >
        <UserCard :user-info="item" :mode="1"></UserCard>
      </div>
    </div>
    <div class="pagination-box">
      <a-pagination
          v-model:current="current"
          :page-size-options="pageSizeOptions"
          :total="total"
          show-size-changer
          :page-size="pageSize"
          @showSizeChange="onShowSizeChange"
      >
        <template #buildOptionText="props">
          <span v-if="props.value !== total">{{ props.value }}条/页</span>
          <span v-else>全部</span>
        </template>
      </a-pagination>
    </div>
  </div>

</template>
<script>
import {defineComponent, getCurrentInstance, onMounted, reactive, ref, watch} from 'vue';
import {getFansList} from '../../../request/api/follow.js'
import {useRoute, useRouter} from "vue-router";
import UserCard from './UserCard.vue'
import {judgeIdentity} from '../../../assets/js/global.js'


export default defineComponent({
  name:'myFanList',
  components:{
    UserCard:UserCard
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const pageSizeOptions = ref(['5', '10', '20', '30', '40', '50']);
    const current = ref(1);
    const pageSizeRef = ref(5);
    const total = ref();
    const userProfile = reactive()
    const onShowSizeChange = (current, pageSize) => {
      console.log(pageSize);
      pageSizeRef.value = pageSize;
    };

    watch(pageSizeRef, () => {
      console.log('pageSize', pageSizeRef.value);
      getFansListData();

    });
    watch(current, () => {
      console.log('current', current.value);
      getFansListData();
    });

    const getFansListData = () => {
      getFansList(current.value, pageSizeRef.value).then(res => {
        console.log(res);
        proxy.userProfile = res.data.data
        proxy.$forceUpdate();

      })
    }
    const pushUserPage = (username,id) => {
      if (judgeIdentity(username)){
        router.push({name:'myHomePage'})
      } else {
        router.push({name:'userInfo',params:{id:id}})
      }
    }

    onMounted(() => {
      getFansListData();
    })
    return {
      pageSizeOptions,
      current,
      pageSize: pageSizeRef,
      total,
      userProfile,
      onShowSizeChange,
      getFansListData,
      pushUserPage,
    };
  },
});
</script>
<style lang="scss" scoped>
.attention-container{
  width: 100%;
  height: 100%;
  background-color: #fff;
}
.user-list {
  height: 95%;
  .user-item{
    height: 92px;
    width: 100%;
    cursor: pointer;
    padding: 18px 20px;
    border-bottom: 1px solid rgb(249, 249, 249);
    &:hover{
      background-color: rgb(242, 242, 242);
    }
  }
}
.pagination-box{
  float: right;
}
.title{
  padding: 7px 7px;
  font-size: 18px;
  font-weight: 700;
}
</style>