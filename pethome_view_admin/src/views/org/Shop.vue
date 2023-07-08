<template>
  <section>
    <!--工具条：关键字查询 + 新增-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="keyword" placeholder="关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="keywordQuery">关键字查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" disabled @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!--列表-->
    <el-table :data="shops" highlight-current-row v-loading="listLoading"
              @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-table :data="props.row.auditLogs" style="width: calc(100% - 47px)" class="two-list">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column prop="state" label="状态">
              <template slot-scope="props">
							  <span v-if="props.row.state ==1">
								<span style="color:#00B46D">通过</span>
							  </span>
                <span v-else>
								<span style="color:#D75C89">驳回</span>
                 			 </span>
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注"></el-table-column>
            <el-table-column prop="auditManager.username" label="审核人员"></el-table-column>
            <el-table-column prop="audit_time" label="审核时间"></el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55">
      </el-table-column>
      <el-table-column type="index" width="60" label="序号" >
      </el-table-column>
      <el-table-column prop="name" label="名称" width="150" sortable>
      </el-table-column>
      <el-table-column prop="tel" label="电话" width="120" sortable>
      </el-table-column>
      <el-table-column prop="registerTime" label="入驻时间" width="120" sortable>
      </el-table-column>
      <el-table-column prop="state" label="状态" width="80" sortable>
        <template scope="scope">
          <span v-if="scope.row.state==1" style="color: hotpink">待审核</span>
          <span v-if="scope.row.state==2" style="color: blue">待激活</span>
          <span v-if="scope.row.state==3" style="color: green">正常使用</span>
          <span v-if="scope.row.state==4" style="color: red">驳回</span>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="150" sortable>
      </el-table-column>
      <el-table-column prop="logo" label="图标" min-width="100" sortable>
        <template scope="scope">
          <img :src="imgPrefix+scope.row.logo" width="50px">
        </template>
      </el-table-column>
      <el-table-column prop="admin.username" label="店长" min-width="80" sortable>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template scope="scope">
          <el-button size="small" type="info" disabled @click="handleEdit(scope.$index, scope.row)">修改</el-button>
          <el-button size="small" type="danger" disabled @click="handleDel(scope.$index, scope.row)">删除</el-button>
          <el-button size="small" type="warning" @click="handleAudit(scope.$index, scope.row)">店铺审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next"
                     @current-change="handleCurrentChange"
                     :page-size="pageSize"
                     :total="totals"
                     :current-page="currentPage"
                     style="float:right;">
      </el-pagination>
    </el-col>

    <!--新增修改界面-->
    <el-dialog :title="title" :visible.sync="saveFormVisible" :close-on-click-modal="false">
      <el-form :model="saveForm" label-width="80px" :rules="saveFormRules" ref="saveForm">
        <el-form-item label="店铺名称" prop="name">
          <el-input v-model="saveForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="tel">
          <el-input v-model="saveForm.tel" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="saveForm.state">
            <el-radio class="radio" :label="1">审核通过</el-radio>
            <el-radio class="radio" :label="0">待审核</el-radio>
            <el-radio class="radio" :label="-1">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="saveForm.address" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="LOGO" prop="logo">
          <el-input v-model="saveForm.logo" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="店长">
          <el-select v-model="saveForm.admin" placeholder="请选择" value-key="id">
            <el-option
                v-for="item in admins"
                :label="item.username"
                :value="item">
              <span style="float: left">{{item.username}}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.phone}}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="saveFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="saveSubmit" :loading="saveLoading">提交</el-button>
      </div>
    </el-dialog>

    <!-- 审核模态框 -->
    <el-dialog title="审核店铺" :visible.sync="shopAuditVisible" :close-on-click-modal="false">
      <el-form :model="shopAuditLog" label-width="80px"  ref="shopAuditLogForm">
        <el-form-item label="备注" prop="note">
          <el-input type="textarea" v-model="shopAuditLog.note"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="shopAuditVisible = false">取消</el-button>
        <el-button type="primary" @click.native="auditPass">通过</el-button>
        <el-button type="primary" @click.native="auditReject">驳回</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>

