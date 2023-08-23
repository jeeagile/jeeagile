<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="queryParam.queryCond.dictName" placeholder="请输入字典名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="字典类型" prop="dictType">
        <el-select v-model="queryParam.queryCond.dictType" placeholder="字典类型" clearable size="small">
          <el-option v-for="dictTypeOption in OnlineDictType.getList()" :key="dictTypeOption.key"
                     :label="dictTypeOption.label"
                     :value="dictTypeOption.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['online:dict:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['online:dict:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['online:dict:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getDictList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dictList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="字典名称" align="center" prop="dictName"/>
      <el-table-column label="字典类型" align="center" prop="dictType">
        <template slot-scope="scope">
          <el-tag size="mini" :type="OnlineDictType.getTag(scope.row.dictType)">
            {{OnlineDictType.getLabel(scope.row.dictType)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否树字典" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.treeFlag ? 'success' : 'danger'">
            {{AgileYesNo.getLabel(scope.row.treeFlag)}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['online:dict:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['online:dict:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDictList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="字典名称" prop="dictName">
              <el-input v-model="form.dictName" placeholder="请输入字典名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字典类型" prop="dictType">
              <el-select v-model="form.dictType" placeholder="字典类型" clearable @change="handleDictType">
                <el-option v-for="dictTypeOption in OnlineDictType.getList()" :key="dictTypeOption.key"
                           :label="dictTypeOption.label"
                           :value="dictTypeOption.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <template v-if="form.dictType===OnlineDictType.TABLE">
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典数据表" prop="tableName">
                <el-select v-model="form.tableName" placeholder="请选择字典表名称"
                           @change="handleJdbcTable">
                  <el-option
                    v-for="tableOption in jdbcTableList"
                    :key="tableOption.tableName"
                    :label="`${tableOption.tableComment}|${tableOption.tableName}`"
                    :value="tableOption.tableName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="字典键字段" prop="keyColumnName">
                <el-select v-model="form.keyColumnName" placeholder="请选择字典表键字段名称">
                  <el-option
                    v-for="tableColumnOption in jdbcTableColumnList"
                    :key="tableColumnOption.columnName"
                    :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                    :value="tableColumnOption.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="字典值字段" prop="valueColumnName">
                <el-select v-model="form.valueColumnName" placeholder="请选择字典值字段名称">
                  <el-option
                    v-for="tableColumnOption in jdbcTableColumnList"
                    :key="tableColumnOption.columnName"
                    :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                    :value="tableColumnOption.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="字典标签字段" prop="labelColumnName">
                <el-select v-model="form.labelColumnName" placeholder="请选择字典标签字段名称">
                  <el-option
                    v-for="tableColumnOption in jdbcTableColumnList"
                    :key="tableColumnOption.columnName"
                    :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                    :value="tableColumnOption.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="树状字典">
                <el-radio-group v-model="form.treeFlag">
                  <el-radio
                    v-for="option in AgileYesNo.getList()"
                    :key="option.value"
                    :label="option.value"
                  >{{option.label}}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="字典父字段" prop="parentKeyColumnName" v-if="form.treeFlag=='1'">
                <el-select v-model="form.parentKeyColumnName" placeholder="请选择字典表父字段名称">
                  <el-option
                    v-for="tableColumnOption in jdbcTableColumnList"
                    :key="tableColumnOption.columnName"
                    :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                    :value="tableColumnOption.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="字典参数" prop="dictParamJson">
                <el-select v-model="dictParamList" placeholder="请添加字典获取参数" multiple filterable
                           allow-create default-first-option>
                  <el-option
                    v-for="tableColumnOption in jdbcTableColumnList"
                    :key="tableColumnOption.columnName"
                    :label="`${tableColumnOption.columnComment}|${tableColumnOption.columnName}`"
                    :value="tableColumnOption.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

        </template>
        <template v-if="form.dictType===OnlineDictType.SYSTEM">
          <el-row>
            <el-col :span="24">
              <el-form-item label="系统字典" prop="systemDictType">
                <el-select v-model="form.systemDictType" size="small" @change="handleDictData">
                  <el-option
                    v-for="dictTypeOption in systemDictTypeList"
                    :key="dictTypeOption.dictId"
                    :label="dictTypeOption.dictName"
                    :value="dictTypeOption.dictType"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <template
          v-if="form.dictType===OnlineDictType.SYSTEM || form.dictType===OnlineDictType.CUSTOM ">
          <el-row style="margin-bottom: 15px">
            <el-col :span="24">
              <el-table
                :data="dictDataList.slice((dictDataPage.currentPage - 1) * dictDataPage.pageSize, dictDataPage.currentPage * dictDataPage.pageSize)"
                size="mini">
                <el-table-column label="字典键值" prop="dictValue"/>
                <el-table-column label="字典标签" prop="dictLabel"/>
                <el-table-column label="显示排序" prop="dictSort" width="80px"/>
                <el-table-column label="操作" width="180px" fixed="right" align="right"
                                 v-if="form.dictType===OnlineDictType.CUSTOM">
                  <template slot="header">
                    <el-row type="flex" justify="end" align="middle">
                      <el-button type="primary" icon="el-icon-plus" size="mini" @click="addCustomDictData">新增
                      </el-button>
                    </el-row>
                  </template>
                  <template slot-scope="scope">
                    <el-button type="text" icon="el-icon-edit" size="mini"
                               @click="editCustomDictData(scope.row,scope.$index)">编辑
                    </el-button>
                    <el-button type="text" icon="el-icon-delete" size="mini"
                               @click="deleteCustomDictData(scope.row,scope.$index)">删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <pagination v-show="dictDataList.length>0" :total-page="dictDataList.length"
                          :current-page.sync="dictDataPage.currentPage" :page-size="dictDataPage.pageSize"
                          :limit.sync="dictDataPage.pageSize"/>
            </el-col>
          </el-row>
        </template>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改自定义字典类型对话框 -->
    <el-dialog :title="customDictTitle" :visible.sync="customDictOpen" width="400px" append-to-body>
      <el-form ref="customDictForm" :model="customDictForm" :rules="customDictRules" label-width="100px">
        <el-form-item label="字典键值" prop="dictValue">
          <el-input v-model="customDictForm.dictValue" placeholder="请输入数据键值"/>
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="customDictForm.dictLabel" placeholder="请输入数据标签"/>
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="customDictForm.dictSort" controls-position="right" :min="0" style="width: 100%"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleCustomDictData">确 定</el-button>
        <el-button @click="customDictOpen=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectDictTypeList } from '@/api/system/dictType'
  import { selectJdbcTableList, selectTableColumnList } from '@/api/system/jdbc'
  import { selectDictPage, detailDict, deleteDict, addDict, updateDict } from '@/api/online/dict'

  export default {
    name: 'Dict',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 已选择的列表
        selectRowList: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 字典表格数据
        dictList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 类型数据字典
        flagOptionList: [],
        // 查询字典
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            dictName: undefined,
            dictType: undefined
          }
        },
        // 系统字典列表
        systemDictTypeList: [],
        // 字典数据
        dictDataList: [],
        // 字典参数
        dictParamList: [],
        // 数据库表
        jdbcTableList: [],
        // 数据库表字段
        jdbcTableColumnList: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dictName: [
            { required: true, message: '字典名称不能为空', trigger: 'blur' }
          ],
          dictType: [
            { required: true, message: '字典类型不能为空', trigger: 'blur' }
          ],
          systemDictType: [
            { required: true, message: '系统字典不能为空', trigger: 'blur' }
          ],
          tableName: [
            { required: true, message: '字典表不能为空', trigger: 'blur' }
          ],
          parentKeyColumnName: [
            { required: true, message: '字典父键不能为空', trigger: 'blur' }
          ],
          keyColumnName: [
            { required: true, message: '字典键不能为空', trigger: 'blur' }
          ],
          valueColumnName: [
            { required: true, message: '字典值不能为空', trigger: 'blur' }
          ],
          labelColumnName: [
            { required: true, message: '字典标签不能为空', trigger: 'blur' }
          ]
        },
        dictDataPage: {
          pageSize: 5,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 3    // 最大显示页数
        },
        dictDataIndex: -1,
        customDictTitle: '添加自定义字典',
        customDictOpen: false,
        customDictForm: {},
        customDictRules: {
          dictValue: [
            { required: true, message: '字典值不能为空', trigger: 'blur' }
          ],
          dictLabel: [
            { required: true, message: '字典标签不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getDictList()
      this.getSystemDictTypeList()
      this.getJdbcTableList()
    },
    methods: {
      /** 查询参数列表 */
      getDictList() {
        this.loading = true
        selectDictPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.dictList = response.data.records
            this.loading = false
          }
        )
      },
      // 获取系统字典类型列表
      getSystemDictTypeList() {
        selectDictTypeList().then(response => {
          this.systemDictTypeList = response.data
        })
      },
      // 获取数据库表列表
      getJdbcTableList() {
        selectJdbcTableList().then(response => {
          this.jdbcTableList = response.data
        })
      },
      // 取消按钮
      cancel() {
        this.openDialog = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          dictName: undefined,
          dictType: this.OnlineDictType.TABLE,
          systemDictType: undefined,
          tableName: undefined,
          treeFlag: this.AgileYesNo.NO,
          parentKeyColumnName: undefined,
          keyColumnName: undefined,
          valueColumnName: undefined,
          labelColumnName: undefined,
          dictDataJson: undefined,
          dictParamJson: undefined,
          remark: undefined
        }
        this.dictParamList = []
        this.dictDataList = []
        this.jdbcTableColumnList = []
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDictList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      resetDictDataPage() {
        this.dictDataPage = {
          pageSize: 5,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 3    // 最大显示页数
        }
      },
      handleJdbcTable() {
        selectTableColumnList(this.form.tableName).then(response => {
          this.jdbcTableColumnList = response.data
        })
      },
      handleDictType() {
        this.resetDictDataPage()
        this.form.systemDictType = undefined
        this.form.parentKeyColumnName = undefined
        this.form.keyColumnName = undefined
        this.form.valueColumnName = undefined
        this.form.labelColumnName = undefined
        this.form.dictParamJson = undefined
        this.dictParamList = []
        this.dictDataList = []
        this.jdbcTableColumnList = []
      },
      handleDictData() {
        this.resetDictDataPage()
        if (this.form.dictType === this.OnlineDictType.TABLE) {
          this.handleJdbcTable()
          this.dictDataList = []
          this.dictParamList = []
          if (this.form.dictParamJson) {
            JSON.parse(this.form.dictParamJson)?.map(item => this.dictParamList.push(item.dictParamName))
          }
          return
        }
        if (this.form.dictType === this.OnlineDictType.SYSTEM) {
          this.dictDataList = []
          this.getSysDictDataList(this.form.systemDictType).then(response => {
            this.dictDataList = response.data
          })
          return
        }
        if (this.form.dictType === this.OnlineDictType.CUSTOM) {
          this.dictDataList = []
          if (this.form.dictDataJson) {
            this.dictDataList = JSON.parse(this.form.dictDataJson)
            this.dictDataList.forEach(item => item.dictKey = item.dictValue)
          }
          return
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加字典'
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDict(row.id).then(response => {
          this.form = response.data
          this.handleDictData()
          this.openDialog = true
          this.dialogTitle = '修改字典'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        if (this.form.dictType === this.OnlineDictType.CUSTOM) {
          this.form.dictDataJson = JSON.stringify(this.dictDataList)
        } else {
          this.form.dictDataJson = undefined
        }
        if (this.form.dictType === this.OnlineDictType.TABLE) {
          const dictParamList = []
          this.dictParamList.map(item => {
            const tableColumn = this.jdbcTableColumnList.find(column => column.columnName == item)
            dictParamList.push({
              dictParamName: tableColumn.columnName,
              dictParamLabel: tableColumn.columnComment,
              dictParamType: tableColumn.javaType
            })
          })
          this.form.dictParamJson = JSON.stringify(dictParamList)
        }
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDict(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getDictList()
              })
            } else {
              addDict(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getDictList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除字典名称为"' + row.dictName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDict(row.id)
        }).then(() => {
          this.getDictList()
          this.messageSuccess('删除成功')
        })
      },
      addCustomDictData() {
        this.customDictOpen = true
        this.dictDataIndex = -1
        this.customDictForm = {
          dictKey: undefined,
          dictValue: undefined,
          dictLabel: undefined,
          dictSort: this.dictDataList?.length + 1
        }
      },
      editCustomDictData(row, index) {
        this.customDictOpen = true
        this.dictDataIndex = (this.dictDataPage.currentPage - 1) * this.dictDataPage.pageSize + index
        this.customDictForm = { ...row }
      },
      deleteCustomDictData(row, index) {
        this.dictDataList.splice((this.dictDataPage.currentPage - 1) * this.dictDataPage.pageSize + index, 1)
      },
      handleCustomDictData() {
        const dictData = this.dictDataList.find(item => item.dictValue == this.customDictForm.dictValue && item.id !== this.customDictForm.id)
        if (dictData) {
          this.messageWarning('字典值已存在！')
          return
        }
        this.$refs.customDictForm.validate(valid => {
          if (!valid) return
          this.customDictForm.dictKey = this.customDictForm.dictValue
          if (this.dictDataIndex === -1) {
            this.dictDataList.push(this.customDictForm)
          } else {
            this.dictDataList.splice(this.dictDataIndex, 1, this.customDictForm)
          }
          this.dictDataList.sort((a, b) => a.dictSort - b.dictSort)
          this.customDictOpen = false
        })
      }
    }
  }
</script>
