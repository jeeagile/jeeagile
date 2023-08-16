<template>
  <el-col :span="widgetConfig.span || 24">
    <img :src="getImageSrc" :height="widgetConfig.height" :width="widgetConfig.width"
         style="max-width: 100%;" :style="getImageStyle"
    />
  </el-col>
</template>

<script>
  export default {
    name: 'CustomImageWidget',
    props: {
      widgetConfig: {
        type: Object,
        required: true
      }
    },
    computed: {
      getImageStyle() {
        return {
          'object-fit': this.widgetConfig.fit
        }
      },
      getImageSrc() {
        return this.widgetConfig.src?.length > 0 ? this.widgetConfig.src : require('@/assets/images/haiyang.jpg')
      }
    }
  }
</script>
