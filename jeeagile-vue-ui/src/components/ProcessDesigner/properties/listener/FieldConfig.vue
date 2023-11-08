<template>
  <div class="properties-option__content">
    <p class="filed-config__title">
      <span>监听器参数:</span>
      <el-button size="mini" type="primary" @click="openFieldInfo()">新增</el-button>
    </p>
    <el-table
      :data="fieldList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)"
      size="mini" border>
      <el-table-column label="名称" align="center" min-width="50px" prop="name" show-overflow-tooltip/>
      <el-table-column label="类型" align="center" min-width="50px" prop="type" :formatter="row => fieldTypeMap[row.type]" show-overflow-tooltip/>
      <el-table-column label="值" align="center" min-width="70px" prop="value" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editFieldInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeFieldInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="fieldList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <el-dialog title="参数配置" :visible.sync="fieldVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="fieldInfo" :model="fieldInfo" :rules="rules" :inline="false" label-width="100px" size="small"
               label-position="right">
        <el-form-item label="字段名称:" prop="name">
          <el-input v-model="fieldInfo.name"/>
        </el-form-item>
        <el-form-item label="字段类型:" prop="type">
          <el-select v-model="fieldInfo.type">
            <el-option v-for="i in Object.keys(fieldTypeMap)" :key="i" :label="fieldTypeMap[i]" :value="i"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="fieldInfo.type" :label="`${fieldTypeMap[fieldInfo.type]}:`" prop="value">
          <el-input v-model="fieldInfo.value"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="fieldVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createFieldInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { FieldTypeMap } from './listener'

  export default {
    name: 'FieldConfig',
    model: {
      prop: 'fields',
      event: 'fieldList'
    },
    props: {
      fields: {
        type: Array,
        required: true,
        default: () => []
      }
    },
    data() {
      return {
        fieldList: [],
        fieldVisible: false,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        fieldTypeMap: FieldTypeMap,
        fieldIndex: -1,
        fieldInfo: {
          name: '',
          type: '',
          value: ''
        },
        rules: {
          name: [
            { required: true, message: '请录入字段名称！', trigger: ['blur', 'change'] }
          ],
          type: [
            { required: true, message: '请录入字段类型！', trigger: ['blur', 'change'] }
          ],
          value: [
            { required: true, message: '请录入字段值/表达式！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      fields: {
        handler() {
          if (this.fields) {
            this.fieldList = this.fields
          } else {
            this.fieldList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openFieldInfo() {
        this.fieldIndex = -1
        this.fieldInfo = {}
        this.fieldVisible = true
        this.$nextTick(() => {
          this.$refs.fieldInfo.clearValidate()
        })
      },
      editFieldInfo(row, index) {
        this.fieldIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.fieldInfo = JSON.parse(JSON.stringify(row))
        this.fieldVisible = true
      },
      removeFieldInfo(row, index) {
        this.fieldList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createFieldInfoInfo() {
        this.$refs.fieldInfo.validate(valid => {
            if (valid) {
              if (this.fieldIndex === -1) {
                this.fieldList.push(this.fieldInfo)
              } else {
                this.fieldList.splice(this.fieldIndex, 1, this.fieldInfo)
              }
              this.$emit('fieldList', this.fieldList)
              this.fieldVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>

</style>
