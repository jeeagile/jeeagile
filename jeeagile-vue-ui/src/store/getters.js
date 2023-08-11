const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  userName: state => state.auth.userName,
  userToken: state => state.auth.userToken,
  userAvatar: state => state.auth.userAvatar,
  userRole: state => state.auth.userRole,
  userRoute: state => state.auth.userRoute,
  userPerm: state => state.auth.userPerm,
  onlinePageCache: state => state.online.onlinePageCache
}
export default getters
