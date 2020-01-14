<template>
  <el-container style="height: 100%;">
    <el-aside width="auto">
      <el-menu :default-active="$route.path" class="el-menu-vertical-demo" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b" :collapse="isCollapse" router>
        <template v-for="item in $store.state.tab.menu">
          <el-menu-item :index="item.path" v-if="!item.children" :key="item.path" @click="$store.commit('selectMenu', item)">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.label }}</span>
          </el-menu-item>

          <el-submenu :index="item.path" v-if="item.children" :key="item.path">
            <template slot="title">
              <i :class="item.icon"></i>
              <span>{{ item.label }}</span>
            </template>
            <el-menu-item-group v-if="item.children">
              <el-menu-item :index="jtem.path" v-for="jtem in item.children" :key="jtem.path" @click="$store.commit('selectMenu', jtem)">{{ jtem.label }}</el-menu-item>
            </el-menu-item-group>
            <!-- <el-submenu :index="ktem.path" v-for="ktem in jtem.children" :key="ktem.path">
            <template slot="title">
              <i :class="ktem.icon"></i>
              <span>{{ ktem.label }}</span>
            </template>
            <el-menu-item :index="ktem.path">{{ ktem.label }}</el-menu-item>
          </el-submenu> -->
          </el-submenu>
        </template>
      </el-menu>
      <div class="box" @click="$store.commit('collapseMenu')">
        <i class="el-icon-d-arrow-left" style="margin: 0 -20px;" v-if="!$store.state.isCollapse"></i>
        <i class="el-icon-d-arrow-right" style="margin: 0 -20px;" v-if="$store.state.isCollapse"></i>
      </div>
    </el-aside>

    <el-container>
      <el-header>
        <div class="l-content">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :to="currentCrumb">{{ currentCrumb.label }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="r-content">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              <el-avatar :size="50" :src="$store.state.user.userInfo.avatar"> </el-avatar>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item @click.native="logOut">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <div style="display:flex;align-items:center;">
            <span style="margin-left:10px;color:#fff;font-size:15px;font-weight:600;width:75px;">{{ $store.state.user.userInfo.nickName }}</span>
          </div>
        </div>
      </el-header>
      <div class="tags">
        <el-tag
          type="warning"
          v-for="tag in tabList"
          :key="tag.path"
          @close="$store.commit('closeTab', tag)"
          size="small"
          :closable="tag.name != 'home'"
          :effect="$route.path == tag.path ? 'dark' : 'light'"
        >
          <router-link :to="tag.path">{{ tag.label }}</router-link>
        </el-tag>
      </div>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      menu: []
    };
  },
  mounted() {
    this.getMenu();
    this.getUserInfo();
  },
  computed: {
    ...mapState({
      isCollapse: state => state.isCollapse,
      currentCrumb: state => state.tab.currentCrumb,
      tabList: state => state.tab.tabList
    })
  },
  methods: {
    getMenu() {
      this.$axios.post("/menu/getMenu").then(res => {
        this.$store.commit("setMenu", res.data.data);
        this.$store.commit("initMenu", this.$router);
      });
    },
    getUserInfo() {
      this.$axios.post("/user/userInfo").then(res => {
        this.$store.state.user.userInfo = res.data.data;
      });
    },
    logOut() {
      this.$axios.post("/user/logout").then(() => {
        this.$store.commit("toLogin");
      });
    }
  }
};
</script>

<style lang="scss">
.el-header {
  display: flex;
  background: #333;
  color: #fff;
  align-items: center;
  padding: 0 20px;
  justify-content: space-between;

  .l-content {
    display: flex;
    align-items: center;

    .el-breadcrumb {
      .el-breadcrumb__item {
        .el-breadcrumb__inner {
          color: #666;
          font-weight: normal;
          cursor: pointer;
        }
        &:last-child {
          .el-breadcrumb__inner {
            color: #fff;
          }
        }
      }
    }
  }

  .r-content {
    display: flex;
    .el-avatar {
      cursor: pointer;

      img {
        width: 100%;
      }
    }
  }
}
.tags {
  padding: 5px 10px;

  .el-tag + .el-tag {
    margin-left: 10px;
  }
}
.el-aside {
  position: relative;
  overflow: visible;

  .el-menu {
    border: none;
    height: 100%;
    z-index: 2;

    &:not(.el-menu--collapse) {
      width: 200px;
      min-height: 100%;
    }
  }

  &:hover .box {
    transition: all 0.2s ease-in;
    transform: translateX(0);
  }
  .box {
    position: absolute;
    width: 0px;
    height: 110px;
    background-color: transparent;
    border-top: 10px solid transparent;
    border-right: 0px solid transparent;
    border-bottom: 10px solid transparent;
    border-left: 20px solid #545c64;
    top: 35%;
    right: -20px;
    display: flex;
    align-items: center;
    color: #fff;
    cursor: pointer;
    transform: translateX(-20px);
    transition: all 0.2s ease-in;
    z-index: 1;

    &:hover {
      color: #ffd04b;
    }
  }
}
</style>
