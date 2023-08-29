<template>
  <div class="app-container">
    <div style="position: relative;" :style="getFlowPageStyle">
      <online-query-page ref="form" v-if="pageType === this.OnlinePageType.QUERY"
                         :operationType="operationType"
                         :pageId="pageId"
                         :params="params"
                         :key="pageKey"
                         @ready="onPageReady"
      />
      <online-edit-page ref="form" v-if="pageType === this.OnlinePageType.EDIT"
                        :saveOnClose="saveOnClose"
                        :pageId="pageId"
                        :key="pageKey"
                        :params="params"
                        :operationType="operationType"
                        :rowData="rowData"
                        :flowData="flowData"
                        @close="onClose"
                        @ready="onPageReady"
      />
      <online-order-page ref="form" v-if="pageType === this.OnlinePageType.ORDER"
                         :entryId="entryId"
                         :pageId="pageId"
                         :key="pageKey"
                         :isPreview="isPreview"
                         :readOnly="readOnly"
                         @ready="onPageReady"
      />
      <label v-if="closeVisible === '1'" class="page-close-box">
        <el-button type="text" @click="onClose(true)" icon="el-icon-close"/>
      </label>
    </div>
  </div>
</template>

<script>
  import OnlineQueryPage from './form/render/onlineQueryPage'
  import OnlineEditPage from './form/render/onlineEditPage'
  import OnlineOrderPage from './form/render/onlineOrderPage'

  export default {
    props: {
      pageId: {
        type: String,
        required: true
      },
      pageType: {
        type: String,
        required: true
      },
      closeVisible: {
        type: String,
        default: '0'
      },
      saveOnClose: {
        type: String,
        default: '1'
      },
      operationType: {
        type: String,
        default: '01'
      },
      params: {
        type: Object
      },
      isPreview: {
        type: Boolean,
        default: false
      },
      rowData: {
        type: Object
      },
      flowData: {
        type: Object
      },
      readOnly: {
        type: [String, Boolean]
      },
      entryId: {
        type: String
      }
    },
    components: {
      OnlineQueryPage,
      OnlineEditPage,
      OnlineOrderPage
    },
    data() {
      return {
        pageKey: 0,
        pageConfig: {}
      }
    },
    methods: {
      onClose(isSuccess, data) {
        if (this.observer != null) {
          this.observer.cancel(isSuccess, data)
        } else {
          this.$router.go(-1)
        }
      },
      setFormPageData(pageData, setPageData, flowData) {
        if (this.$refs.form && this.$refs.form.setFormPageData) this.$refs.form.setFormPageData(pageData, setPageData, flowData)
      },
      getVariableData(variableList) {
        let funGetVariableData = null
        if (Array.isArray(this.$refs.form) && this.$refs.form.length > 0) {
          funGetVariableData = this.$refs.form[0].getVariableData
        } else {
          if (this.$refs.form != null) funGetVariableData = this.$refs.form.getVariableData
        }
        if (typeof funGetVariableData === 'function') {
          return funGetVariableData(variableList)
        }
      },
      getFormPageData() {
        let funGetFormPageData = null
        if (Array.isArray(this.$refs.form) && this.$refs.form.length > 0) {
          funGetFormPageData = this.$refs.form[0].getFormPageData
        } else {
          if (this.$refs.form != null) funGetFormPageData = this.$refs.form.getFormPageData
        }

        if (typeof funGetFormPageData === 'function') {
          return funGetFormPageData()
        }
      },
      onPageReady() {
        this.$emit('ready')
      }
    },
    computed: {
      getFlowPageStyle() {
        if (this.pageType === this.OnlinePageType.FLOW && this.pageConfig) {
          return {
            width: (this.pageConfig.width != null && this.pageConfig.width !== '') ? this.pageConfig.width + 'px' : '100%'
          }
        } else {
          return undefined
        }
      }
    },
    provide() {
      return {
        preview: () => this.isPreview
      }
    },
    watch: {
      pageId: {
        handler() {
          this.pageKey++
        }
      }
    }
  }
</script>

<style lang='scss'>
  .input-item {
    width: 100% !important;
  }
</style>
