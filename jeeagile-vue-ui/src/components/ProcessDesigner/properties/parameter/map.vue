<template>
  <div class="properties-option__content">
    <p class="filed-config__title">
      <span>Map列表:</span>
      <el-button size="mini" type="primary" @click="openMapInfo()">新增键值</el-button>
    </p>
    <el-table :data="mapList.slice((pageInfo.currentPage - 1) * pageInfo.pageSize, pageInfo.currentPage * pageInfo.pageSize)" size="mini" border>
      <el-table-column label="Key" align="center" min-width="70px" prop="key" show-overflow-tooltip/>
      <el-table-column label="值" align="center" min-width="90px" prop="value" show-overflow-tooltip/>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row, $index }">
          <el-button size="mini" type="text" @click="editMapInfo(row, $index)">编辑</el-button>
          <el-divider direction="vertical"/>
          <el-button size="mini" type="text" @click="removeMapInfo(row, $index)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      :total="mapList.length"
      :page-size="pageInfo.pageSize"
      :page-count="pageInfo.pageCount"
      :current-page.sync="pageInfo.currentPage"
      layout="prev, pager, next"
    />
    <el-dialog title="添加值" :visible.sync="mapVisible" width="350px" append-to-body destroy-on-close>
      <el-form ref="mapInfo" :model="mapInfo" :rules="rules" :inline="false" label-width="100px" size="small" label-position="right">
        <el-form-item label="Key:" prop="key">
          <el-input v-model="mapInfo.key"/>
        </el-form-item>
        <el-form-item label="值:" prop="value">
          <el-input v-model="mapInfo.value"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="mini" @click="mapVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createMapInfoInfo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'MapList',
    model: {
      prop: 'maps',
      event: 'mapList'
    },
    props: {
      maps: {
        type: Array,
        required: true,
        default: () => []
      }
    },
    data() {
      return {
        mapList: [],
        mapVisible: false,
        pageInfo: {
          pageSize: 3,    // 每页的数据条数
          currentPage: 1, // 默认开始页面
          pageCount: 5    // 最大显示页数
        },
        mapIndex: -1,
        mapInfo: {
          key: '',
          value: ''
        },
        rules: {
          map: [
            { required: true, message: '请录入值！', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    watch: {
      maps: {
        handler() {
          if (this.maps) {
            this.mapList = this.maps
          } else {
            this.mapList = []
          }
        },
        deep: true,
        immediate: true
      }
    },
    methods: {
      openMapInfo() {
        this.mapIndex = -1
        this.mapInfo = {}
        this.mapVisible = true
        this.$nextTick(() => {
          this.$refs.mapInfo.clearValidate()
        })
      },
      editMapInfo(row, index) {
        this.mapIndex = (this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index
        this.mapInfo = JSON.parse(JSON.stringify(row))
        this.mapVisible = true
      },
      removeMapInfo(row, index) {
        this.mapList.splice((this.pageInfo.currentPage - 1) * this.pageInfo.pageSize + index, 1)
      },
      createMapInfoInfo() {
        this.$refs.mapInfo.validate(valid => {
            if (valid) {
              if (this.mapIndex === -1) {
                this.mapList.push(this.mapInfo)
              } else {
                this.mapList.splice(this.mapIndex, 1, this.mapInfo)
              }
              this.$emit('mapList', this.mapList)
              this.mapVisible = false
            }
          }
        )
      }
    }
  }
</script>

<style lang='scss'>

</style>
