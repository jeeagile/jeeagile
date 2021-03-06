import Vue from 'vue'
import VueRouter from 'vue-router'

import store from '@/store'

import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import { Message } from 'element-ui'

import { getUserToken, getUserTenantId, getUserTenantSign } from '@/utils/cookie'

import Layout from '@/layout/index'

Vue.use(VueRouter)

export const agileRouter = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/components/Redirect')
      }
    ]
  },
  {
    path: '/404',
    hidden: true,
    component: () => import('@/components/Error/404')
  },
  {
    path: '/401',
    hidden: true,
    component: () => import('@/components/Error/401')
  },
  {
    path: '/login',
    hidden: true,
    component: () => import('@/views/login')
  },
  {
    path: '',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true },
        component: () => import('@/views/home')
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'person',
        name: 'Person',
        component: () => import('@/views/person/index'),
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictTypeId(.*)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/tool',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'form/drawer',
        component: () => import('@/views/tool/form/FormDrawer'),
        name: 'FormDrawer',
        meta: { title: '表单运行', icon: '' },
        params: {}
      }
    ]
  },
  {
    path: '/quartz',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'logger/:jobCode(.*)',
        component: () => import('@/views/quartz/logger/index'),
        name: 'JobLogger',
        meta: { title: '执行日志' }
      }
    ]
  },
  {
    path: '/generator',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(.*)',
        component: (resolve) => require(['@/views/tool/generator/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  },
  {
    path: '/process',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'form/designer/:formId(.*)',
        component: (resolve) => require(['@/views/process/form/designer'], resolve),
        name: 'FormDesigner',
        meta: { title: '表单设计' }
      },
      {
        path: 'designer/:modelId(.*)',
        component: (resolve) => require(['@/views/process/designer/index'], resolve),
        name: 'ProcessDesigner',
        meta: { title: '流程设计' }
      },
      {
        path: 'definition/:modelId(.*)',
        component: (resolve) => require(['@/views/process/definition/index'], resolve),
        name: 'ProcessDefinition',
        meta: { title: '流程定义' }
      },
      {
        path: 'instance/detail/:instanceId(.*)',
        component: (resolve) => require(['@/views/process/instance/detail'], resolve),
        name: 'DetailInstance',
        meta: { title: '流程实例信息' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: agileRouter
})

export default router

NProgress.configure({ showSpinner: false })
const whiteList = ['/login'] // no Redirect whitelist
router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getUserToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      if (store.getters.userRole.length === 0) {
        store.dispatch('auth/getInfo').then(() => {
          store.dispatch('auth/getRoutes').then(accessRoutes => {
            if (accessRoutes instanceof Array) {
              for (const itemRoute of accessRoutes) {
                router.addRoute(itemRoute)
              }
            } else {
              router.addRoute(accessRoutes)
            }
            next({ ...to, replace: true })
          })
        }).catch(err => {
          store.dispatch('auth/fedLogOut').then(() => {
            Message.error(err)
            const tenantId = getUserTenantId()
            const tenantSign = getUserTenantSign()
            if (tenantId && tenantSign) {
              next({ path: `/login?tenantId=${tenantId}&tenantSign=${tenantSign}` })
            } else {
              next({ path: '/login' })
            }
          })
        })
      } else {
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      const tenantId = getUserTenantId()
      const tenantSign = getUserTenantSign()
      if (tenantId && tenantSign) {
        next(`/login?tenantId=${tenantId}&tenantSign=${tenantSign}`)
      } else {
        next(`/login?redirect=${to.fullPath}`)
      }
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
