import Vue from "vue";
import VueRouter from "vue-router";

Vue.use(VueRouter);
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err);
};

const routes = [
  {
    path: "/",
    name: "main",
    component: () => import("@/views/main")
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/login.vue")
  }
];

const router = new VueRouter({
  routes
});

export default router;
