<template>
  <div class="app-container" style="position: relative;">
    <el-form ref="form" :model="formData" class="full-width-input" :rules="rules" style="width: 100%;"
             label-width="120px" size="mini" label-position="right" @submit.native.prevent>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="过滤数据表" prop="tableId">
            <el-select class="input-item" v-model="formData.tableId"
                       :clearable="true" placeholder="字段数据表"
                       @change="changeTable">
              <el-option v-for="(table, index) in tableList" :key="table.tableId"
                         :label="table.tableName" :value="table.tableId">
                <el-row type="flex" justify="space-between" align="middle">
                  <span>{{table.tableName}}</span>
                  <el-tag
                    :type="table.tableType == OnlineTableType.MASTER ? 'success' : 'parimary'"
                    style="margin-left: 30px;" size="mini" effect="dark">
                    {{(table.tableType == OnlineTableType.MASTER || index === 0) ? '数据主表' : '一对一从表'}}
                  </el-tag>
                </el-row>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="过滤数据表字段" prop="columnId">
            <el-select class="input-item" v-model="formData.columnId"
                       :clearable="true" placeholder="字段数据表" @change="changeColumn">
              <el-option v-for="column in getTableColumnList" :key="column.columnId"
                         :label="column.columnName" :value="column.columnId"/>
            </el-select>
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
          <el-form-item label="参数值" prop="paramValue">
            <el-select class="input-item" v-model="formData.paramValue" key="FORM_PARAM"
                       :clearable="true" placeholder="参数值">
              <el-option v-for="item in pageParamList" :key="item.columnName"
                         :label="item.columnName" :value="item.columnName"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.TABLE_COLUMN">
          <el-form-item label="参数值" prop="paramValue">
            <el-select class="input-item" v-model="formData.paramValue" key="TABLE_COLUMN"
                       :clearable="true" placeholder="参数值">
              <el-option-group v-for="table in tableFilterColumnList" :key="table.tableId" :label="table.tableName">
                <el-option v-for="item in table.columnList" :key="item.columnId"
                           :label="item.columnName" :value="item.columnId"/>
              </el-option-group>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.STATIC_DICT">
          <el-form-item label="参数值" prop="paramValue">
            <el-cascader v-model="formData.paramValue" :options="staticData" :props="staticPops"/>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="formData.paramValueType === OnlineParamValueType.INPUT_VALUE">
          <el-form-item label="参数值" prop="paramValue">
            <el-input v-model="formData.paramValue" placeholder="请输入参数值" clearable/>
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
  import { findItemFromList } from '../util'
  import * as SystemStaticDict from '@/components/AgileDict/system'


  export default {
    name: 'EditWidgetTableQueryParam',
    props: {
      // 表格数据表
      tableList: {
        type: Array,
        required: true
      },
      // 已经使用的过滤字段
      usedColumnList: {
        type: Array,
        required: true
      },
      // 表单参数列表
      pageParamList: {
        type: Array,
        required: true
      },
      // 表单过滤字段列表
      tableFilterColumnList: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        formData: {
          tableId: undefined,
          columnId: undefined,
          paramValueType: undefined,
          paramValue: undefined
        },
        staticPops: {
          label: 'label',
          value: 'value'
        },
        staticData: [],
        rules: {
          tableId: [
            { required: true, message: '表格列绑定数据表不能为空', trigger: 'blur' }
          ],
          columnId: [
            { required: true, message: '表格列绑定字段不能为空', trigger: 'blur' }
          ],
          paramValueType: [
            { required: true, message: '请选择参数值类型', trigger: 'blur' }
          ],
          paramValue: [
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
          this.formData.onlineTable = this.getCurrentTable
          this.formData.tableId = this.getCurrentTable.tableId
          this.formData.onlineColumn = findItemFromList(this.getTableColumnList, this.formData.columnId, 'columnId')
          this.onCancel(true)
        })
      },
      changeTable() {
        this.formData.columnId = undefined
        this.formData.paramValueType = undefined
        this.formData.paramValue = undefined
      },
      changeColumn() {
        this.formData.paramValueType = undefined
        this.formData.paramValue = undefined
      },
      changeParamValueType() {
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
    computed: {
      getCurrentTable() {
        return findItemFromList(this.tableList, this.formData.tableId, 'tableId')
      },
      getTableColumnList() {
        if (this.getCurrentTable) {
          return this.getCurrentTable.tableColumnList.filter(item => {
            return this.usedColumnList.indexOf(item.columnId) === -1 && item.filterType !== this.OnlineFilterType.NONE
          })
        } else {
          return []
        }
      }
    },
    mounted() {
      this.loadStaticData()
    }
  }
</script>

<style>
</style>
