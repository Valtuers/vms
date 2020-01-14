<template>
  <div>
    <el-dialog title="新增用户" :visible.sync="addUserModal" width="30%">
      <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
        <el-form-item label="头像" prop="avatar">
          <el-upload ref="uploadAvatar" class="avatar-uploader" action="" :on-change="uploadAvatar" :auto-upload="false" :show-file-list="false">
            <img v-if="addForm.avatar" :src="addForm.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="addForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称" prop="nickName">
          <el-input v-model="addForm.nickName"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addForm.gender" placeholder="请选择">
            <el-option label="未知" value="未知"></el-option>
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input type="number" v-model="addForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input type="email" v-model="addForm.email"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleList">
          <el-select v-model="addForm.roleList" placeholder="请选择" value-key="id" multiple>
            <el-option v-for="item in roleList" :key="item.id" :label="item.name" :value="item"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addUser" size="mini">确 定</el-button>
        <el-button
          @click="
            addUserModal = false;
            addForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-form :inline="true" :model="userForm" class="demo-form-inline" size="mini">
      <el-form-item label="用户名称">
        <el-input v-model="userForm.user_name" placeholder="用户名称" @keydown.enter.native="getUser"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getUser">搜索</el-button>
      </el-form-item>
      <el-form-item style="float:right;">
        <el-button type="success" @click="addUserModal = true">新增</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="userList.list" style="width: 100%" size="mini" border>
      <el-table-column label="序号" type="index" align="center"></el-table-column>
      <el-table-column label="用户名" prop="userName" align="center"></el-table-column>
      <el-table-column label="用户昵称" prop="nickName" align="center"></el-table-column>
      <el-table-column label="联系方式" prop="phone" align="center"></el-table-column>
      <el-table-column label="性别" prop="gender" align="center"></el-table-column>
      <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
      <el-table-column label="操作" width="160px" align="center">
        <template slot-scope="scope">
          <!-- <el-button type="text" size="small" @click="showHasMenu(scope.row)">路由权限</el-button> -->
          <!-- <el-button
            type="text"
            size="small"
            @click="
              updateRoleModal = true;
              updateForm = JSON.parse(JSON.stringify(scope.row));
            "
            >编辑</el-button
          > -->
          <el-button type="text" size="small" @click="delUser(scope.row)" style="color:red;">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" :total="userList.total" style="text-align: center;margin: 10px 0;"></el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      roleModal: false,
      roleList: [],
      userList: {},
      userForm: {
        user_name: "",
        index: 1,
        size: 10
      },
      addUserModal: false,
      addForm: {
        avatar: ""
      }
    };
  },
  mounted() {
    this.getUser();
    this.getRole();
  },
  methods: {
    getRole() {
      this.$axios.post("/role/getAllRoleAndUser", { name: "" }).then(res => {
        this.roleList = res.data.data;
      });
    },
    getUser() {
      this.$axios.post("/user/getUserByPage", this.userForm).then(res => {
        this.userList = res.data.data;
      });
    },
    addUser() {
      this.$axios.post("/user/addUser", this.addForm).then(res => {
        if (res.data.code == 0) {
          this.$message.success(res.data.msg);
          this.getUser();
          this.addUserModal = false;
          this.$refs.addForm.resetFields();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    delUser(row) {
      this.$confirm("是否删除该用户?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$axios.post("/user/delUser", row).then(res => {
            if (res.data.code == 0) {
              this.$message.success(res.data.msg);
              this.getUser();
              this.addUserModal = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch(() => {});
    },
    uploadAvatar(file) {
      let reader = new FileReader();
      reader.readAsDataURL(file.raw);
      reader.onload = e => {
        this.addForm.avatar = e.target.result;
      };
    }
  }
};
</script>

<style lang="scss">
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 22px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  line-height: 78px;
  text-align: center;
}
.avatar {
  display: block;
  width: 78px;
  height: 78px;
  object-fit: cover;
}
</style>
