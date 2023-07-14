<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search"
                    style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree ref="deptTree" :data="deptTreeOptionList" :props="deptTreeProps" :expand-on-click-node="false"
                   :filter-node-method="filterDeptNode" default-expand-all @node-click="handleDeptNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
          <el-form-item label="用户名称" prop="userName">
            <el-input v-model="queryParam.queryCond.userName" placeholder="请输入用户名称" clearable size="small"
                      style="width: 240px" @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="userPhone">
            <el-input v-model="queryParam.queryCond.userPhone" placeholder="请输入手机号码" clearable size="small"
                      style="width: 240px" @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="userStatus">
            <el-select v-model="queryParam.queryCond.userStatus" placeholder="用户状态" clearable size="small"
                       style="width: 240px"
            >
              <el-option v-for="userStatusOption in userStatusOptionList" :key="userStatusOption.dictValue"
                         :label="userStatusOption.dictLabel" :value="userStatusOption.dictValue"
              />
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
                       v-hasPerm="['system:user:add']">
              新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                       v-hasPerm="['system:user:update']">
              修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                       v-hasPerm="['system:user:delete']">
              删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="info" icon="el-icon-upload2" size="mini" @click="handleImport"
                       v-hasPerm="['system:user:import']">
              导入
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport"
                       v-hasPerm="['system:user:export']">
              导出
            </el-button>
          </el-col>
          <right-toolbar :show-search.sync="showSearch" @queryTable="getUserList"/>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
          <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属部门" align="center" prop="deptName" :show-overflow-tooltip="true"
                           :formatter="deptNameFormat"
          />
          <el-table-column label="手机号码" align="center" prop="userPhone"/>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.userStatus" active-value="0" inactive-value="1"
                         @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" lass-name="small-padding fixed-width" width="200px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                         v-hasPerm="['system:user:update']">
                修改
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         v-hasPerm="['system:user:delete']">
                删除
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-key" @click="handleResetPassword(scope.row)"
                         v-hasPerm="['system:user:password']">
                重置
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                    :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                    @pagination="getUserList"
        />

      </el-col>
    </el-row>

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.id == undefined" label="用户名称" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickName">
              <el-input v-model="form.nickName" placeholder="请输入用户昵称"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <tree-select v-model="form.deptId" :options="deptTreeOptionList" :show-count="true"
                           :normalizer="deptNormalizer" placeholder="请选择归属部门"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="userSort">
              <el-input-number v-model="form.userSort" controls-position="right" :min="0"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="userPhone">
              <el-input v-model="form.userPhone" placeholder="请输入手机号码" maxlength="11"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="userEmail">
              <el-input v-model="form.userEmail" placeholder="请输入邮箱" maxlength="50"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户性别">
              <el-select v-model="form.userSex" placeholder="请选择">
                <el-option v-for="userSexOption in userSexOptionList" :key="userSexOption.dictValue"
                           :label="userSexOption.dictLabel" :value="userSexOption.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.userStatus">
                <el-radio v-for="userStatusOption in userStatusOptionList" :key="userStatusOption.dictValue"
                          :label="userStatusOption.dictValue"
                >
                  {{ userStatusOption.dictLabel }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="岗位">
              <el-select v-model="form.postIdList" multiple placeholder="请选择">
                <el-option v-for="postOption in postList" :key="postOption.id"
                           :label="postOption.postName" :value="postOption.id"
                           :disabled="postOption.postStatus == 1"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色">
              <el-select v-model="form.roleIdList" multiple placeholder="请选择">
                <el-option v-for="roleOption in roleList" :key="roleOption.id"
                           :label="roleOption.roleName" :value="roleOption.id"
                           :disabled="roleOption.roleStatus == 1"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
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

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        action="#"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"/>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip text-center">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="importTemplate"
          >下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    initData,
    selectUserPage,
    detailUser,
    deleteUser,
    addUser,
    updateUser,
    resetPassword,
    changeUserStatus,
    exportUser,
    importUser,
    importTemplate
  } from '@/api/system/user'

  export default {
    name: 'User',
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
        // 用户表格数据
        userList: null,
        // 弹出层标题
        dialogTitle: '',
        // 部门数据
        deptList: undefined,
        // 部门树选项
        deptTreeOptionList: undefined,
        // 是否显示弹出层
        openDialog: false,
        // 部门名称
        deptName: undefined,
        // 默认密码
        defaultPwd: undefined,
        // 日期范围
        dateRange: [],
        // 状态数据字典
        userStatusOptionList: [],
        // 性别状态字典
        userSexOptionList: [],
        // 岗位选项
        postList: [],
        // 角色选项
        roleList: [],
        // 表单参数
        form: {},
        deptTreeProps: {
          children: 'children',
          label: 'deptName'
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: '',
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0
        },
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            userName: undefined,
            userPhone: undefined,
            userStatus: undefined,
            deptId: undefined,
            excelName: '用户信息'
          }
        },
        // 表单校验
        rules: {
          userName: [
            { required: true, message: '用户名称不能为空', trigger: 'blur' }
          ],
          nickName: [
            { required: true, message: '用户昵称不能为空', trigger: 'blur' }
          ],
          deptId: [
            { required: true, message: '归属部门不能为空', trigger: 'blur' }
          ],
          userPwd: [
            { required: true, message: '用户密码不能为空', trigger: 'blur' }
          ],
          userEmail: [
            { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
            {
              type: 'email',
              message: '请输入正确的邮箱地址',
              trigger: ['blur', 'change']
            }
          ],
          userPhone: [
            { required: true, message: '手机号码不能为空', trigger: 'blur' },
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: '请输入正确的手机号码',
              trigger: 'blur'
            }
          ]
        }
      }
    },
    watch: {
      /** 根据名称筛选部门树 */
      deptName(val) {
        this.$refs.deptTree.filter(val)
      }
    },
    created() {
      this.initData()
      this.getUserList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.userStatusOptionList = response.data
      })
      this.getSysDictDataList('sys_user_sex').then(response => {
        this.userSexOptionList = response.data
      })
      this.getSysConfig('sys.user.pwd').then(response => {
        this.defaultPwd = response.data.configValue
      })
    },
    methods: {
      /** 初始化页面数据 */
      initData() {
        initData().then(response => {
          this.deptList = response.data.deptList
          this.postList = response.data.postList
          this.roleList = response.data.roleList
          this.deptTreeOptionList = this.handleTree(this.deptList)
        })
      },
      /** 查询用户列表 */
      getUserList() {
        this.loading = true
        selectUserPage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.userList = response.data.records
            this.loading = false
          }
        )
      },
      /** 部门名称翻译 */
      deptNameFormat(row) {
        for (const dept of this.deptList) {
          if (dept.id === row.deptId) {
            return dept.deptName
          }
        }
        return ''
      },
      /** 转换部门数据结构 */
      deptNormalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.deptName,
          children: node.children
        }
      },
      /** 筛选节点 */
      filterDeptNode(value, data) {
        if (!value) return true
        return data.deptName.indexOf(value) !== -1
      },
      /** 节点单击事件 */
      handleDeptNodeClick(data) {
        this.queryParam.queryCond.deptId = data.id
        this.getUserList()
      },
      /** 用户状态修改 */
      handleStatusChange(row) {
        let text = row.userStatus === '0' ? '启用' : '停用'
        this.$confirm('确认要"' + text + '""' + row.userName + '"用户吗?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const data = { userId: row.id, userStatus: row.userStatus }
          return changeUserStatus(data)
        }).then(() => {
          this.messageSuccess(text + '成功')
        }).catch(() => {
          row.userStatus = row.userStatus === '0' ? '1' : '0'
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
          deptId: undefined,
          userName: undefined,
          nickName: undefined,
          userSort: 0,
          userPwd: undefined,
          userPhone: undefined,
          userEmail: undefined,
          userSex: undefined,
          userStatus: '0',
          remark: undefined,
          postIdList: [],
          roleIdList: []
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.page = 1
        this.getUserList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加用户'
        this.form.userPwd = this.defaultPwd
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailUser(row.id).then(response => {
          this.form = response.data
          this.form.postIdList = response.data.postIdList
          this.form.roleIdList = response.data.roleIdList
          this.openDialog = true
          this.dialogTitle = '修改用户'
        })
      },
      /** 重置密码按钮操作 */
      handleResetPassword(row) {
        this.$prompt('请输入用户"' + row.userName + '"的新密码', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({ value }) => {
          const data = { userId: row.id, password: value }
          resetPassword(data).then(() => {
            this.messageSuccess('修改成功，新密码是：' + value)
          })
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateUser(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getUserList()
              })
            } else {
              addUser(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getUserList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除用户编号为"' + row.userName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteUser(row.id)
        }).then(() => {
          this.getUserList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$confirm('请确认是否导出用户数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportUser(this.queryParam.queryCond)
        })
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = '用户导入'
        this.upload.open = true
      },
      /** 下载模板操作 */
      importTemplate() {
        return importTemplate(this.queryParam.queryCond)
      },
      /** 文件上传中处理 */
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true
      },
      /** 提交上传文件 */
      submitFileForm() {
        if (this.$refs.upload.uploadFiles.length < 1) {
          return
        }
        let formData = new FormData()
        formData.append('importExcel', this.$refs.upload.uploadFiles[0].raw),
          importUser(formData).then(response => {
            this.upload.open = false
            this.upload.isUploading = false
            this.$refs.upload.clearFiles()
            this.$alert('<div style=\'overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;\'>' + response.data + '</div>', '导入结果', { dangerouslyUseHTMLString: true })
            this.getUserList()
          })
      }
    }
  }
</script>
