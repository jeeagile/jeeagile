<template>
  <el-form ref="form" :model="userPwd" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPwd">
      <el-input v-model="userPwd.oldPwd" placeholder="请输入旧密码" type="password"/>
    </el-form-item>
    <el-form-item label="新密码" prop="newPwd">
      <el-input v-model="userPwd.newPwd" placeholder="请输入新密码" type="password"/>
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPwd">
      <el-input v-model="userPwd.confirmPwd" placeholder="请确认密码" type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import { updatePersonPwd } from '@/api/system/person'

  export default {
    data() {
      const equalToPwd = (rule, value, callback) => {
        if (this.userPwd.newPwd !== value) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      }
      return {
        test: '1test',
        userPwd: {
          oldPwd: undefined,
          newPwd: undefined,
          confirmPwd: undefined
        },
        // 表单校验
        rules: {
          oldPwd: [
            { required: true, message: '旧密码不能为空', trigger: 'blur' }
          ],
          newPwd: [
            { required: true, message: '新密码不能为空', trigger: 'blur' },
            { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
          ],
          confirmPwd: [
            { required: true, message: '确认密码不能为空', trigger: 'blur' },
            { required: true, validator: equalToPwd, trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      submit() {
        this.$refs.form.validate(valid => {
          if (valid) {
            updatePersonPwd(this.userPwd).then(response => {
                this.messageSuccess('修改成功')
              }
            )
          }
        })
      },
      close() {
        this.$store.dispatch('tagsView/delView', this.$route)
        this.$router.push({ path: '/home' })
      }
    }
  }
</script>
