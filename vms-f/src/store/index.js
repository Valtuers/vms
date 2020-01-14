import Vue from "vue";
import Vuex from "vuex";
import tab from "./tab.js";
import user from "./user.js";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    isCollapse: false
  },
  mutations: {
    collapseMenu(state) {
      state.isCollapse = !state.isCollapse;
    }
  },
  actions: {},
  modules: {
    user,
    tab
  }
});
