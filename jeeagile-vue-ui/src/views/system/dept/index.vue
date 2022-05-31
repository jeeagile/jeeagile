<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="部门名称" prop="deptName">
        <el-input v-model="queryParam.deptName" placeholder="请输入部门名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParam.deptStatus" placeholder="部门状态" clearable size="small">
          <el-option v-for="deptStatusOption in deptStatusOptionList" :key="deptStatusOption.dictValue"
                     :label="deptStatusOption.dictLabel" :value="deptStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:dept:add']">
          新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getDeptTreeList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deptTreeList" row-key="id" DEFAULT-EXPAND-ALL
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="deptName" label="部门名称" width="260"></el-table-column>
      <el-table-column prop="deptCode" label="部门编码" width="260"></el-table-column>
      <el-table-column prop="deptSort" label="排序" width="200"></el-table-column>
      <el-table-column prop="deptStatus" label="状态" :formatter="deptStatusFormat" width="100"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     v-hasPerm="['system:dept:add']">
            新增
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:dept:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:dept:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上级部门" prop="parentId">
              <tree-select v-model="form.parentId" :options="deptTreeList" :normalizer="normalizer"
                           placeholder="选择上级部门"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input v-model="form.deptCode" placeholder="请输入部门编码"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="deptSort">
              <el-input-number v-model="form.deptSort" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门状态">
              <el-radio-group v-model="form.deptStatus">
                <el-radio v-for="deptStatusOption in deptStatusOptionList" :key="deptStatusOption.dictValue"
                          :label="deptStatusOption.dictValue">
                  {{ deptStatusOption.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectDeptList, detailDept, addDept, updateDept, deleteDept } from '@/api/system/dept'

  export default {
    name: 'Dept',
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 部门树选项
        deptTreeList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        deptStatusOptionList: [],
        // 查询参数
        queryParam: {
          deptName: undefined,
          deptCode: undefined,
          status: undefined
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          deptName: [
            { required: true, message: '部门名称不能为空', trigger: 'blur' }
          ],
          deptCode: [
            { required: true, message: '部门编码不能为空', trigger: 'blur' }
          ],
          deptSort: [
            { required: true, message: '显示排序不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getDeptTreeList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.deptStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询部门列表 */
      getDeptTreeList() {
        this.loading = true
        selectDeptList(this.queryParam).then(response => {
          this.deptTreeList = this.handleTree(response.data)
          this.loading = false
        })
      },
      /** 转换部门数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.deptName,
          children: node.children
        }
      },
      // 字典状态字典翻译
      deptStatusFormat(row, column) {
        return this.handleDictLabel(this.deptStatusOptionList, row.deptStatus)
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
          parentId: undefined,
          deptName: undefined,
          deptSort: 0,
          deptStatus: '0'
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getDeptTreeList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        if (row != undefined) {
          this.form.parentId = row.id
        }
        this.openDialog = true
        this.dialogTitle = '添加部门'
        selectDeptList().then(response => {
          this.deptTreeList = this.handleTree(response.data)
        })
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        detailDept(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改部门'
        })
        //TODO 排除自身
        selectDeptList().then(response => {
          this.deptTreeList = this.handleTree(response.data)
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateDept(this.form).then(response => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getDeptTreeList()
              })
            } else {
              addDept(this.form).then(response => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getDeptTreeList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除名称为"' + row.deptName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteDept(row.id)
        }).then(() => {
          this.getDeptTreeList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
