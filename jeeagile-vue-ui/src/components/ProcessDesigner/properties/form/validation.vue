<template>
  <div class="properties-item__content">
<!--    <p class="filed-config__title">-->
<!--      <span>约束条件:</span>-->
<!--      <el-button size="mini" type="primary" @click="openConstraintInfo()">新增约束</el-button>-->
<!--    </p>-->
    <el-table
      :data="constraintList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)"
      size="mini" border>
      <el-table-column label="约束名称" align="center" min-width="60px" prop="name" show-overflow-tooltip/>
      <el-table-column label="约束配置" align="center" min-width="80px" prop="config" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editConstraintInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeConstraintInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="constraintList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openConstraintInfo">新增约束</el-button>
    </div>
    <el-dialog title="参数配置" :visible.sync="constraintVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="constraintInfo" :model="constraintInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="约束名称:" prop="name">
          <el-input v-model="constraintInfo.name"/>
        </el-form-item>
        <el-form-item label="约束配置:" prop="config">
          <el-input v-model="constraintInfo.config"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="constraintVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createConstraintInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'Validation',
    model: {
      prop: 'optionList',
      event: 'constraintList'
    },
    props: {
      optionList: {
        type: Array,
        required: true,
        default: () => []
      }
    },
    data() {
      return {
        constraintList: [],
        constraintVisible: false,
        constraintIndex: -1,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        constraintInfo: {
          name: '',
          config: ''
        },
        rules: {
          name: [
            { required: true, message: '请录入约束名称！', trigger: ['blur', 'change'] }
          ],
          config: [
            { required: true, message: '请录入约束配置！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      optionList: {
        handler() {
          if (this.constraintList) {
            this.constraintList = this.optionList
          } else {
            this.constraintList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openConstraintInfo() {
        this.constraintIndex = -1
        this.constraintInfo = {}
        this.constraintVisible = true
        this.$nextTick(() => {
          this.$refs.constraintInfo.clearValidate()
        })
      },
      editConstraintInfo(row, index) {
        this.constraintIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.constraintInfo = JSON.parse(JSON.stringify(row))
        this.constraintVisible = true
      },
      removeConstraintInfo(row, index) {
        this.constraintList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createConstraintInfoInfo() {
        this.$refs.constraintInfo.validate(valid => {
            if (valid) {
              if (this.constraintIndex === -1) {
                this.constraintList.push(this.constraintInfo)
              } else {
                this.constraintList.splice(this.constraintIndex, 1, this.constraintInfo)
              }
              this.$emit('constraintList', this.constraintList)
              this.constraintVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>
</style>
