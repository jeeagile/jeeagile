<template>
  <div class="properties-item__content">
<!--    <p class="filed-config__title">-->
<!--      <span>字段枚举:</span>-->
<!--      <el-button size="mini" type="primary" @click="openEnumInfo()">新增枚举</el-button>-->
<!--    </p>-->
    <el-table :data="enumList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="枚举值编号" align="center" min-width="70px" prop="id" show-overflow-tooltip/>
      <el-table-column label="枚举值名称" align="center" min-width="80px" prop="name" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editEnumInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeEnumInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="enumList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openEnumInfo">新增枚举</el-button>
    </div>
    <el-dialog title="枚举配置" :visible.sync="enumVisible" width="350px" append-to-body destroy-on-close>
      <el-form ref="enumInfo" :model="enumInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="枚举值编号:" prop="id">
          <el-input v-model="enumInfo.id"/>
        </el-form-item>
        <el-form-item label="枚举值名称:" prop="name">
          <el-input v-model="enumInfo.name"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="enumVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createEnumInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'Enums',
    model: {
      prop: 'optionList',
      event: 'enumList'
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
        enumList: [],
        enumVisible: false,
        enumIndex: -1,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        enumInfo: {
          id: '',
          name: ''
        },
        rules: {
          id: [
            { required: true, message: '请录入枚举值编号！', trigger: ['blur', 'change'] }
          ],
          name: [
            { required: true, message: '请录入枚举值名称！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      optionList: {
        handler() {
          if (this.enumList) {
            this.enumList = this.optionList
          } else {
            this.enumList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openEnumInfo() {
        this.enumIndex = -1
        this.enumInfo = {}
        this.enumVisible = true
        this.$nextTick(() => {
          this.$refs.enumInfo.clearValidate()
        })
      },
      editEnumInfo(row, index) {
        this.enumIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.enumInfo = JSON.parse(JSON.stringify(row))
        this.enumVisible = true
      },
      removeEnumInfo(row, index) {
        this.enumList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createEnumInfoInfo() {
        this.$refs.enumInfo.validate(valid => {
            if (valid) {
              if (this.enumIndex === -1) {
                this.enumList.push(this.enumInfo)
              } else {
                this.enumList.splice(this.enumIndex, 1, this.enumInfo)
              }
              this.$emit('enumList', this.enumList)
              this.enumVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>

</style>
