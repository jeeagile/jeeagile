<template>
  <div>
    <el-row>
      <draggable class="flex-box" draggable=".draggable-item" group="componentsGroup">
        <slot/>
      </draggable>
    </el-row>

    <el-row type="flex">
      <el-col :span="12" align="left"><slot name="operator"/></el-col>
      <el-col :span="12" align="right"><slot name="search"/></el-col>
    </el-row>

  </div>
</template>

<script>
  import Draggable from 'vuedraggable'

  export default {
    name: 'DragWidgetFilter',
    components: {
      Draggable
    }
  }
</script>
