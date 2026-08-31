<template>
  <div>
    <el-input v-if="modelField.fieldType === DroolsFieldType.String" clearable
              :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input>
    <el-input-number v-if="modelField.fieldType === DroolsFieldType.int" controls-position="right" precision="0"
                     :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input-number>

    <el-input-number v-if="modelField.fieldType === DroolsFieldType.Integer" controls-position="right" precision="0"
                     :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input-number>

    <el-input-number v-if="modelField.fieldType === DroolsFieldType.BigDecimal"
                     controls-position="right" :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input-number>

    <el-input-number v-if="modelField.fieldType === DroolsFieldType.Double" controls-position="right"
                     :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input-number>

    <el-input-number v-if="modelField.fieldType === DroolsFieldType.Float" controls-position="right"
                     :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-input-number>

    <el-date-picker v-if="modelField.fieldType === DroolsFieldType.Date" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                     :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-date-picker>
    <el-date-picker v-if="modelField.fieldType === DroolsFieldType.LocalDate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                    :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-date-picker>
    <el-date-picker v-if="modelField.fieldType === DroolsFieldType.LocalDateTime" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd HH:mm:ss"
                    :value="value" @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-date-picker>

    <el-switch :value="value" v-if="modelField.fieldType === DroolsFieldType.Boolean"  @input="handlerInput">
      <slot name="button" slot="append"/>
    </el-switch>
  </div>
</template>

<script>

export default {
  name: 'Input',
  props: {
    modelField: {
      type: Object,
      required: true
    },
    value: {
      type: [String, Number, Date, Object, Array, Boolean]
    }
  },
  data() {
    return {}
  },
  methods: {
    handlerInput(value) {
      this.$emit('input', value)
    }
  }
}
</script>
<style lang='scss'>
</style>
