<template>
  <div class="selector" v-focus="placeholder">
    <selector-input
        :placeholder="placeholder"
        :value="inputValue"
        @searchOptions="searchOptions"
    >
    </selector-input>
    <selector-menu
        :data="data"
        @setItemValue="setItemValue"
        :searchValue="searchValue"
    >

    </selector-menu>
  </div>
</template>

<script>

import SelectorInput from './SelectorInput.vue'
import SelectorMenu from './SelectorMenu.vue'
import focus from './directives/focus.js'
import {reactive, toRefs} from "vue";


/*
* name:带搜索的下拉菜单
* data:数组对象,
* {
*   id:1,
*   value:'vue',
*   text:'Vue' //显示的文本
* }
* @setItemValue:获取菜单中选中的值
* @searchOptions:输入监听
* */
export default {
  name: "my-selector",
  directives: {
    focus
  },
  components: {
    SelectorInput,
    SelectorMenu
  },
  props: {
    placeholder: String,
    data: Array,
    inputValue:String
  },
  setup(props, ctx) {

    const state = reactive({
      inputValue: '',
      searchValue: ''
    })

    // 传出在菜单中选中的值
    const setItemValue = (item) => {
      state.inputValue = item.text;
      ctx.emit('setItemValue', item)
    }

    const searchOptions = (value) => {
      state.searchValue = value
      ctx.emit('searchOptions',value)
    }

    return {
      ...toRefs(state),
      setItemValue,
      searchOptions
    }
  }
}
</script>

<style lang="scss" scoped>
.selector {
  position: relative;
  width: 300px;
  max-width: 100%;
}
</style>