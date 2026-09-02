<template>
  <div class="app-container" v-if="loading">
    <el-form ref="queryForm" :inline="true" label-width="80px">
      <el-form-item prop="ruleId">
        <el-select v-model="ruleId" placeholder="规则" size="small" @change="changeRule">
          <el-option v-for="item in ruleList" :key="item.id"
                     :label="item.ruleName" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" size="mini" style="margin-left: 20px;margin-right: 20px;" @click="handleSaveContent" v-hasPerm="['system:rule:content']">
          规则保存
        </el-button>
        <el-button type="primary" size="mini" style="margin-left: 20px;margin-right: 20px;" @click="handleValidate">规则验证</el-button>
        <el-button type="success" size="mini" style="margin-left: 20px;margin-right: 20px;" @click="handleOpenTest">规则测试</el-button>
      </el-form-item>
    </el-form>
    <div v-if="ruleInfo.ruleType===DroolsRuleType.DRL_FILE">
      <div class="code-editor">
        <code-editor
          :value="ruleContent"
          @input="handleInput"
          @init="editorInit"
          theme="xcode"
          lang="drools"
          :options="options"
          width="100%"
          height="100%"/>
      </div>
    </div>

    <div v-if="ruleInfo.ruleType===DroolsRuleType.GUIDED_RULE">
      <el-tabs v-model="tabName">
        <el-tab-pane label="规则配置" name="ruleConfig">

          <el-form :model="queryParam.data" ref="queryForm" :inline="true" v-show="showSearch" label-width="80px">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="queryParam.data.ruleName" placeholder="请输入规则名称" clearable size="small"
                        @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="规则状态" prop="ruleStatus">
              <el-select v-model="queryParam.data.ruleStatus" placeholder="规则状态" clearable size="small">
                <el-option v-for="item in AgileSwitchStatus.getList()" :key="item.value"
                           :label="item.label" :value="item.value"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="cyan" icon="el-icon-search" size="mini">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini">重置</el-button>
            </el-form-item>
          </el-form>

          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                         v-hasPerm="['system:rule:add']">
                新增
              </el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                         v-hasPerm="['system:rule:edit']">
                修改
              </el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                         v-hasPerm="['system:rule:delete']">
                删除
              </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getDroolsRuleList"></right-toolbar>
          </el-row>

          <el-table v-loading="loading" :data="ruleList">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="规则名称" align="center" prop="ruleName"/>
            <el-table-column label="规则状态" align="center" width="100">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.ruleStatus" :active-value="AgileSwitchStatus.ENABLE"
                           :inactive-value="AgileSwitchStatus.DISABLE"
                           @change="handleStatusChange(scope.row)"/>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-s-custom"
                           v-hasPerm="['system:rule:designer']">
                  设计
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-edit"
                           v-hasPerm="['system:rule:edit']">
                  修改
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-delete"
                           v-hasPerm="['system:rule:delete']">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-tab-pane>
        <el-tab-pane label="规则源码" name="ruleSource">
          <div class="code-editor">
            <code-editor
              :value="ruleContent"
              theme="xcode"
              lang="drools"
              :options="optionsReadonly"
              width="100%"
              height="100%"/>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog title="规则测试" :visible.sync="openTest" width="750px" append-to-body>
      <el-divider content-position="left">测试参数</el-divider>
      <el-scrollbar style="height:300px;" wrap-style="overflow-x:hidden;">
        <param-data v-for="item in ruleInfo.droolsModelList.filter(item=>item.inputFlag===AgileYesNo.YES)"
                    :drools-model="item" :key="item.id"
                    :model-data="paramData[item.modelName]"/>
        <el-card class="box-card">
          <div slot="header" style="justify-content:center;padding: 0 0 0 10px">
            <span style="line-height: 38px">Map对象出入参</span>
          </div>
          <el-form :ref="paramData" :model="paramData" label-width="80px">
            <el-row>
              <el-form-item label="入参" :prop="paramData.inputParam">
                <el-input v-model="paramData.inputParam" type="textarea" placeholder="仅支持json格式数据，最终将转为Map对象使用！"/>
              </el-form-item>
              <el-form-item label="出参" :prop="paramData.outputParam">
                <el-input v-model="paramData.outputParam" type="textarea" placeholder="仅支持json格式数据，最终将转为Map对象使用！"/>
              </el-form-item>
            </el-row>
          </el-form>
        </el-card>
      </el-scrollbar>

      <el-divider content-position="left">测试结果</el-divider>
      <el-form ref="resultsData" label-width="80px">
        <el-input v-model="resultsData" type="textarea" :rows="5" placeholder="规则执行结果，仅展示出参对象" />
      </el-form>

      <div slot="footer" class="dialog-footer" style="margin-top: 5px">
        <el-button type="primary" @click="handleSubmitTest">测 试</el-button>
        <el-button @click="openTest=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import { selectDroolsRuleList, detailDroolsRuleInfo, saveDroolsRuleContent, validateDroolsRuleContent, testDroolsRule } from '@/api/drools/rule'

  import ParamData from '../param/index'
  import CreateRule from './CreateRule'

  export default {
    name: 'Designer',
    components: {
      CodeEditor: require('vue2-ace-editor'), ParamData, CreateRule
    },
    data() {
      return {
        loading: true,
        ruleId: undefined,
        ruleInfo: {
          droolsModelList: []
        },
        ruleList: [],
        ruleContent: '',
        options: {
          tabSize: 4, // tab默认大小
          showPrintMargin: false, // 去除编辑器里的竖线
          fontSize: 15, // 字体大小
          highlightActiveLine: true, // 高亮配置
          enableBasicAutocompletion: true, // 启用基本自动完成
          enableSnippets: true, // 启用代码段
          enableLiveAutocompletion: true // 启用实时自动完成
        },
        optionsReadonly: {
          tabSize: 4, // tab默认大小
          showPrintMargin: false, // 去除编辑器里的竖线
          fontSize: 15, // 字体大小
          highlightActiveLine: true, // 高亮配置
          enableBasicAutocompletion: true, // 启用基本自动完成
          enableSnippets: true, // 启用代码段
          enableLiveAutocompletion: true, // 启用实时自动完成
          readOnly: true
        },
        openTest: false,
        paramData: { inputParam: undefined, outputParam: undefined },
        resultsData: undefined,
        tabName: 'ruleConfig',

        ruleConfig:
          { rules: 'and', list: [{ types: '', site: '', symbol: '', rules: '', type: 'condition' }] }
      }
    },
    computed: {},
    created() {
      this.ruleId = this.$route.params && this.$route.params.ruleId
      this.getDroolsRuleList()
    },
    methods: {
      getDroolsRuleList() {
        selectDroolsRuleList().then(response => {
            this.ruleList = response.data
            this.detailDroolsRule()
          }
        )
      },
      detailDroolsRule() {
        detailDroolsRuleInfo(this.ruleId).then(response => {
          if (!response.data) return
          this.$nextTick(() => {
            this.ruleId = response.data.id
            this.ruleInfo = response.data
            this.ruleContent = response.data.ruleContent
            this.loading = true
          })
        })
      },
      // 代码块初始化
      editorInit() {
        require('brace/ext/language_tools')
        require('brace/mode/drools')
        require('brace/theme/xcode')
      },
      handleInput(content) {
        this.ruleContent = content
      },
      changeRule() {
        this.detailDroolsRule()
      },
      handleSaveContent() {
        saveDroolsRuleContent({ id: this.ruleId, ruleContent: this.ruleContent }).then(response => {
          this.messageSuccess('规则内容保存成功！')
        })
      },
      handleValidate() {
        validateDroolsRuleContent({ ruleId: this.ruleId, ruleContent: this.ruleContent }).then(response => {
          this.messageSuccess('规则内容验证成功！')
        })
      },
      handleOpenTest() {
        this.resultsData = undefined
        const that = this

        function addDroolsModelData(modelData, droolsModeInfo) {
          if (!modelData) {
            modelData = {}
          }
          droolsModeInfo.droolsModelFieldList.forEach(field => {
            if (field.fieldType === that.DroolsFieldType.Object) {
              if (field.listFlag === that.AgileYesNo.YES) {
                const tempData = addDroolsModelData({}, field.droolsModelInfo)
                modelData[field.fieldName] = [tempData]
              } else {
                modelData[field.fieldName] = addDroolsModelData({}, field.droolsModelInfo)
              }
            } else {
              if (field.listFlag === that.AgileYesNo.YES) {
                modelData[field.fieldName] = [field.fieldType === that.DroolsFieldType.Boolean ? false : undefined]
              } else {
                modelData[field.fieldName] = field.fieldType === that.DroolsFieldType.Boolean ? false : undefined
              }
            }
          })
          return modelData
        }

        this.ruleInfo.droolsModelList.forEach(droolsModeInfo => {
          let modelData = this.paramData[droolsModeInfo.modelName]
          this.paramData[droolsModeInfo.modelName] = addDroolsModelData(modelData, droolsModeInfo)
        })
        this.openTest = true
      },
      handleSubmitTest() {
        testDroolsRule({ ruleId: this.ruleId, ruleContent: this.ruleContent, paramData: this.paramData }).then(response => {
          this.resultsData = JSON.stringify(response.data, null, '\t')
        })
      }
    }
  }
</script>

<style lang='scss'>
  .code-editor {
    width: 100%;
    height: calc(100vh - 190px);
  }
</style>
