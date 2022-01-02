<template>
  <div class="selector-input">
<!--    <label class="placeholder">{{ placeholder }}</label>-->
    <input
        type="text"
        class="input"
        :value="value"
        ref="inputValue"
        @input="searchOptions($event)"
        @focus="searchOptions($event)"
        :placeholder="placeholder"
    >
<!--    @blur="setValue(value)"-->

    <span class="iconfont icon-icon_arrow_down"></span>
  </div>
</template>

<script>

import {getCurrentInstance} from 'vue'

export default {
  name: "SelectorInput",
  props: {
    placeholder: {
      type: String,
      default: '请选择'
    },
    value: String

  },
  setup(props, ctx) {

    const instance = getCurrentInstance()

    //@input监听输入
    const searchOptions = (e) => {

      ctx.emit('searchOptions', e.target.value)
    }

    const setValue = (value) => {
      const _input = instance.refs.inputValue;

      if (_input.value.length>0){
        _input.value = value
      }
    }

    return {
      searchOptions,
      setValue
    }
  }
}
</script>

<style lang="scss" scoped>
.selector-input {
  position: relative;
  width: 100%;
  height: 38px;

  .input {
    width: 100%;
    height: 100%;
    padding: 0 15px;
    box-sizing: border-box; //改变盒模型，width=content+padding+border
    background-color: #fff;
    border: 1px solid #999;
    border-radius: 5px;
    outline: none;
    transition: all .2s linear;
    z-index: 100;

    //&是父节点占位符
    &:focus {
      border-color: #1890ff;
      box-shadow: 0 0 3px #1890ff;
    }
  }

  .placeholder, .iconfont {
    position: absolute;
  }

  .placeholder {
    left: 15px;
    top: 8px;
    color: #999;
  }

  .iconfont {
    right: 15px;
    top: 8px;
    color: #999;

    &.icon-search {
      font-size: 22px;
      top: 8px;
    }
  }
}
</style>