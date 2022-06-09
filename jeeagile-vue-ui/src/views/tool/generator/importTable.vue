<template>
  <!-- 导入表 -->
  <el-dialog title="导入表" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form :model="queryParam" ref="queryForm" :inline="true">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParam.queryCond.tableName"
          placeholder="请输入表名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParam.queryCond.tableComment"
          placeholder="请输入表描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="dbTableList" @selection-change="handleSelectionChange" height="260px">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="tableName" label="表名称" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="tableComment" label="表描述" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      </el-table>
      <!-- 分页 -->
      <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getDbTableList"/>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { selectDbTablePage, importGeneratorTable } from '@/api/generator/generator'

  export default {
    data() {
      return {
        // 遮罩层
        visible: false,
        // 选中数组值
        tableNameList: [],
        // 表数据
        dbTableList: [],
        // 查询参数
        queryParam: {
          pageTotal: 0,
          pageSize: 10,
          currentPage: 1,
          queryCond: {
            title: undefined,
            type: undefined,
            status: undefined,
            userName: undefined
          }
        }
      }
    },
    methods: {
      // 显示弹框
      show() {
        this.getDbTableList()
        this.visible = true
      },
      clickRow(row) {
        this.$refs.table.toggleRowSelection(row)
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.tableNameList = selection.map(item => item.tableName)
      },
      // 查询数据库表数据
      getDbTableList() {
        selectDbTablePage(this.queryParam).then(response => {
          this.queryParam.pageTotal = response.data.pageTotal
          this.dbTableList = response.data.records
          this.loading = false
        })
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.pageNum = 1
        this.getDbTableList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 导入按钮操作 */
      handleImportTable() {
        importGeneratorTable(this.tableNameList).then(res => {
          this.messageSuccess(res.message)
          this.visible = false
          this.$emit('ok')
        })
      }
    }
  }
</script>
