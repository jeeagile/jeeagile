<template>
  <div>
    <el-popover v-model="cronPopover">
      <cron v-model="cronValue" @change="changeCron" @close="cronPopover=false"></cron>
      <el-input slot="reference" @click="cronPopover=true" v-model="cronValue" placeholder="请输入定时策略"></el-input>
    </el-popover>
  </div>
</template>

<script>
  import Cron from './index.vue'

  export default {
    name: 'CronInput',
    model: {
      prop: 'value',
      event: 'update'
    },
    props: {
      value: {
        type: String,
        default: '* * * * * ? *'
      }
    },
    components: {
      Cron
    },
    data() {
      return {
        cronPopover: false,
        cronValue: this.value
      }
    },
    methods: {
      changeCron(val) {
        this.cronValue = val
        this.$emit('update', this.cronValue)
      },
      setCron(newValue) {
        if (!newValue || newValue.trim().length < 11) {
          this.$message.error('格式错误')
          return
        }
        this.cronValue = newValue
      }
    },
    watch: {
      value(val) {
        this.setCron(val)
      }
    },
    created() {
      this.setCron(this.value)
      this.$emit('update', this.cronValue)
    }
  }
</script>
