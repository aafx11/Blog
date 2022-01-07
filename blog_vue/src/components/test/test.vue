<template>








<!--  mode="vertical"-->

  <my-menu background-color="black" text-color="#fff"   >
    <template v-for="item of menu">
      <my-menu-item v-if="!item.children" :key="item.id" :path="item.path">
        {{ item.title }}
      </my-menu-item>
      <my-reSub-menu :data="item" v-else>

      </my-reSub-menu>
    </template>
  </my-menu>

</template>

<script>
import {
  computed,
  defineComponent,
  toRefs,
  getCurrentInstance,
  onMounted,
  reactive,
  ref,
  watch,
  onBeforeMount
} from 'vue';


export default {
  name: "test",
  setup() {
    let dongtai = ref('')
    let menu = [
      {
        id: 1,
        title: '菜单1111',
        path: {name: 'test1'}
      },
      {
        id: 2,
        title: '菜单2222',
        children: [
          {
            id: 21,
            title: '子菜单21',
            path: {name: 'test2'}
          },
          {
            id: 22,
            title: '子菜单22',
            children: [
              {
                id: 221,
                title: '子菜单221',
                children: [
                  {
                    id: 2211,
                    title: '子菜单2211',
                    path:{name:'test2'}
                  },
                  {
                    id: 2212,
                    title: '子菜单2212'
                  },
                  {
                    id: 2213,
                    title: '子菜单2213'
                  },
                ]
              },
              {
                id: 222,
                title: '子菜单222'
              },
              {
                id: 223,
                title: '子菜单223'
              },
            ]
          },
          {
            id: 23,
            title: '子菜单23',
            path:{name:'test2'}
          },
        ]
      },
      {
        id: 3,
        title: '菜单333',
        path: {name: 'test2'}
      },
    ]
    const dianji = () => {
      dongtai.value = 'active'
    }

    return {dongtai, menu}
  }

}


</script>

<style lang="scss" scoped>


.router-link-active {
  background-color: pink;
}


</style>



<!--<template>-->
<!--  <a-menu-->
<!--      v-model:openKeys="openKeys"-->
<!--      v-model:selectedKeys="selectedKeys"-->
<!--      style="width: 256px"-->
<!--      mode="vertical"-->
<!--      @click="handleClick"-->
<!--  >-->
<!--    <a-menu-item key="1">-->
<!--      <template #icon>-->
<!--        <MailOutlined />-->
<!--      </template>-->
<!--      Navigation One-->
<!--    </a-menu-item>-->
<!--    <a-menu-item key="2">-->
<!--      <template #icon>-->
<!--        <CalendarOutlined />-->
<!--      </template>-->
<!--      Navigation Two-->
<!--    </a-menu-item>-->
<!--    <a-sub-menu key="sub1">-->
<!--      <template #icon>-->
<!--        <AppstoreOutlined />-->
<!--      </template>-->
<!--      <template #title>Navigation Three</template>-->
<!--      <a-menu-item key="3">Option 3</a-menu-item>-->
<!--      <a-menu-item key="4">Option 4</a-menu-item>-->
<!--      <a-sub-menu key="sub1-2" title="Submenu">-->
<!--        <a-menu-item key="5">Option 5</a-menu-item>-->
<!--        <a-menu-item key="6">Option 6</a-menu-item>-->
<!--      </a-sub-menu>-->
<!--    </a-sub-menu>-->
<!--    <a-sub-menu key="sub2">-->
<!--      <template #icon>-->
<!--        <SettingOutlined />-->
<!--      </template>-->
<!--      <template #title>Navigation Four</template>-->
<!--      <a-menu-item key="7">Option 7</a-menu-item>-->
<!--      <a-menu-item key="8">Option 8</a-menu-item>-->
<!--      <a-menu-item key="9">Option 9</a-menu-item>-->
<!--      <a-menu-item key="10">Option 10</a-menu-item>-->
<!--    </a-sub-menu>-->
<!--  </a-menu>-->
<!--</template>-->
<!--<script>-->
<!--import { defineComponent, reactive, toRefs } from 'vue';-->
<!--import {-->
<!--  MailOutlined,-->
<!--  CalendarOutlined,-->
<!--  AppstoreOutlined,-->
<!--  SettingOutlined,-->
<!--} from '@ant-design/icons-vue';-->
<!--export default defineComponent({-->
<!--  setup() {-->
<!--    const state = reactive({-->
<!--      selectedKeys: [],-->
<!--      openKeys: [],-->
<!--    });-->

<!--    const handleClick = e => {-->
<!--      console.log('click ', e);-->
<!--    };-->

<!--    return { ...toRefs(state), handleClick };-->
<!--  },-->

<!--  components: {-->
<!--    MailOutlined,-->
<!--    CalendarOutlined,-->
<!--    AppstoreOutlined,-->
<!--    SettingOutlined,-->
<!--  },-->
<!--});-->
<!--</script>-->