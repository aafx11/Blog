<template>

  <div
      class="tree-menu"
      :class="[
        mode,
        {

        }
      ]"
      :style="navStyle"
  >
    <slot></slot>
  </div>
</template>

<script>
import {computed, toRefs} from "vue";

export default {
  name: "my-menu",
  provide(){
    return{
      mode: this.mode
    }
  },
  props: {
    mode: {
      type: String, //vertical horizontal inline
      default: 'horizontal'
    },
    backgroundColor: {
      type: String,
      default: '#fff'
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
    let {mode, backgroundColor, textColor, width} = toRefs(props)

    let navStyle = computed(() => {
      return {
        background: backgroundColor.value,
        color: textColor.value,
        width: width.value === '100%' ? '100%' : width.value + 'px'

      }
    })

    return {
      ...toRefs(props),
      navStyle
    }
  }
}
</script>

<style lang="scss" scoped>

.tree-menu {
  width: 255px;
  max-width: 100%;
}

.horizontal {
  width: 100%;
  display: flex;

}

.vertical {
  min-width: 255px;
  max-width: 100%;
  display: flex;
  flex-direction: column;
}
</style>