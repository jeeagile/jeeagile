<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true" label-width="68px">
      <el-form-item label="表名称" prop="tableName">
        <el-input v-model="queryParam.queryCond.tableName" placeholder="请输入表名称" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input v-model="queryParam.queryCond.tableComment" placeholder="请输入表描述" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPerm="['tool:generator:code']" type="primary" icon="el-icon-download" size="mini" @click="handleGeneratorTable">
          生成
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['tool:generator:import']" type="info" icon="el-icon-upload" size="mini" @click="openImportTable">
          导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['tool:generator:edit']" type="success" icon="el-icon-edit" size="mini" :disabled="single" @click="handleEditTable">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button v-hasPerm="['tool:generator:remove']" type="danger" icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">
          删除
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getGeneratorTableList"/>
    </el-row>

    <el-table v-loading="loading" :data="generatorTableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序号" type="index" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ (queryParam.currentPage - 1) * queryParam.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="表名称" align="center" prop="tableName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="表描述" align="center" prop="tableComment" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="实体" align="center" prop="className" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-hasPerm="['tool:generator:preview']" type="text" size="small" icon="el-icon-view" @click="handlePreview(scope.row)">
            预览
          </el-button>
          <el-button v-hasPerm="['tool:generator:update']" type="text" size="small" icon="el-icon-edit" @click="handleEditTable(scope.row)">
            编辑
          </el-button>
          <el-button v-hasPerm="['tool:generator:remove']" type="text" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)">
            删除
          </el-button>
          <el-button v-hasPerm="['tool:generator:sync']" type="text" size="small" icon="el-icon-refresh" @click="handleSyncGeneratorTable(scope.row)">
            同步
          </el-button>
          <el-button v-hasPerm="['tool:generator:code']" type="text" size="small" icon="el-icon-download" @click="handleGeneratorTable(scope.row)">
            生成代码
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <pagination v-show="queryParam.pageTotal>0" :total-page="queryParam.pageTotal" :current-page.sync="queryParam.currentPage" :limit.sync="queryParam.pageSize" @pagination="getGeneratorTableList"/>

    <!-- 预览界面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(value, key) in preview.data" :key="key" :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))" :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))">
          <pre>{{ value }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table ref="import" @ok="handleQuery"/>
  </div>
</template>

<script>
  import { selectGeneratorTablePage, syncGeneratorTable, deleteGeneratorTable, previewGeneratorCode, downloadGeneratorCode } from '@/api/generator/generator'
  import importTable from './importTable'

  export default {
    name: 'Generator',
    components: { importTable },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 唯一标识符
        uniqueId: '',
        // 已选择的列表
        selectRowList: [],
        // 已选择的列表ID
        tableIdList: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 表数据
        generatorTableList: [],
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
        },
        // 预览参数
        preview: {
          open: false,
          title: '代码预览',
          data: {},
          activeName: 'domain.java'
        }
      }
    },
    created() {
      this.getGeneratorTableList()
    },
    activated() {
      const time = this.$route.query.t
      if (time != null && time != this.uniqueId) {
        this.uniqueId = time
        this.resetQuery()
      }
    },
    methods: {
      /** 查询表集合 */
      getGeneratorTableList() {
        this.loading = true
        selectGeneratorTablePage(this.queryParam).then(response => {
            this.queryParam.pageTotal = response.data.pageTotal
            this.generatorTableList = response.data.recordList
            this.loading = false
          }
        )
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParam.currentPage = 1
        this.getGeneratorTableList()
      },
      /** 生成代码操作 */
      handleGeneratorTable(row) {
        if (row != undefined && row.id) {
          this.tableIdList.push(row.id)
        } else {
          this.tableIdList = this.selectRowList.map(item => item.id)
        }
        downloadGeneratorCode(this.tableIdList)
      },
      /** 同步数据库操作 */
      handleSyncGeneratorTable(row) {
        this.$confirm('确认要强制同步"' + row.tableName + '"表结构吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return syncGeneratorTable(row.id)
        }).then(() => {
          this.messageSuccess('同步成功')
        })
      },
      /** 打开导入表弹窗 */
      openImportTable() {
        this.$refs.import.show()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = []
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 预览按钮 */
      handlePreview(row) {
        previewGeneratorCode(row.id).then(response => {
          this.preview.data = response.data
          this.preview.open = true
        })
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.selectRowList = selection
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      handleEditTable(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$router.push('/generator/edit/' + row.id)
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        row = undefined === row.id ? this.selectRowList[0] : row
        this.$confirm('是否确认删除表编号为"' + row.tableName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteGeneratorTable(row.id)
        }).then(() => {
          this.getGeneratorTableList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
