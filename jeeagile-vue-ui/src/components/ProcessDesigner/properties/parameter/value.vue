<template>
  <div class="properties-option__content">
    <p class="filed-config__title">
      <span>值列表:</span>
      <el-button size="mini" type="primary" @click="openValueInfo()">新增值</el-button>
    </p>
    <el-table :data="valueList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="值" align="center" min-width="150px" prop="value" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editValueInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeValueInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="valueList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <el-dialog title="添加值" :visible.sync="valueVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="valueInfo" :model="valueInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="值:" prop="value">
          <el-input v-model="valueInfo.value"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="valueVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createValueInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'ValueList',
    model: {
      prop: 'values',
      event: 'valueList'
    },
    props: {
      values: {
        type: Array,
        required: true,
        default: () => []
      }
    },
    data() {
      return {
        valueList: [],
        valueVisible: false,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        valueIndex: -1,
        valueInfo: {
          value: ''
        },
        rules: {
          value: [
            { required: true, message: '请录入值！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      values: {
        handler() {
          if (this.values) {
            this.valueList = this.values
          } else {
            this.valueList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openValueInfo() {
        this.valueIndex = -1
        this.valueInfo = {}
        this.valueVisible = true
        this.$nextTick(() => {
          this.$refs.valueInfo.clearValidate()
        })
      },
      editValueInfo(row, index) {
        this.valueIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.valueInfo = JSON.parse(JSON.stringify(row))
        this.valueVisible = true
      },
      removeValueInfo(row, index) {
        this.valueList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createValueInfoInfo() {
        this.$refs.valueInfo.validate(valid => {
            if (valid) {
              if (this.valueIndex === -1) {
                this.valueList.push(this.valueInfo)
              } else {
                this.valueList.splice(this.valueIndex, 1, this.valueInfo)
              }
              this.$emit('valueList', this.valueList)
              this.valueVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>

</style>