export default {
  data() {
    return {
      //列表显示图片的前缀
      imgPrefix: "http://dfs.java.itsource.cn",

      //分页显示所属数据
      shops: [],
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
      admins: [],
      saveFormRules: {
        name: [
          { required: true, message: '请输入店铺名称', trigger: 'blur' }
        ]
      },
      //编辑界面数据
      saveForm: {
        id: null,
        name: '',
        tel: '',
        state: 1,
        address:'',
        logo:'',
        admin: null
      },

      //审核模态框
      shopAuditVisible: false,
      //编辑界面数据
      shopAuditLog: {
        shop_id: null,
        note:''
      }
    }
  },
  methods: {
    //获取店铺列表
    getShops(){
      let paras = {
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        keyword: this.keyword
      };
      this.listLoading = true;
      this.$http.post("/shop",paras).then(res=>{
        this.totals = res.data.totals;
        this.shops = res.data.data;
        this.listLoading = false;
      }).catch(res => {
        this.$message.error("获取分页数据失败!!!");
      })
    },

    //审核驳回
    auditReject(){
      this.$http.post("/shop/audit/reject",this.shopAuditLog).then(res=>{
        if(res.data.success){
          this.$message({
            message: '驳回成功',
            type: 'success'
          });
        }else{
          this.$message({
            message: '驳回失败',
            type: 'error'
          });
        }
        this.shopAuditVisible = false;
        this.getShops();
      })
    },
    //审核通过
    auditPass(){
      this.$http.post("/shop/audit/pass",this.shopAuditLog)
          .then(result=>{
            result = result.data;
            if(result.success){
              this.$message({
                message: '审核通过',
                type: 'success'
              });
            }else{
              this.$message({
                message: '审核失败',
                type: 'error'
              });
            }
            this.shopAuditVisible = false;
            this.getShops();
          })
    },


    //关键字查询
    keywordQuery(){
      this.currentPage = 1;
      this.getShops();
    },

    //分页查询 - 点击某一页查询
    handleCurrentChange(val) {
      this.currentPage = val;
      this.getShops();
    },

    //删除
    handleDel: function (index, row) {
      this.$confirm('确认删除该记录吗?', '提示', {
        type: 'warning'
      }).then(() => {
        this.listLoading = true;
        this.$http.delete("/shop/" + row.id).then(res => {
          this.listLoading = false;
          if(res.data.success){
            //计算总页数
            let totalPage = this.totals%this.pageSize==0?this.totals/this.pageSize
                :Math.ceil(this.totals/this.pageSize);
            if(this.currentPage > 1 && this.currentPage==totalPage && (this.totals-1)%this.pageSize==0){
              this.currentPage = this.currentPage - 1;
            }
            this.getShops();
          }else{
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
      this.getAdmins();
      this.saveFormVisible = true;
    },

    //点击添加按钮弹出模态框
    handleAdd: function () {
      this.title = "添加";
      this.saveForm = {
        id: null,
        name: '',
        tel: '',
        state: 1,
        address:'',
        logo:'',
        admin: null
      };
      this.getAdmins();
      this.saveFormVisible = true;
    },

    //点击店铺审核弹出模态框
    handleAudit: function (index, row) {
      this.shopAuditLog.shop_id = row.id;
      this.shopAuditVisible = true;
    },

    //点击模态框提交数据
    saveSubmit: function () {
      this.$refs.saveForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.saveLoading = true;
            let paras = Object.assign({}, this.saveForm);
            this.$http.put("/shop",paras).then(res => {
              this.saveFormVisible = false;
              this.saveLoading = false;
              if(res.data.success){
                this.getShops();
              }else{
                this.$message.error("操作失败【500】");
              }
            }).catch(res => {
              this.$message.error("系统错误【400,404】");
            })
          });
        }
      });
    },

    //批量操作获取选中的数据
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
        this.$http.patch("/shop",ids).then(res => {
          this.listLoading = false;
          if(res.data.success){
            this.currentPage = 1;
            this.getShops();
          }else{
            this.$message.error("批量删除失败【500】");
          }
        }).catch(res => {
          this.$message.error("系统错误【400,404】");
        })
      }).catch(() => {
      });
    },

    //获取所有店铺管理员=店长
    getAdmins(){
      this.$http.get("/employee").then(res=>{
        if(res) {
          this.admins = res.data
        }
      })
    }
  },
  mounted() {
    this.getShops();
  }
}

</script>

<style scoped>

</style>