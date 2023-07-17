<template>
  <section>
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="keyword" placeholder="关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="keywordQuery">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="employeeLists" highlight-current-row v-loading="listLoading"
              @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60" label="编号">
      </el-table-column>
      <el-table-column prop="username" label="员工名称" width="120" sortable>
      </el-table-column>
      <el-table-column prop="phone" label="员工电话" width="150" sortable>
      </el-table-column>
      <el-table-column prop="age" label="员工年龄" width="100" sortable>
      </el-table-column>
      <el-table-column prop="state" label="员工状态" width="120" sortable>
        <template scope="scope">
          <span v-if="scope.row.state == 1" style="color: green">启用</span>
          <span v-if="scope.row.state == 0" style="color: gray">禁用</span>
        </template>
      </el-table-column>
      <el-table-column prop="dept.name" label="所属部门" width="120" sortable>

      </el-table-column>
      <el-table-column prop="departmentId" label="员工类型" width="150" sortable>
        <template scope="scope">
          <span v-if="scope.row.departmentId == null" style="color: green">平台系统管理员</span>
          <span v-if="scope.row.departmentId != null" style="color: gray">店铺管理员</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template scope="scope">
          <el-button type="info" size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button v-if="scope.row.state == 1" type="warning" size="small" @click="handleDisable(scope.row)">禁用
          </el-button>
          <el-button v-if="scope.row.state == 0" type="success" size="small" @click="handleDisable(scope.row)">启用
          </el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next"
                     @current-change="handleCurrentChange"
                     :page-size="pageSize" :total="total" :current-page="currentPage" style="float:right;">
      </el-pagination>
    </el-col>

    <!--编辑界面-->
    <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
      <el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
        <el-form-item label="姓名" prop="username">
          <el-input v-model="editForm.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="editForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密钥" prop="password">
          <el-input v-model="editForm.password" auto-complete="off" placeholder="pethome"></el-input>
        </el-form-item>
        <!--<el-form-item label="新密码">-->
        <!--  <el-input v-model="editForm.password" auto-complete="off"  ></el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="editForm.age" :min="0" :max="200"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-radio-group v-model="editForm.state">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-cascader v-model="editForm.departmentId" :options="deptTree"
                       :props="{
                checkStrictly: true,
                label: 'name',
                value: 'id'
              }"
                       clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="所属店铺" prop="shopId">
          <el-select v-model="editForm.shopId" placeholder="请选择">
            <el-option v-for="item in shopList" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--新增界面-->
    <el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false">
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.username" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="addForm.phone" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="addForm.email" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密钥">
          <el-input v-model="addForm.salt" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="addForm.password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="addForm.age" :min="0" :max="200"></el-input-number>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="addForm.state">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-cascader v-model="addForm.departmentId" :options="deptTree"
                       :props="{
                checkStrictly: true,
                label: 'name',
                value: 'id'
              }"
                       clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="所属店铺" prop="shopId">
          <el-select v-model="addForm.shopId" placeholder="请选择">
            <el-option v-for="item in shopList" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import util from '../../common/js/util'
//import NProgress from 'nprogress'
// import { getUserListPage, removeUser, batchRemoveUser, editUser, addUser } from '../../api/api';

