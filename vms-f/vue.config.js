module.exports = {
  devServer: {
    port: 8333,
    hot: true,
    open: true
  },
  css: {
    loaderOptions: {
      sass: {
        // 新版本sass-loader， 将data改成prependData进行配置
        prependData: `@import "@/assets/scss/base.scss";`
      }
    }
  }
};
