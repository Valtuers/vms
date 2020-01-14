<template>
  <div>
    <el-dialog title="新增角色" :visible.sync="addRoleModal" width="30%">
      <el-form ref="addForm" :model="addForm" label-width="100px" size="mini">
        <el-form-item label="角色名称">
          <el-input v-model="addForm.name"></el-input>
        </el-form-item>
        <el-form-item label="角色菜单权限">
          <el-cascader v-model="addForm.menu" :options="menu" :props="{ expandTrigger: 'hover', multiple: true, value: 'id' }" collapse-tags clearable></el-cascader>
        </el-form-item>
        <el-form-item label="角色备注">
          <el-input v-model="addForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addRole" size="mini">确 定</el-button>
        <el-button
          @click="
            addRoleModal = false;
            addForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="编辑角色" :visible.sync="updateRoleModal" width="50%">
      <!-- <el-dialog width="30%" title="内层 Dialog" :visible.sync="innerVisible" append-to-body></el-dialog> -->
      <el-form ref="updateForm" :model="updateForm" label-width="100px" size="mini">
        <el-form-item label="角色名称">
          <el-input v-model="updateForm.name"></el-input>
        </el-form-item>
        <el-form-item label="角色备注">
          <el-input v-model="updateForm.remark"></el-input>
        </el-form-item>
        <el-form-item label="角色权限">
          <el-select v-model="updateForm.permissionList" value-key="id" multiple placeholder="请选择">
            <el-option v-for="item in permissionList" :key="item.id" :label="item.url" :value="item"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <!-- <el-table :data="updateForm.permissionList" style="width:80%;margin:auto;" size="mini" border>
        <el-table-column label="序号" type="index" align="center"></el-table-column>
        <el-table-column label="路由名称" align="center">
          <template slot-scope="scope">
            <span v-if="!scope.row.edit">{{ scope.row.url }}<i class="el-icon-edit-outline edit_icon" @click="scope.row.edit = true"></i></span>
            <div v-if="scope.row.edit">
              <el-input v-model="scope.row.url" size="mini" style="width:50%;"></el-input>
              <i class="el-icon-check edit_icon" @click="scope.row.edit = false"></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100px" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="
                updateRoleModal = true;
                updateForm = JSON.parse(JSON.stringify(scope.row));
              "
              >编辑</el-button
            >
            <el-button type="text" size="small" @click="delRole(scope.row)" style="color:red;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="addPermit_btn" @click="updateRoleModal = true">
        <i class="el-icon-plus"></i>
      </div> -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateRole" size="mini">确 定</el-button>
        <el-button
          @click="
            updateRoleModal = false;
            updateForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="新增权限" :visible.sync="addPermitModal" width="30%">
      <el-form ref="addPermitForm" :model="addPermitForm" label-width="100px" size="mini">
        <el-form-item label="权限名称">
          <el-input v-model="addPermitForm.url"></el-input>
        </el-form-item>
        <el-form-item label="权限排序">
          <el-input-number :min="0" :max="999" v-model="addPermitForm.sort"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addPermit" size="mini">确 定</el-button>
        <el-button
          @click="
            addPermitModal = false;
            addPermitForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="编辑权限" :visible.sync="updatePermitModal" width="30%">
      <el-form ref="updatePermitForm" :model="updatePermitForm" label-width="100px" size="mini">
        <el-form-item label="权限名称">
          <el-input v-model="updatePermitForm.url"></el-input>
        </el-form-item>
        <el-form-item label="权限排序">
          <el-input-number :min="0" :max="999" v-model="updatePermitForm.sort"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updatePermit" size="mini">确 定</el-button>
        <el-button
          @click="
            updatePermitModal = false;
            updatePermitForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-tabs v-model="activeName" @tab-click="tabsClick">
      <el-tab-pane label="角色管理" name="manager">
        <el-form :inline="true" :model="managerForm" class="demo-form-inline" size="mini">
          <el-form-item label="角色名称">
            <el-input v-model="managerForm.name" placeholder="角色名称" @keydown.enter.native="getRole"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getRole">搜索</el-button>
          </el-form-item>
          <el-form-item style="float:right;">
            <el-button type="success" @click="addRoleModal = true">新增</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="roleList" style="width: 100%" size="mini" border>
          <el-table-column label="序号" type="index" align="center"></el-table-column>
          <el-table-column label="角色名称" prop="name" align="center"></el-table-column>
          <el-table-column label="角色用户数" prop="userList.length" align="center"></el-table-column>
          <el-table-column label="备注" prop="remark" align="center"></el-table-column>
          <el-table-column label="操作" width="160px" align="center">
            <template slot-scope="scope">
              <!-- <el-button type="text" size="small" @click="showHasMenu(scope.row)">路由权限</el-button> -->
              <el-button
                type="text"
                size="small"
                @click="
                  updateRoleModal = true;
                  updateForm = JSON.parse(JSON.stringify(scope.row));
                "
                >编辑</el-button
              >
              <el-button type="text" size="small" @click="delRole(scope.row)" style="color:red;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="角色权限" name="permit">
        <el-form :inline="true" :model="permitForm" class="demo-form-inline" size="mini">
          <el-form-item label="权限路由">
            <el-input v-model="permitForm.url" placeholder="权限路由"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="getPermission">搜索</el-button>
          </el-form-item>
          <el-form-item style="float:right;">
            <el-button type="success" @click="addPermitModal = true">新增</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="permissionList" style="width: 100%" size="mini" border>
          <el-table-column label="序号" type="index" align="center"></el-table-column>
          <el-table-column label="权限路由" prop="url" align="center"></el-table-column>
          <el-table-column label="排序" prop="sort" align="center"></el-table-column>
          <el-table-column label="操作" width="120px" align="center">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click="
                  updatePermitModal = true;
                  updatePermitForm = JSON.parse(JSON.stringify(scope.row));
                "
                >修改</el-button
              >
              <el-button type="text" size="small" @click="delPermit(scope.row.id)" style="color:red;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: "manager",
      menu: [],
      roleList: [],
      permissionList: [],
      managerForm: {
        name: ""
      },
      addRoleModal: false,
      addForm: {
        name: "",
        remark: "",
        menu: []
      },
      updateRoleModal: false,
      updateForm: {
        name: "",
        remark: "",
        permissionList: []
      },
      permitForm: {
        url: ""
      },
      addPermitModal: false,
      addPermitForm: {
        url: "",
        sort: 0
      },
      updatePermitModal: false,
      updatePermitForm: {
        url: "",
        sort: 0
      }
    };
  },
  mounted() {
    this.getPermission();
    this.getRole();
    this.getMenu();
  },
  methods: {
    getMenu() {
      this.$axios.post("/menu/allMenu", this.managerForm).then(res => {
        this.menu = res.data.data;
      });
    },
    getPermission() {
      this.$axios.post("/role/getAllPermission", this.permitForm).then(res => {
        this.permissionList = res.data.data;
      });
    },
    getRole() {
      this.$axios.post("/role/getAllRoleAndUser", this.managerForm).then(res => {
        this.roleList = res.data.data;
      });
    },
    addRole() {
      let menu = [];
      if (this.addForm.menu) {
        this.addForm.menu.forEach(item => {
          menu = menu.concat(item);
        });
      }
      this.addForm.menu = [...new Set(menu)];
      this.$axios.post("/role/addRoleAndMenu", this.addForm).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.getRole();
          this.addRoleModal = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    updateRole() {
      this.updateForm.userList = null;
      this.$axios.post("/role/updateRole", this.updateForm).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.getRole();
          this.updateRoleModal = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    delRole(row) {
      this.$confirm("是否删除该角色?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$axios.post("/role/delRole", { id: row.id }).then(res => {
            if (res.data.code === 0) {
              this.$message.success(res.data.msg);
              this.getRole();
            } else {
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch(() => {});
    },
    addPermit() {
      this.$axios.post("/role/addPermission", this.addPermitForm).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.getPermission();
          this.addPermitModal = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    updatePermit() {
      this.$axios.post("/role/updatePermission", this.updatePermitForm).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.getPermission();
          this.updatePermitModal = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    delPermit(id) {
      this.$confirm("是否删除该权限?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$axios.post("/role/delPermission", { id: id }).then(res => {
            if (res.data.code === 0) {
              this.$message.success(res.data.msg);
              this.getPermission();
            } else {
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch(() => {});
    },
    tabsClick(tab) {
      if (tab.name == "permit") {
        this.getPermission();
      }
    }
  }
};
</script>

<style lang="scss">
.addPermit_btn {
  width: 80%;
  margin: auto;
  text-align: center;
  font-size: 22px;
  border-left: 1px solid #ebeef5;
  border-right: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
  padding: 5px;
  color: #a2a2a2;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    background: #f5f7fa;
    transition: all 0.3s;
  }
}
.edit_icon {
  cursor: pointer;
  font-size: 15px;
  margin-left: 5px;

  &:hover {
    color: #409eff;
  }
}
</style>
