<template>
  <div class="sub-menu"
       @mouseenter="showSubMenu"
       @mouseleave="hideSubMenu"
  >
    <div class="title">
      <slot name="title"></slot>
      <span class="icon">&gt;</span>
    </div>
    <transition :name="mode">
      <div :class="itemClass" v-show="subMenuShow">
        <slot></slot>
      </div>
    </transition>
  </div>
</template>

<script>
import {computed, ref, inject} from "vue";

export default {
  name: "my-sub-menu",
  inject: ['mode'],
  setup() {
    const subMenuShow = ref(false)
    let mode = inject('mode')

    const showSubMenu = () => {
      subMenuShow.value = true
    }

    const hideSubMenu = () => {
      subMenuShow.value = false
    }

    let itemClass = computed(() => {
      let mode = inject('mode')
      if (mode === 'horizontal') return 'horizontal-item'
      else if (mode === 'vertical') return 'vertical-item'
      else if (mode === 'inline') return 'inline-item'
    })

    return {
      mode,
      subMenuShow,
      itemClass,
      showSubMenu,
      hideSubMenu
    }
  }
}
</script>

<style lang="scss" scoped>
.sub-menu {
  position: relative;

  .title {
    position: relative;
    height: 50px;
    line-height: 50px;
    text-align: center;

    .icon {
      position: absolute;
      top: 0;
      right: 15px;
    }
  }

  .horizontal-item {
    position: absolute;
    top: 100%;
    left: 10%;
    background-color: #333;
    width: 100px;

  }

  .vertical-item {
    position: absolute;
    left: 100%;
    top: 0;
    width: 100%;
    background-color: #333;
  }


}

// 子菜单动画

//水平动画
@keyframes extend {
  0% {
    height: 0%;
  }
  100% {
    height: 100%;
  }
}

.horizontal-enter-active{
  animation: extend .2s linear;
}
.horizontal-leave-active{
  animation: extend .2s reverse;
}

//垂直动画
.vertical-enter-active {
  animation: zoom .4s;
}

.vertical-leave-active {
  animation: zoom .4s reverse;
}

@keyframes zoom {
  0% {
    //opacity: 0;
    //transform:scaleX(0.5);
    width: 50%;
  }
  100% {
    //opacity: 1;
    //transform: scaleX(1);
    width: 100%;
  }
}
</style>