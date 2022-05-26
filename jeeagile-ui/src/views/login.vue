<template>
  <div class="login_container">
    <div class="login_box">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <h3 class="title">JeeAgile 敏捷快速开发平台</h3>
        <el-form-item prop="userName">
          <el-input v-model="loginForm.userName" type="text" auto-complete="off" placeholder="账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
                    @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input v-model="loginForm.captchaCode" auto-complete="off" placeholder="验证码" style="width: 63%"
                    @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCodeImage" class="login-code-img"/>
          </div>
        </el-form-item>
        <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button :loading="loading" size="medium" type="primary" style="width:100%;"
                     @click.native.prevent="handleLogin">
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2021-2022 JeeAgile All Rights Reserved.</span>
    </div>
    <vue-particles
      color="#dedede"
      :particleOpacity="0.7"
      :particlesNumber="80"
      shapeType="circle"
      :particleSize="4"
      linesColor="#dedede"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="3"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
    ></vue-particles>
  </div>
</template>


<script>
  import { getCodeImage } from '@/api/system/kaptcha'
  import Cookies from 'js-cookie'

  export default {
    name: 'Login',
    data() {
      return {
        codeUrl: '',
        loginForm: {
          userName: 'admin',
          password: '123456',
          uuid: undefined,
          captchaCode: undefined,
          rememberMe: false
        },
        loginRules: {
          userName: [
            { required: true, trigger: 'blur', message: '用户名不能为空' }
          ],
          password: [
            { required: true, trigger: 'blur', message: '密码不能为空' }
          ],
          captchaCode: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
        },
        loading: false,
        redirect: undefined
      }
    },
    watch: {
      $route: {
        handler: function (route) {
          this.redirect = route.query && route.query.redirect
        },
        immediate: true
      }
    },
    created() {
      this.getCodeImage()
      this.getCookie()
    },
    methods: {
      getCodeImage() {
        getCodeImage().then(res => {
          this.codeUrl = 'data:image/gif;base64,' + res.data.image
          this.loginForm.uuid = res.data.uuid
        })
      },
      getCookie() {
        const userName = Cookies.get('userName')
        const rememberMe = Cookies.get('rememberMe')
        this.loginForm = {
          userName: userName === undefined ? this.loginForm.userName : userName,
          password: this.loginForm.password,
          rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
        }
      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            if (this.loginForm.rememberMe) {
              Cookies.set('userName', this.loginForm.userName, { expires: 30 })
              Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
            } else {
              Cookies.remove('userName')
              Cookies.remove('rememberMe')
            }
            this.$store.dispatch('auth/login', this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || '/' })
              })
              .catch(() => {
                this.loading = false
                // this.getCode()
              })
          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  .login_container {
    background-image: linear-gradient(-180deg, #3726e7 0%, #129dc7 100%);
    background-repeat: no-repeat;
    background-size: cover;
    height: 100%;
  }

  .login_box {
    background-color: #2e527bb3;
    border-radius: 5px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
  }

  .title {
    margin: 0px auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    align-items: center;
    border-radius: 6px;
    background: #ffffff;
    width: 400px;
    padding: 25px 25px 5px 25px;

    .el-input {
      height: 38px;

      input {
        height: 38px;
      }
    }

    .input-icon {
      height: 39px;
      width: 14px;
      margin-left: 2px;
    }
  }

  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }

  .login-code {
    width: 33%;
    height: 38px;
    float: right;

    img {
      cursor: pointer;
      vertical-align: middle;
    }
  }

  .el-login-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial;
    font-size: 12px;
    letter-spacing: 1px;
  }

  .login-code-img {
    height: 38px;
  }
</style>
