<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="queryParam.queryCond.roleName" placeholder="请输入角色名称" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="queryParam.queryCond.roleCode" placeholder="请输入角色编码" clearable size="small"
                  style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="roleStatus">
        <el-select v-model="queryParam.queryCond.roleStatus" placeholder="角色状态" clearable size="small"
                   style="width: 240px">
          <el-option v-for="roleStatusOption in roleStatusOptionList" :key="roleStatusOption.dictValue"
                     :label="roleStatusOption.dictLabel" :value="roleStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:role:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:role:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                   v-hasPerm="['system:role:delete']">
          删除
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getRoleList"/>
    </el-row>

    <el-table v-loading="loading" :data="roleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="角色名称" prop="roleName" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="角色编码" prop="roleCode" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="显示顺序" prop="roleSort" width="100"/>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.roleStatus" active-value="0" inactive-value="1"
                     @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:role:update']">
            修改
          </el-button>
          <el-button ize="mini" type="text" icon="el-icon-circle-check" @click="handleDataScope(scope.row)"
                     v-hasPerm="['system:role:dataScope']">
            数据权限
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:role:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getRoleList"/>

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称"/>
        </el-form-item>
        <el-form-item label="角色编码" prop="roleKey">
          <el-input v-model="form.roleCode" placeholder="角色编码"/>
        </el-form-item>
        <el-form-item label="角色顺序" prop="roleSort">
          <el-input-number v-model="form.roleSort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.roleStatus">
            <el-radio v-for="roleStatusOption in roleStatusOptionList" :key="roleStatusOption.dictValue"
                      :label="roleStatusOption.dictValue">
              {{ roleStatusOption.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
          <el-tree ref="menu" class="tree-border" :data="menuTreeOptionList" show-checkbox node-key="id"
                   empty-text="加载中，请稍后" :props="menuTreeProps"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 分配角色数据权限对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDataScopeDialog" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true"/>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="form.roleCode" :disabled="true"/>
        </el-form-item>
        <el-form-item label="数据范围">
          <el-select v-model="form.dataScope">
            <el-option v-for="dataScopeOption in dataScopeOptionList" :key="dataScopeOption.dictValue"
                       :label="dataScopeOption.dictLabel" :value="dataScopeOption.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item v-show="form.dataScope == '05'" label="数据权限">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
          <el-tree ref="dept" class="tree-border" :data="deptTreeOptionList" show-checkbox default-expand-all
                   node-key="id" empty-text="加载中，请稍后" :props="deptTreeProps"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {
    initData,
    selectRolePage,
    detailRole,
    deleteRole,
    addRole,
    updateRole,
    changeRoleStatus,
    updateDataScope
  } from '@/api/system/role'
  import { selectMenuList } from '@/api/system/menu'
  import { selectDeptList } from '@/api/system/dept'

  export default {
    name: 'Role',
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
        // 角色表格数据
        roleList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 是否显示弹出层（数据权限）
        openDataScopeDialog: false,
        menuExpand: false,
        menuNodeAll: false,
        deptExpand: true,
        deptNodeAll: false,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        roleStatusOptionList: [],
        // 数据范围
        dataScopeOptionList: [],
        // 菜单列表
        menuTreeOptionList: [],
        // 部门列表
        deptTreeOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            roleName: undefined,
            roleCode: undefined,
            roleStatus: undefined
          }
        },
        // 表单参数
        form: {},
        menuTreeProps: {
          children: 'children',
          label: 'menuName'
        },
        deptTreeProps: {
          children: 'children',
          label: 'deptName'
        },
        // 表单校验
        rules: {
          roleName: [
            { required: true, message: '角色名称不能为空', trigger: 'blur' }
          ],
          roleCode: [
            { required: true, message: '角色编码不能为空', trigger: 'blur' }
          ],
          roleSort: [
            { required: true, message: '角色顺序不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.initData()
      this.getRoleList()
      this.getDictDataByDictType('sys_normal_disable').then(response => {
        this.roleStatusOptionList = response.data
      })
      this.getDictDataByDictType('sys_data_scope').then(response => {
        this.dataScopeOptionList = response.data
      })
    },
    methods: {
      /** 初始化数据 */
      initData() {
        initData().then(response => {
          this.menuTreeOptionList = this.handleTree(response.data.menuList)
          this.deptTreeOptionList = this.handleTree(response.data.deptList)
        })
      },
      /** 查询角色列表 */
      getRoleList() {
        this.loading = true
        selectRolePage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.roleList = response.data.recordList
            this.loading = false
          }
        )
      },
      /** 所有菜单节点数据 */
      getMenuAllCheckedKeys() {
        // 目前被选中的菜单节点
        let checkedKeys = this.$refs.menu.getCheckedKeys()
        // 半选中的菜单节点
        let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys()
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
        return checkedKeys
      },
      /** 所有部门节点数据 */
      getDeptAllCheckedKeys() {
        // 目前被选中的部门节点
        let checkedKeys = this.$refs.dept.getCheckedKeys()
        // 半选中的部门节点
        let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys()
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys)
        return checkedKeys
      },
      /** 角色状态修改 */
      handleStatusChange(row) {
        let text = row.roleStatus === '0' ? '启用' : '停用'
        this.$confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { id: row.id, status: row.roleStatus }
          return changeRoleStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(() => {
          row.roleStatus = row.roleStatus === '0' ? '1' : '0'
        })
      },
      /** 取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /** 取消按钮（数据权限） */
      cancelDataScope() {
        this.openDataScopeDialog = false
        this.reset()
      },
      /** 表单重置 */
      reset() {
        if (this.$refs.menu != undefined) {
          this.$refs.menu.setCheckedKeys([])
        }
        this.menuExpand = false
        this.menuNodeAll = false
        this.deptExpand = true
        this.deptNodeAll = false
        this.form = {
          id: undefined,
          roleName: undefined,
          roleCode: undefined,
          roleSort: 0,
          roleStatus: '0',
          dataScope: undefined,
          menuIdList: [],
          deptIdList: [],
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getRoleList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 树权限（展开/折叠）*/
      handleCheckedTreeExpand(value, type) {
        if (type == 'menu') {
          let treeList = this.menuTreeOptionList
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value
          }
        } else if (type == 'dept') {
          let treeList = this.deptTreeOptionList
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value
          }
        }
      },
      /** 树权限（全选/全不选）*/
      handleCheckedTreeNodeAll(value, type) {
        if (type == 'menu') {
          this.$refs.menu.setCheckedNodes(value ? this.menuTreeOptionList : [])
        } else if (type == 'dept') {
          this.$refs.dept.setCheckedNodes(value ? this.deptTreeOptionList : [])
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加角色'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailRole(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.$nextTick(() => {
            this.$refs.menu.setCheckedKeys(response.data.menuIdList)
          })
          this.dialogTitle = '修改角色'
        })
      },
      /** 修改数据范围 */
      handleDataScope(row) {
        this.reset()
        detailRole(row.id).then(response => {
          this.form = response.data
          this.openDataScopeDialog = true
          this.$nextTick(() => {
            this.$refs.dept.setCheckedKeys(response.data.deptIdList)
          })
          this.dialogTitle = '分配数据权限'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              this.form.menuIdList = this.getMenuAllCheckedKeys()
              updateRole(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getRoleList()
              })
            } else {
              this.form.menuIdList = this.getMenuAllCheckedKeys()
              addRole(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getRoleList()
              })
            }
          }
        })
      },
      /** 提交按钮（数据权限） */
      submitDataScope: function () {
        if (this.form.id != undefined) {
          this.form.deptIdList = this.getDeptAllCheckedKeys()
          updateDataScope(this.form).then(() => {
            this.messageSuccess('修改成功')
            this.openDataScopeDialog = false
            this.getRoleList()
          })
        }
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除角色名称为"' + row.roleName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteRole(row.id)
        }).then(() => {
          this.getRoleList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
