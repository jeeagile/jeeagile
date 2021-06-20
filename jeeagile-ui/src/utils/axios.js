import axios from 'axios'
import store from '@/store'
import {Message, MessageBox} from 'element-ui'
import {getUserToken} from '@/utils/cookie'
// api 的 base_url
const baseURL = process.env.VUE_APP_BASE_API

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时时间设置
  timeout: 60 * 1000
})

// request 拦截器
service.interceptors.request.use(config => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    if (getUserToken() && !isToken) {
      config.headers['agile-token'] = getUserToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
  },
  error => {
    Promise.reject(error).then(r => error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    let res = response.data
    if (response.request.responseType === 'blob') { // 文件二进制流响应全部数据（PS:文件名在请求头中）
      res = binaryParser(response)
    }
    if (res.code === '1000' || res.code === '1001') {
      MessageBox.confirm('用户登录状态已过期，您可以继续留在该页面，或重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('user/fedLogOut').then(() => {
          window.location.href = '/'
        })
      })
    } else if (res.code === '1002') {
      Message.error(res.message || '权限不足!')
      window.location.href = '/401'
    } else if (res.code !== '0000' && res.message) {
      // 弹警告信息
      Message.warning(res.message)
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    Message.error(message)
    return Promise.reject(error)
  }
)

/**
 * 发送ajax请求
 * @param    {String}  url     地址
 * @param    {String}  method  请求方式 get post put delete
 * @param    {Object}  data    参数对象
 * @param    {Object}  ctx 参数
 */
const callApi = (url, method, data, ctx) => {
  if (process.env.NODE_ENV === 'development') {
    // 开发环境
    if (url.indexOf('?') > -1) {
      url += '&t=' + Math.random()
    } else {
      url += '?t=' + Math.random()
    }
  }
  return service(
    Object.assign(
      {},
      {
        url: `${url}`,
        method,
        params: method === 'get' ? data : {},
        data: method !== 'get' ? data : {},
        withCredentials: false
      },
      ctx
    )
  )
}

/**
 * 请求接口
 * @param url 接口地址
 * @param data 接口参数
 * @param ctx 所有请求参数
 */
export const getApi = (url, data = {}, ctx = {}) => callApi(url, 'get', data, ctx)
export const postApi = (url, data = {}, ctx = {}) => callApi(url, 'post', data, ctx)
export const putApi = (url, data = {}, ctx = {}) => callApi(url, 'put', data, ctx)
export const deleteApi = (url, data = {}, ctx = {}) => callApi(url, 'delete', data, ctx)

/**
 * 上传请求地址
 * @param url 上传地址
 */
export const upLoadApi = url => baseURL + url

/**
 * 添加基本地址
 * @param url 地址
 */
export const addBaseURL = url => baseURL + url

/**
 * 二进制响应解析
 */
const binaryParser = (response) => {
  return new Promise((resolve, reject) => {
    let resData = ''
    let fileName
    if (response.headers['content-disposition']) {
      fileName = response.headers['content-disposition'].split(';')[1].split('=')[1]
    }
    const blob = response.data
    if (!fileName) {
      const errorData = new FileReader()
      errorData.addEventListener('loadend', (data) => {
        try {
          resData = JSON.parse(data.target.result)
        } catch (e) {
          resData = ''
        }
        resolve(resData)
      })
      errorData.readAsText(blob)
    } else {
      resData = {
        fileName: window.decodeURIComponent(fileName),
        blob: response.data
      }
      const reader = new FileReader()
      reader.readAsDataURL(blob)
      reader.onload = (e) => {
        const aEl = document.createElement('a')
        aEl.download = window.decodeURIComponent(resData.fileName)
        aEl.href = e.target.result
        document.body.appendChild(aEl)
        aEl.click()
        document.body.removeChild(aEl)
        resData = {
          code: '0000',
          message: '成功'
        }
        resolve(resData)
      }
    }
  })
}
