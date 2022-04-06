<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="字典名称" prop="dictName">
        <el-input v-model="queryParam.queryCond.dictName" placeholder="请输入字典名称" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="字典类型" prop="dictType">
        <el-input v-model="queryParam.queryCond.dictType" placeholder="请输入字典类型" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParam.queryCond.dictStatus" placeholder="字典状态" clearable size="small"
                   style="width: 240px">
          <el-option v-for="dictStatusOption in dictStatusOptionList" :key="dictStatusOption.dictValue"
                     :label="dictStatusOption.dictLabel" :value="dictStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd"
                   v-hasPerm="['system:dict:type:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:dict:type:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPerm="['system:dict:type:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['system:dict:type:export']" type="warning" icon="el-icon-download" size="mini"
                   @click="handleExport"
        >
          导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-refresh" size="mini" @click="handleClearCache"
                   v-hasPerm="['system:dict:type:clear']">
          清理缓存
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getDictTypeList"/>
    </el-row>

    <el-table v-loading="loading" :data="dictTypeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="字典名称" align="center" prop="dictName" :show-overflow-tooltip="true"/>
      <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <router-link :to="'/dict/type/data/' + scope.row.id" class="link-type">
            <span>{{ scope.row.dictType }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="dictStatus" :formatter="dictStatusFormat"/>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:dict:type:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:dict:type:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getDictTypeList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName" placeholder="请输入字典名称"/>
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="form.dictType" placeholder="请输入字典类型"/>
        </el-form-item>
        <el-form-item label="状态" prop="dictStatus">
          <el-radio-group v-model="form.dictStatus">
            <el-radio v-for="dictStatusOption in dictStatusOptionList" :key="dictStatusOption.dictValue"
                      :label="dictStatusOption.dictValue">
              {{ dictStatusOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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
  import {
    selectDictTypePage,
    detailDictType,
    addDictType,
    updateDictType,
    deleteDictType,
    clearDictTypeCache,
    exportDictType
  } from '@/api/system/dictType'

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
        dictTypeList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        dictStatusOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            dictName: undefined,
            dictType: undefined,
            dictStatus: undefined,
            excelName: '字段类型'
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          dictName: [
            { required: true, message: '字典名称不能为空', trigger: 'blur' }
          ],
          dictType: [
            { required: true, message: '字典类型不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getDictTypeList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.dictStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询字典类型列表 */
      getDictTypeList() {
        this.loading = true
        selectDictTypePage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.dictTypeList = response.data.recordList
            this.loading = false
          }
        )
      },
      /**  字典状态字典翻译  */
      dictStatusFormat(row, column) {
        return this.handleDictLabel(this.dictStatusOptionList, row.dictStatus)
      },
      /** 取消按钮  */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /**  表单重置  */
      reset() {
        this.form = {
          dictId: undefined,
          dictName: undefined,
          dictType: undefined,
          status: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDictTypeList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加字典类型'
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
        detailDictType(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改字典类型'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDictType(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getDictTypeList()
              })
            } else {
              addDictType(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getDictTypeList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除字典编号为"' + row.dictName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDictType(row.id)
        }).then(() => {
          this.getDictTypeList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$confirm('请确认是否导出字段类型数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportDictType(this.queryParam.queryCond)
        })
      },
      /** 清理缓存按钮操作 */
      handleClearCache() {
        clearDictTypeCache().then(response => {
          this.messageSuccess('清理成功')
        })
      }
    }
  }
</script>
