<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam.queryCond" :inline="true" label-width="80px">
      <el-form-item label="数据对象" prop="modelId">
        <el-select v-model="queryParam.queryCond.modelId" size="small" @change="changeDroolsModel">
          <el-option v-for="item in modelList" :key="item.id"
                     :label="item.modelLabel" :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="字段名称" prop="fieldName">
        <el-input v-model="queryParam.queryCond.fieldName" placeholder="请输入字段名称" clearable size="small"
                  style="width: 240px"
                  @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="字段标签" prop="fieldLabel">
        <el-input v-model="queryParam.queryCond.fieldLabel" placeholder="请输入字段标签" clearable size="small"
                  style="width: 240px"
                  @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPerm="['drools:model:field:add']" type="primary" icon="el-icon-plus" size="mini"
                   @click="handleAdd"
        >
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['drools:model:field:edit']" type="success" icon="el-icon-edit" size="mini"
                   :disabled="single"
                   @click="handleUpdate"
        >
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['drools:model:field:delete']" type="danger" icon="el-icon-delete" size="mini"
                   :disabled="single"
                   @click="handleDelete"
        >
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleValidate">
          验证
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getDroolsModelFieldList"/>
    </el-row>

    <el-table v-loading="loading" :data="modelFieldList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="字段名称" align="center" prop="fieldName"/>
      <el-table-column label="字段标签" align="center" prop="fieldLabel" :show-overflow-tooltip="true"/>
      <el-table-column label="字段类型" align="center" prop="fieldType" :show-overflow-tooltip="true"/>
      <el-table-column label="是否列表" align="center" prop="listFlag" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          {{ AgileYesNo.getLabel(scope.row.listFlag) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPerm="['drools:model:field:edit']" size="mini" type="text" icon="el-icon-edit"
                     @click="handleUpdate(scope.row)"
          >
            修改
          </el-button>
          <el-button v-hasPerm="['drools:model:field:delete']" size="mini" type="text" icon="el-icon-delete"
                     @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDroolsModelFieldList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="字段名称" prop="fieldName">
          <el-input v-model="form.fieldName" placeholder="请输入字段名称"/>
        </el-form-item>
        <el-form-item label="字段标签" prop="fieldLabel">
          <el-input v-model="form.fieldLabel" placeholder="请输入字段标签"/>
        </el-form-item>
        <el-form-item label="字段类型" prop="fieldType">
          <el-select v-model="form.fieldType" size="small">
            <el-option v-for="item in DroolsFieldType.getList()" :key="item.value"
                       :label="item.label" :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.fieldType===DroolsFieldType.Object" label="数据对象" prop="objectId">
          <el-select v-model="form.objectId" size="small">
            <el-option v-for="item in objectList" :key="item.value"
                       :label="item.label" :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="显示排序" prop="fieldSort">
          <el-input-number v-model="form.fieldSort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否列表" prop="listFlag">
              <el-radio-group v-model="form.listFlag">
                <el-radio v-for="item in AgileYesNo.getList()" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入参标识" prop="inputFlag">
              <el-radio-group v-model="form.inputFlag">
                <el-radio v-for="item in AgileYesNo.getList()" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="字段描述" prop="fieldDesc">
          <el-input v-model="form.fieldDesc" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { detailDroolsModel, selectDroolsModelList, validateDroolsModel } from '@/api/drools/model'
  import {
    selectDroolsModelFieldPage,
    detailDroolsModelField,
    deleteDroolsModelField,
    addDroolsModelField,
    updateDroolsModelField
  } from '@/api/drools/field'

  export default {
    name: 'ModelField',
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
        defaultModelId: undefined,
        // 数据对象列表
        modelList: [],
        modelFieldList: [],
        objectList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            modelId: undefined,
            fieldName: undefined,
            fieldLabel: undefined
          }
        },
        // 数据对象
        modelInfo: {},
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          fieldName: [
            { required: true, message: '字段名称不能为空', trigger: 'blur' },
            { pattern: /[a-zA-Z][\-_.0-9_a-zA-Z$]*/, message: '请以字母开头' }
          ],
          fieldLabel: [
            { required: true, message: '字段标签不能为空', trigger: 'blur' }
          ],
          fieldType: [
            { required: true, message: '字段类型不能为空', trigger: 'change' }
          ],
          objectId: [
            { required: true, message: 'object对象不能为空', trigger: 'change' }
          ]
        }
      }
    },
    created() {
      const modelId = this.$route.params && this.$route.params.modelId
      this.getDroolsModelInfo(modelId)
      this.getDroolsModelList(modelId)
    },
    methods: {
      /** 查询参数列表 */
      getDroolsModelList(modelId) {
        this.loading = true
        if (this.form.fieldType != this.DroolsFieldType.Object) {
          this.form.objectId = undefined
        }
        selectDroolsModelList().then(response => {
            this.queryParam.queryCond.modelId = modelId
            this.defaultModelId = modelId
            this.modelList = response.data
            this.modelList.forEach(item => {
              if (item.id != modelId && item.modelStatus === this.AgileSwitchStatus.ENABLE) {
                this.objectList.push({
                  value: item.id,
                  label: item.modelLabel + '|' + item.modelPackage + '.' + item.modelName
                })
              }
            })
            this.getDroolsModelFieldList()
          }
        )
      },
      getDroolsModelInfo(modelId) {
        detailDroolsModel(modelId).then(response => {
          this.modelInfo = response.data
        })
      },
      getDroolsModelFieldList() {
        this.loading = true
        selectDroolsModelFieldPage(this.queryParam).then(response => {
            this.modelFieldList = response.data.records
            this.queryParam.pageTotal = response.data.pageTotal
            this.loading = false
          }
        )
      },
      changeDroolsModel() {
        this.getDroolsModelInfo(this.queryParam.queryCond.modelId)
        this.getDroolsModelFieldList()
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: undefined,
          modelId: undefined,
          fieldName: undefined,
          fieldLabel: undefined,
          fieldDesc: undefined,
          fieldType: undefined,
          objectId: undefined,
          listFlag: this.AgileYesNo.NO,
          inputFlag: this.AgileYesNo.YES,
          fieldSort: this.total + 1
        }
        this.resetForm('form')
        this.queryParam.queryCond.modelId = this.defaultModelId
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDroolsModelFieldList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加字段'
        this.form.modelId = this.queryParam.queryCond.modelId
        if (this.modelInfo.inputFlag != this.AgileYesNo.YES) {
          this.form.inputFlag = this.AgileYesNo.NO
        }
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDroolsModelField(row.id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改字段'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDroolsModelField(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.open = false
                this.getDroolsModelFieldList()
              })
            } else {
              addDroolsModelField(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.open = false
                this.getDroolsModelFieldList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除字段名称为"' + row.fieldLabel + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDroolsModelField(row.id)
        }).then(() => {
          this.getDroolsModelFieldList()
          this.messageSuccess('删除成功')
        })
      },
      handleValidate() {
        validateDroolsModel(this.queryParam.queryCond.modelId).then(response => {
          this.messageSuccess('验证成功')
        })
      }
    }
  }
</script>
