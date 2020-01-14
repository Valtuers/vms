import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

import "element-ui/lib/theme-chalk/index.css";
import ElementUI from "element-ui";
import axios from "axios";

Vue.use(ElementUI);

Vue.prototype.$axios = axios;
// axios.defaults.baseURL = "http://47.113.109.9:9090";
// axios.defaults.baseURL = "http://172.18.253.191:9090";
axios.defaults.baseURL = "http://localhost:9090";

// http request拦截器 添加一个请求拦截器
axios.interceptors.request.use(
  function(config) {
    let token = store.state.user.token;
    if (token) {
      //将token放到请求头发送给服务器,将token放在请求头中
      config.headers.token = token;
    }
    return config;
  },
  function(error) {
    return Promise.reject(error);
  }
);
axios.interceptors.response.use(
  function(response) {
    if (response.data.code === 2) {
      store.commit("toLogin");
    } else if (response.data.code === 3) {
      ElementUI.Message({
        type: "warning",
        message: response.data.msg,
        duration: 3000
      });
    }
    return response;
  },
  function(error) {
    return Promise.reject(error);
  }
);

router.beforeEach((to, from, next) => {
  store.commit("getToken");
  let token = store.state.user.token;
  if (!token && to.name !== "login") {
    store.commit("clearToken");
    store.commit("clearMenu");
    next({ name: "login" });
  } else {
    next();
  }
});

Vue.config.productionTip = false;

new Vue({
  created() {
    this.$store.commit("initMenu", this.$router);
  },
  router,
  store,
  render: h => h(App)
}).$mount("#app");
