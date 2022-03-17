<template>
  <div class="properties-item__content">
    <el-table
      :data="buttonList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)"
      size="mini" border>
      <el-table-column label="名称" align="center" min-width="60px" prop="name" show-overflow-tooltip/>
      <el-table-column label="编码" align="center" min-width="70px" prop="code" show-overflow-tooltip/>
      <el-table-column label="排序" align="center" min-width="40px" prop="sort" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editButtonInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeButtonInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="buttonList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next">
    </el-pagination>
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openButtonInfo">添加按钮</el-button>
    </div>
    <el-dialog title="按钮配置" :visible.sync="buttonVisible" width="350px" append-to-body destroy-on-close>
      <el-form ref="buttonInfo" :model="buttonInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="按钮名称:" prop="name">
          <el-input v-model="buttonInfo.name"/>
        </el-form-item>
        <el-form-item label="按钮编码:" prop="code">
          <el-input v-model="buttonInfo.code"/>
        </el-form-item>
        <el-form-item label="按钮排序">
          <el-input-number v-model="buttonInfo.sort" :min="1"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="buttonVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createButtonInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'Button',
    props: {
      processModeler: {  // 流程Modeler
        type: Object,
        required: true
      },
      processInfo: {    // 流程基础信息
        type: Object,
        required: true
      },
      activeElement: {  // 当前选中元素
        type: Object,
        required: true
      }
    },
    data() {
      return {
        buttonList: [],
        buttonVisible: false,
        buttonIndex: -1,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        buttonInfo: {
          name: '',
          code: '',
          sort: 1
        },
        rules: {
          name: [
            { required: true, message: '请录入按钮名称！', trigger: ['blur', 'change'] }
          ],
          code: [
            { required: true, message: '请录入按钮编码！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initButtonList()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initButtonList() {
        this.buttonList = this.activeElement.businessObject?.extensionElements?.values
          .find(element => element.$type === `${this.processInfo.processPrefix}:Buttons`)?.buttons
          .map(buttonElement => {
            return {
              name: buttonElement.name,
              code: buttonElement.code,
              sort: buttonElement.sort
            }
          }) ?? []
      },
      openButtonInfo() {
        this.buttonIndex = -1
        this.buttonInfo = { sort: this.buttonList?.length + 1 }
        this.buttonVisible = true
        this.$nextTick(() => {
          this.$refs.buttonInfo.clearValidate()
        })
      },
      editButtonInfo(row, index) {
        this.buttonIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.buttonInfo = JSON.parse(JSON.stringify(row))
        this.buttonVisible = true
      },
      removeButtonInfo(row, index) {
        this.buttonList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
        this.updateExtensionButtonElements()
      },
      createButtonInfo() {
        this.$refs.buttonInfo.validate(valid => {
            if (valid) {
              if (this.buttonIndex === -1) {
                this.buttonList.push(this.buttonInfo)
              } else {
                this.buttonList.splice(this.buttonIndex, 1, this.buttonInfo)
              }
              this.buttonList.sort((a, b) => {
                return a.sort - b.sort
              })
              this.updateExtensionButtonElements()
              this.buttonVisible = false
            }
          }
        )
      },
      updateExtensionButtonElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:Buttons`) ?? []
        if (this.buttonList?.length) {
          const buttonElementList = this.buttonList.map(buttonInfo => {
            const buttonElementName = `${this.processInfo.processPrefix}:Button`
            return processHelper.createElement(buttonElementName, buttonInfo)
          })
          const buttonsElementName = `${this.processInfo.processPrefix}:Buttons`
          const buttonsElement = processHelper.createElement(buttonsElementName, { buttons: buttonElementList })
          extensionElements.get('values').push(buttonsElement)
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
