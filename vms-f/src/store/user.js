import Cookie from "js-cookie";

export default {
  state: {
    token: "",
    userInfo: {}
  },
  mutations: {
    setToken(state, val) {
      state.token = val;
      Cookie.set("token", val);
    },
    clearToken(state) {
      state.token = "";
      Cookie.remove("token");
    },
    getToken(state) {
      state.token = Cookie.get("token");
    },
    toLogin() {
      this.commit("clearToken");
      this.commit("clearMenu");
      location.reload();
    }
  },
  actions: {}
};
