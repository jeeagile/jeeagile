<template>
  <TreeSelect v-model="form.parentId" :options="deptTreeOptionList" :normalizer="normalizer" placeholder="选择上级部门"/>
</template>

<script>
  import TreeSelect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'TreeSelect',
    components: { TreeSelect },
    props: {
      treeOption: {
        type: Array
      },
      dataOption: {
        type: Array
      }
    }
  }
</script>
