<template>
  <div>
    <el-dialog title="新增菜单" :visible.sync="addMenuModal" width="30%">
      <el-form ref="addForm" :model="addForm" label-width="80px" size="mini">
        <el-form-item label="菜单名称" prop="label">
          <el-input v-model="addForm.label"></el-input>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="addForm.path"></el-input>
        </el-form-item>
        <!-- <el-form-item label="菜单图标" v-if="addForm.level != 1">
          <el-input v-model="addForm.icon"></el-input>
        </el-form-item> -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addMenu" size="mini">确 定</el-button>
        <el-button
          @click="
            addMenuModal = false;
            addForm = { label: '', path: '', icon: '' };
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="修改菜单" :visible.sync="updateMenuModal" width="30%">
      <el-form ref="updateForm" :model="updateForm" label-width="80px" size="mini">
        <el-form-item label="菜单名称" prop="label">
          <el-input v-model="updateForm.label"></el-input>
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="updateForm.path"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="updateForm.icon"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="updateMenu" size="mini">确 定</el-button>
        <el-button
          @click="
            updateMenuModal = false;
            updateForm = {};
          "
          size="mini"
          >取 消</el-button
        >
      </span>
    </el-dialog>
    <el-dialog title="菜单权限" :visible.sync="setMenuModal.show" width="30%">
      <el-input placeholder="查找菜单" v-model="filterMenu" size="mini"></el-input>
      <el-tree ref="menuPermit" :data="menu" node-key="id" size="mini" :filter-node-method="filterNode" check-strictly show-checkbox>
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span>{{ data.id }}</span>
        </span>
      </el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="setMenuPermit">确 定</el-button>
        <el-button @click="setMenuModal.show = false" size="mini">取 消</el-button>
      </span>
    </el-dialog>
    <el-tabs v-model="activeName" @tab-click="tabsClick">
      <el-tab-pane label="菜单管理" name="manager">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>菜单管理</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="addMenuModal = true">新增</el-button>
          </div>
          <div class="text item">
            <el-tree ref="menuList" :data="menu" node-key="id" size="mini" @node-drop="sortBtn = false" :allow-drop="allowDrop" show-checkbox highlight-current draggable>
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
                <span class="list_oper">
                  <el-button
                    type="text"
                    class="btn_item"
                    v-if="node.level < 2"
                    @click="
                      addMenuModal = true;
                      addForm.pid = data.id;
                    "
                  >
                    添加子菜单
                  </el-button>
                  <el-button
                    type="text"
                    class="btn_item"
                    @click="
                      updateMenuModal = true;
                      updateForm = JSON.parse(JSON.stringify(data));
                    "
                  >
                    修改
                  </el-button>
                  <el-popconfirm title="确定删除该菜单吗？" class="btn_item" @onConfirm="delMenu(data.id)">
                    <el-button type="text" slot="reference">
                      删除
                    </el-button>
                  </el-popconfirm>
                </span>
              </span>
            </el-tree>
            <div class="menuOper">
              <el-popconfirm class="oper_item" title="确定删除所选菜单吗？" @onConfirm="delMenu('all')">
                <el-button type="danger" size="mini" slot="reference" plain>删除选中菜单</el-button>
              </el-popconfirm>
              <el-button class="oper_item" size="mini" @click="updateMenuSort" :disabled="sortBtn" plain>修改菜单排序</el-button>
            </div>
          </div>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="菜单权限" name="permit" style="position:relative;">
        <el-form :inline="true" :model="roleForm" class="demo-form-inline" size="mini">
          <el-form-item label="角色名称">
            <el-input v-model="roleForm.name" placeholder="角色名称"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary">搜索</el-button>
          </el-form-item>
        </el-form>
        <el-table :data="roleList" style="width: 100%" size="mini" border>
          <el-table-column label="序号" type="index" align="center"></el-table-column>
          <el-table-column label="角色名称" prop="name" align="center"></el-table-column>
          <el-table-column label="操作" width="120px" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="showHasMenu(scope.row)">设置权限</el-button>
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
      addMenuModal: false,
      activeName: "manager",
      menu: [],
      addForm: {
        pid: 0,
        label: "",
        path: "",
        icon: ""
      },
      sortBtn: true,
      roleList: [],
      setMenuModal: {
        show: false,
        roleId: 0
      },
      filterMenu: "",
      updateMenuModal: false,
      updateForm: {
        label: "",
        path: "",
        icon: ""
      },
      roleForm: {
        name: ""
      }
    };
  },
  watch: {
    filterMenu(val) {
      this.$refs.menuPermit.filter(val);
    }
  },
  mounted() {
    this.getMenu();
  },
  methods: {
    getMenu() {
      this.$axios.post("/menu/allMenu").then(res => {
        this.menu = res.data.data;
      });
    },
    addMenu() {
      this.$axios.post("/menu/addMenu", this.addForm).then(res => {
        if (res.data.code == 0) {
          this.$message.success(res.data.msg);
          this.addMenuModal = false;
          this.$refs.addForm.resetFields();
          this.getMenu();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    updateMenu() {
      this.$axios.post("/menu/updateMenu", this.updateForm).then(res => {
        if (res.data.code == 0) {
          this.$message.success(res.data.msg);
          this.updateMenuModal = false;
          this.$refs.updateForm.resetFields();
          this.getMenu();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    updateMenuSort() {
      this.menu.forEach((item, i) => {
        item.sort = i;
        this.$set(this.menu, i, item);
        if (item.children) {
          item.children.forEach((jtem, j) => {
            jtem.sort = j;
            this.$set(this.menu[i].children, j, jtem);
            if (jtem.children) {
              jtem.children.forEach((ktem, k) => {
                ktem.sort = k;
                this.$set(this.menu[i].children[j].children, k, ktem);
              });
            }
          });
        }
      });
      this.$axios.post("/menu/setMenuSort", { menus: this.menu }).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.sortBtn = true;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    delMenu(id) {
      if (id !== "all") {
        this.$axios.post("/menu/delMenu", { id: id }).then(res => {
          if (res.data.code == 0) {
            this.$message.success(res.data.msg);
            this.getMenu();
          } else {
            this.$message.error(res.data.msg);
          }
        });
      } else {
        let keys = this.$refs.menuList.getCheckedKeys();
        this.$axios.post("/menu/delMenuBatch", { id: keys }).then(res => {
          if (res.data.code == 0) {
            this.$message.success(res.data.msg);
            this.getMenu();
          } else {
            this.$message.error(res.data.msg);
          }
        });
      }
    },
    getRole() {
      this.$axios.post("/role/getAllRole", this.roleForm).then(res => {
        this.roleList = res.data.data;
      });
    },
    tabsClick(tab) {
      if (tab.name == "permit") {
        this.getRole();
      }
    },
    showHasMenu(row) {
      this.$axios.post("/menu/getMenuId", { id: row.id }).then(res => {
        this.setMenuModal.roleId = row.id;
        this.setMenuModal.show = true;
        this.$nextTick(() => {
          this.$refs.menuPermit.setCheckedKeys(res.data.data);
        });
      });
    },
    setMenuPermit() {
      let keys = this.$refs.menuPermit.getCheckedKeys().concat(this.$refs.menuPermit.getHalfCheckedKeys());
      this.$axios.post("/role/setMenuPermit", { menusId: keys, roleId: this.setMenuModal.roleId }).then(res => {
        if (res.data.code === 0) {
          this.$message.success(res.data.msg);
          this.setMenuModal.show = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    allowDrop(dragNode, dropNode, type) {
      if (type == "inner" && dropNode.level === 2) {
        return false;
      }
      return true;
    }
  }
};
</script>

<style lang="scss">
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  padding-right: 8px;
}
.el-tree {
  margin: 20px 0;
}
.menuOper > .oper_item:nth-child(even) {
  margin: 0 10px;
}
.list_oper > .btn_item:nth-child(even) {
  margin: 0 6px;
}
.el-popconfirm__main {
  margin: 12px 0;
}
</style>
