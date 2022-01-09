<template>
  <div class="img-wrapper" :style="img_wrapper_style">
    <a
        class="mag-wrapper"
        :href="link"
        :target="blank ? '_blank':''"
        :style="mag_wrapper_style"
    >
      <img class="mag-img" :src="imgUrl" :alt="imgAlt" :style="mag_img_style">
    </a>
    <a class="img-link" :href="link">
      <img class="static-img" :src="imgUrl" :alt="imgAlt">
    </a>
  </div>
</template>

<script>
import {computed} from "vue";

export default {
  name: "my-magnifier",
  props: {
    imgWidth: {
      type: Number,
      // request: true
      default: 375
    },
    imgHeight: {
      type: Number,
      // request: true
      default:500
    },
    magWidth: {
      type: Number,
      // request: true
      default: 150
    },
    magHeight: {
      type: Number,
      // request: true
      default: 150
    },
    link: {
      type: String,
      // request: true
    },
    imgUrl: {
      type:String,
      // request: true
    },
    blank: {
      type:Boolean,
      default:false
    },
    imgAlt:{
      type:String,
    }
  },
  setup(props, ctx) {

    // 图片样式
    let img_wrapper_style = computed(() => {
      return {
        width: props.imgWidth + 'px',
        height: props.imgHeight + 'px'
      }
    })

    // 放大镜样式
    let mag_wrapper_style = computed(() => {
      return {
        width: props.magWidth + 'px',
        height: props.magHeight + 'px'
      }
    })

    // 放大后的图片样式
    let mag_img_style = computed(()=>{
      return {
        width: props.imgWidth + 'px',
        height: props.imgHeight + 'px'
      }
    })

    return {
      img_wrapper_style,
      mag_wrapper_style,
      mag_img_style
    }
  }
}
</script>

<style scoped>

</style>