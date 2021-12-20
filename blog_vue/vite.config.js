import { resolve } from 'path';

function pathResolve(dir) {
    return resolve(__dirname, '.', dir);
}

module.exports = {
    /**
     * 在生产环境的基本公共路径。
     * @default '/'
     */
    base: '',

    /**
     * 构建输出目录。如果目录存在，它将在构建之前被删除。
     * @default 'dist'
     */
    outDir: 'dist',

    host:'localhost',
    // 端口号
    port: 8080,

    // 是否自动在浏览器打开
    open: false,

    // 是否开启 https
    https: false,

    // 服务端渲染
    // ssr: false,

    // 引入第三方的配置
     optimizeDeps: {
      include: ['@kangc/v-md-editor/lib/theme/vuepress.js',
          '@kangc/v-md-editor/lib/theme/github.js',
          '@modules/prismjs/prism.js',
          '@kangc/v-md-editor/lib/preview.js',
          'highlight.js/lib/index.js',
          '@kangc/v-md-editor/lib/plugins/emoji/index.js',
      ],
    },

    // 目录别名
    alias: {
        '/@/': pathResolve('src'),
    },

    // 打包后静态资源 js/css/image 存放目录，@default '_assets'
    assetsDir: '',
    // modules:'all',

    // 代理
    //  proxy: {
    //       '/blog': {
    //         target: 'http://localhost:8082/blog',
    //         ws:true,
    //         changeOrigin: true,
    //         pathRewrite: {
    //             '^blog/': ''//请求的时候使用这个api就可以
    //         }
    //       }
    //   }
};

