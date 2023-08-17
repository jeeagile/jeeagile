import { getUserInfo, getUserMenu, login, logout } from '@/api/auth'
import { getUserToken, removeUserToken, setUserToken } from '@/utils/cookie'
import { agileRouter } from '@/router'
import Layout from '@/layout/index'
import { handleTree } from '@/utils/agile'
import { SysMenuKind } from '@/components/AgileDict/system'
import { OnlinePageType } from '@/components/AgileDict/online'

const state = {
  userName: '',
  userAvatar: '',
  userRole: [],
  userPerm: [],
  userRoute: [],
  userAddRoute: [],
  userToken: getUserToken()
}

const mutations = {
  SET_USER_NAME: (state, userName) => {
    state.userName = userName
  },
  SET_USER_AVATAR: (state, userAvatar) => {
    state.userAvatar = userAvatar
  },
  SET_USER_ROLE: (state, userRole) => {
    state.userRole = userRole
  },
  SET_USER_ROUTE: (state, routes) => {
    state.userAddRoute = routes
    state.userRoute = agileRouter.concat(routes)
  },
  SET_USER_PERM: (state, userPerm) => {
    state.userPerm = userPerm
  },
  SET_USER_TOKEN: (state, userToken) => {
    state.userToken = userToken
  }
}

const actions = {
  login({ commit }, loginUser) {
    return new Promise((resolve, reject) => {
      login(loginUser).then(response => {
        const { data } = response
        commit('SET_USER_TOKEN', data.userToken)
        setUserToken(data.userToken)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getUserInfo(state.userToken).then(response => {
        const userInfo = response.data
        const userAvatar = userInfo.userAvatar === '' ? require('@/assets/images/person.jpg') : process.env.VUE_APP_BASE_API + userInfo.userAvatar
        if (userInfo.userPermList && userInfo.userPermList.length > 0) {
          commit('SET_USER_ROLE', userInfo.userRoleList)
          commit('SET_USER_PERM', userInfo.userPermList)
        } else {
          commit('SET_USER_ROLE', ['ROLE_DEFAULT'])
        }
        commit('SET_USER_NAME', userInfo.userName)
        commit('SET_USER_AVATAR', userAvatar)
        resolve(userInfo)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getRoutes({ commit }) {
    return new Promise(resolve => {
      // 向后端请求路由数据
      getUserMenu().then(response => {
        let asyncRoutes = handleUserMenuTree(response.data)
        let accessedRoutes = filterAsyncRoutes(asyncRoutes)
        accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
        commit('SET_USER_ROUTE', accessedRoutes)
        resolve(accessedRoutes)
      })
    })
  },
  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_USER_TOKEN', '')
        commit('SET_USER_ROLE', [])
        commit('SET_USER_PERM', [])
        removeUserToken()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 前端 登出
  fedLogOut({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      removeUserToken()
      resolve()
    })
  },
  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_USER_TOKEN', '')
      commit('SET_USER_ROLE', [])
      commit('SET_USER_PERM', [])
      removeUserToken()
      resolve()
    })
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(asyncRoutes) {
  return asyncRoutes.filter(route => {
    if (route.component) {
      // Layout组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children !== null && route.children && route.children.length) {
      route.children = filterAsyncRoutes(route.children)
    }
    return true
  })
}

function handleUserMenuTree(userMenu) {
  const routerList = []
  userMenu.forEach(menu => {
    let router = {
      id: menu.id,
      parentId: menu.parentId,
      name: menu.menuName,
      component: handleMenuComponent(menu),
      path: handleMenuPath(menu),
      meta: {
        title: menu.menuName,
        icon: menu.menuIcon
      },
      props: {}
    }
    if (menu.menuKind === SysMenuKind.ONLINE || menu.menuKind === SysMenuKind.ORDER) {
      router.props.pageId = menu.pageId
      router.props.pageType = menu.menuKind === SysMenuKind.ONLINE ? OnlinePageType.QUERY : OnlinePageType.ORDER
    }
    routerList.push(router)
  })
  return handleTree(routerList)
}


function handleMenuPath(menu) {
  let routerPath = menu.menuPath
  // 非外链并且是一级目录（类型为目录）
  if (menu.parentId === '0' && 'M' === menu.menuType && menu.menuFrame === '1') {
    routerPath = '/' + menu.menuPath
  } else if (isMenuFrame(menu)) {
    routerPath = '/'
  }
  return routerPath
}

function handleMenuComponent(menu) {
  if (menu.menuKind === SysMenuKind.ONLINE || menu.menuKind === SysMenuKind.ORDER) {
    return 'online/index'
  }
  if (menu.menuComp && !isMenuFrame(menu)) {
    return menu.menuComp
  }
  return 'Layout'
}

function isMenuFrame(menu) {
  return menu.parentId === '0' && 'C' === menu.menuType && menu.menuFrame === '1'
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve).default
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
