<template>
  <div class="no-data-tip">
    - 没有搜索到数据 -
  </div>
</template>

<script>
export default {
  name: "NoDataTip"
}
</script>

<style lang="scss" scoped>
.no-data-tip{
  height: 200px;
  line-height: 200px;
  font-size: 16px;
  text-align: center;
  color: #666;
}
</style>