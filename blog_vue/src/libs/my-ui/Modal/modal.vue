<template>
  <div
      class="modal-mask"
      v-show="show"
  >
    <div
        class="my-modal"
        ref="myModal"
        :style="{
          width:width + 'px',
          borderRadius: borderRadius + 'px',
          marginLeft: - width / 2 + 'px',
          top: position ==='center' ? '50%' : '50px',
          marginTop: position === 'center' ? - myModalHeight / 2 + 'px' : ''
        }"
    >
      <header
          class="header"
          :style="{
            backgroundColor:titleBackground,
            borderBottom:`1px solid ${borderBottomColor}`

          }"
      >
        <slot name="title">
          <h1
              :style="{
              color:titleTextColor
            }">{{ titleText }}</h1>
        </slot>
        <a
            href="javascript:;"
            :style="{
              color: titleTextColor
            }"
            @click="close"
        >&times;</a>
      </header>
      <article
          class="content"
          :style="{
             borderBottom: bottomShow ? `1px solid ${borderBottomColor}` : ''
          }"
      >
        <slot name="content">
          <p
              :style="{
              color:contentTextColor,
          }"
          >{{ contentText }}</p>
        </slot>
      </article>
      <div
          class="btn-group"
          v-if="bottomShow"
      >
        <slot name="bottom">
          <button
              class="btn btn-confirm"
              :style="{
              backgroundColor: confirmBackground
            }"
              @click="confirm"
          >{{ confirmText }}
          </button>
          <button
              class="btn btn-cancel"
              :style="{
              backgroundColor: cancelBackground
            }"
              @click="close"
          >{{ cancelText }}
          </button>
        </slot>
      </div>
    </div>
  </div>
</template>

<script>

import {onMounted, onUpdated, reactive, ref, toRefs, watch} from 'vue'
/*
*  show:是否展示模态框，
*  bottomShow：是否展示底部按钮
*  @confirm:确定函数
*  @close:关闭函数， show.value = false;
*  <template #title>:自定义标题部分
*  <template #content>:自定义内容部分
*  <template #bottom>：自定义底部
* */
export default {
  name: "my-modal",// name要小写
  props: {
    show: {
      type: Boolean,
      default: false
    },
    width: {
      type: Number,
      default: 300
    },
    borderRadius: {
      type: Number,
      default: 0
    },
    titleBackground: {
      type: String,
      default: '#fff'
    },
    titleText: {
      type: String,
      default: '标题'
    },
    titleTextColor: {
      type: String,
      default: '#000'
    },
    // 内部边框颜色,开头字母要小写
    borderBottomColor: {
      type: String,
      default: '#c4c2c2'
    },
    contentText: {
      type: String,
      default: '内容'
    },
    contentTextColor: {
      type: String,
      default: '#000'
    },
    position: {
      type: String,
      default: 'top' //top,center
    },
    // 底部按钮展示
    bottomShow: {
      type: Boolean,
      default: false
    },
    confirmText: {
      type: String,
      default: '确定'
    },
    confirmBackground: {
      type: String,
      default: '#0052d9'
    },
    cancelText: {
      type: String,
      default: '取消'
    },
    cancelBackground: {
      type: String,
      default: '#ddd'
    },

  },
  setup(props, ctx) {

    const myModal = ref(null);

    const state = reactive({
      myModalHeight: 0,
      modalShow: props.show
    })

    // onMounted(() => {
    //   state.myModalHeight = myModal.value.offsetHeight;
    //   console.log('高度',state.myModalHeight);
    // })

    onUpdated(() => {
      state.myModalHeight = myModal.value.offsetHeight;

    })

    // 确认事件回调
    const confirm = () => {
      // state.modalShow = false;
      ctx.emit('confirm')

    }

    // 取消事件回调
    const close = () => {
      // state.modalShow = false

      ctx.emit('close')
    }


    return {
      myModal,
      ...toRefs(state),
      confirm,
      close
    }
  }
}
</script>

<style lang="scss" scoped>
.modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .2);
  z-index: 1000;


  a {
    text-decoration: none;
    outline: none;
    color: #333;
  }

  h1, p {
    font-weight: normal;
    margin: 0;
  }

  button {
    outline: none;
    border: none;
  }

  .my-modal {
    position: fixed;
    left: 50%;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
    box-shadow: 1px 2px 3px #333;

    .header {
      position: relative;
      //height: 44px;
      min-height: 44px;
      padding: 0 15px;
      box-sizing: border-box;

      h1 {
        display: inline-block;
        font-size: 18px;
        line-height: 44px;
      }

      a {
        position: absolute;
        top: -5px;
        right: 8px;
        //float: right;
        font-size: 24px;
        //margin-top: 6px;
      }
    }

    .content {
      padding: 15px;
      box-sizing: border-box;
    }

    .btn-group {
      //height: 50px;
      min-height: 50px;
      padding: 10px 15px;

      .btn {
        float: right;
        min-width: 80px;
        height: 30px;
        font-size: 14px;
        border-radius: 3px;
        cursor: pointer;

        &.btn-confirm {
          color: #fff;
          margin-left: 15px;
        }

        &.btn-cancel {
          color: #666;
        }
      }

    }
  }
}
</style>