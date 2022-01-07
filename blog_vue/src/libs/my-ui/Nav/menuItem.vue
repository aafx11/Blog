<template>
  <div class="menu-item" v-if="!path">
    <slot></slot>
  </div>
  <template v-else>
    <router-link
        :to="path"
        custom
        v-slot="{ href, route, navigate, isActive, isExactActive }"

    >
      <div class="menu-item" :class="[isActive && 'router-link-active']">
        <a :href="href" @click="navigate" class="link">
          <slot></slot>
        </a>
      </div>
    </router-link>
  </template>
</template>

<script>
import {getCurrentInstance, toRefs,inject} from "vue";

export default {
  name: "my-menu-item",
  inject: ['mode'],
  props: {
    path:Object,
  },
  setup(props, ctx) {
    let {path} = toRefs(props)

    console.log(inject('mode'));

    return {

      path
    }
  },
  created() {
    // console.log(this.mode);
  }
}
</script>

<style lang="scss" scoped>
.menu-item {
  width: 100%;
  height: 50px;
  color: #fff;
  text-align: center;
  line-height: 50px;
  padding: 0 10px;

  a {
    text-decoration: none;
    color: #fff;
  }

  .link{
    display: block;
  }
}

.router-link-active {
  background-color: pink ;
  color: pink;
}
</style>