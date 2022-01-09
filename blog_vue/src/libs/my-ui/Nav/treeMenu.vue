<template>

  <div
      class="tree-menu"
      :class="[
        mode,
        navClass,
        borderClass
      ]"
      :style="navStyle"
  >
    <slot></slot>
  </div>
</template>

<script>
import {computed, toRefs} from "vue";

/*
* mode:vertical horizontal inline,垂直，水平，行内,inline效果不好
* theme:dark light
* width:100%或者number + px
* backgroundColor:背景颜色，效果不好
*
* my-menu-item组件:
* :path,接收一个路由对象{name:'test',query:{id:1}}
*
* my-sub-menu:多级菜单 :index="1" ,标明是第一层
* template #title 标题
* template #content 子菜单，可以填my-menu-item
*
* my-item-menu 配合 my-reSub-menu 无限级菜单
*
* <template v-for="(item,index) in menu">
*    <my-menu-item v-if="!item.children" :key="item.id" :path="item.path">
*       {{ item.title }}-->
*    </my-menu-item>-->
*    <my-reSub-menu :data="item" :index="index" v-else></my-reSub-menu>
*</template>
* :index="index" 标明层数 :data为子菜单
* menu:[
      {
        id: 1,
        title: '菜单1',
        path: {name: 'test1'},
        children: [
          {
            id: 11,
            title: '菜单11',
            path: {name: 'test2'},
          }
        ]
      },
      {
        id: 2,
        title: '菜单2',
        path: {name: 'test2'},
      }
    ]
* */


export default {
  name: "my-menu",
  provide() {
    return {
      theme: this.theme,
      mode: this.mode,

    }
  },
  props: {
    mode: {
      type: String, //vertical horizontal inline
      default: 'horizontal'
    },
    theme: {
      type: String,
      default: 'light' //light dark
    },
    backgroundColor: {
      type: String,
      default: ''
    },
    textColor: {
      type: String,
      default: '#000'
    },
    width: {
      type: String,
      default: '100%'
    }

  },
  setup(props, ctx) {
    let {mode, theme, backgroundColor, textColor, width} = toRefs(props)

    let navClass = computed(() => {
      if (theme.value === 'light') return 'light'
      if (theme.value === 'dark') return 'dark'
    })

    let navStyle = computed(() => {
      return {
        background: backgroundColor.value,
        color: textColor.value,
        width: width.value === '100%' ? '100%' : width.value + 'px'

      }
    })

    let borderClass = computed(()=>{
      if (mode.value==='horizontal' && theme.value === 'light') return 'bottom-light'
      if (mode.value==='vertical' && theme.value === 'light') return 'right-light'

    })
    return {
      ...toRefs(props),
      navClass,
      navStyle,
      borderClass,
    }
  }
}
</script>

<style lang="scss" scoped>
// 基础样式


// 水平，垂直，行内的样式
.horizontal {
  width: 100%;
  display: flex;

}

.vertical {
  min-width: 255px;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.inline {
  min-width: 255px;
  display: flex;
  flex-direction: column;
  height: 100%;
}

// 白亮和暗黑皮肤
.light {
  background-color: rgb(255, 255, 255);
}
.dark {
  background-color: rgb(84, 92, 100);
  //background-color: #001529;
}

// 边框
.bottom-light{
  border-bottom: 1px solid #f0f0f0;
}

.right-light{
  border-right:1px solid #f0f0f0;

}
</style>