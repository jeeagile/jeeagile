<template>
  <div class="properties-item__content">
    <el-table :data="taskListenerList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="事件" align="center" min-width="40px" prop="event" :formatter="row => eventTypeMap[row.event]"/>
      <el-table-column label="类型" align="center" min-width="55px" prop="type" :formatter="row => listenerTypeMap[row.type]" show-overflow-tooltip/>
      <el-table-column label="参数" align="center" min-width="70px">
        <template slot-scope="scope">
          <el-badge :value="scope.row.fieldList ? scope.row.fieldList.length : 0">
            <el-button size="mini" type="primary" @click="editTaskListenerField(scope.row, scope.$index)">配置</el-button>
          </el-badge>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="editTaskListenerInfo(scope.row, scope.$index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeTaskListenerInfo(scope.row, scope.$index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="taskListenerList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openTaskListenerInfo">添加监听器</el-button>
    </div>
    <el-dialog title="任务监听器" :visible.sync="taskListenerVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="taskListenerInfo" :model="taskListenerInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="事件类型:" prop="event">
          <el-select v-model="taskListenerInfo.event">
            <el-option v-for="i in Object.keys(eventTypeMap)" :key="i" :label="eventTypeMap[i]" :value="i"/>
          </el-select>
        </el-form-item>
        <el-form-item label="监听器类型:" prop="type">
          <el-select v-model="taskListenerInfo.type">
            <el-option v-for="i in Object.keys(listenerTypeMap)" :key="i" :label="listenerTypeMap[i]" :value="i"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="taskListenerInfo.type && taskListenerInfo.type != 'script'" :label="`${listenerTypeMap[taskListenerInfo.type]}:`" prop="value">
          <el-input v-model="taskListenerInfo.value" clearable/>
        </el-form-item>
        <template v-if="taskListenerInfo.type === 'script'">
          <el-form-item label="脚本格式:" prop="scriptFormat">
            <el-input v-model="taskListenerInfo.scriptFormat"/>
          </el-form-item>
          <el-form-item label="脚本类型:" prop="scriptType">
            <el-select v-model="taskListenerInfo.scriptType">
              <el-option label="内联脚本" value="inline"/>
              <el-option label="外部资源" value="external"/>
            </el-select>
          </el-form-item>
          <el-form-item v-if="taskListenerInfo.scriptType === 'inline'" label="脚本内容:" prop="scriptValue">
            <el-input v-model="taskListenerInfo.scriptValue" type="textarea" resize="vertical" clearable :autosize="{ minRows: 2, maxRows: 4 }"/>
          </el-form-item>
          <el-form-item v-if="taskListenerInfo.scriptType === 'external'" label="资源地址:" prop="scriptResource">
            <el-input v-model="taskListenerInfo.scriptResource" clearable/>
          </el-form-item>
        </template>
        <el-divider/>
        <field-config v-model="taskListenerInfo.fieldList" :fields="taskListenerInfo.fieldList"/>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="taskListenerVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createTaskListenerInfo">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="参数配置" :visible.sync="taskListenerFieldVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="taskListenerInfo" :model="taskListenerInfo" :rules="rules">
        <field-config ref="taskListenerInfo" v-model="taskListenerInfo.fieldList" :fields="taskListenerInfo.fieldList"/>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="taskListenerFieldVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createTaskListenerInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'
  import { TaskEventTypeMap, TaskListenerTypeMap, FieldTypeMap } from './listener'
  import FieldConfig from './FieldConfig'

  export default {
    name: 'TaskListeners',
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
        taskListenerList: [],
        taskListenerVisible: false,
        taskListenerFieldVisible: false,
        taskListenerIndex: -1,
        eventTypeMap: TaskEventTypeMap,
        listenerTypeMap: TaskListenerTypeMap,
        fieldTypeMap: FieldTypeMap,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        taskListenerInfo: {
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
            this.initTaskListenerList()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initTaskListenerList() {
        this.taskListenerList = this.activeElement.businessObject?.extensionElements?.values
          .filter(element => element.$type === `${this.processInfo.processPrefix}:TaskListener`)
          .map(element => {
            const listenerType = Object.keys(this.listenerTypeMap).filter(listenerType => element[listenerType])
            const taskListenerInfo = {
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
              taskListenerInfo.scriptFormat = element.script.scriptFormat
              taskListenerInfo.scriptType = element.script.resource ? 'external' : 'inline'
              taskListenerInfo.scriptResource = element.script.resource
              taskListenerInfo.scriptValue = element.script.value
            } else {
              taskListenerInfo.value = element[listenerType]
            }
            return taskListenerInfo
          }) ?? []
      },
      editTaskListenerField(row, index) {
        this.taskListenerIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.taskListenerInfo = JSON.parse(JSON.stringify(row))
        this.taskListenerFieldVisible = true
      },
      editTaskListenerInfo(row, index) {
        this.taskListenerIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.taskListenerInfo = JSON.parse(JSON.stringify(row))
        this.taskListenerVisible = true
      },
      removeTaskListenerInfo(row, index) {
        this.taskListenerList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
        this.updateExtensionElements()
      },
      openTaskListenerInfo() {
        this.taskListenerIndex = -1
        this.taskListenerInfo = {}
        this.taskListenerVisible = true
        this.$nextTick(() => {
          this.$refs.taskListenerInfo.clearValidate()
        })
      },
      createTaskListenerInfo() {
        this.$refs.taskListenerInfo.validate(valid => {
            if (valid) {
              if (this.taskListenerIndex === -1) {
                this.taskListenerList.push(this.taskListenerInfo)
              } else {
                this.taskListenerList.splice(this.taskListenerIndex, 1, this.taskListenerInfo)
              }
              this.updateExtensionElements()
              this.taskListenerFieldVisible = false
              this.taskListenerVisible = false
            }
          }
        )
      },
      updateExtensionElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:TaskListener`) ?? []
        if (this.taskListenerList?.length) {
          this.taskListenerList.forEach(taskListenerInfo => {
            const taskListenerProperties = Object.create(null)
            taskListenerProperties.event = taskListenerInfo.event
            if (taskListenerInfo.type === 'script') {
              const scriptProperties = {
                scriptFormat: taskListenerInfo.scriptFormat,
                resource: taskListenerInfo.scriptType === 'external' ? taskListenerInfo.scriptResource : null,
                value: taskListenerInfo.scriptType === 'inline' ? taskListenerInfo.scriptValue : null
              }
              const scriptElementName = `${this.processInfo.processPrefix}:Script`
              taskListenerProperties.script = processHelper.createElement(scriptElementName, scriptProperties)
            } else {
              taskListenerProperties[taskListenerInfo.type] = taskListenerInfo.value
            }
            const taskListenerElementName = `${this.processInfo.processPrefix}:TaskListener`
            const taskListenerElement = processHelper.createElement(taskListenerElementName, taskListenerProperties)
            if (taskListenerInfo.fieldList?.length) {
              const fieldElementList = taskListenerInfo.fieldList.map(field => {
                const fieldProperties = Object.create(null)
                fieldProperties.name = field.name
                fieldProperties[field.type] = field.value
                const fieldElementName = `${this.processInfo.processPrefix}:Field`
                return processHelper.createElement(fieldElementName, fieldProperties)
              })
              taskListenerElement.fields = fieldElementList
            }
            extensionElements.get('values').push(taskListenerElement)
          })
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
