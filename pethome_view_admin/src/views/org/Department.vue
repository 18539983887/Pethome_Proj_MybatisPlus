<template>
  <section>
    <!--工具栏：关键字查询 + 添加-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="keyword" placeholder="请输入关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="keywordQuery" v-perm="'department:list'">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-perm="'department:save'" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--分页列表-->
    <el-table :data="departments" highlight-current-row v-loading="listLoading"
              @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" label="序号" width="60">
      </el-table-column>
      <el-table-column prop="name" label="部门名称" width="100">
      </el-table-column>
      <el-table-column prop="sn" label="部门编号" width="100">
      </el-table-column>
      <el-table-column label="部门状态" width="100" sortable>
        <template scope="scope">
          <span v-if="scope.row.state==1" style="color: green">启用</span>
          <span v-else style="color: red">禁用</span>
        </template>
      </el-table-column>
      <el-table-column prop="employee.username" label="部门经理" width="100">
      </el-table-column>
      <el-table-column  label="上级部门" width="100">
        <template scope="scope">
          <span v-if="scope.row.parent==null" style="color: gold">顶级部门</span>
          <span v-else>{{scope.row.parent.name}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template scope="scope">
          <el-button size="small" v-perm="'department:update'" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button type="danger" size="small" v-perm="'department:delete'" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--工具栏：批量删除 + 分页栏-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange"
                     :page-size="pageSize" :total="totals" :current-page="currentPage"
                     style="float:right;">
      </el-pagination>
    </el-col>
    <!--
       el-dialog - 对话框
          :title="title" - 标题
          :visible.sync="saveFormVisible" - 控制对话框是都显示，默认为false。点击添加或修改时改成true，就可以显示对话框了
          :close-on-click-modal="false" - 点对话框外不关闭对话框
      el-form - 表单
          :model="saveForm" - 表单数据/模型数据
          label-width="80px" - 标签的宽度
          :rules="saveFormRules" - 表单项验证
          ref="saveForm" - 表单验证，只有表单项每一项都通过，才能提交数据
      el-form-item - 表单项
          label="部门名称" - 标签
          prop="name" - 需要验证就要写prop
    -->
    <!--添加或编辑对话框-->
    <el-dialog :title="title" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="saveForm.name"></el-input>
        </el-form-item>
        <el-form-item label="部门编号" prop="sn">
          <el-input v-model="saveForm.sn"></el-input>
        </el-form-item>
        <el-form-item label="部门状态">
          <el-radio-group v-model="saveForm.state">
            <el-radio class="radio" :label="1">启用</el-radio>
            <el-radio class="radio" :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <!--
          回显机制【修改】：先定位（找到回显哪一个对象）再显示
            1.通过（v-model="saveForm.managerId"）的managerId去（v-for="item in managers"）的item中去找，找到回显哪一个对象
            2.对象中有很多属性值。再通过（:label="item.username"）回显对象的username属性值
          传值机制【添加和修改】
            1.当选中某一个经理时
            2.将这个经理的（:value="item.id"）id，传递给（v-model="saveForm.managerId" ）managerId，传给后端操作数据库
        -->
        <el-form-item label="部门经理">
          <el-select v-model="saveForm.managerId" placeholder="请选择">
            <el-option v-for="item in managers"
                :label="item.username"
                :value="item.id">
              <span style="float: left">{{ item.username }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.phone }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <!--
          回显机制【修改】：先定位（找到回显哪一个对象）再显示
            1.通过（v-model="saveForm.parentId"）parentId去（:options="deptTree"）的每一个对象中去找，找到回显哪一个对象
            2.对象中有很多属性值。再通过（ label: 'name',）回显对象的name属性值
          传值机制【添加和修改】
            1.当选中某一个上级部门的时候
            2.将这个部门的（value: 'id'）id，传递给（v-model="saveForm.parentId" ）parentId，传给后端操作数据库
        -->
        <el-form-item label="上级部门">
          <el-cascader v-model="saveForm.parentId" :options="deptTree"
              :props="{
                checkStrictly: true,
                label: 'name',
                value: 'id'
              }"
              clearable>
          </el-cascader>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
