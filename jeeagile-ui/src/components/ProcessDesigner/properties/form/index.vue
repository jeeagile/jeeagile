<template>
  <div class="properties-item__content">
    <el-form v-model="formInfo" :inline="false" label-width="100px" size="small" label-position="right">
      <el-form-item label="表单标识:">
        <el-input v-model="formInfo.formKey" @change="updateFormInfo('formKey')"/>
      </el-form-item>
    </el-form>
    <el-divider>表单字段</el-divider>
    <el-table :data="formPropertyList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="名称" align="center" min-width="50px" prop="label" show-overflow-tooltip/>
      <el-table-column label="类型" align="center" min-width="55px" prop="type" :formatter="row => formPropertyType[row.type] || row.type" show-overflow-tooltip/>
      <el-table-column label="默认值" align="center" min-width="55px" prop="defaultValue" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editFormPropertyInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeFormPropertyInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="formPropertyList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"/>
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openFormPropertyInfo">添加字段</el-button>
    </div>
    <el-dialog class="form-property__dialog" title="字段配置" :visible.sync="formPropertyVisible" width="360px" append-to-body destroy-on-close>
      <el-form ref="formPropertyInfo" :model="formPropertyInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="字段编号:" prop="id">
          <el-input v-model="formPropertyInfo.id" clearable/>
        </el-form-item>
        <el-form-item label="字段名称:" prop="label">
          <el-input v-model="formPropertyInfo.label" clearable/>
        </el-form-item>
        <el-form-item label="字段类型:" prop="propertyType">
          <el-select v-model="formPropertyInfo.propertyType" placeholder="请选择字段类型" @change="changeFormPropertyType">
            <el-option v-for="(value, key) of formPropertyType" :key="key" :label="value" :value="key"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="formPropertyInfo.propertyType === 'custom'" label="类型名称:">
          <el-input v-model="formPropertyInfo.type" clearable/>
        </el-form-item>
        <el-form-item v-if="formPropertyInfo.type === 'date'" label="时间格式:">
          <el-input v-model="formPropertyInfo.datePattern" clearable/>
        </el-form-item>
        <el-form-item label="默认值:">
          <el-input v-model="formPropertyInfo.defaultValue" clearable/>
        </el-form-item>
        <el-form-item label="" label-width="10px" align="left">
          <el-checkbox v-model="formPropertyInfo.required" label="必须"/>
          <el-checkbox v-model="formPropertyInfo.readable" label="可读" checked="checked"/>
          <el-checkbox v-model="formPropertyInfo.writable" label="可写" checked="checked"/>
          <el-checkbox v-model="formPropertyInfo.variable" label="可见" checked="checked"/>
        </el-form-item>
        <el-form-item class="form-item__button" label-width="10px">
          <template v-if="formPropertyInfo.type === 'enum'">
            <el-badge :value="formPropertyInfo.enumList ? formPropertyInfo.enumList.length : 0">
              <el-button size="small" type="primary" @click="openFormPropertyOption('enum')">枚举列表</el-button>
            </el-badge>
          </template>
          <el-badge :value="formPropertyInfo.constraintList ? formPropertyInfo.constraintList.length : 0">
            <el-button size="small" type="primary" @click="openFormPropertyOption('validation')">约束条件</el-button>
          </el-badge>
          <el-badge :value="formPropertyInfo.propertyList ? formPropertyInfo.propertyList.length : 0">
            <el-button size="small" type="primary" @click="openFormPropertyOption('properties')">字段属性</el-button>
          </el-badge>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="formPropertyVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createFormPropertyInfo">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="formPropertyOptionTitle" :visible.sync="formPropertyOptionVisible" width="360px" append-to-body destroy-on-close>
      <component :is="formPropertyOptionComponent" v-model="formPropertyOptionList" :option-list="formPropertyOptionList"/>
      <div slot="footer">
        <el-button size="mini" @click="formPropertyOptionVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createFormPropertyOption">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'
  import Enums from './enums'
  import Validation from './validation'
  import Properties from './properties'

  export default {
    name: 'Template',
    components: { Validation, Properties, Enums },
    props: {
      processModeler: {  // 流程Modeler
        type: Object,
        required: true
      },
      processInfo: {    // 流程基础信息
        type: Object,
        required: true
      },
      activeElement: {  // 当前选中元素
        type: Object,
        required: true
      }
    },
    data() {
      return {
        formPropertyList: [],    // 表单字段列表
        formPropertyInfo: {      // 表单字段信息
          id: '',                // 字段编号
          label: '',             // 字段名称
          propertyType: '',      // 字段类型
          type: '',              // 字段类型
          datePattern: '',       // 日期格式
          defaultValue: '',      // 默认值
          required: false,       // 必需
          readable: true,        // 可读
          writable: true,        // 可写
          variable: true,        // 可见
          enumList: [],          // 枚举列表
          constraintList: [],    // 约束条件列表
          propertyList: []       // 参数列表
        },
        formPropertyType: {      // 表单字段类型
          string: '字符型',
          long: '长整型',
          boolean: '布尔型',
          date: '日期型',
          enum: '枚举型',
          custom: '自定义'
        },
        formPropertyIndex: -1,
        formPropertyVisible: false,
        formPropertyOptionVisible: false,
        formPropertyOptionType: '',
        formPropertyOptionTitle: '',
        formPropertyOptionComponent: null,
        formPropertyOptionList: [],
        pageInfo: {            // 分页信息
          pageSize: 3,         // 每页的数据条数
          currentPage: 1,      // 默认开始页面
          pageCount: 5         // 最大显示页数
        },
        formInfo: {            // 表单信息
          formKey: ''          // 表单KEY
        },
        rules: {
          id: [
            { required: true, message: '请录入字段编号！', trigger: ['blur', 'change'] }
          ],
          label: [
            { required: true, message: '请录入字段名称！', trigger: ['blur', 'change'] }
          ],
          propertyType: [
            { required: true, message: '请录入字段类型！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            let businessObject = this.activeElement.businessObject
            this.formInfo.formKey = businessObject?.formKey
            this.initFormPropertyInfo()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initFormPropertyInfo() {
        this.formPropertyList = this.activeElement.businessObject?.extensionElements?.values
          .filter(element => element.$type === `${this.processInfo.processPrefix}:FormProperty`)
          .map(formPropertyElement => {
            return {
              id: formPropertyElement.id,
              label: formPropertyElement.label,
              propertyType: !this.formPropertyType[formPropertyElement.type] ? 'custom' : formPropertyElement.type,
              type: formPropertyElement.type,
              datePattern: formPropertyElement.datePattern,
              defaultValue: formPropertyElement.defaultValue,
              required: formPropertyElement.required,
              readable: formPropertyElement.readable,
              writable: formPropertyElement.writable,
              variable: formPropertyElement.variable,
              enumList: formPropertyElement.values?.map(valueElement => {
                return JSON.parse(JSON.stringify(valueElement))
              }) ?? [],
              constraintList: formPropertyElement.validation?.constraints?.map(constraintElement => {
                return JSON.parse(JSON.stringify(constraintElement))
              }) ?? [],
              propertyList: formPropertyElement.properties?.values?.map(propertyElement => {
                return JSON.parse(JSON.stringify(propertyElement))
              }) ?? []
            }
          }) ?? []
      },
      updateFormInfo(key) {
        const properties = Object.create(null)
        properties[key] = this.formInfo[key]
        processHelper.updateProperties(this.activeElement, properties)
      },
      changeFormPropertyType(propertyType) {
        this.$set(this.formPropertyInfo, 'type', propertyType === 'custom' ? '' : propertyType)
      },
      openFormPropertyInfo() {
        this.formPropertyIndex = -1
        this.formPropertyInfo = {}
        this.formPropertyVisible = true
        this.$nextTick(() => {
          this.$refs.formPropertyInfo.clearValidate()
        })
      },
      editFormPropertyInfo(row, index) {
        this.formPropertyIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.formPropertyInfo = JSON.parse(JSON.stringify(row))
        this.formPropertyVisible = true
      },
      removeFormPropertyInfo(row, index) {
        this.formPropertyList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
        this.updateFormPropertyElements()
      },
      createFormPropertyInfo() {
        this.$refs.formPropertyInfo.validate(valid => {
            if (valid) {
              if (this.formPropertyIndex === -1) {
                this.formPropertyList.push(this.formPropertyInfo)
              } else {
                this.formPropertyList.splice(this.formPropertyIndex, 1, this.formPropertyInfo)
              }
              this.updateFormPropertyElements()
              this.formPropertyVisible = false
            }
          }
        )
      },
      updateFormPropertyElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:FormProperty`) ?? []
        if (this.formPropertyList?.length) {
          this.formPropertyList.forEach(formPropertyInfo => {
            const { id, type, label, datePattern, defaultValue, required, readable, writable, variable } = formPropertyInfo
            let formPropertyProperties = { id, type, label, defaultValue, required, readable, writable, variable }
            if (type === 'date') {
              formPropertyProperties.datePattern = datePattern
            }
            const formPropertyElementName = `${this.processInfo.processPrefix}:FormProperty`
            let formPropertyElement = processHelper.createElement(formPropertyElementName, formPropertyProperties)
            formPropertyElement.values = this.createPropertyEnumsElement(formPropertyInfo)
            formPropertyElement.validation = this.createPropertyValidationElement(formPropertyInfo)
            formPropertyElement.properties = this.createPropertyPropertiesElement(formPropertyInfo)
            extensionElements.get('values').push(formPropertyElement)
            processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
          })
        }
      },
      createPropertyEnumsElement(formPropertyInfo) {
        if (formPropertyInfo.type === 'enum' && formPropertyInfo.enumList?.length) {
          return formPropertyInfo.enumList.map(enumInfo => {
            const valueElementName = `${this.processInfo.processPrefix}:Value`
            return processHelper.createElement(valueElementName, enumInfo)
          })
        } else {
          return null
        }
      },
      createPropertyValidationElement(formPropertyInfo) {
        if (formPropertyInfo.constraintList?.length) {
          const constraintElementList = formPropertyInfo.constraintList.map(constrainInfo => {
            const constraintElementName = `${this.processInfo.processPrefix}:Constraint`
            return processHelper.createElement(constraintElementName, constrainInfo)
          })
          const validationElementName = `${this.processInfo.processPrefix}:Validation`
          return processHelper.createElement(validationElementName, { constraints: constraintElementList })
        } else {
          return null
        }
      },
      createPropertyPropertiesElement(formPropertyInfo) {
        if (formPropertyInfo.propertyList?.length) {
          const propertyElementList = formPropertyInfo.propertyList.map(propertyInfo => {
            const propertyElementName = `${this.processInfo.processPrefix}:Property`
            return processHelper.createElement(propertyElementName, propertyInfo)
          })
          const propertiesElementName = `${this.processInfo.processPrefix}:Properties`
          return processHelper.createElement(propertiesElementName, { values: propertyElementList })
        } else {
          return null
        }
      },
      openFormPropertyOption(optionType) {
        this.formPropertyOptionType = optionType
        switch (optionType) {
          case 'enum':
            this.formPropertyOptionList = this.formPropertyInfo.enumList
            this.formPropertyOptionComponent = Enums
            this.formPropertyOptionTitle = '字段枚举'
            break
          case 'validation':
            this.formPropertyOptionList = this.formPropertyInfo.constraintList
            this.formPropertyOptionComponent = Validation
            this.formPropertyOptionTitle = '约束条件'
            break
          case 'properties':
            this.formPropertyOptionList = this.formPropertyInfo.propertyList
            this.formPropertyOptionComponent = Properties
            this.formPropertyOptionTitle = '字段属性'
            break
          default:
        }
        this.formPropertyOptionVisible = true
      },
      createFormPropertyOption() {
        switch (this.formPropertyOptionType) {
          case 'enum':
            this.formPropertyInfo.enumList = this.formPropertyOptionList
            break
          case 'validation':
            this.formPropertyInfo.constraintList = this.formPropertyOptionList
            break
          case 'properties':
            this.formPropertyInfo.propertyList = this.formPropertyOptionList
            break
          default:
        }
        this.formPropertyOptionComponent = null
        this.formPropertyOptionVisible = false
      }
    }
  }
</script>
<style lang='scss'>
</style>
