<template>
  <section>
    <!--工具条：关键字查询 + 新增-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="keyword" placeholder="请输入关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="keywordQuery">关键字查询</el-button>
        </el-form-item>
        <el-form-item>
          <!--v-perm="'menu:add'"-->
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表：优秀实践，copy domain修改列表-->
    <el-table :data="menus" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
              style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="name" label="菜单名称" width="135" sortable>
      </el-table-column>
      <el-table-column prop="component" label="组件路径" width="150" sortable>
      </el-table-column>
      <el-table-column prop="parent.name" label="上级菜单" width="120" sortable>
      </el-table-column>
      <el-table-column prop="url" label="访问URL" width="160" sortable>
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="80" sortable>
        <template scope="scope">
          <i :class="scope.row.icon" style="color: #1d8ce0"></i>
        </template>
      </el-table-column>
      <el-table-column prop="index" label="排序" width="80" sortable>
      </el-table-column>
      <!--<el-table-column prop="intro" label="简介" width="200" sortable>-->
      <!--</el-table-column>-->
      <el-table-column prop="state" label="状态" width="100" sortable>
        <template scope="scope">
          <span style="color: green" v-if="scope.row.state">正常</span>
          <span style="color: red" v-else-if="scope.row.state">禁用</span>
          <span style="color: blueviolet" v-else>未知</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="120">
        <template scope="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <!-- v-perm="'menu:delete'" -->
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :current-page="currentPage"
                     :page-size="pageSize" :total="totals" style="float:right;">
      </el-pagination>
    </el-col>

    <!--新增和编辑对话框-->
    <el-dialog :title="title" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <!--ref="menuForm" 指定表单名称，以后可以通过this.$refs.menuForm-->
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="saveForm.component" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="访问URL" prop="url">
          <el-input v-model="saveForm.url" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="saveForm.icon" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="index">
          <el-input-number v-model="saveForm.index" controls-position="right" @change="handleChange" :min="1"
                           :max="10"></el-input-number>
        </el-form-item>
        <el-form-item label="上级菜单">
          <!--
            <el-cascader - 级联
              :options="deptTree" - 级联框中需要的数据【集合】
            回显机制：
              v-model="saveForm.parent"
                parent：[2,5]
          -->
          <el-cascader v-model="saveForm.parentId"
                       :options="menuTree"
                       :props="{
                checkStrictly: true,
                label: 'name', //级联框显示那个属性的值
                value: 'id'    //回显时需要,回传值的时候也需要
              }" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input v-model="saveForm.intro" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="saveForm.state">
            <el-radio class="radio" :label="true">启用</el-radio>
            <el-radio class="radio" :label="false">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
export default {
  data() {
    return {
      //分页显示所属数据
      menus: [],
      listLoading: false,
      currentPage: 1,
      pageSize: 5,
      totals: 0,

      //高级查询所需数据
      keyword: null,

      //多条数据选中
      sels: [],//列表选中列


      //添加或修改数据
      saveFormVisible: false,//添加或修改界面是否显示
      saveLoading: false,
      title: null,
      menuTree: [],
      saveFormRules: {
        name: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ],
        index: [
          {required: true, message: '请输入排序索引', trigger: 'blur'}
        ],
        url: [
          {required: true, message: '请输入访问路径', trigger: 'blur'}
        ],
        component: [
          {required: true, message: '请输入组件路径', trigger: 'blur'}
        ],
      },

      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        component: '',
        url: '',
        icon: '',
        index: 1,
        intro: '',
        state: true,
        parent: null,
      }
    }
  },
  methods: {
    //获取菜单列表
    getMenus() {
      let paras = {
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        keyword: this.keyword
      };
      this.listLoading = true;
      this.$http.post("/menu", paras).then(res => {
        this.totals = res.data.total;
        this.menus = res.data.records;
        this.listLoading = false;
      }).catch(res => {
        this.$message.error("获取分页数据失败!!!");
      })
    },

    //关键字查询
    keywordQuery() {
      this.currentPage = 1;
      this.getMenus();
    },

    //分页查询 - 点击某一页查询
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getMenus();
    },

    //点击排序
    handleChange(value) {
      this.saveForm.index = value;
    },

    //删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/menu/" + row.id).then(res => {
          this.listLoading = false;
          if (res.data.success) {
            //计算总页数
            let totalPage = this.totals % this.pageSize == 0 ? this.totals / this.pageSize
                : Math.ceil(this.totals / this.pageSize);
            if (this.currentPage > 1 && this.currentPage == totalPage && (this.totals - 1) % this.pageSize == 0) {
              this.currentPage = this.currentPage - 1;
            }
            this.getMenus();
          } else {
            this.$message.error("删除失败【500】");
          }
        }).catch(res => {
          this.$message.error("系统错误【400,404】");
        })
      }).catch(() => {
        //确认框点击取消之后走这里
      });
    },

    //点击修改按钮弹出模态框
    handleEdit: function (index, row) {
      this.title = "编辑";
      this.saveForm = Object.assign({}, row);
      this.getMenuTree();//加载父级菜单

      //对上级菜单回显做处理：//这里没有dirPath - 无法显示多级部门，只能显示上级部门
      //注意回显的时候：只能选顶级部门，
      this.saveFormVisible = true;
    },

    //点击添加按钮弹出模态框
    handleAdd: function () {
      //修改对话框标题
      this.title = "添加"
      //置空数据，防止先点修改，后点添加有值
      this.saveForm = {
        id: null,
        name: '',
        component: '',
        url: '',
        icon: '',
        index: 1,
        intro: '',
        state: true,
        parent: null
      };
      this.getMenuTree();//加载父级菜单
      //弹出对话框
      this.saveFormVisible = true;
    },

    //点击模态框提交数据
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;


            let temp = this.saveForm.parentId;
            if (temp == null || temp.length == 0) {
              //如果没有选择，那就是顶级部门
              this.saveForm.parentId = null;
            } else if (temp.length) {
              //如果选中多级，则只有最后一个才是真正的上级部门。
              this.saveForm.parentId = temp[temp.length - 1];
            }

            let paras = Object.assign({}, this.saveForm);
            this.$http.put("/menu", paras).then(res => {
              this.saveFormVisible = false;
              this.saveLoading = false;
              if (res.data.success) {
                this.getMenus();
              } else {
                this.$message.error("操作失败【500】");
              }
            }).catch(res => {
              this.$message.error("系统错误【400,404】");
            })
          });
        }
      });
    },

    selsChange: function (sels) {
      this.sels = sels;
    },

    //批量删除
    batchRemove: function () {
      var ids = this.sels.map(item => item.id);
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.patch("/menu", ids).then(res => {
          this.listLoading = false;
          if (res.data.success) {
            this.currentPage = 1;
            this.getMenus();
          } else {
            this.$message.error("批量删除失败【500】");
          }
        }).catch(res => {
          this.$message.error("系统错误【400,404】");
        })
      }).catch(() => {
      });
    },

    //获取菜单树 - 无线级联
    getMenuTree() {
      this.$http.get("/menu/menuTree").then(res => {
        if (res.data) {
          this.menuTree = res.data;
        }
      });
    }
  },
  mounted() {
    //分页查询 - 页面一加载查询
    this.getMenus();
    this.getMenuTree();
  }
}

</script>

<style scoped>

</style>