<template>
<!--  <my-modal-->

<!--      :width=600-->
<!--      :border-radius="10"-->
<!--      title-background="pink"-->
<!--      title-text="测试"-->
<!--      title-text-color="white"-->
<!--      border-bottom-color="#666"-->
<!--      content-text="烧0现状"-->
<!--      content-text-color="pink"-->
<!--      @confirm="confirm"-->
<!--      @close="close"-->
<!--      :btn-group-show="true"-->
<!--      confirm-text="是的"-->
<!--      confirm-background="blue"-->
<!--      cancel-text="否"-->
<!--      cancel-background="black"-->
<!--      position="center"-->
<!--      :show="sho"-->
<!--  >-->
<!--    <template #title>-->
<!--      <div>测试</div>-->
<!--    </template>-->
<!--   <template #content>-->
<!--     <div class="test">123</div>-->
<!--     <div class="test">123</div>-->
<!--     <div class="test">123</div>-->
<!--     <div class="test">123</div>-->

<!--     <div class="test">123</div>-->
<!--     <div class="test">123</div>-->
<!--     <div class="test">123</div>-->

<!--   </template>-->
<!--    <template #bottom>-->

<!--    </template>-->
<!--  </my-modal>-->
  <my-modal :show="sho"
            @confirm="confirm"
            @close="close"
            :bottom-show="true"
  >
    <template #bottom>
<!--      <div>123</div>-->
    </template>
  </my-modal>
  <!--      @confirm="confirm"-->
  <!--      @close="close"-->
  <div class="test" @click="test">123</div>
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
    let sho = ref(false)
    const test = () =>{
      sho.value = true

    }

    const confirm = () =>{
      console.log('确认');
    }

    const close = () =>{
      console.log('关闭');
      sho.value = false
    }
    return {
      sho,
      confirm,
      close,
      test
    }
  }

}


</script>

<style lang="scss" scoped>
.test{

  height: 100px;
  //background-color: black;
}

</style>