<template>
  <div>
    <process-designer
      v-model="processXml"
      :process-id="processId"
      :process-name="processName"
      :process-desc="processDesc"
      :process-xml="processXml"
      :process-prefix="processPrefix"
    />
  </div>
</template>
<script>
export default {
  name: 'Process',
  data() {
    return {
      processId: `process_${new Date().getTime()}`,
      processName: `流程_${new Date().getTime()}`,
      processPrefix: 'activiti',
      processDesc: '',
      processXml: null
    }
  }
}
</script>
