<template>
  <div class="app-container" style="position: relative;">
    <el-form ref="form" :model="formData" class="full-width-input" :rules="rules"
             label-width="100px" size="mini" label-position="right" @submit.native.prevent>
      <el-row>
        <el-col :span="24">
          <el-form-item label="参数名称">
            <el-input class="input-item" v-model="formData.dictParamName" readonly/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="参数值类型" prop="paramValueType">
            <el-select class="input-item" v-model="formData.paramValueType"
                       :clearable="true" placeholder="参数值类型" @change="changeParamValueType">
              <el-option v-for="item in OnlineParamValueType.getList()" :key="item.value"
                         :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.FORM_PARAM">
          <el-form-item label="参数值" prop="dictValue">
            <el-select class="input-item" v-model="formData.dictValue" key="FORM_PARAM"
                       :clearable="true" placeholder="参数值">
              <el-option v-for="item in pageParamList" :key="item.fieldName"
                         :label="item.fieldName" :value="item.fieldName"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.TABLE_COLUMN">
          <el-form-item label="参数值" prop="dictValue">
            <el-select class="input-item" v-model="formData.dictValue" key="TABLE_COLUMN"
                       :clearable="true" placeholder="参数值">
              <el-option v-for="item in columnList"
                         :key="item.columnId"
                         :label="`${item.columnComment}|${item.columnName}`"
                         :value="item.columnId"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.STATIC_DICT">
          <el-form-item label="参数值" prop="dictValue">
            <el-cascader v-model="formData.dictValue" :options="staticData" :props="staticPops"/>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.INPUT_VALUE">
          <el-form-item label="参数值" prop="dictValue">
            <el-input v-model="formData.dictValue" placeholder="请输入参数值" clearable/>
          </el-form-item>
        </el-col>
        <el-col :span="24" style="margin-top: 15px;">
          <el-row class="no-scroll flex-box" type="flex" justify="end">
            <el-button type="primary" size="mini" :plain="true" @click="onCancel(false)">
              取消
            </el-button>
            <el-button type="primary" size="mini" @click="onSubmit()">
              确定
            </el-button>
          </el-row>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import * as SystemStaticDict from '@/components/AgileDict'

  export default {
    name: 'EditWidgetDictParam',
    props: {
      rowData: {
        type: Object,
        required: true
      },
      pageParamList: {
        type: Array,
        required: true
      },
      columnList: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        formData: {
          dictParamName: undefined,
          paramValueType: undefined,
          dictValue: undefined
        },
        staticPops: {
          label: 'label',
          value: 'value'
        },
        staticData: [],
        rules: {
          paramValueType: [
            { required: true, message: '请选择参数值类型', trigger: 'blur' }
          ],
          dictValue: [
            { required: true, message: '请选择参数值', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      onCancel(isSuccess) {
        if (this.observer != null) {
          this.observer.cancel(isSuccess, this.formData)
        }
      },
      onSubmit() {
        this.$refs.form.validate(valid => {
          if (!valid) return
          if (this.formData.paramValueType === this.OnlineParamValueType.STATIC_DICT && this.formData.dictValue.length !== 2) {
            this.$message.error('静态字典类型的参数值必须选择静态字典的值！')
            return
          }
          this.onCancel(true)
        })
      },
      changeParamValueType(value) {
        this.formData.dictValue = undefined
        this.$refs.form.clearValidate()
      },
      loadStaticData() {
        this.staticData = Object.keys(SystemStaticDict).map(key => {
          let dictItem = SystemStaticDict[key]
          return {
            value: key,
            label: dictItem.dictName,
            children: dictItem.getList()
          }
        }).filter(item => item != null)
      }
    },
    mounted() {
      this.formData = {
        ...this.formData,
        ...this.rowData
      }
      this.loadStaticData()
    }
  }
</script>

<style>
</style>
