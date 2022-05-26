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
      processId: `kkkk`,
      processName: `流程kkkkk`,
      processPrefix: 'activiti',
      processDesc: '',
      processXml: null
    }
  }
}
</script>
