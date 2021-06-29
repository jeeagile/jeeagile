<template>
  <el-form ref="generatorInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>

      <el-col :span="12">
        <el-form-item prop="projectName">
          <span slot="label">
            项目名称
            <el-tooltip content="父项目名称，例如 jeeagile-demo" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.projectName" style="width: 280px"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="tableType">
          <span slot="label">表类型
          <el-tooltip content="数据库表类型单表或树形结构表" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.tableType" style="width: 280px">
            <el-option label="单表（增删改查）" value="crud"/>
            <el-option label="树表（增删改查）" value="tree"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            生成包路径
            <el-tooltip content="生成在哪个java包下，例如 com.jeeagile.demo" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName" style="width: 280px"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            生成模块名
            <el-tooltip content="可理解为子系统名，例如 system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName" style="width: 280px"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            生成业务名
            <el-tooltip content="可理解为功能英文名，例如 user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName" style="width: 280px"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            生成功能名
            <el-tooltip content="用作类描述，例如 用户管理" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName" style="width: 280px"/>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            上级菜单
            <el-tooltip content="分配到指定菜单下，例如 系统管理" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <treeselect :append-to-body="true" v-model="info.parentMenuId" :options="menus" :normalizer="normalizer"
                      :show-count="true" placeholder="请选择系统菜单" style="width: 280px"/>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="info.tableType == 'tree'">
      <h4 class="form-header">其他信息</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树编码字段
            <el-tooltip content="树显示的编码字段名， 如：dept_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" placeholder="请选择" style="width: 280px">
            <el-option
              v-for="column in info.agileGeneratorTableColumnList"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树父编码字段
            <el-tooltip content="树显示的父编码字段名， 如：parent_Id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" placeholder="请选择" style="width: 280px">
            <el-option
              v-for="column in info.agileGeneratorTableColumnList"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            树名称字段
            <el-tooltip content="树节点的显示名称字段名， 如：dept_name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" placeholder="请选择" style="width: 280px">
            <el-option
              v-for="column in info.agileGeneratorTableColumnList"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
  import Treeselect from '@riophae/vue-treeselect'
  import '@riophae/vue-treeselect/dist/vue-treeselect.css'

  export default {
    name: 'GeneratorInfoForm',
    components: { Treeselect },
    props: {
      info: {
        type: Object,
        default: null
      },
      menus: {
        type: Array,
        default: []
      }
    },
    data() {
      return {
        rules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
          ],
          templateType: [
            { required: true, message: '请选择模版类型', trigger: 'blur' }
          ],
          tableType: [
            { required: true, message: '请选择表类型', trigger: 'blur' }
          ],
          packageName: [
            { required: true, message: '请输入生成包路径', trigger: 'blur' }
          ],
          moduleName: [
            { required: true, message: '请输入生成模块名', trigger: 'blur' }
          ],
          businessName: [
            { required: true, message: '请输入生成业务名', trigger: 'blur' }
          ],
          functionName: [
            { required: true, message: '请输入生成功能名', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      /** 转换菜单数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children
        }
        return {
          id: node.id,
          label: node.menuName,
          children: node.children
        }
      }
    }
  }
</script>
