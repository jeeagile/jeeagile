<template>
  <div class="rules-group-container tree box">
    <el-card class="box-card" style="margin-top: 10px">
      <el-main style="padding: 10px">
        <el-row>
          <el-row class="child">
            <el-radio-group v-model="object.rules" size="small">
              <el-radio-button label="and" value="and"></el-radio-button>
              <el-radio-button label="or" value="or"></el-radio-button>
            </el-radio-group>
            <el-button size="small" style="float: right; margin-left: 10px" type="primary" icon="el-icon-circle-plus-outline"
              @click="object.list.push({ rules: 'and', type: 'group', list: [{ types: '', site: '', symbol: '', rules: '', type: 'condition' }] })"
            >新增分组
            </el-button>
            <el-button size="small" style="float: right; margin-left: 10px" type="primary" icon="el-icon-circle-plus-outline"
              @click="object.list.push({ types: '', site: '', symbol: '', rules: '', type: 'condition' })"
            >新增条件
            </el-button>
          </el-row>

          <template v-for="(item, index) in object.list">
            <el-row :key="'condition' + index" class="child" v-if="item.type == 'condition'">
              <el-select v-model="item.types" size="small" style="width: 120px" placeholder="请选择">
                <el-option label="无" value="无"></el-option>
                <el-option label="19位编码" value="19位编码"></el-option>
                <el-option label="内部编码" value="内部编码"></el-option>
              </el-select>
              <!-- multiple  -->
              <el-select v-model="item.site" size="small" placeholder="请选择位置" style="margin-left: 10px; width: 150px">
                <el-option label="1" value="1"></el-option>
                <el-option label="2" value="2"></el-option>
                <el-option label="3" value="3"></el-option>
                <el-option label="4" value="4"></el-option>
                <el-option label="5" value="5"></el-option>
                <el-option label="6" value="6"></el-option>
                <el-option label="7" value="7"></el-option>
                <el-option label="8" value="8"></el-option>
                <el-option label="9" value="9"></el-option>
              </el-select>
              <el-select v-model="item.symbol" size="small" placeholder="请选择" style="margin-left: 10px; width: 100px">
                <el-option label="等于" value="=="></el-option>
                <el-option label="大于" value=">"></el-option>
                <el-option label="小于" value="<"></el-option>
                <el-option label="不等于" value="!="></el-option>
                <el-option label="包含" value="包含"></el-option>
                <el-option label="不包含" value="不包含"></el-option>
                <el-option label="以...开头" value="以...开头"></el-option>
                <el-option label="不以...开头" value="不以...开头"></el-option>
              </el-select>
              <el-input v-model="item.rules" size="small" style="width: 200px; margin-left: 10px"
                        placeholder="请输入规则"></el-input>
              <el-button @click="object.list.splice(index, 1)" size="small">删除</el-button>
            </el-row>
            <el-row v-if="item.type == 'group' && item.list.length > 0" :key="'group' + index" class="child">
              <create-rule :object="item"></create-rule>
            </el-row>
          </template>
        </el-row>
      </el-main>
    </el-card>
  </div>
</template>
<script>
  export default {
    name: 'CreateRule',
    props: {
      object: {
        type: Object,
        default: {
          rules: 'and',
          list: [{ type: '', site: '', symbol: '', rules: '' }]
        }
      }
    },
    data() {
      return {}
    }
  }

</script>
<style lang="scss" scoped>
  .box {
    width: 100%;
  }

  /* 只需要左边边框线 */
  .child {
    width: 100%;
    position: relative;
    border: 1px solid #d9d9d9;
    border-style: none none none solid;
    padding: 10px 0;
    padding-left: 12px;
  }

  /* 设置一个伪元素宽2px 高50% 用于遮挡多余的左边框线 */
  .child::before {
    display: block;
    content: '';
    position: absolute;
    background-color: white;
    width: 1px;
    height: 50%;
  }

  /* 设置第一个子元素的伪类定位 */
  .box .child:first-child::before {
    left: -1px;
    top: 0;
  }

  /* 设置第二个子元素的伪类定位 */
  .box .child:last-child::before {
    left: -1px;
    bottom: 0;
  }

  /* 设置子元素的横线，定位在高度的50% */
  .box .child::after {
    top: 50%;
    left: 0;
    position: absolute;
    content: '';
    display: block;
    width: 10px;
    height: 1px;
    border: 1px solid #d9d9d9;
    border-style: solid none none none;
  }
</style>
