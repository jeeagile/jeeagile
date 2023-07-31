<template>
  <div class="table-column">
    <el-container style="height: 100%;">
      <el-aside class="table-column-list" style="width: 250px">
        <div class="title">
          <span v-bind:title="tableLabel" style="width: 200px">{{tableName}}</span>
          <el-dropdown trigger="click" @command="addNewOnlineColumn">
            <el-button class="table-btn success" size="mini" type="text"
                       icon="el-icon-circle-plus-outline" :disabled="getAddOnlineColumnList.length <= 0"
                       title="新增"/>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="onlineColumn in getAddOnlineColumnList" :key="onlineColumn.columnName"
                                :command="onlineColumn">
                {{onlineColumn.columnName}}
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
              <span style="margin-right: 10px;">{{column.columnName}}</span>
              <el-tag v-if="column.deletedFlag" size="mini" type="danger">已删除</el-tag>
            </div>
            <div class="refresh" style="margin-left: 10px;">
              <el-button class="table-btn success" size="mini" type="text" v-if="getAddOnlineColumnList.length <= 0"
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
                                    :command="{column, newColumn}">
                    {{newColumn.columnName}}
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
                 label-width="120px" label-position="right" @submit.native.prevent size="small">
          <template v-if="onlineColumn!=null">
            <el-col>
              <el-form-item label="字段名：">
                <span :title="onlineColumn.columnComment">{{onlineColumn.columnName}}</span>
                <el-tag size="mini" type="warning" v-if="onlineColumn.primaryFlag==SysYesNo.YES"
                        style="margin-left: 20px;">主键
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="字段类型：">
                <span>{{onlineColumn.columnType}}</span>
                <el-tag size="mini" type="success" effect="dark" style="margin-left: 10px;">{{onlineColumn.fieldType}}
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="过滤支持：">
                <el-radio-group v-model="onlineColumn.filterType">
                  <el-radio-button v-for="item in OnlineFilterType.getList()"
                                   :label="item.value" :value="item.value" :key="item.value">
                    {{item.label}}
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
                           clearable filterable :disabled="onlineColumn.fieldType === 'Boolean'">
                  <el-option v-for="item in onlineDictList" :key="item.id" :value="item.id" :label="item.dictName"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="attribute-item">
              <el-form-item label="字段分类：">
                <el-select v-model="onlineColumn.fieldClassify" clearable placeholder="字段业务分类" style="width: 250px;">
                  <el-option v-for="item in OnlineFieldClassify.getList()" :key="item.value"
                             :label="item.label" :value="item.value" :disabled="disabledFieldClassify(item)"
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
        <el-table size="mini" :data="onlineColumnRuleList" :show-header="false" empty-text="请添加验证规则">
          <el-table-column label="规则名称" prop="ruleName" :show-overflow-tooltip="true"/>
          <el-table-column label="提示信息" prop="message" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" width="100px">
            <template slot-scope="scope">
              <el-button class="table-btn success" type="text" @click="editOnlineColumnRule(scope.row)">编辑</el-button>
              <el-button class="table-btn delete" type="text" @click="deleteOnlineColumnRule(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button style="width: 100%; margin-top: 10px; border: 1px dashed #EBEEF5;"
                   :disabled="onlineColumn == null" icon="el-icon-plus" @click="addOnlineColumnRule">
          添加验证规则
        </el-button>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
  import { selectTableColumnList } from '@/api/system/jdbc'
  import { selectDictList } from '@/api/online/dict'
  import { selectOnlineColumnList, updateOnlineColumn, deleteOnlineColumn } from '@/api/online/column'
  import { addOnlineColumn, refreshOnlineColumn } from '@/api/online/table'


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
        // 字段校验规则列表
        onlineColumnRuleList: []
      }
    },
    created() {
      this.getOnlineColumnList()
      this.getJdbcTableColumnList()
      this.getOnlineDictList()
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
      /** 字段选中 */
      handleOnlineColumn(onlineColumn) {
        this.onlineColumn = onlineColumn
      },
      /** 处理字段分类 */
      disabledFieldClassify(item) {
        switch (item.value) {
        case this.OnlineFieldClassify.UPLOAD_FILE:
        case this.OnlineFieldClassify.UPLOAD_IMAGE:
        case this.OnlineFieldClassify.RICH_TEXT:
        case this.OnlineFieldClassify.CREATE_USER:
        case this.OnlineFieldClassify.UPDATE_USER:
          return this.onlineColumn.fieldType !== 'String'
        case this.OnlineFieldClassify.CREATE_TIME:
        case this.OnlineFieldClassify.UPDATE_TIME:
          return this.onlineColumn.fieldType !== 'Date'
        case this.OnlineFieldClassify.LOGIC_DELETE:
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
          this.getOnlineColumnList()
        })
      },
      /** 刷新新增字段 */
      refreshNewOnlineColumn(command) {
        this.refreshOnlineColumn(command.column, command.newColumn)
      },
      /** 新增字段验证规则 */
      addOnlineColumnRule() {

      },
      /** 编辑字段验证规则 */
      editOnlineColumnRule() {

      },
      /** 删除字段验证规则 */
      deleteOnlineColumnRule() {

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
