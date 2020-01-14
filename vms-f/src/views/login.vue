<template>
  <div style="padding: 20px">
    <el-form :model="form" label-width="120" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.userName"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password" type="password" @keyup.enter.native="login"></el-input>
      </el-form-item>
      <el-form-item align="center">
        <el-button type="primary" @click="login">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        userName: "",
        password: ""
      }
    };
  },
  methods: {
    login() {
      this.$axios.post("/user/login", this.form).then(res => {
        res = res.data;
        if (res.code === 0) {
          this.$store.commit("clearMenu");
          this.$store.commit("setMenu", res.data.menu);
          this.$store.commit("initMenu", this.$router);
          this.$store.commit("setToken", res.data.token);
          this.$router.push({ name: "home" });
        } else {
          this.$message.warning(res.msg);
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.el-form {
  width: 50%;
  margin: auto;
  padding: 45px;
  height: 450px;
  background-color: #fff;
}
</style>
