import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './vuex/store'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import routes from './routes'
// import Mock from './mock'
// Mock.bootstrap();
import 'font-awesome/css/font-awesome.min.css'

//引入axios
import axios from 'axios'
//配置axios的全局基本路径，访问后端接口使用8080
axios.defaults.baseURL='http://localhost:8080/'
//全局属性配置，在任意组件内可以使用this.$http获取axios对象
Vue.prototype.$http = axios
Vue.prototype.$http.imgPrefix="http://192.168.136.133:8888/"
Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)
//1.axios前置拦截器：每次通过axios发送请求之前，让所有的请求都携带uToken
axios.interceptors.request.use(res => {
  //携带token
  let uToken = localStorage.getItem("token");
  if (uToken) {
    res.headers['token'] = uToken;
  }
  return res;
}, error => {
  Promise.reject(error);
})
//2.axios后置拦截器：后端处理完axios请求之后，处理响应的数据
axios.interceptors.response.use(result=>{
  //后端响应的是没有登录的信息
  if(!result.data.success && result.data.message==="noLogin"){
    localStorage.removeItem("token");
    localStorage.removeItem("logininfo");
    router.push({path: '/login'});
  }
  return result;
},error => {
  Promise.reject(error);
})
//NProgress.configure({ showSpinner: false });

const router = new VueRouter({
  routes
})

/*router.beforeEach((to, from, next) => {
  //NProgress.start();
  if (to.path == '/login') {
    sessionStorage.removeItem('user');
  }
  let user = JSON.parse(sessionStorage.getItem('user'));
  if (!user && to.path != '/login') { //
    alert(111);
    next({ path: '/login' })
  } else {
    next()
  }
})*/

//router.afterEach(transition => {
//NProgress.done();
//});
//3.路由前置拦截器 - 拦截非axios请求
router.beforeEach((to, from, next) => {
  //如果访问登录页面   或  注册页面
  if (to.path == '/login' || to.path == "/register") {
    next();//放行
  }else{//其他页面都需要判断你是否登录过
    //获取localStorage的登录信息
    let logininfo = localStorage.getItem('logininfo');
    if (logininfo) {
      next(); //登录过放行
    } else {
      next({path: '/login'});//跳转到login - 登录页面
    }
  }
})
new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  //components: { App }
  render: h => h(App)
}).$mount('#app')

