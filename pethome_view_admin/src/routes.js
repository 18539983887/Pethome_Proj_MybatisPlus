import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Main from './views/Main.vue'
import Table from './views/nav1/Table.vue'
import Form from './views/nav1/Form.vue'
import user from './views/nav1/user.vue'
import Page4 from './views/nav2/Page4.vue'
import Page5 from './views/nav2/Page5.vue'
import Page6 from './views/nav3/Page6.vue'
import echarts from './views/charts/echarts.vue'
import Department from "./views/org/Department";
import Employee from "./views/org/Employee.vue";

let routes = [
  {
    path: '/login',
    component: Login,
    name: '',
    hidden: true
  },
  {
    path: '/404',
    component: NotFound,
    name: '',
    hidden: true
  },
  {
    path: '/',
    component: Home,
    name: 'Charts',
    leaf: true,//只有一个节点
    iconCls: 'fa fa-bar-chart',
    children: [
      {path: '/echarts', component: echarts, name: 'echarts'}
    ]
  },
  {
    path: '/',
    component: Home,
    name: '组织机构管理',
    iconCls: 'el-icon-monitor',//图标样式class
    children: [
      {path: '/department', component: Department, name: '部门管理'},
      {path: '/employee', component: Employee, name: '员工管理'},
      {path: '/shop', component: Table, name: '店铺管理'},
    ]
  },
  {
    path: '/',
    component: Home,
    name: '系统管理',
    iconCls: 'fa fa-id-card-o',
    children: [
      {path: '/role', component: Table, name: '角色管理'},
      {path: '/permission', component: Table, name: '权限管理'},
      {path: '/menu', component: Table, name: '菜单管理'},
      {path: '/log', component: Table, name: '日志管理'},
      {path: '/dictionary', component: Table, name: '数据字典'}
    ]
  },
  {
    path: '*',
    hidden: true,
    redirect: {path: '/404'}
  }
];

export default routes;