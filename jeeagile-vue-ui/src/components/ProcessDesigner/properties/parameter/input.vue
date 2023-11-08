<template>
  <div class="properties-item__content">
    <el-table :data="inputParameterList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="参数名称" align="center" min-width="60px" prop="name" show-overflow-tooltip/>
      <el-table-column label="参数类型" align="center" min-width="90px" prop="type" :formatter="row => parameterType[row.type]" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editInputParameterInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeInputParameterInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="inputParameterList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next">
    </el-pagination>
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openInputParameterInfo">添加参数</el-button>
    </div>
    <el-dialog title="参数配置" :visible.sync="inputParameterVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="inputParameterInfo" :model="inputParameterInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="参数名称:" prop="name">
          <el-input v-model="inputParameterInfo.name"/>
        </el-form-item>
        <el-form-item label="参数类型:" prop="type">
          <el-select v-model="inputParameterInfo.type" placeholder="请选择参数类型">
            <el-option v-for="(value, key) of parameterType" :key="key" :label="value" :value="key"/>
          </el-select>
        </el-form-item>
        <template v-if="inputParameterInfo.type==='text'">
          <el-form-item label="参数名称:" prop="value">
            <el-input v-model="inputParameterInfo.value"/>
          </el-form-item>
        </template>
        <template v-if="inputParameterInfo.type==='script'">
          <el-form-item label="脚本格式:" prop="scriptFormat">
            <el-input v-model="inputParameterInfo.scriptFormat"/>
          </el-form-item>
          <el-form-item label="脚本类型:" prop="scriptType">
            <el-select v-model="inputParameterInfo.scriptType">
              <el-option label="内联脚本" value="inline"/>
              <el-option label="外部资源" value="external"/>
            </el-select>
          </el-form-item>
          <el-form-item v-if="inputParameterInfo.scriptType === 'inline'" label="脚本内容:" prop="scriptValue">
            <el-input v-model="inputParameterInfo.scriptValue" type="textarea" resize="vertical" clearable :autosize="{ minRows: 2, maxRows: 4 }"/>
          </el-form-item>
          <el-form-item v-if="inputParameterInfo.scriptType === 'external'" label="资源地址:" prop="scriptResource">
            <el-input v-model="inputParameterInfo.scriptResource" clearable/>
          </el-form-item>
        </template>
        <template v-if="inputParameterInfo.type==='list'">
          <value-list values="inputParameterInfo.valueList" v-model="inputParameterInfo.valueList"/>
        </template>
        <template v-if="inputParameterInfo.type==='map'">
          <map-list values="inputParameterInfo.mapList" v-model="inputParameterInfo.mapList"/>
        </template>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="inputParameterVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createInputParameterInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'
  import ValueList from './value'
  import MapList from './map'

  export default {
    name: 'Input',
    components: { ValueList, MapList },
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
        inputParameterList: [],
        inputParameterVisible: false,
        inputParameterIndex: -1,
        parameterType: {
          text: 'Text',
          script: 'Script',
          list: 'List',
          map: 'Map'
        },
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        inputParameterInfo: {
          name: '',
          type: '',
          value: '',
          valueList: [],
          mapList: []
        },
        rules: {
          name: [
            { required: true, message: '请录入参数名称！', trigger: ['blur', 'change'] }
          ],
          type: [
            { required: true, message: '请选择参数类型！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initParameterList()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initParameterList() {
        const inputOutputElementName = `${this.processInfo.processPrefix}:InputOutput`
        this.inputParameterList = this.activeElement.businessObject?.extensionElements?.values
          .find(element => element.$type === inputOutputElementName)?.inputParameters?.map(element => {
            if (element.definition) {
              if (element.definition.$type.indexOf('Script') > 0) {
                return {
                  name: element.name,
                  type: 'script',
                  scriptFormat: element.definition.scriptFormat,
                  scriptType: element.definition.resource ? 'external' : 'inline',
                  scriptResource: element.definition.resource,
                  scriptValue: element.definition.value
                }
              }
              if (element.definition.$type.indexOf('List') > 0) {
                return {
                  name: element.name,
                  type: 'list',
                  valueList: element.definition.items?.map(element => {
                    return {
                      value: element.value
                    }
                  }) ?? []
                }
              }
              if (element.definition.$type.indexOf('Map') > 0) {
                return {
                  name: element.name,
                  type: 'map',
                  mapList: element.definition.entries?.map(element => {
                    return {
                      key: element.key,
                      value: element.value
                    }
                  }) ?? []
                }
              }
            } else {
              return {
                name: element.name,
                value: element.value,
                type: 'text'
              }
            }
          }) ?? []
      },
      openInputParameterInfo() {
        this.inputParameterIndex = -1
        this.inputParameterInfo = {}
        this.inputParameterVisible = true
        this.$nextTick(() => {
          this.$refs.inputParameterInfo.clearValidate()
        })
      },
      editInputParameterInfo(row, index) {
        this.inputParameterIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.inputParameterInfo = JSON.parse(JSON.stringify(row))
        this.inputParameterVisible = true
      },
      removeInputParameterInfo(row, index) {
        this.inputParameterList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
        this.updateInputParameterElements()
      },
      createInputParameterInfo() {
        this.$refs.inputParameterInfo.validate(valid => {
            if (valid) {
              if (this.inputParameterIndex === -1) {
                this.inputParameterList.push(this.inputParameterInfo)
              } else {
                this.inputParameterList.splice(this.inputParameterIndex, 1, this.inputParameterInfo)
              }
              this.updateInputParameterElements()
              this.inputParameterVisible = false
            }
          }
        )
      },
      updateInputParameterElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        const inputOutputElementName = `${this.processInfo.processPrefix}:InputOutput`
        let inputOutputElement = extensionElements.values?.filter(item => item.$type == inputOutputElementName)
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== inputOutputElementName)
        if (this.inputParameterList?.length) {
          const parameterElementList = this.inputParameterList.map(inputParameterInfo => {
            return this.createInputParameterElement(inputParameterInfo)
          })
          let outputParameters = []
          if (inputOutputElement?.length) {
            outputParameters = inputOutputElement[0]?.outputParameters
          }
          inputOutputElement = processHelper.createElement(inputOutputElementName, {
            inputParameters: parameterElementList,
            outputParameters: outputParameters
          })
          extensionElements.get('values').push(inputOutputElement)
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      },
      createInputParameterElement(inputParameterInfo) {
        const parameterElementName = `${this.processInfo.processPrefix}:InputParameter`
        if (inputParameterInfo.type === 'text') {
          let properties = Object.create(null)
          properties.name = inputParameterInfo.name
          properties.value = inputParameterInfo.value
          return processHelper.createElement(parameterElementName, properties)
        }
        if (inputParameterInfo.type === 'script') {
          const scriptProperties = {
            scriptFormat: inputParameterInfo.scriptFormat,
            resource: inputParameterInfo.scriptType === 'external' ? inputParameterInfo.scriptResource : null,
            value: inputParameterInfo.scriptType === 'inline' ? inputParameterInfo.scriptValue : null
          }
          const scriptElementName = `${this.processInfo.processPrefix}:Script`
          const scriptElement = processHelper.createElement(scriptElementName, scriptProperties)
          let properties = Object.create(null)
          properties.name = inputParameterInfo.name
          properties.definition = scriptElement
          return processHelper.createElement(parameterElementName, properties)
        }
        if (inputParameterInfo.type === 'list') {
          const valueElementList = inputParameterInfo.valueList?.map(valueInfo => {
            const valueElementName = `${this.processInfo.processPrefix}:Value`
            return processHelper.createElement(valueElementName, valueInfo)
          })
          const listElementName = `${this.processInfo.processPrefix}:List`
          const listElement = processHelper.createElement(listElementName, { items: valueElementList })
          let properties = Object.create(null)
          properties.name = inputParameterInfo.name
          properties.definition = listElement
          return processHelper.createElement(parameterElementName, properties)
        }
        if (inputParameterInfo.type === 'map') {
          const entryElementList = inputParameterInfo.mapList?.map(mapInfo => {
            const entryElementName = `${this.processInfo.processPrefix}:Entry`
            return processHelper.createElement(entryElementName, mapInfo)
          })
          const mapElementName = `${this.processInfo.processPrefix}:Map`
          const mapElement = processHelper.createElement(mapElementName, { entries: entryElementList })
          let properties = Object.create(null)
          properties.name = inputParameterInfo.name
          properties.definition = mapElement
          return processHelper.createElement(parameterElementName, properties)
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
