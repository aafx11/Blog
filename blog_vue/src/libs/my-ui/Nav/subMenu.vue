<template>
  <div class="sub-menu"
       :class="themeClass"
       @mouseenter="showSubMenu"
       @mouseleave="hideSubMenu"

  >
    <div class="title" @click="clickSubMenu">
      <slot name="title"></slot>
      <span class="icon" v-if="mode === 'vertical' || mode === 'inline' ">&gt;</span>
      <span class="icon" v-else>∨</span>

    </div>
    <transition :name="mode">
      <div :class="itemClass" class="test" v-show="subMenuShow">
        <slot name="content"></slot>
      </div>
    </transition>
  </div>
</template>

<script>
import {computed, ref, inject, watch} from "vue";

export default {
  name: "my-sub-menu",
  inject: ['mode', 'theme'],
  props: {
    index: {
      type:Number,
      // default:1
    }

  },
  setup(props, ctx) {
    let {index} = props
    let mode = inject('mode')
    const subMenuShow = ref(false)

    const showSubMenu = () => {
      if (mode === 'inline') return
      subMenuShow.value = true
    }

    const hideSubMenu = () => {
      if (mode === 'inline') return
      subMenuShow.value = false
    }

    const clickSubMenu = () => {
      subMenuShow.value = !subMenuShow.value
    }


    let itemClass = computed(() => {
      let mode = inject('mode')
      let theme = inject('theme')
      if (mode === 'horizontal' && index == 1) return 'horizontal'
      if (mode === 'horizontal' && index != 1) return 'horizontal-item'
      else if (mode === 'vertical') return 'vertical-item'
      else if (mode === 'inline' && theme === 'light') return 'inline-light'
      else if (mode === 'inline' && theme === 'dark') return 'inline-dark'

      // else if (mode === 'inline' && theme === 'light') return 'inline-light'
      // else if (mode === 'inline' &&  theme === 'dark') return 'inline-dark'

    })

    let themeClass = computed(() => {
      let theme = inject('theme')
      if (theme === 'light') return 'light'
      if (theme === 'dark') return 'dark'
    })
    return {
      mode,
      subMenuShow,
      itemClass,
      themeClass,
      showSubMenu,
      hideSubMenu,
      clickSubMenu,
    }
  }
}
</script>

<style lang="scss" scoped>
.sub-menu {
  position: relative;
  min-width: 150px;

  .title {
    box-sizing: content-box;
    position: relative;
    min-width: 100%;
    //padding: 0 20px;
    height: 50px;
    line-height: 50px;
    text-align: center;
    //display: flex;
    //justify-content: flex-start;
    //align-items: center;

    .icon {
      min-width: 10px;
      margin-left: 10px;
    }
  }

  .horizontal {
    position: absolute;
    top: 100%;
    left: 0%;
    background-color: #333;
    width: 100px;
  }

  .horizontal-item {
    position: absolute;
    top: 0%;
    left: 100%;
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


  .inline-light {
    display: block;
  }

  .inline-dark {
    display: block;
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

.horizontal-enter-active {
  animation: extend .2s linear;
}

.horizontal-leave-active {
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

// 白亮和暗黑皮肤
.light {
  background-color: rgb(255, 255, 255);
  color: rgb(48, 49, 51);

  a {
    text-decoration: none;
    color: rgb(48, 49, 51);
  }
}

.light:hover {
  background-color: rgb(230, 247, 255);
}


.dark {
  background-color: rgb(84, 92, 100);
  color: #fff;

  a {
    text-decoration: none;
    color: #fff;
  }
}

.dark:hover {
  background-color: rgb(67, 74, 80);
}


</style>