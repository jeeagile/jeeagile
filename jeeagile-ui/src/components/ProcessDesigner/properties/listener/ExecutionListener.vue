<template>
  <div class="properties-item__content">
    <el-table :data="executionListenerList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="事件" align="center" min-width="40px" prop="event" :formatter="row => eventTypeMap[row.event]" show-overflow-tooltip/>
      <el-table-column label="类型" align="center" min-width="55px" prop="type" :formatter="row => listenerTypeMap[row.type]" show-overflow-tooltip/>
      <el-table-column label="参数" align="center" min-width="70px">
        <template slot-scope="scope">
          <el-badge :value="scope.row.fieldList ? scope.row.fieldList.length : 0">
            <el-button size="mini" type="primary" @click="editExecutionListenerField(scope.row, scope.$index)">配置</el-button>
          </el-badge>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="editExecutionListenerInfo(scope.row, scope.$index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeExecutionListenerInfo(scope.row, scope.$index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="executionListenerList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openExecutionListenerInfo">添加监听器</el-button>
    </div>
    <el-dialog title="执行监听器" :visible.sync="executionListenerVisible" width="360px" append-to-body destroy-on-close>
      <el-form ref="executionListenerInfo" :model="executionListenerInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="事件类型:" prop="event">
          <el-select v-model="executionListenerInfo.event">
            <el-option v-for="i in Object.keys(eventTypeMap)" :key="i" :label="eventTypeMap[i]" :value="i"/>
          </el-select>
        </el-form-item>
        <el-form-item label="监听器类型:" prop="type">
          <el-select v-model="executionListenerInfo.type">
            <el-option v-for="i in Object.keys(listenerTypeMap)" :key="i" :label="listenerTypeMap[i]" :value="i"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="executionListenerInfo.type && executionListenerInfo.type != 'script'" :label="`${listenerTypeMap[executionListenerInfo.type]}:`" prop="value">
          <el-input v-model="executionListenerInfo.value" clearable/>
        </el-form-item>
        <template v-if="executionListenerInfo.type === 'script'">
          <el-form-item label="脚本格式:" prop="scriptFormat">
            <el-input v-model="executionListenerInfo.scriptFormat"/>
          </el-form-item>
          <el-form-item label="脚本类型:" prop="scriptType">
            <el-select v-model="executionListenerInfo.scriptType">
              <el-option label="内联脚本" value="inline"/>
              <el-option label="外部资源" value="external"/>
            </el-select>
          </el-form-item>
          <el-form-item v-if="executionListenerInfo.scriptType === 'inline'" label="脚本内容:" prop="scriptValue">
            <el-input v-model="executionListenerInfo.scriptValue" type="textarea" resize="vertical" clearable :autosize="{ minRows: 2, maxRows: 4 }"/>
          </el-form-item>
          <el-form-item v-if="executionListenerInfo.scriptType === 'external'" label="资源地址:" prop="scriptResource">
            <el-input v-model="executionListenerInfo.scriptResource" clearable/>
          </el-form-item>
        </template>
        <el-divider/>
        <field-config v-model="executionListenerInfo.fieldList" :fields="executionListenerInfo.fieldList"/>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="executionListenerVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createExecutionListenerInfo">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="参数配置" :visible.sync="executionListenerFieldVisible" width="360px" append-to-body destroy-on-close>
      <field-config v-model="executionListenerInfo.fieldList" :fields="executionListenerInfo.fieldList"/>
      <div slot="footer">
        <el-button size="mini" @click="executionListenerFieldVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createExecutionListenerInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'
  import { ExecutionEventTypeMap, ExecutionListenerTypeMap, FieldTypeMap } from './listener'
  import FieldConfig from './FieldConfig'

  export default {
    name: 'ExecutionListeners',
    components: { FieldConfig },
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
        executionListenerList: [],
        executionListenerVisible: false,
        executionListenerFieldVisible: false,
        executionListenerIndex: -1,
        eventTypeMap: ExecutionEventTypeMap,
        listenerTypeMap: ExecutionListenerTypeMap,
        fieldTypeMap: FieldTypeMap,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        executionListenerInfo: {
          event: '',
          type: '',
          value: '',
          fieldList: []
        },
        rules: {
          event: [
            { required: true, message: '请选择事件类型！', trigger: ['blur', 'change'] }
          ],
          type: [
            { required: true, message: '请选择监听器类型！', trigger: ['blur', 'change'] }
          ],
          value: [
            { required: true, message: '请填写相应内容！', trigger: ['blur', 'change'] }
          ],
          scriptFormat: [
            { required: true, message: '请填写脚本格式！', trigger: ['blur', 'change'] }
          ],
          scriptType: [
            { required: true, message: '请选择脚本类型！', trigger: ['blur', 'change'] }
          ],
          scriptResource: [
            { required: true, message: '请填写资源地址！', trigger: ['blur', 'change'] }
          ],
          scriptValue: [
            { required: true, message: '请填写脚本内容！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initExecutionListenerList()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initExecutionListenerList() {
        this.executionListenerList = this.activeElement.businessObject?.extensionElements?.values
          .filter(element => element.$type === `${this.processInfo.processPrefix}:ExecutionListener`)
          .map(element => {
            const listenerType = Object.keys(this.listenerTypeMap).filter(listenerType => element[listenerType])
            const executionListenerInfo = {
              event: element.event,
              type: listenerType[0],
              fieldList: element.fields?.map(field => {
                const fieldType = Object.keys(this.fieldTypeMap).filter(fieldType => field[fieldType])
                return {
                  name: field.name,
                  type: fieldType[0],
                  value: field[fieldType]
                }
              }) ?? []
            }
            if (listenerType[0] === 'script') {
              executionListenerInfo.scriptFormat = element.script.scriptFormat
              executionListenerInfo.scriptType = element.script.resource ? 'external' : 'inline'
              executionListenerInfo.scriptResource = element.script.resource
              executionListenerInfo.scriptValue = element.script.value
            } else {
              executionListenerInfo.value = element[listenerType]
            }
            return executionListenerInfo
          }) ?? []
      },
      editExecutionListenerField(row, index) {
        this.executionListenerIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.executionListenerInfo = JSON.parse(JSON.stringify(row))
        this.executionListenerFieldVisible = true
      },
      editExecutionListenerInfo(row, index) {
        this.executionListenerIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.executionListenerInfo = JSON.parse(JSON.stringify(row))
        this.executionListenerVisible = true
      },
      removeExecutionListenerInfo(row, index) {
        this.executionListenerList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
        this.updateExtensionElements()
      },
      openExecutionListenerInfo() {
        this.executionListenerIndex = -1
        this.executionListenerInfo = {}
        this.executionListenerVisible = true
        this.$nextTick(() => {
          this.$refs.executionListenerInfo.clearValidate()
        })
      },
      createExecutionListenerInfo() {
        this.$refs.executionListenerInfo.validate(valid => {
            if (valid) {
              if (this.executionListenerIndex === -1) {
                this.executionListenerList.push(this.executionListenerInfo)
              } else {
                this.executionListenerList.splice(this.executionListenerIndex, 1, this.executionListenerInfo)
              }
              this.updateExtensionElements()
              this.executionListenerFieldVisible = false
              this.executionListenerVisible = false
            }
          }
        )
      },
      updateExtensionElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:ExecutionListener`) ?? []
        if (this.executionListenerList?.length) {
          this.executionListenerList.forEach(executionListenerInfo => {
            const executionListenerProperties = Object.create(null)
            executionListenerProperties.event = executionListenerInfo.event
            if (executionListenerInfo.type === 'script') {
              const scriptProperties = {
                scriptFormat: executionListenerInfo.scriptFormat,
                resource: executionListenerInfo.scriptType === 'external' ? executionListenerInfo.scriptResource : null,
                value: executionListenerInfo.scriptType === 'inline' ? executionListenerInfo.scriptValue : null
              }
              const scriptElementName = `${this.processInfo.processPrefix}:Script`
              executionListenerProperties.script = processHelper.createElement(scriptElementName, scriptProperties)
            } else {
              executionListenerProperties[executionListenerInfo.type] = executionListenerInfo.value
            }
            const executionListenerElementName = `${this.processInfo.processPrefix}:ExecutionListener`
            const executionListenerElement = processHelper.createElement(executionListenerElementName, executionListenerProperties)
            if (executionListenerInfo.fieldList?.length) {
              const fieldElementList = executionListenerInfo.fieldList.map(field => {
                const fieldProperties = Object.create(null)
                fieldProperties.name = field.name
                fieldProperties[field.type] = field.value
                const fieldElementName = `${this.processInfo.processPrefix}:Field`
                return processHelper.createElement(fieldElementName, fieldProperties)
              })
              executionListenerElement.fields = fieldElementList
            }

            extensionElements.get('values').push(executionListenerElement)
          })
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
