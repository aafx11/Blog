<template>
  <button :class="['base',type,size,
  {
    'is-disabled': disabled,
    'auto-insert-space':letterSpacing
  }]" :disabled="disabled">
    <slot></slot>
  </button>
</template>

<script>
import {onMounted, toRefs, watch} from "vue";

export default {
  name: "myButton",
  props: {
    base: {
      type: String,
      default: 'base'
    },
    type: {
      type: String,
      default: 'default'
    },
    size: {
      type: String,
      default: 'small'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    letterSpacing: {
      type: Boolean,
      default: false
    }
  },
  setup(props) {
    let {type, size, disabled} = toRefs(props)

    return {
      type, size, disabled
    }
  }
}
</script>

<style scoped>
/*禁用状态*/
.is-disabled {
  cursor: not-allowed !important;
  opacity: 0.4;
}

/*基础样式*/
.base {
  font-size: 14px;
  line-height: 14px;
  text-shadow: 0 -1px 0 rgba(0, 0, 0, .12);
  box-shadow: 0 2px 0 rgba(0, 0, 0, .045);
  transition: all .3s cubic-bezier(.645, .045, .355, 1);
  border-radius: 3px;
  outline: none;
  cursor: pointer;
}

/*颜色控制*/
.default {
  color: black;
  background: rgb(255, 255, 255);
  /*border: 1px solid #d9d9d9;*/
  border: 1px solid rgb(220, 223, 230);

}

.default:hover {
  background: rgb(236, 245, 255);
  border-color: rgb(220, 223, 230);
}

.primary {
  color: #fff;
  /*background: rgba(0,82,217,var(--value));*/
  background: #0052d9;
  border: 1px solid #0052d9;

}

.primary:hover {
  background: #2575e6;
  border-color: #2575e6;
}

.danger {
  color: #fff;
  background: #ff4d4f;
  border: 1px solid #ff4d4f;
}

.danger:hover {
  background: #f6696b;
  border-color: #f85e60;
}

.warning {
  color: #fff;
  background: rgb(237, 123, 47);
  border: 1px solid rgb(237, 123, 47);

}

.success {
  color: #fff;
  background: rgb(0, 168, 112);
  border: 1px solid rgb(0, 168, 112);
}

/*尺寸控制*/
.small {
  height: 32px;
  padding: 4px 15px;
}

.mini {
  height: 26px;
  padding: 1px 7px;

}

/*是否有字间距*/
.auto-insert-space {
  letter-spacing: 3px;

}
</style>