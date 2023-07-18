<template>
  <div :class="{'hidden':hidden}" class="pagination-container">
    <el-pagination
      :background="background"
      :current-page.sync="initCurrentPage"
      :page-size.sync="initPageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="totalPage"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
  import { scrollTo } from '@/components/AgileUtil/scroll-to'

  export default {
    name: 'Pagination',
    props: {
      totalPage: {
        required: true,
        type: Number
      },
      currentPage: {
        type: Number,
        default: 1
      },
      pageSize: {
        type: Number,
        default: 10
      },
      pageSizes: {
        type: Array,
        default() {
          return [5, 10, 20, 30, 50]
        }
      },
      layout: {
        type: String,
        default: 'total, sizes, prev, pager, next, jumper'
      },
      background: {
        type: Boolean,
        default: true
      },
      autoScroll: {
        type: Boolean,
        default: true
      },
      hidden: {
        type: Boolean,
        default: false
      }
    },
    computed: {
      initCurrentPage: {
        get() {
          return this.currentPage
        },
        set(val) {
          this.$emit('update:currentPage', val)
        }
      },
      initPageSize: {
        get() {
          return this.pageSize
        },
        set(val) {
          this.$emit('update:limit', val)
        }
      }
    },
    methods: {
      handleSizeChange(val) {
        this.$emit('pagination', { page: this.currentPage, limit: val })
        if (this.autoScroll) {
          scrollTo(0, 800)
        }
      },
      handleCurrentChange(val) {
        this.$emit('pagination', { page: val, limit: this.pageSize })
        if (this.autoScroll) {
          scrollTo(0, 800)
        }
      }
    }
  }
</script>

<style scoped>
  .pagination-container {
    background: #fff;
    padding: 32px 16px;
  }

  .pagination-container.hidden {
    display: none;
  }
</style>
