<template>
  <div
      class="menu-item"
      :class="[
          themeClass,
          'whole'
      ]"
      v-if="!path"
      v-switch-style
  >
    <span
        :class="[
        'base',
         widthClass,

      ]"
    >
      <slot></slot>
    </span>
  </div>
  <template v-else>
    <router-link
        :to="path"
        custom
        v-slot="{ href, route, navigate, isActive, isExactActive }"

    >
      <div class="menu-item"
           :class="[
              {
              'light-horizontal':isActive && mode==='horizontal' && theme ==='light',
              'light-vertical':isActive && (mode === 'vertical' || mode=== 'inline') && theme==='light',
              'dark-horizontal':isActive && mode==='horizontal' && theme === 'dark',
              'dark-vertical':isActive &&  (mode === 'vertical' || mode=== 'inline') && theme==='dark'
              },
              themeClass,
              widthClass,
              'whole'
              ]">
        <a :href="href" @click="navigate" class="link base">
          <slot></slot>
        </a>
      </div>
    </router-link>
  </template>
</template>

<script>
import {getCurrentInstance, toRefs, inject, computed, watch} from "vue";
import switchStyle from './switchStyle.js'
import {useRoute} from "vue-router";

export default {
  name: "my-menu-item",
  inject: ['mode', 'theme'],
  directives: {
    switchStyle
  },
  props: {
    path: Object,
  },
  setup(props, ctx) {
    let {path} = toRefs(props)
    let route = useRoute()
    let theme = inject('theme')
    let mode = inject('mode')

    let themeClass = computed(() => {
      if (theme == 'light') return 'light'
      if (theme == 'dark') return 'dark'
    })


    let widthClass = computed(() => {
      if (mode === 'vertical' || mode === 'inline') return 'max-width'
      if (mode === 'horizontal') return 'min-width'
    })


    return {
      themeClass,
      widthClass,
      theme,
      path,
      mode,
    }
  },
  created() {
    // console.log(this.mode);
  }
}
</script>

<style lang="scss" scoped>

// 基础样式
.whole{
  cursor: pointer;

}
.base {
  overflow:hidden;

  text-overflow:ellipsis;

  white-space:nowrap;
}

//水平，垂直，行内模式的样式
.min-width {
  //min-width: 150px!important;
  min-height: 45px;
  padding: 0 20px;
}

.max-width {
  width: 100%
}


.menu-item {
  //min-width: 150px!important;
  //min-width: 100%;
  min-width: 150px;
  height: 50px;
  //color: #fff;
  text-align: center;
  line-height: 50px;
  //display: flex;
  //justify-content: flex-start;
  //align-items: center;
  padding: 0 20px;
  overflow:hidden;

  text-overflow:ellipsis;

  white-space:nowrap;

  .link {
    display: block;
  }
}


// 白亮和黑夜皮肤
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


.light-horizontal {
  background-color: rgb(230, 247, 255);
  border-bottom: 3px solid rgb(24, 144, 255);
}

.light-vertical {
  background-color: rgb(230, 247, 255);
  border-right: 3px solid rgb(24, 144, 255);
}

.dark-horizontal {
  background-color: rgb(67, 74, 80);
  //border-bottom: 5px solid rgb(77, 124, 149);
  border-bottom: 3px solid rgb(255, 208, 75);

  a {
    color: rgb(255, 208, 75);
  }
}

.dark-vertical {
  background-color: rgb(67, 74, 80);
  border-right: 5px solid rgb(77, 124, 149);
}
</style>