<template>
  <div class="param-container">
    <el-card style="margin-bottom: 5px">
      <div slot="header" style="justify-content:center;">
        <span style="line-height: 38px">{{ droolsModel.modelLabel }}</span>
        <slot name="button"></slot>
      </div>
      <el-form :ref="droolsModel.modelName" :model="formData" label-width="80px">
        <el-row>
          <template
            v-for="field in droolsModel.droolsModelFieldList.filter(field=>field.inputFlag===AgileYesNo.YES && field.fieldType!=DroolsFieldType.Object)">
            <template v-if="field.listFlag===AgileYesNo.NO">
              <el-col :span="12">
                <el-form-item :label="field.fieldLabel" :prop="field.fieldName">
                  <input-filed :model-field="field" v-model="modelData[field.fieldName]"></input-filed>
                </el-form-item>
              </el-col>
            </template>
            <template v-if="field.listFlag===AgileYesNo.YES">
              <el-col :span="12">
                <el-form-item :label="field.fieldLabel" :prop="field.fieldName">
                  <div v-for="(inputItem ,index) in modelData[field.fieldName]" :key="index">
                    <input-filed :model-field="field" v-model="modelData[field.fieldName][index]">
                      <template v-if="index===modelData[field.fieldName].length-1">
                        <el-button slot="button" icon="el-icon-plus" @click="addInput(field)"></el-button>
                      </template>
                      <template v-else>
                        <el-button slot="button" icon="el-icon-minus" @click="deleteInput(field,index)"></el-button>
                      </template>
                    </input-filed>
                  </div>
                </el-form-item>
              </el-col>
            </template>
          </template>
        </el-row>
        <el-row>
          <template
            v-for="field in droolsModel.droolsModelFieldList.filter(field=>field.inputFlag===AgileYesNo.YES && field.fieldType===DroolsFieldType.Object)">
            <template v-if="field.listFlag===AgileYesNo.NO">
              <param-data :drools-model="field.droolsModelInfo" :key="field.id"
                          :model-data="modelData[field.fieldName]"/>
            </template>
            <template v-if="field.listFlag===AgileYesNo.YES">
              <div v-for="(inputItem ,index) in modelData[field.fieldName]" :key="index">
                <param-data :drools-model="field.droolsModelInfo" :key="field.id + index"
                            :model-data="modelData[field.fieldName][index]">
                  <template v-if="index===modelData[field.fieldName].length-1">
                    <el-button style="float: right;background-color:#1890ff;padding: 10px" slot="button"
                               icon="el-icon-plus"
                               @click="addObject(field)"></el-button>
                  </template>
                  <template v-else>
                    <el-button style="float: right;background-color:#1890ff;padding: 10px" slot="button"
                               icon="el-icon-minus"
                               @click="deleteObject(field,index)"></el-button>
                  </template>
                </param-data>
              </div>
            </template>
          </template>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>

  import ParamData from './index'
  import InputFiled from './input'

  export default {
    name: 'ParamData',
    components: { ParamData, InputFiled },
    props: {
      droolsModel: {
        type: Object,
        required: true
      },
      modelData: {
        type: Object,
        required: true
      }
    },
    watch: {
      modelData: {
        handler() {
          this.$emit('update:modelData', this.modelData)
        },
        deep: true,
        immediate: true
      }
    },
    data() {
      return { formData: this.modelData }
    },
    methods: {
      addInput(item) {
        this.modelData[item.fieldName].push(item.fieldType === this.DroolsFieldType.Boolean ? false : undefined)
      },
      deleteInput(item, index) {
        this.modelData[item.fieldName].splice(index, 1)
      },
      addObject(item) {
        this.modelData[item.fieldName].push({})
      },
      deleteObject(item, index) {
        this.modelData[item.fieldName].splice(index, 1)
      }
    }
  }
</script>
<style lang='scss'>
  .param-container {
    .el-input-group__append {
      background-color: #1890ff;
    }

    .el-icon-plus {
      color: #FFFFFF;
      font-size: 16px;
    }

    .el-icon-minus {
      color: #FFFFFF;
      font-size: 16px;
    }

    .el-card__body {
      padding: 5px;
    }

    .el-card__header {
      padding: 0 0 0 20px;
    }
  }
</style>
