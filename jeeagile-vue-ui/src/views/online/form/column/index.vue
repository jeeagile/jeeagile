<template>
  <div class="table-column">
    <el-container style="height: 100%;">
      <el-aside class="table-column-list" style="width: 250px">
        <div class="title">
          <span :title="tableLabel" style="width: 200px">{{ tableName }}</span>
          <el-dropdown trigger="click" @command="addNewOnlineColumn">
            <el-button class="table-btn success" size="mini" type="text"
                       icon="el-icon-circle-plus-outline" :disabled="getAddOnlineColumnList.length <= 0"
                       title="新增"
            />
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="onlineColumn in getAddOnlineColumnList" :key="onlineColumn.columnName"
                                :command="onlineColumn"
              >
                {{ onlineColumn.columnName }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <el-scrollbar style="height:calc(100vh - 160px)">
          <div v-for="column in getOnlineTableColumnList" :key="column.id"
               class="table-column-item" :class="{'active-column': column === onlineColumn}"
               :title="column.columnComment"
               @click.stop="handleOnlineColumn(column)"
          >
            <div>
              <span style="margin-right: 10px;">{{ column.columnName }}</span>
              <el-tag v-if="column.deletedFlag" size="mini" type="danger">已删除</el-tag>
            </div>
            <div class="refresh" style="margin-left: 10px;">
              <el-button v-if="getAddOnlineColumnList.length <= 0" class="table-btn success" size="mini" type="text"
                         @click.stop="refreshOnlineColumn(column, column)"
              >
                刷新
              </el-button>
              <el-dropdown v-else trigger="click" @command="refreshNewOnlineColumn">
                <el-button class="table-btn success" size="mini" type="text" @click.stop="() => {}">
                  刷新
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-for="newColumn in getAddOnlineColumnList" :key="newColumn.columnName"
                                    :command="{column, newColumn}"
                  >
                    {{ newColumn.columnName }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-button v-if="column.deletedFlag" class="table-btn delete" size="mini" type="text"
                         style="margin-left: 10px;"
                         @click.stop="deleteOnlineColumn(column)"
              >
                删除
              </el-button>
            </div>
          </div>
        </el-scrollbar>
      </el-aside>
      <el-main class="table-column-info">
        <div class="title">
          <span>字段属性</span>
          <div style="float: right">
            <el-button size="mini" type="text" icon="el-icon-check" @click="saveOnlineColumn">保存</el-button>
            <el-button size="mini" type="text" icon="el-icon-back" style="color: #67C23A;" @click="handleBack">返回
            </el-button>
          </div>
        </div>
        <el-form ref="onlineColumnForm" :model="onlineColumn" class="full-width-input" style="width: 100%;"
                 label-width="120px" label-position="right" size="small" @submit.native.prevent
        >
          <template v-if="onlineColumn!=null">
            <el-col>
              <el-form-item label="字段名：">
                <span :title="onlineColumn.columnComment">{{ onlineColumn.columnName }}</span>
                <el-tag v-if="onlineColumn.primaryFlag==AgileYesNo.YES" size="mini" type="warning"
                        style="margin-left: 20px;"
                >主键
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="字段类型：">
                <span>{{ onlineColumn.columnType }}</span>
                <el-tag size="mini" type="success" effect="dark" style="margin-left: 10px;">{{ onlineColumn.fieldType }}
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="过滤支持：">
                <el-radio-group v-model="onlineColumn.filterType">
                  <el-radio-button v-for="item in OnlineFilterType.getList()"
                                   :key="item.value" :label="item.value" :value="item.value"
                  >
                    {{ item.label }}
                  </el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="显示名称：">
                <el-input v-model="onlineColumn.fieldLabel" style="width: 250px;" placeholder="字段在表单上的显示名称"/>
              </el-form-item>
            </el-col>
            <el-col class="attribute-item">
              <el-form-item label="字典数据：">
                <el-select v-model="onlineColumn.dictId" placeholder="选择字段绑定的字典" style="width: 250px;"
                           clearable filterable :disabled="onlineColumn.fieldType === 'Boolean'"
                >
                  <el-option v-for="item in onlineDictList" :key="item.id" :value="item.id" :label="item.dictName"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="attribute-item">
              <el-form-item label="字段分类：">
                <el-select v-model="onlineColumn.fieldKind" clearable placeholder="字段业务分类" style="width: 250px;">
                  <el-option v-for="item in OnlineFieldKind.getList()" :key="item.value"
                             :label="item.label" :value="item.value" :disabled="disabledFieldKind(item)"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </template>
        </el-form>
      </el-main>
      <el-aside class="table-column-rules" style="width: 280px">
        <div class="title">
          <span>验证规则</span>
        </div>
        <el-table size="mini" :data="columnRuleList" :show-header="false" empty-text="请添加验证规则">
          <el-table-column label="规则名称" prop="ruleName" :show-overflow-tooltip="true"/>
          <el-table-column label="提示信息" prop="message" :formatter="formatRuleMessage" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" width="100px">
            <template slot-scope="scope">
              <el-button class="table-btn success" type="text" @click="editColumnRule(scope.row)">编辑</el-button>
              <el-button class="table-btn delete" type="text" @click="deleteColumnRule(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button style="width: 100%; margin-top: 10px; border: 1px dashed #EBEEF5;"
                   :disabled="onlineColumn == null" icon="el-icon-plus" @click="addColumnRule"
        >
          添加验证规则
        </el-button>
        <el-dialog :title="columnRuleTitle" :visible.sync="columnRuleOpen" width="450px" append-to-body>
          <!--设置字段验证规则 -->
          <el-form v-if="!createOnlineRule" ref="columnRuleForm" class="full-width-input" :rules="rules"
                   :model="columnRuleForm"
                   label-width="100px" size="mini" label-position="right" @submit.native.prevent
          >
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item key="ruleId" label="验证规则:" prop="ruleId">
                  <el-row type="flex" justify="space-between">
                    <el-select v-model="columnRuleForm.ruleId" class="input-item" placeholder="选择验证规则" clearable
                               :disabled="editRule"
                               @change="changeOnlineRule"
                    >
                      <el-option v-for="item in onlineRuleList" :key="item.id" :value="item.id" :label="item.ruleName">
                        <el-row type="flex" justify="end" align="middle" class="rule-item">
                          <span style="width: 100%;">{{ item.ruleName }}</span>
                          <el-button v-if="item.ruleType === OnlineRuleType.CUSTOM" class="table-btn success" type="text"
                                     icon="el-icon-edit-outline"
                                     style="margin-left: 30px;" @click.stop="editOnlineRule(item)"
                          />
                          <el-button v-if="item.ruleType === OnlineRuleType.CUSTOM" class="table-btn delete" type="text"
                                     icon="el-icon-circle-close"
                                     @click.stop="deleteOnlineRule(item)"
                          />
                        </el-row>
                      </el-option>
                    </el-select>
                    <el-button v-if="!editRule" type="warning" style="margin-left: 3px;" @click="addOnlineRule">
                      新建规则
                    </el-button>
                  </el-row>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item key="message" label="错误信息:" prop="message">
                  <el-input v-model="columnRuleForm.message" placeholder="输入校验错误信息"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="最小值:">
                  <el-input-number v-model="columnRuleForm.min" style="width: 100%" placeholder="输入最小值"
                                   :disabled="!columnRuleForm || columnRuleForm.ruleType !== OnlineRuleType.RANGE"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item label="最大值:">
                  <el-input-number v-model="columnRuleForm.max" style="width: 100%" placeholder="输入最大值"
                                   :disabled="!columnRuleForm || columnRuleForm.ruleType !== OnlineRuleType.RANGE"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <!--新建验证规则-->
          <el-form v-if="createOnlineRule" ref="onlineRuleForm" class="full-width-input" :rules="rules"
                   :model="onlineRuleForm"
                   label-width="100px" size="mini" label-position="right" @submit.native.prevent
          >
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="规则类型:" prop="ruleType">
                  <el-input value="自定义规则" disabled/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item key="ruleName" label="规则名称:" prop="ruleName">
                  <el-input v-model="onlineRuleForm.ruleName" placeholder="规则名称"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item key="pattern" label="正则表达式:" prop="pattern">
                  <el-input v-model="onlineRuleForm.pattern" type="textarea" placeholder="规则正则表达式"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitOnlineRule">确 定</el-button>
            <el-button @click="cancelOnlineRule">取 消</el-button>
          </div>
        </el-dialog>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
  import { selectTableColumnList } from '@/api/system/jdbc'
  import { selectDictList } from '@/api/online/dict'
  import { selectOnlineColumnList, updateOnlineColumn, deleteOnlineColumn } from '@/api/online/column'
  import { addOnlineColumn, refreshOnlineColumn } from '@/api/online/table'
  import { selectRuleList, addOnlineRule, updateOnlineRule, deleteOnlineRule } from '@/api/online/rule'
  import { selectColumnRuleList, addColumnRule, updateColumnRule, deleteColumnRule } from '@/api/online/column_rule'
  import { findItemFromList } from '../util'

  export default {
    name: 'TableColumn',
    props: {
      formId: {    // 表单ID
        type: String,
        required: true
      },
      tableId: {    // 数据表ID
        type: String,
        required: true
      },
      tableName: {    // 数据表名称
        type: String,
        required: true
      },
      tableLabel: {    // 数据表描述
        type: String,
        required: true
      }
    },
    data() {
      return {
        // 在线表单数据表字段
        onlineColumnList: [],
        // 在线字典列表
        onlineDictList: [],
        // 数据库表字段列表
        jdbcTableColumnList: [],
        // 选中字段信息
        onlineColumn: undefined,
        onlineRuleList: [],
        columnRuleList: [],
        columnRuleTitle: '新增字段验证规则',
        columnRuleOpen: false,
        columnRuleForm: {},
        rules: {
          ruleId: [
            { required: true, message: '请选择验证规则', trigger: 'blur' }
          ],
          message: [
            { required: true, message: '请输入校验错误信息', trigger: 'blur' }
          ],
          ruleName: [
            { required: true, message: '规则名称不能为空', trigger: 'blur' }
          ],
          pattern: [
            { required: true, message: '正则表达式不能为空', trigger: 'blur' }
          ]
        },
        editRule: false,
        createOnlineRule: false,
        onlineRuleForm: {}
      }
    },
    computed: {
      /** 获取在线表单数据表字段 */
      getOnlineTableColumnList() {
        let onlineColumnList = []
        if (this.onlineColumnList != null) {
          this.onlineColumnList.forEach((onlineColumn) => {
            if (this.jdbcTableColumnList != null) {
              const jdbcTableColumn = this.jdbcTableColumnList.find(item => item.columnName == onlineColumn.columnName)
              onlineColumn.deletedFlag = (jdbcTableColumn == null)
            } else {
              onlineColumn.deletedFlag = false
            }
            onlineColumnList.push(onlineColumn)
          })
        }
        return onlineColumnList
      },
      /** 获取新增数据表字段 */
      getAddOnlineColumnList() {
        let addOnlineColumnList = []
        this.jdbcTableColumnList.map(item => {
          const onlineColumn = this.onlineColumnList?.find(onlineColumn => onlineColumn.columnName == item.columnName)
          if (onlineColumn == null) {
            addOnlineColumnList.push(item)
          }
        })
        return addOnlineColumnList
      }
    },
    created() {
      this.getOnlineColumnList()
      this.getJdbcTableColumnList()
      this.getOnlineDictList()
      this.getOnlineRuleList()
    },
    methods: {
      /** 获取在线表单数据表字段列表 */
      getOnlineColumnList() {
        selectOnlineColumnList({ formId: this.formId, tableId: this.tableId }).then(response => {
            this.onlineColumnList = response.data
          }
        )
      },
      /** 获取数据库表字段列表 */
      getJdbcTableColumnList() {
        selectTableColumnList(this.tableName).then(response => {
          this.jdbcTableColumnList = response.data
        })
      },
      /** 获取字典列表 */
      getOnlineDictList() {
        selectDictList().then(response => {
            this.onlineDictList = response.data
          }
        )
      },
      getOnlineRuleList() {
        return selectRuleList().then(response => {
            this.onlineRuleList = response.data
          }
        )
      },
      getColumnRuleList(columnId) {
        return selectColumnRuleList(columnId).then(response => {
            this.columnRuleList = response.data
          }
        )
      },
      /** 字段选中 */
      handleOnlineColumn(onlineColumn) {
        this.onlineColumn = onlineColumn
        this.columnRuleList = undefined
        this.getColumnRuleList(onlineColumn.id)
      },
      /** 处理字段分类 */
      disabledFieldKind(item) {
        switch(item.value){
          case this.OnlineFieldKind.UPLOAD_FILE:
          case this.OnlineFieldKind.UPLOAD_IMAGE:
          case this.OnlineFieldKind.RICH_TEXT:
          case this.OnlineFieldKind.CREATE_USER:
          case this.OnlineFieldKind.UPDATE_USER:
            return this.onlineColumn.fieldType !== 'String'
          case this.OnlineFieldKind.CREATE_TIME:
          case this.OnlineFieldKind.UPDATE_TIME:
            return this.onlineColumn.fieldType !== 'Date'
          case this.OnlineFieldKind.LOGIC_DELETE:
            return this.onlineColumn.fieldType !== 'Integer' && this.onlineColumn.fieldType !== 'String'
          default:
            return false
        }
      },
      /** 新增字段 */
      addNewOnlineColumn(newOnlineColumn) {
        const onlineColumn = {
          formId: this.formId,
          tableId: this.tableId,
          columnName: newOnlineColumn.columnName
        }
        addOnlineColumn(onlineColumn).then(response => {
          this.messageSuccess('表字段添加成功！')
          this.getOnlineColumnList()
        })
      },
      /** 删除字段 */
      deleteOnlineColumn(onlineColumn) {
        this.$confirm('是否确认删除字段为"' + onlineColumn.columnName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteOnlineColumn(onlineColumn.id)
        }).then(() => {
          this.getOnlineColumnList()
          this.messageSuccess('删除成功')
        })
      },
      /** 刷新字段 */
      refreshOnlineColumn(onlineColumn, newOnlineColumn) {
        onlineColumn.columnName = newOnlineColumn.columnName
        refreshOnlineColumn(onlineColumn).then(response => {
          this.messageSuccess('表字段刷新成功！')
          this.onlineColumn = response.data
          this.getOnlineColumnList()
        })
      },
      /** 刷新新增字段 */
      refreshNewOnlineColumn(command) {
        this.refreshOnlineColumn(command.column, command.newColumn)
      },
      /** 新增字段验证规则 */
      addColumnRule() {
        this.columnRuleOpen = true
        this.createOnlineRule = false
        this.columnRuleForm = {
          ruleId: undefined,
          ruleType: undefined,
          ruleName: undefined,
          message: undefined,
          min: undefined,
          max: undefined
        }
      },
      /** 编辑字段验证规则 */
      editColumnRule(row) {
        const ruleConfig = JSON.parse(row.ruleConfig)
        this.columnRuleForm = {
          id: row.id,
          formId: row.formId,
          tableId: row.tableId,
          columnId: row.columnId,
          ruleId: row.ruleId,
          ruleType: row.ruleType,
          ruleName: row.ruleName,
          message: ruleConfig?.message,
          min: ruleConfig?.min,
          max: ruleConfig?.max
        }
        this.columnRuleOpen = true
        this.createOnlineRule = false
      },
      /** 删除字段验证规则 */
      deleteColumnRule(row) {
        this.$confirm('是否确认删除规则配置名称为"' + row.ruleName + '"的配置项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteColumnRule(row.id)
        }).then(() => {
          this.getColumnRuleList(row.columnId)
          this.messageSuccess('删除成功')
        })
      },
      /** 添加自定义规则 */
      addOnlineRule() {
        this.createOnlineRule = true
        this.onlineRuleForm = {}
      },
      /** 规则 */
      changeOnlineRule() {
        this.columnRuleForm.message = undefined
        this.columnRuleForm.min = undefined
        this.columnRuleForm.max = undefined
        const onlineRuleForm = findItemFromList(this.onlineRuleList, this.columnRuleForm.ruleId, 'id')
        this.columnRuleForm.ruleType = onlineRuleForm?.ruleType
        this.columnRuleForm.ruleName = onlineRuleForm?.ruleName
      },
      /** 规则信息 */
      formatRuleMessage(row, column) {
        return JSON.parse(row.ruleConfig)?.message
      },
      /** 编辑规则 */
      editOnlineRule(item) {
        this.createOnlineRule = true
        this.onlineRuleForm = item
      },
      /** 删除规则 */
      deleteOnlineRule(item) {
        this.$confirm('是否确认删除规则配置名称为"' + item.ruleName + '"的配置项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteOnlineRule(item.id)
        }).then(() => {
          this.getOnlineRuleList()
          this.messageSuccess('删除成功')
        })
      },
      /** 提交规则 */
      submitOnlineRule() {
        if (this.createOnlineRule) {
          this.$refs.onlineRuleForm.validate(valid => {
            if (valid) {
              this.onlineRuleForm.ruleType = this.OnlineRuleType.CUSTOM
              if (this.onlineRuleForm.id != undefined) {
                updateOnlineRule(this.onlineRuleForm).then(response => {
                  this.messageSuccess('规则配置修改成功')
                  this.getOnlineRuleList()
                  this.createOnlineRule = false
                })
              } else {
                addOnlineRule(this.onlineRuleForm).then(response => {
                  this.messageSuccess('新增规则配置成功')
                  this.getOnlineRuleList()
                  this.createOnlineRule = false
                })
              }
            }
          })

        } else {
          this.$refs.columnRuleForm.validate(valid => {
            if (valid) {
              let columnRule = {
                formId: this.onlineColumn.formId,
                tableId: this.onlineColumn.tableId,
                columnId: this.onlineColumn.id,
                ruleId: this.columnRuleForm.ruleId,
                ruleConfig: JSON.stringify({
                  message: this.columnRuleForm.message,
                  min: this.columnRuleForm.min,
                  max: this.columnRuleForm.max
                })
              }
              if (this.columnRuleForm.id != undefined) {
                columnRule.id = this.columnRuleForm.id
                updateColumnRule(columnRule).then(response => {
                  this.messageSuccess('规则配置修改成功')
                  this.getColumnRuleList(columnRule.columnId)
                  this.columnRuleOpen = false
                })
              } else {
                addColumnRule(columnRule).then(response => {
                  this.messageSuccess('新增规则配置成功')
                  this.getColumnRuleList(columnRule.columnId)
                  this.columnRuleOpen = false
                })
              }
            }
          })
        }
      },
      /** 取消规则配置修改 */
      cancelOnlineRule() {
        if (this.createOnlineRule) {
          this.createOnlineRule = false
        } else {
          this.columnRuleOpen = false
        }
      },
      /** 保存字段信息 */
      saveOnlineColumn() {
        this.$refs.onlineColumnForm.validate(valid => {
          if (valid) {
            updateOnlineColumn(this.onlineColumn).then(response => {
              this.messageSuccess('表字段信息保存成功！')
            })
          }
        })
      },
      /** 返回 */
      handleBack() {
        this.$emit('close')
      }
    }
  }
