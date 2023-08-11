const state = {
  onlinePageCache: {}
}

const mutations = {
  addOnlinePageCache: (state, data) => {
    state.onlinePageCache[data.key] = data.value
  },
  deleteOnlinePageCache: (state, key) => {
    delete state.onlinePageCache[key]
  },
  clearOnlinePageCache: (state) => {
    state.onlinePageCache = {}
  }
}

const actions = {}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
