import Cookie from "js-cookie";

export default {
  state: {
    menu: [],
    currentCrumb: {},
    tabList: [
      {
        path: "/home",
        name: "home",
        label: "首页",
        icon: "el-icon-s-home"
      }
    ]
  },
  mutations: {
    setMenu(state, val) {
      state.menu = val;
      Cookie.set("menu", JSON.stringify(val));
    },
    clearMenu(state) {
      state.menu = [];
      Cookie.remove("menu");
    },
    initMenu(state, router) {
      if (!Cookie.get("menu")) {
        return;
      }
      let menu = JSON.parse(Cookie.get("menu"));
      state.menu = menu;

      let currentMenu = [
        {
          path: "/",
          name: "main",
          component: () => import("@/views/main"),
          children: []
        }
      ];

      if (menu) {
        menu.forEach(item => {
          if (item.children) {
            item.children = item.children.map(jtem => {
              jtem.component = () => import(`@/views/${jtem.url}`);
              return jtem;
            });
            currentMenu[0].children.push(...item.children);
          } else {
            item.component = () => import(`@/views/${item.url}`);
            currentMenu[0].children.push(item);
          }
        });
      }
      router.addRoutes(currentMenu);
    },
    selectMenu(state, val) {
      if (val.name != "home") {
        state.currentCrumb = val;

        if (state.tabList.findIndex(item => item.name === val.name) === -1) {
          state.tabList.push(val);
        }
      } else {
        state.currentCrumb = {};
      }
    },
    closeTab(state, tag) {
      let index = state.tabList.findIndex(item => item.name === tag.name);

      state.tabList.splice(index, 1);
    }
  },
  actions: {}
};
