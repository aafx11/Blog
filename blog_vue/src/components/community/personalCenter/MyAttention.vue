<template>
  <div class="attention-container">
    <div class="title">我的粉丝</div>
    <div class="user-list" v-if="showUserList">
      <div class="user-item" @click="pushUserPage(item.username,item.userId)" v-for="(item,index) in userProfile">
        <UserCard :user-info="item" :mode="1"></UserCard>
      </div>
    </div>
    <div class="noAttention" v-if="showUser">
      <div class="user-list">
        <div class="title">您尚未关注任何人,向您推荐:</div>
        <div class="user-item" @click="pushUserPage(item.username,item.userId)" v-for="(item,index) in userData">
          <UserCard :user-info="item" :mode="1"></UserCard>

        </div>
      </div>
    </div>
    <div class="pagination-box">
      <a-pagination
          show-size-changer
          v-model:current="current"
          v-model:pageSize="pageSize"
          :total="total"
          @showSizeChange="onShowSizeChange"
          v-if="showUserList"
      />
    </div>
  </div>

</template>

<script>
import {ref, onMounted, reactive, watch, getCurrentInstance} from "vue";
import {getFollowListByPage} from '../../../request/api/follow.js'
import {judgeIdentity} from '../../../assets/js/global.js'
import UserCard from './UserCard.vue'
import {useRouter} from "vue-router";
import {getRandomUsers} from '../../../request/api/userList.js'

export default {
  name: "MyAttention",
  components: {
    UserCard: UserCard
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const router = useRouter();
    const pageSize = ref(10);
    const current = ref(1);
    const total = ref();
    const userProfile = reactive();

    const showUserList = ref(false)
    const followListData = () => {
      getFollowListByPage(current.value, pageSize.value).then(res => {

        proxy.userProfile = res.data.data.profileVOList;
        console.log("我的关注", proxy.userProfile);
        total.value = res.data.data.total;
        console.log("关注人数", total.value);
        if (total.value !== 0) {
          showUserList.value = false;
          showUserList.value = true;
        } else {
          getRandomUsersData();
        }


        proxy.$forceUpdate();
      })
    }

    const onShowSizeChange = (current, pageSize) => {
      console.log(current, pageSize);
    };

    watch(pageSize, () => {
      console.log('pageSize', pageSize.value);
      followListData();
    });
    watch(current, () => {
      console.log('current', current.value);
      followListData();
    });

    const pushUserPage = (username, id) => {
      if (judgeIdentity(username)) {
        router.push({name: 'myHomePage'})
      } else {
        router.push({name: 'userInfo', params: {id: id}})
      }
    }

    const userData = reactive({
      userId: ''
    })
    const userCurrent = ref(1)
    const userSize = ref(8)
    const showUser = ref(false)
    const getRandomUsersData = () => {
      getRandomUsers(userCurrent.value, userSize.value).then(res => {
        const data = res.data.data;
        proxy.userData = data.records;
        proxy.userData.forEach(user => {
          user.userId = user.id;
        })
        console.log("随机用户", proxy.userData);
        showUser.value = false;
        showUser.value = true;
      })
    }
    onMounted(() => {
      followListData();
    })
    return {
      pageSize,
      current,
      total,
      userProfile,
      showUserList,
      showUser,
      userData,
      pushUserPage,
      onShowSizeChange,
    }
  }
}
</script>

<style lang="scss" scoped>
@mixin bor($color) {
  border: 1px solid $color;
}

.attention-container {
  width: 100%;
  height: 100%;
  background-color: #fff;

  .user-list {
    height: 95%;

    .user-item {
      height: 92px;
      width: 100%;
      cursor: pointer;
      padding: 18px 20px;
      border-bottom: 1px solid rgb(249, 249, 249);

      &:hover {
        background-color: rgb(242, 242, 242);
      }
    }
  }

  .noAttention {
    height: 95%;
    width: 100%;
    display: flex;
    flex-direction: column;
    //align-items: center;
    //justify-content: flex-start;

    .title {
      font-size: 1rem;
      font-weight: bolder;
    }
  }

  .pagination-box {
    float: right;
  }
}
.title{
  padding: 7px 7px;
  font-size: 18px;
  font-weight: 700;
}

</style>