export default {
  data() {
    return {
      //1.分页查询的相关数据
      departments: [],    //列表数据
      listLoading: false, //列表的加载框
      pageSize: 5,        //每页显示条数
      currentPage: 1,     //当前页
      totals: 0,          //总条数

      //2.高级查询相关数据
      keyword: '',

      //3.批量删除的相关数据
      sels: [],//列表选中列


      //4.添加或修改的相关数据
      title: '',          //标题
      saveFormVisible: false,//控制对话框是都显示，默认为false
      saveLoading: false, //加载框
      saveFormRules: { // 表单项验证
        name: [
          {required: true, message: '请输入部门名称', trigger: 'blur'}
        ],
        sn: [
          {required: true, message: '请输入部门编号', trigger: 'blur'}
        ]
      },
      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        sn: '',
        state: 1,
        managerId: null,
        parentId: null
      },

      //经理们
      managers: [],
      //部门树
      deptTree: []
    }
  },
  methods: {
    //1.分页查询 - 获取部门列表
    getDepartments() {
      let para = {
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        keyword: this.keyword
      };
      this.listLoading = true;//显示加载框
      this.$http.post("/department",para).then(res=>{
        this.departments = res.data.records;//显示当前页分页数据
        this.totals = res.data.total;//计算分页栏
        this.listLoading = false;//关闭加载框
      }).catch(res=>{
        this.$message.error("系统繁忙，请稍后重试!!!【400,404】")
      })
    },

    //2.点击分页实现
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
      this.getDepartments();
    },

    //3.关键字查询
    keywordQuery(){
      //假如当前页不在第一页，防止高级查询没有那么多页的时候出现空数据的问题
      this.currentPage = 1;
      this.getDepartments();
    },

    //4.删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => { //点确认框的确认按钮
        this.listLoading = true;
        this.$http.delete("/department/"+row.id).then(res=>{
          if(res.data.success){
            //计算总页数
            let totalPage = Math.ceil(this.totals/this.pageSize);
            // //当删除最后一页的最后一条数据，并且当前不是第一页，就让当前页-1
            if(this.currentPage==totalPage && (this.totals-1)%this.pageSize==0 && this.currentPage!=1){
              this.currentPage = this.currentPage - 1;
            }
            this.getDepartments();
          }else{
            this.$message.error("系统繁忙，请稍后重试!!!【500】");
          }
          this.listLoading = false;
        }).catch(res=>{
          this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
        })
      }).catch(() => { //点取消
      });
    },

    //5.选项改变时触发
    selsChange: function (sels) {
      //将选中的所有对象【集合】赋值给模型数据sels
      this.sels = sels;
    },

    //6.批量删除
    batchRemove: function () {
      //获取选中对象的id
      var ids = this.sels.map(item => item.id);
      this.$confirm('确认删除选中记录吗？', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.patch("/department",ids).then(res=>{
          if(res.data.success){
            //处理当前页
            //算总页数
            let totalPage = Math.ceil(this.totals/this.pageSize);
            //当删除最后一页的所有数据，并且不是第一页，就让当前页-1
            if(this.currentPage==totalPage && (this.totals-this.sels.length)%this.pageSize==0 && this.currentPage!=1){
              this.currentPage = this.currentPage - 1;
            }

            this.getDepartments();
          }else{
            this.$message.error("系统繁忙，请稍后重试!!!【500】");
          }
        }).catch(res=>{
          this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
        })
      }).catch(() => {
      });
    },

    //7.显示编辑界面
    handleEdit: function (index, row) {
      this.title = " 编 辑 "
      //防止多个引用指向同一个对象
      this.saveForm = Object.assign({}, row);
      this.getManagers();
      this.getDeptTree();
      //显示对话框
      this.saveFormVisible = true;
    },

    //8.显示新增界面
    handleAdd: function () {
      this.title = " 新 增 "
      //重置
      this.saveForm = {
        id: null,
        name: '',
        sn: '',
        state: 1,
        managerId: null,
      };
      this.getManagers();
      this.getDeptTree();
      this.saveFormVisible = true;
    },

    //9.提交数据
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {//表单验证
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;

            //无限极树的数据处理：选中时,默认获取所有级别的数据。
            //例如：不选择-> null/[ ]，选择1层->[ 2 ]， 选择两层-> [ 2, 5]
            let temp = this.saveForm.parentId;
            if(temp==null || temp.length==0){
              //如果没有选择，那就是顶级部门
              this.saveForm.parentId = null;
            }else if(temp.length){
              //如果选中多级，则只有最后一个才是真正的上级部门。
              this.saveForm.parentId = temp[temp.length-1];
            }

            this.$http.put("/department",this.saveForm).then(res=>{
              if(res.data.success){
                this.getDepartments();
              }else{
                this.$message.error("系统繁忙，请稍后重试!!!【500】");
              }
              this.saveLoading = false; //关闭加载框
              this.saveFormVisible = false;//关闭对话框
            }).catch(res=>{
              this.$message.error("系统繁忙，请稍后重试!!!【400,404】");
            })
          });
        }
      });
    },

    //10.获取所有的部门经理
    getManagers(){
      this.$http.get("/employee").then(res=>{
        this.managers = res.data;
      })
    },

    //11.获取部门树
    getDeptTree(){
      this.$http.get("/department/deptTree").then(res=>{
        this.deptTree = res.data;
      })
    }
  },
  mounted() {
    this.getDepartments();
  }
}

</script>

<style scoped>
</style>