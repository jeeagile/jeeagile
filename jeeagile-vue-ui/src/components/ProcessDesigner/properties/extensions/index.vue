<template>
  <div class="properties-item__content">
    <el-table :data="propertyList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="属性名" align="center" min-width="60px" prop="name" show-overflow-tooltip/>
      <el-table-column label="属性值" align="center" min-width="90px" prop="value" show-overflow-tooltip/>
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
      layout="prev, pager, next">
    </el-pagination>
    <div class="button-drawer__line">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="openPropertyInfo">添加属性</el-button>
    </div>
    <el-dialog title="属性配置" :visible.sync="propertyVisible" width="450px" append-to-body destroy-on-close>
      <el-form ref="propertyInfo" :model="propertyInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="属性名:" prop="name">
          <el-input v-model="propertyInfo.name"/>
        </el-form-item>
        <el-form-item label="属性值" prop="value">
          <el-input v-model="propertyInfo.value"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="propertyVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createPropertyInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import processHelper from '../../helper/ProcessHelper'

  export default {
    name: 'Property',
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
        propertyList: [],
        propertyVisible: false,
        propertyIndex: -1,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        propertyInfo: {
          name: '',
          value: ''
        },
        rules: {
          name: [
            { required: true, message: '请录入属性名！', trigger: ['blur', 'change'] }
          ],
          value: [
            { required: true, message: '请录入属性值！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      activeElement: {
        handler() {
          if (this.activeElement) {
            this.initPropertyList()
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      initPropertyList() {
        this.propertyList = this.activeElement.businessObject?.extensionElements?.values
          .find(element => element.$type === `${this.processInfo.processPrefix}:Properties`)?.values
          .map(propertyElement => {
            return {
              name: propertyElement.name,
              value: propertyElement.value
            }
          }) ?? []
      },
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
        this.updateExtensionPropertyElements()
      },
      createPropertyInfo() {
        this.$refs.propertyInfo.validate(valid => {
            if (valid) {
              if (this.propertyIndex === -1) {
                this.propertyList.push(this.propertyInfo)
              } else {
                this.propertyList.splice(this.propertyIndex, 1, this.propertyInfo)
              }
              this.updateExtensionPropertyElements()
              this.propertyVisible = false
            }
          }
        )
      },
      updateExtensionPropertyElements() {
        let extensionElements = this.activeElement.businessObject.get('extensionElements')
        if (!extensionElements) {
          extensionElements = processHelper.createElement('bpmn:ExtensionElements')
        }
        extensionElements.values = extensionElements.values?.filter(item => item.$type !== `${this.processInfo.processPrefix}:Properties`) ?? []
        if (this.propertyList?.length) {
          const propertyElementList = this.propertyList.map(propertyInfo => {
            const propertyElementName = `${this.processInfo.processPrefix}:Property`
            return processHelper.createElement(propertyElementName, propertyInfo)
          })
          const propertiesElementName = `${this.processInfo.processPrefix}:Properties`
          const propertiesElement = processHelper.createElement(propertiesElementName, { values: propertyElementList })
          extensionElements.get('values').push(propertiesElement)
          processHelper.updateProperties(this.activeElement, { extensionElements: extensionElements })
        }
      }
    }
  }
</script>
<style lang='scss'>
</style>