export default {
  data() {
    return {
      //1.分页查询的相关数据
      employeeLists: [],
      total: 0,
      currentPage: 1,
      pageSize: 5,
      listLoading: false,
      sels: [],//列表选中列

      //2.高级查询相关数据
      keyword: '',

      editFormVisible: false,//编辑界面是否显示
      editLoading: false,
      editFormRules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ]
      },
      //编辑界面数据
      editForm: {
        id: null,
        username: '',
        phone: '',
        email: 1,
        salt: null,
        age: null,
        state: 1,
        departmentId:null,
        shopId: null

      },

      departments: [],

      addFormVisible: false,//新增界面是否显示
      addLoading: false,
      addFormRules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ]
      },

      //新增界面数据
      addForm: {},
      //数据回显
      //显示部门数
      deptTree: [],
      shopList: []

    }
  },
  methods: {
    //性别显示转换
    formatSex: function (row, column) {
      return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知';
    },

    //1.分页获取用户列表
    getEmployeeLists() {
      let para = {
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        keyword: this.keyword
      };
      this.listLoading = true;
      this.$http.post("/employee", para).then((res) => {
        this.total = res.data.total;
        this.employeeLists = res.data.records;
        this.listLoading = false;
      }).catch(res => {
        this.$message.error("系统繁忙，请稍后重试!!!【400,404】")
      });
    },

    //2.点击分页实现
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getEmployeeLists();
    },

    //3.关键字查询
    keywordQuery() {
      //假如当前页不在第一页，防止高级查询没有那么多页的时候出现空数据的问题
      this.currentPage = 1;
      this.getEmployeeLists();
    },

    //4.修改用户状态
    handleDisable: function (row) {
      this.$confirm('确认禁用该用户吗?禁用后该账户不可再进行操作！', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.put("/employee/" + row.id).then(res => {
          if (res.data.success) {
            //计算总页数
            let totalPage = Math.ceil(this.total / this.pageSize);
            // 当删除最后一页的最后一条数据，并且当前不是第一页，就让当前页-1
            if (this.currentPage == totalPage && (this.total - 1) % this.pageSize == 0 && this.currentPage != 1) {
              this.currentPage = this.currentPage - 1;
            }
            this.$message({
              message: '修改成功',
              type: 'success'
            });
            this.getEmployeeLists();
          } else {
            this.$message.error("系统繁忙，请稍后重试!!!【500】");
          }
          this.listLoading = false;
        }).catch(res => {
          this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
        })
      }).catch(() => {
      });
    },

    //5.删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/employee/" + row.id).then(res => {
          if (res.data.success) {
            //计算总页数
            let totalPage = Math.ceil(this.total / this.pageSize);
            // 当删除最后一页的最后一条数据，并且当前不是第一页，就让当前页-1
            if (this.currentPage == totalPage && (this.total - 1) % this.pageSize == 0 && this.currentPage != 1) {
              this.currentPage = this.currentPage - 1;
            }
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.getEmployeeLists();
          } else {
            this.$message.error("系统繁忙，请稍后重试!!!【500】");
          }
          this.listLoading = false;
        }).catch(res => {
          this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
        })
      }).catch(() => {
      });
    },

    //6.选项改变时触发
    selsChange: function (sels) {
      this.sels = sels;
    },

    //7.批量删除
    batchRemove: function () {
      var ids = this.sels.map(item => item.id).toString();
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.patch("/employee", ids).then(res => {
          if (res.data.success) {
            //处理当前页
            //算总页数
            let totalPage = Math.ceil(this.total / this.pageSize);
            //当删除最后一页的所有数据，并且不是第一页，就让当前页-1
            if (this.currentPage == totalPage && (this.total - this.sels.length) % this.pageSize == 0 && this.currentPage != 1) {
              this.currentPage = this.currentPage - 1;
            }
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.getEmployeeLists();
          } else {
            this.$message.error("系统繁忙，请稍后重试!!!【500】");
          }
          this.listLoading = false;
        }).catch(res => {
          this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
        })
      }).catch(() => {
      });

    },

    //8.显示编辑界面
    handleEdit: function (index, row) {
      this.editForm = Object.assign({}, row);
      this.getDepartment();
      this.editFormVisible = true;

    },

    //9.显示新增界面
    handleAdd: function () {
      this.addForm = {
        password: "111111",
        age: 25,
        state: 1
      };
      this.addFormVisible = true;
    },

    //10.提交编辑数据
    editSubmit: function () {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true;

            let para = Object.assign({}, this.editForm);
            para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
            this.$http.put("/employee", para).then(res => {
              if (res.data.success) {
                this.$message({
                  message: '提交成功',
                  type: 'success'
                });
                this.$refs['editForm'].resetFields();
              } else {
                this.$message.error("系统繁忙，请稍后重试!!!【500】");
              }
              this.editLoading = false;
              this.editFormVisible = false;
              this.getEmployeeLists();
            });
          });
        }
      });
    },
    getDepartment() {
      this.$http.get("/department").then(res => {
        this.departments = res.data;
      })
    },

    //11.新增
    addSubmit: function () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.addLoading = true;
            let para = Object.assign({}, this.addForm);
            para.birth = (!para.birth || para.birth == '') ? '' : util.formatDate.format(new Date(para.birth), 'yyyy-MM-dd');
            this.$http.put("/employee", para).then(res => {

              if (res.data.success) {
                this.$message({
                  message: '提交成功',
                  type: 'success'
                });
                this.$refs['addForm'].resetFields();
              } else {
                this.$message.error("系统繁忙，请稍后重试!!!【500】");
              }
              this.addLoading = false;
              this.addFormVisible = false;
              this.getEmployeeLists();
            });
          });
        }
      });
    },
    //11.获取所有店铺
    getShopList() {
      this.$http.get("/shop").then(res => {
        this.shopList = res.data;
      })
    },
    //11.获取部门树
    getDeptTree() {
      this.$http.get("/department/deptTree").then(res => {
        this.deptTree = res.data;
      })
    }
  },
  mounted() {
    this.getEmployeeLists();
    this.getDeptTree();
    this.getShopList();
  }
}

</script>

<style scoped>

</style>