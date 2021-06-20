const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  userName: state => state.user.userName,
  userToken: state => state.user.userToken,
  userAvatar: state => state.user.userAvatar,
  userRole: state => state.user.userRole,
  userRoute: state => state.user.userRoute,
  userPerm: state => state.user.userPerm

}
export default getters
