<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />手机号码
                <div class="pull-right">{{ user.userPhone }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" />用户邮箱
                <div class="pull-right">{{ user.userEmail }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree" />所属部门
                <div class="pull-right">{{ user.deptName }} </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属角色
                <div class="pull-right">{{ user.roleNameList }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" />所属岗位
                <div class="pull-right">{{ user.postNameList }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userInfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="userPwd">
              <userPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from './userAvatar'
import userInfo from './userInfo'
import userPwd from './userPwd'
import { getPersonInfo } from '@/api/system/person'

export default {
  name: 'Person',
  components: { userAvatar, userInfo, userPwd },
  data() {
    return {
      user: {},
      activeTab: 'userInfo'
    }
  },
  created() {
    this.getPersonInfo()
  },
  methods: {
    getPersonInfo() {
      getPersonInfo().then(response => {
        this.user = response.data
      })
    }
  }
}
</script>
