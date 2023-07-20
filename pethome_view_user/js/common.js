//配置axios的全局基本路径，访问后端接口使用8080
axios.defaults.baseURL = 'http://localhost:8080/'
//全局属性配置，在任意组件内可以使用this.$http获取axios对象
Vue.prototype.$http = axios

//======================axios的前置拦截器【拦截调用后端的请求====================//
//每次发送axios请求都会走这里面的流程
axios.interceptors.request.use(res => {
    //获取localStorage的token
    let token = localStorage.getItem("token");
    if (token) {
        //设置到请求头
        res.headers["token"] = token;
    }
    return res;
}, error => {
    Promise.reject(error)
})
//======================axios的前置拦截器【拦截调用后端的请求】====================//
//======================axios的后置拦截器【处理后台登录拦截的结果】====================//
//=============axios的后置拦截器==========================//
//作用：专门处理axios响应回来的数据。先于回调函数执行
axios.interceptors.response.use(res => {
    //后端响应的是没有登录的信息
    if (false === res.data.success && "noLogin" === res.data.msg) {
        //删除localStorage的token和logininfo
        localStorage.removeItem("token");
        localStorage.removeItem("logininfo");
        //跳转到登录页面
        location.href="login";
    }else if (false === res.data.success && "timeout" === res.data.msg) {
        alert("登录超时，请重新登录");
        //删除localStorage的token和logininfo
        localStorage.removeItem("token");
        localStorage.removeItem("logininfo");
        //跳转到登录页面
        location.href="login";
    }
    return res;
},error => {
    Promise.reject(error)
})
//======================axios的后置拦截器【处理后台登录拦截的结果】====================//

//静态资源拦截器：
var url = location.href; //ttp://localhost/register.html
// 1.如果访问的不是login.html也不是register.html
if (url.indexOf("/login.html") == -1 && url.indexOf("/register.html") == -1
    && url.indexOf("/index.html") == -1 && url.indexOf("/callback.html") == -1
    && url.indexOf("/binder.html") == -1) {
    // 2.如果访问的不是以上，判断是否登录过
    let logininfo = localStorage.getItem("logininfo");
    if (!logininfo) {
        location.href = "/login.html";
    }
}

// 将?号参数后面的参数封装成一个json对象 - 方便获取数据
// http://bugtracker.itsource.cn/callback.html?code=051Gtm100OXeHP1kTT200jYdOy4Gtm1x&state=1
function parseUrlParams2Obj(url) {
    let paramStr = url.substring(url.indexOf("?")+1);//code=xxx&state=1
    let paramArr = paramStr.split("&");//[code=xxx,state=1]
    let paramObj = {};
    for(let i = 0;i<paramArr.length;i++){
        let paramTemp = paramArr[i];//code=xxx
        let paramName = paramTemp.split("=")[0];//code
        let paramValue= paramTemp.split("=")[1];//xxx
        paramObj[paramName] = paramValue;//{code:xxx,state:1}
    }
    return paramObj;
}

//console.log(parseUrlParams2Obj("http://bugtracker.itsource.cn/callback.html?code=051Gtm100OXeHP1kTT200jYdOy4Gtm1x&state=1"))

