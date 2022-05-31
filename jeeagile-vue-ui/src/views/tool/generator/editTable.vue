<template>
  <el-card>
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="basic">
        <basic-info-form ref="basicInfo" :info="agileGeneratorTableInfo"/>
      </el-tab-pane>
      <el-tab-pane label="字段信息" name="column">
        <el-table ref="dragTable" :data="agileGeneratorTableInfo.agileGeneratorTableColumnList" row-key="id" :max-height="tableHeight">
          <el-table-column label="序号" type="index" min-width="5%" class-name="allowDrag"/>
          <el-table-column label="字段列名" prop="columnName" min-width="10%" :show-overflow-tooltip="true"/>
          <el-table-column label="字段描述" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.columnComment"/>
            </template>
          </el-table-column>
          <el-table-column label="物理类型" prop="columnType" min-width="10%" :show-overflow-tooltip="true"/>
          <el-table-column label="Java类型" min-width="11%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.javaType">
                <el-option label="Long" value="Long"/>
                <el-option label="String" value="String"/>
                <el-option label="Integer" value="Integer"/>
                <el-option label="Double" value="Double"/>
                <el-option label="BigDecimal" value="BigDecimal"/>
                <el-option label="Date" value="Date"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="java属性" min-width="10%">
            <template slot-scope="scope">
              <el-input v-model="scope.row.javaField"/>
            </template>
          </el-table-column>

          <el-table-column label="插入" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.insertFlag" true-label="1"/>
            </template>
          </el-table-column>
          <el-table-column label="编辑" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.editFlag" true-label="1"/>
            </template>
          </el-table-column>
          <el-table-column label="列表" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.listFlag" true-label="1"/>
            </template>
          </el-table-column>
          <el-table-column label="查询" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.queryFlag" true-label="1"/>
            </template>
          </el-table-column>
          <el-table-column label="查询方式" min-width="10%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.queryType">
                <el-option label="=" value="EQ"/>
                <el-option label="!=" value="NE"/>
                <el-option label=">" value="GT"/>
                <el-option label=">=" value="GE"/>
                <el-option label="<" value="LT"/>
                <el-option label="<=" value="LE"/>
                <el-option label="LIKE" value="LIKE"/>
                <el-option label="LIKE_LEFT" value="LIKE_LEFT"/>
                <el-option label="LIKE_RIGHT" value="LIKE_RIGHT"/>
                <el-option label="BETWEEN" value="BETWEEN"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="必填" min-width="5%">
            <template slot-scope="scope">
              <el-checkbox v-model="scope.row.requiredFlag" true-label="1"/>
            </template>
          </el-table-column>
          <el-table-column label="显示类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.htmlType">
                <el-option label="文本框" value="input"/>
                <el-option label="文本域" value="textarea"/>
                <el-option label="数字框" value="number"/>
                <el-option label="下拉框" value="select"/>
                <el-option label="单选框" value="radio"/>
                <el-option label="复选框" value="checkbox"/>
                <el-option label="日期控件" value="datetime"/>
                <el-option label="上传控件" value="uploadImage"/>
                <el-option label="富文本控件" value="editor"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="字典类型" min-width="12%">
            <template slot-scope="scope">
              <el-select v-model="scope.row.dictType" clearable filterable placeholder="请选择">
                <el-option v-for="dictOption in dictOptionList" :key="dictOption.dictType" :label="dictOption.dictName" :value="dictOption.dictType">
                  <span style="float: left">{{ dictOption.dictName }}|{{ dictOption.dictType }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="生成信息" name="agileGeneratorTableInfo">
        <generator-info-form ref="generatorInfo" :info="agileGeneratorTableInfo" :menus="menus"/>
      </el-tab-pane>
    </el-tabs>
    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-100px;margin-top:10px;">
        <el-button type="primary" @click="submitForm()">提交</el-button>
        <el-button @click="close()">返回</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>
<script>
  import { detailGeneratorTable, updateGeneratorTable } from '@/api/generator/generator'
  import { selectDictTypeList } from '@/api/system/dictType'
  import { selectMenuList } from '@/api/system/menu'
  import basicInfoForm from './basicInfoForm'
  import generatorInfoForm from './generatorInfoForm'
  import Sortable from 'sortablejs'

  export default {
    name: 'EditTable',
    components: {
      basicInfoForm,
      generatorInfoForm
    },
    data() {
      return {
        // 选中选项卡的 name
        activeName: 'column',
        // 表格的高度
        tableHeight: document.documentElement.scrollHeight - 245 + 'px',
        // 字典信息
        dictOptionList: [],
        // 菜单信息
        menus: [],
        // 表详细信息
        agileGeneratorTableInfo: {
          agileGeneratorTableColumnList: []
        }
      }
    },
    created() {
      const tableId = this.$route.params && this.$route.params.tableId
      if (tableId) {
        // 获取表详细信息
        detailGeneratorTable(tableId).then(res => {
          this.agileGeneratorTableInfo = res.data
        })
        /** 查询字典下拉列表 */
        selectDictTypeList().then(response => {
          this.dictOptionList = response.data
        })
        /** 查询菜单下拉列表 */
        selectMenuList().then(response => {
          this.menus = this.handleTree(response.data)
        })
      }
    },
    mounted() {
      const el = this.$refs.dragTable.$el.querySelectorAll('.el-table__body-wrapper > table > tbody')[0]
      const sortable = Sortable.create(el, {
        handle: '.allowDrag',
        onEnd: evt => {
          const targetRow = this.cloumns.splice(evt.oldIndex, 1)[0]
          this.cloumns.splice(evt.newIndex, 0, targetRow)
          for (let index in this.cloumns) {
            this.cloumns[index].sort = parseInt(index) + 1
          }
        }
      })
    },
    methods: {
      /** 提交按钮 */
      submitForm() {
        const basicForm = this.$refs.basicInfo.$refs.basicInfoForm
        const generatorForm = this.$refs.generatorInfo.$refs.generatorInfoForm
        Promise.all([basicForm, generatorForm].map(this.getFormPromise)).then(res => {
          const validateResult = res.every(item => !!item)
          if (validateResult) {
            const agileGeneratorTableInfo = Object.assign({}, basicForm.model, generatorForm.model)
            agileGeneratorTableInfo.params = {
              treeCode: agileGeneratorTableInfo.treeCode,
              treeName: agileGeneratorTableInfo.treeName,
              treeParentCode: agileGeneratorTableInfo.treeParentCode,
              parentMenuId: agileGeneratorTableInfo.parentMenuId
            }
            updateGeneratorTable(agileGeneratorTableInfo).then(res => {
              if (res.code === '0000') {
                this.messageSuccess('修改成功')
                this.close()
              } else {
                this.messageError(res.msg)
              }
            })
          } else {
            this.messageError('表单校验未通过，请重新检查提交内容')
          }
        })
      },
      getFormPromise(form) {
        return new Promise(resolve => {
          form.validate(res => {
            resolve(res)
          })
        })
      },
      /** 关闭按钮 */
      close() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.push({ path: '/tool/generator', query: { t: Date.now() }})
      }
    }
  }
</script>