</script>
<style lang='scss'>
  .table-column {
    height: 100%;

    .table-column-list {
      height: 100%;
      overflow: hidden;
      flex-shrink: 0;
      background: white;

      .title {
        font-size: 14px;
        color: #043254;
        line-height: 30px;
        height: 40px;
        padding-left: 10px;
        margin-bottom: 10px;
        font-weight: 700;
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-bottom: 2px dashed #dcdfe6;

        .el-button {
          padding-right: 10px;
        }
      }

      .table-column-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-left: 10px;
        margin-right: 10px;
        margin-bottom: 8px;
        padding-right: 10px;
        font-size: 12px;
        color: #043254;
        cursor: pointer;
        height: 35px;
        line-height: 35px;
        background: #f4f7fa;
        border: 1px dashed #f3f9ff;
      }

      .table-column-item:hover {
        background: #ecf4fc;
      }

      .table-column-item.active-column {
        background: #bef1ba;
        border: 1px dashed #82b6ea;
      }

      .table-column-item .refresh {
        display: none;
      }

      .table-column-item:hover .refresh {
        display: block;
      }
    }

    .table-column-info {
      border-right: 8px solid #EBEEF5;
      border-left: 8px solid #EBEEF5;

      .title {
        font-size: 14px;
        color: #043254;
        height: 40px;
        line-height: 40px;
        padding-left: 10px;
        font-weight: 700;
        display: flex;
        justify-content: space-between;
        border-bottom: 2px dashed #dcdfe6;

        .el-button {
          margin-top: 5px;
          margin-right: 20px;
        }
      }
    }

    .table-column-rules {
      background: white;

      .title {
        font-size: 14px;
        color: #043254;
        height: 40px;
        line-height: 40px;
        padding-left: 10px;
        font-weight: 700;
        display: flex;
        margin-bottom: 0px;
        justify-content: space-between;
        border-bottom: 2px dashed #dcdfe6;
      }
    }
  }
</style>
