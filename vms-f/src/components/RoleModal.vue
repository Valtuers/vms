<template>
  <div>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="30%" append-to-body>
      <el-transfer filterable filter-placeholder="请输入城市拼音" v-model="value" :data="data"></el-transfer>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: true,
      data: [{ label: "" }],
      value: []
    };
  }
};
</script>

<style lang="scss">
.el-dialog__wrapper {
  z-index: 2999;
}
</style>
