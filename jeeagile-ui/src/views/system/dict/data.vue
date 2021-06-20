<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="字典名称" prop="dictType">
        <el-select v-model="queryParam.queryCond.dictType" size="small">
          <el-option v-for="dictTypeOption in dictTypeOptionList" :key="dictTypeOption.dictId" :label="dictTypeOption.dictName" :value="dictTypeOption.dictType"/>
        </el-select>
      </el-form-item>
      <el-form-item label="字典标签" prop="dictLabel">
        <el-input v-model="queryParam.queryCond.dictLabel" placeholder="请输入字典标签" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="dictStatus">
        <el-select v-model="queryParam.queryCond.dictStatus" placeholder="数据状态" clearable size="small">
          <el-option v-for="dictStatusOption in dictStatusOptionList" :key="dictStatusOption.dictValue" :label="dictStatusOption.dictLabel" :value="dictStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:dict:data:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPerm="['system:dict:data:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPerm="['system:dict:data:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getDictDataList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dictDataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="字典标签" align="center" prop="dictLabel"/>
      <el-table-column label="字典键值" align="center" prop="dictValue"/>
      <el-table-column label="字典排序" align="center" prop="dictSort"/>
      <el-table-column label="状态" align="center" prop="dictStatus" :formatter="dictStatusFormat"/>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPerm="['system:dict:data:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPerm="['system:dict:data:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getDictDataList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典类型">
          <el-input v-model="form.dictType" :disabled="true"/>
        </el-form-item>
        <el-form-item label="数据标签" prop="dictLabel">
          <el-input v-model="form.dictLabel" placeholder="请输入数据标签"/>
        </el-form-item>
        <el-form-item label="数据键值" prop="dictValue">
          <el-input v-model="form.dictValue" placeholder="请输入数据键值"/>
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="form.dictSort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="状态" prop="dictStatus">
          <el-radio-group v-model="form.dictStatus">
            <el-radio v-for="dictStatusOption in dictStatusOptionList" :key="dictStatusOption.dictValue" :label="dictStatusOption.dictValue">
              {{ dictStatusOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
  import { selectDictDataPage, addDictData, detailDictData, deleteDictData, updateDictData } from '@/api/system/dictData'
  import { selectDictTypeList, detailDictType } from '@/api/system/dictType'

  export default {
    name: 'Data',
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
        // 总条数
        total: 0,
        // 字典数据
        dictDataList: [],
        // 默认字典类型
        defaultDictType: '',
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        dictStatusOptionList: [],
        // 类型数据字典
        dictTypeOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            dictName: undefined,
            dictType: undefined,
            dictStatus: undefined
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dictLabel: [
            { required: true, message: '数据标签不能为空', trigger: 'blur' }
          ],
          dictValue: [
            { required: true, message: '数据键值不能为空', trigger: 'blur' }
          ],
          dictSort: [
            { required: true, message: '数据顺序不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      const dictTypeId = this.$route.params && this.$route.params.dictTypeId
      this.detailDictType(dictTypeId)
      this.getDictTypeList()
      this.getDictDataByDictType('sys_normal_disable').then(response => {
        this.dictStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询字典类型详细 */
      detailDictType(dictTypeId) {
        detailDictType({ dictTypeId: dictTypeId }).then(response => {
          this.queryParam.queryCond.dictType = response.data.dictType
          this.defaultDictType = response.data.dictType
          this.getDictDataList()
        })
      },
      /** 查询字典类型列表 */
      getDictTypeList() {
        selectDictTypeList().then(response => {
          this.dictTypeOptionList = response.data
        })
      },
      /** 查询字典数据列表 */
      getDictDataList() {
        this.loading = true
        selectDictDataPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.dictDataList = response.data.recordList
          this.loading = false
        })
      },
      /** 数据状态字典翻译 */
      dictStatusFormat(row) {
        return this.handleDictLabel(this.dictStatusOptionList, row.dictStatus)
      },
       /**  取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /**  表单重置 */
      reset() {
        this.form = {
          id: undefined,
          dictLabel: undefined,
          dictValue: undefined,
          dictSort: 0,
          dictStatus: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDictDataList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.queryParam.queryCond.dictType = this.defaultDictType
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加字典数据'
        this.form.dictType = this.queryParam.queryCond.dictType
      },
      /**  多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailDictData({ dictDataId: row.id }).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改字典数据'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDictData(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getDictDataList()
              })
            } else {
              addDictData(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getDictDataList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除字典编码为"' + row.dictLabel + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDictData({ dictDataId: row.id })
        }).then(() => {
          this.getDictDataList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
