<template>
  <div class="app-container">
    我的发起
  </div>
</template>

<script>
  export default {
    name: 'Apply',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {}
        }
      }
    }
  }
</script>
