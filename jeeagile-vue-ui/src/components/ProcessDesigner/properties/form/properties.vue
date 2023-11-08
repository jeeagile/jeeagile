<template>
  <div class="properties-item__content">
<!--    <p class="filed-config__title">-->
<!--      <span>字段属性:</span>-->
<!--      <el-button size="mini" type="primary" @click="openPropertyInfo()">新增属性</el-button>-->
<!--    </p>-->
    <el-table :data="propertyList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="属性编号" align="center" min-width="60px" prop="id" show-overflow-tooltip/>
      <el-table-column label="属性值" align="center" min-width="80px" prop="value" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editPropertyInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removePropertyInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="propertyList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openPropertyInfo">新增属性</el-button>
    </div>
    <el-dialog title="属性配置" :visible.sync="propertyVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="propertyInfo" :model="propertyInfo" :rules="rules" :inline="false" label-width="100px" size="small"
               label-position="right">
        <el-form-item label="属性编号:" prop="id">
          <el-input v-model="propertyInfo.id"/>
        </el-form-item>
        <el-form-item label="属性值:" prop="value">
          <el-input v-model="propertyInfo.value"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="propertyVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createPropertyInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'Properties',
    model: {
      prop: 'optionList',
      event: 'propertyList'
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
        propertyList: [],
        propertyVisible: false,
        propertyIndex: -1,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        propertyInfo: {
          id: '',
          value: ''
        },
        rules: {
          id: [
            { required: true, message: '请录入属性编号！', trigger: ['blur', 'change'] }
          ],
          value: [
            { required: true, message: '请录入属性值！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      optionList: {
        handler() {
          if (this.propertyList) {
            this.propertyList = this.optionList
          } else {
            this.propertyList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openPropertyInfo() {
        this.propertyIndex = -1
        this.propertyInfo = {}
        this.propertyVisible = true
        this.$nextTick(() => {
          this.$refs.propertyInfo.clearValidate()
        })
      },
      editPropertyInfo(row, index) {
        this.propertyIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.propertyInfo = JSON.parse(JSON.stringify(row))
        this.propertyVisible = true
      },
      removePropertyInfo(row, index) {
        this.propertyList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createPropertyInfoInfo() {
        this.$refs.propertyInfo.validate(valid => {
            if (valid) {
              if (this.propertyIndex === -1) {
                this.propertyList.push(this.propertyInfo)
              } else {
                this.propertyList.splice(this.propertyIndex, 1, this.propertyInfo)
              }
              this.$emit('propertyList', this.propertyList)
              this.propertyVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>

</style>
