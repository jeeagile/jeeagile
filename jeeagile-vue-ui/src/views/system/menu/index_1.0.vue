<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParam" :inline="true">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="queryParam.menuName" placeholder="请输入菜单名称" clearable size="small"
                  @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="menuStatus">
        <el-select v-model="queryParam.menuStatus" placeholder="菜单状态" clearable size="small">
          <el-option v-for="item in AgileNormalDisable.getList()" :key="item.value"
                     :label="item.label" :value="item.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPerm="['system:menu:add']">
          新增
        </el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleUpdateSort"
                   v-hasPerm="['system:menu:sort']">
          保存排序
        </el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getMenuList"/>
    </el-row>

    <el-table v-loading="loading" :data="menuTreeList" row-key="id"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true"/>
      <el-table-column prop="menuIcon" label="图标" align="center" style="width: 100px">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.menuIcon"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="menuSort" align="center" width="100px">
        <template slot-scope="scope">
          <el-input-number v-model="scope.row.menuSort" size="mini" controls-position="right" :min="0"
                           @change="changeMenuSort(scope.row)" style="width: 80px"/>
        </template>
      </el-table-column>
      <el-table-column label="类型" prop="menuType" align="center" width="60px">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.menuType === 'M'" size="mini">目录</el-tag>
          <el-tag v-else-if="scope.row.menuType === 'C'" type="success" size="mini">菜单</el-tag>
          <el-tag v-else-if="scope.row.menuType === 'F'" type="info" size="mini">权限</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="menuComp" align="center" label="组件路径" :show-overflow-tooltip="true"/>
      <el-table-column prop="menuStatus" label="菜单状态" :formatter="menuStatusFormat" align="center" width="80"/>
      <el-table-column prop="menuVisible" label="显示状态" :formatter="menuVisibleFormat" align="center" width="80"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     v-hasPerm="['system:menu:add']">
            新增
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     v-hasPerm="['system:menu:update']">
            修改
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPerm="['system:menu:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="openDialog" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="上级菜单">
              <tree-select v-model="form.parentId" :options="menuTreeOptionList" :normalizer="normalizer"
                           :show-count="true" placeholder="选择上级菜单"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="menuSort">
              <el-input-number v-model="form.menuSort" controls-position="right" :min="0" style="width: 100%;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="菜单图标">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selectedMenuIcon"/>
                <el-input slot="reference" v-model="form.menuIcon" placeholder="点击选择图标" readonly>
                  <svg-icon v-if="form.menuIcon" slot="prefix" :icon-class="form.menuIcon" class="el-input__icon"
                            style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon"/>
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'" label="菜单分类" prop="menuKind">
              <el-select v-model="form.menuKind" placeholder="菜单分类" @change="onMenuKindChange" clearable>
                <el-option
                  v-for="option in SysMenuKind.getList()"
                  :key="option.key"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType == 'C' && form.menuKind == SysMenuKind.ONLINE" :span="12">
            <el-form-item label="在线表单" prop="pageId">
              <el-cascader v-model="formPageId" :options="formPageData" filterable
                           :clearable="true" placeholder="选择菜单绑定的在线表单"
                           :props="{value: 'id', label: 'name'}"
                           @change="onOnlinePageChange"/>
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType == 'C' && form.menuKind == SysMenuKind.ORDER" :span="12">
            <el-form-item label="在线流程" prop="processId">
              <el-cascader v-model="processPageId" :options="processPageData" filterable
                           :clearable="true" placeholder="选择菜单绑定的流程工单"
                           :props="{value: 'id', label: 'name'}"
                           @change="onProcessPageChange"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="路由地址" prop="menuPath">
              <el-input v-model="form.menuPath" placeholder="请输入路由地址"/>
            </el-form-item>
          </el-col>

          <el-col v-if="form.menuType == 'C' && form.menuKind == SysMenuKind.ROUTER" :span="12">
            <el-form-item label="组件路径" prop="menuComp">
              <el-input v-model="form.menuComp" placeholder="请输入组件路径"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'" label="权限标识">
              <el-input v-model="form.menuPerm" placeholder="请权限标识" maxlength="50"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="是否外链">
              <el-radio-group v-model="form.menuFrame">
                <el-radio v-for="item in AgileYesNo.getList()" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="显示状态">
              <el-radio-group v-model="form.menuVisible">
                <el-radio v-for="item in AgileShowHide.getList()" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" label="菜单状态">
              <el-radio-group v-model="form.menuStatus">
                <el-radio v-for="item in AgileNormalDisable.getList()" :key="item.value" :label="item.value">
                  {{ item.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { selectMenuList, detailMenu, deleteMenu, addMenu, updateMenu, updateMenuSort } from '@/api/system/menu'
  import { selectOnlineFormPageList } from '@/api/online/form'
  import {selectProcessOnlinePageList} from '@/api/process/model'
  import IconSelect from '@/components/IconSelect'
  import _ from 'lodash'

  export default {
    name: 'Menu',
    components: { IconSelect },
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 菜单表格树数据
        menuTreeList: [],
        // 菜单树选项
        menuTreeOptionList: [],
        // 弹出层标题
        dialogTitle: '',
        // 是否显示弹出层
        openDialog: false,
        // 修改菜单排序记录
        menuSortList: [],
        // 查询参数
        queryParam: {
          menuName: undefined,
          menuVisible: undefined
        },
        // 表单页面ID
        formPageId: undefined,
        formPageData: [],
        // 流程表单页面ID
        processPageId: undefined,
        processPageData: [],
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          menuName: [
            { required: true, message: '菜单名称不能为空', trigger: 'blur' }
          ],
          menuSort: [
            { required: true, message: '菜单顺序不能为空', trigger: 'blur' }
          ],
          menusPath: [
            { required: true, message: '路由地址不能为空', trigger: 'blur' }
          ],
          menusKind: [
            { required: true, message: '菜单分类不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getMenuList()
      this.getOnlineFormPageList()
      this.getOnlineProcessPageList()
    },
    methods: {
      /** 选择图标 */
      selectedMenuIcon(name) {
        this.form.menuIcon = name
      },
      /** 查询菜单列表 */
      getMenuList() {
        this.loading = true
        selectMenuList(this.queryParam).then(response => {
          this.menuTreeList = this.handleTree(response.data)
          this.loading = false
        })
      },
      /** 查询在线表单列表 */
      getOnlineFormPageList() {
        selectOnlineFormPageList().then(response => {
          let formList = response.data.onlineFormList
          let pageList = response.data.onlinePageList
          if (!Array.isArray(formList) || !Array.isArray(pageList)) {
            this.formPageData = []
            return
          }
          this.formPageData = formList.map(form => {
            let children = pageList.filter(page => {
              return form.id === page.formId
            }).map(page => {
              return {
                id: page.id,
                name: page.pageName
              }
            })

            return {
              id: form.id,
              name: form.formName,
              children
            }
          })
          if (this.form.pageId) {
            this.formPageId = this.findTreeNodePath(this.formPageData, this.form.pageId, 'id')
          }
        })
      },
      /** 查询在线流程表单列表 */
      getOnlineProcessPageList() {
        selectProcessOnlinePageList().then(response => {
          let processList = response.data.processList
          let pageList = response.data.orderPageList
          if (!Array.isArray(processList) || !Array.isArray(pageList)) {
            this.processPageData = []
            return
          }
          this.processPageData = processList.map(process => {
            let children = pageList.filter(page => {
              return process.formId === page.formId
            }).map(page => {
              return {
                id: page.id,
                name: page.pageName
              }
            })

            return {
              id: process.id,
              name: process.modelName,
              children
            }
          })
          if (this.form.pageId && this.form.processId) {
            this.processPageId = this.findTreeNodePath(this.formPageData, this.form.pageId, 'id')
          }
        })
      },
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
      },
      /** 查询菜单下拉树结构 */
      getMenuTreeSelect() {
        selectMenuList().then(response => {
          this.menuTreeOptionList = []
          const menu = { id: 0, menuName: '主类目', children: [] }
          menu.children = this.handleTree(response.data)
          this.menuTreeOptionList.push(menu)
        })
      },
      /** 菜单状态字典翻译 */
      menuStatusFormat(row) {
        if (row.menuType == 'F') {
          return ''
        }
        return this.AgileNormalDisable.getLabel(row.menuStatus)
      },
      /** 菜单显示状态字典翻译 */
      menuVisibleFormat(row) {
        if (row.menuType == 'F') {
          return ''
        }
        return this.AgileShowHide.getLabel(row.menuVisible)
      },
      /** 取消按钮 */
      cancel() {
        this.openDialog = false
        this.reset()
      },
      /** 表单重置 */
      reset() {
        this.form = {
          id: undefined,
          parentId: 0,
          menuName: undefined,
          menuIcon: undefined,
          menuComp: '',
          menuPath: '',
          menuType: 'M',
          menuSort: 0,
          menuKind: '01',
          processId: undefined,
          pageId: undefined,
          menuPerm: '',
          menuFrame: '0',
          menuVisible: '0',
          menuStatus: '0'
        }
        this.menuSortList = []
        this.formPageId = undefined
        this.processPageId = undefined
        this.resetForm('form')
      },
      /** 更改菜单排序 */
      changeMenuSort(row) {
        const currentSort = _.find(this.menuSortList, item => item.id === row.id)
        if (currentSort) {
          currentSort.menuSort = row.menuSort
        } else {
          this.menuSortList.push({
            id: row.id,
            menuSort: row.menuSort
          })
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getMenuList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      /** 修改在线表单 */
      onOnlinePageChange(values) {
        this.form.pageId = values[1]
      },
      /** 修改流程表单ID */
      onProcessPageChange(values) {
        this.form.processId = values[0]
        this.form.pageId = values[1]
      },
      onMenuKindChange() {
        if (this.form.menuKind === this.SysMenuKind.ROUTER) {
          this.form.processId = undefined
          this.form.pageId = undefined
        } else {
          this.form.menuPath = undefined
        }
      },
      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset()
        this.getMenuTreeSelect()
        if (row != null && row.id) {
          this.form.parentId = row.id
        } else {
          this.form.parentId = 0
        }
        this.openDialog = true
        this.dialogTitle = '添加菜单'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        this.getMenuTreeSelect()
        detailMenu(row.id).then(response => {
          this.form = response.data
          if (this.form.menuKind === this.SysMenuKind.ONLINE) {
            this.formPageId = this.findTreeNodePath(this.formPageData, this.form.pageId, 'id')
          }
          if (this.form.menuKind === this.SysMenuKind.ORDER) {
            this.processPageId = this.findTreeNodePath(this.processPageData, this.form.pageId, 'id')
          }
          this.openDialog = true
          this.dialogTitle = '修改菜单'
        })
      },
      /** 提交按钮 */
      submitForm: function () {
        if (this.form.menuType !== 'C') {
          this.form.menuKind = undefined
          this.form.pageId = undefined
        }
        if (this.form.menuKind === this.SysMenuKind.ROUTER) {
          this.form.pageId = undefined
          this.form.processId = undefined
        }
        if (this.form.menuType == 'C' && this.form.menuKind !== this.SysMenuKind.ROUTER && !this.form.pageId) {
          this.messageWarning('表单ID不能为空！')
          return
        }
        this.$refs.form.validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateMenu(this.form).then(() => {
                this.messageSuccess('修改成功')
                this.openDialog = false
                this.getMenuList()
              })
            } else {
              addMenu(this.form).then(() => {
                this.messageSuccess('新增成功')
                this.openDialog = false
                this.getMenuList()
              })
            }
          }
        })
      },
      /** 保存排序操作 */
      handleUpdateSort() {
        updateMenuSort(this.menuSortList).then(() => {
          this.messageSuccess('修改成功')
          this.openDialog = false
          this.menuSortList = []
          this.getMenuList()
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除名称为"' + row.menuName + '"的数据项?', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          return deleteMenu(row.id)
        }).then(() => {
          this.getMenuList()
          this.messageSuccess('删除成功')
        })
      }
    }
  }
</script>
