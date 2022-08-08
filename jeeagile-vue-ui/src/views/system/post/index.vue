<template>
  <div class="app-container">
    <el-form :model="queryParam" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="岗位编码" prop="postCode">
        <el-input v-model="queryParam.queryCond.postCode" placeholder="请输入岗位编码" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="岗位名称" prop="postName">
        <el-input v-model="queryParam.queryCond.postName" placeholder="请输入岗位名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="postStatus">
        <el-select v-model="queryParam.queryCond.postStatus" placeholder="岗位状态" clearable size="small">
          <el-option v-for="postStatusOption in postStatusOptionList" :key="postStatusOption.dictValue"
                     :label="postStatusOption.dictLabel" :value="postStatusOption.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:post:add']">
          新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                   v-hasPerm="['system:post:update']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete"
                   v-hasPerm="['system:post:delete']">
          删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExport"
                   v-hasPerm="['system:post:export']">
          导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getPostList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="岗位编码" align="center" prop="postCode"/>
      <el-table-column label="岗位名称" align="center" prop="postName"/>
      <el-table-column label="岗位排序" align="center" prop="postSort"/>
      <el-table-column label="状态" align="center" prop="postStatus" :formatter="postStatusFormat"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:post:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:post:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal"
                :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize"
                @pagination="getPostList"/>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="岗位编码" prop="postCode">
          <el-input v-model="form.postCode" placeholder="请输入编码名称"/>
        </el-form-item>
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称"/>
        </el-form-item>
        <el-form-item label="岗位顺序" prop="postSort">
          <el-input-number v-model="form.postSort" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item label="岗位状态" prop="postStatus">
          <el-radio-group v-model="form.postStatus">
            <el-radio v-for="postStatusOption in postStatusOptionList" :key="postStatusOption.dictValue"
                      :label="postStatusOption.dictValue">
              {{ postStatusOption.dictLabel }}
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
  import { selectPostPage, detailPost, deletePost, addPost, updatePost, exportPost } from '@/api/system/post'

  export default {
    name: 'Post',
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
        // 岗位表格数据
        postList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 状态数据字典
        postStatusOptionList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            postCode: undefined,
            postName: undefined,
            postStatus: undefined,
            excelName: '岗位数据'
          }
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          postName: [
            { required: true, message: '岗位名称不能为空', trigger: 'blur' }
          ],
          postCode: [
            { required: true, message: '岗位编码不能为空', trigger: 'blur' }
          ],
          postSort: [
            { required: true, message: '岗位顺序不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getPostList()
      this.getSysDictDataList('sys_normal_disable').then(response => {
        this.postStatusOptionList = response.data
      })
    },
    methods: {
      /** 查询岗位列表 */
      getPostList() {
        this.loading = true
        selectPostPage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.postList = response.data.records
          this.loading = false
        })
      },
      /** 岗位状态字典翻译 */
      postStatusFormat(row, column) {
        return this.handleDictLabel(this.postStatusOptionList, row.postStatus)
      },
      /** 取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /** 表单重置 */
      reset() {
        this.form = {
          id: undefined,
          postCode: undefined,
          postName: undefined,
          postSort: 0,
          postStatus: '0',
          remark: undefined
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getPostList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 多选框选中数据 */
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.openDialog = true
        this.dialogTitle = '添加岗位'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        row = undefined === row.id ? this.selectRowList[0] : row
        detailPost(row.id).then(response => {
          this.form = response.data
          this.openDialog = true
          this.dialogTitle = '修改岗位'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updatePost(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getPostList()
              })
            } else {
              addPost(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getPostList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除岗位编号为"' + row.postName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deletePost(row.id)
        }).then(() => {
          this.getPostList()
          this.messageSuccess('删除成功')
        })
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$confirm('请确认是否导出岗位数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return exportPost(this.queryParam.queryCond)
        })
      }
    }
  }
</script>
