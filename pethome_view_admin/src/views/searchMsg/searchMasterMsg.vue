<template>
    <section>
        <!--工具条-->
        <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
            <el-form :inline="true">
                <el-form-item>
                    <el-input v-model="keyword" placeholder="关键字"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="keywordQuery">查询</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <el-table :data="searchMasterMsgs" highlight-current-row v-loading="listLoading" @selection-change="selsChange"
                  style="width: 100%;">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column type="index" width="60">
            </el-table-column>
            <el-table-column prop="title" label="标题" width="250" sortable>
            </el-table-column>
            <el-table-column prop="name" label="名称" width="100" sortable>
            </el-table-column>
            <el-table-column prop="price" label="价格" width="120" sortable>
            </el-table-column>
            <el-table-column prop="type.name" label="品种" width="120" sortable>
            </el-table-column>
            <el-table-column prop="user.username" label="用户" width="200" sortable>
            </el-table-column>
            <el-table-column prop="shop.name" label="店铺" width="120" sortable>
            </el-table-column>
            <el-table-column label="操作" min-width="120">
                <template scope="scope">
                    <el-button type="warning" plain size="small" @click="handleAudit(scope.$index, scope.row)">消息审核</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
            <el-button type="danger" :disabled="this.sels.length===0">批量删除</el-button>
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :current-page="currentPage"
                           :page-size="pageSize" :total="totals" style="float:right;">
            </el-pagination>
        </el-col>

        <!-- 审核模态框 -->
        <el-dialog v-loading="searchMsgAuditMsgLoading" title="审核寻主消息" :visible.sync="searchMsgAuditVisible"
                   :close-on-click-modal="false">
            <el-form :model="searchMsgAuditLog" label-width="80px" ref="shopAuditLogForm">
                <el-form-item label="备注" prop="note">
                    <el-input type="textarea" v-model="searchMsgAuditLog.note"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click.native="searchMsgAuditVisible = false">取消</el-button>
                <el-button type="primary" @click.native="auditMsg(1)">通过</el-button>
                <el-button type="danger" @click.native="auditMsg(0)">驳回</el-button>
            </div>
        </el-dialog>

    </section>
</template>

<script>
    export default {
        data() {
            return {
                //分页显示所属数据
                searchMasterMsgs: [],
                listLoading: false,
                currentPage: 1,
                pageSize: 5,
                totals: 0,
                //高级查询所需数据
                keyword: '',
                sels: [],//列表选中列

                searchMsgAuditVisible: false,//审核页面是否显示
                searchMsgAuditMsgLoading: false, //接单界面加载框

                //审核日志信息
                searchMsgAuditLog: {
                    msgId: '',  //被审核的消息的ID
                    state: 0,  //审核结果(0失败,1成功)
                    note: ''    //审核描述
                },
            }
        },
        methods: {
            //高级查询
            keywordQuery() {
                //修改页码
                this.currentPage = 1;
                //改查询条件=双向绑定输入时自动改变，只需要重新查询一次就ok
                this.getSearchMasterMsgs();
            },

            //点击某一页的处理
            handleCurrentChange(val) {
                this.currentPage = val;
                this.getSearchMasterMsgs();
            },

            //获取寻主消息列表(待审核)
            getSearchMasterMsgs() {
                let paras = {
                    currentPage: this.currentPage,
                    pageSize: this.pageSize,
                    keyword: this.keyword
                };
                this.listLoading = true;
                this.$http.post("/searchMasterMsg/toAudit", paras).then(res => {
                    this.totals = res.data.total;
                    this.searchMasterMsgs = res.data.records;
                    this.listLoading = false;
                }).catch(result => {
                    this.$message.error("获取分页数据失败!!!");
                })
            },

            //显示审核界面
            handleAudit: function (index, row) {
                this.searchMsgAuditLog.msgId = row.id;
                this.searchMsgAuditVisible = true;
            },

            //审核结果
            auditMsg: function (state) {
                //弹出加载框
                this.searchMsgAuditMsgLoading = true;

                this.searchMsgAuditLog.state = state;
                //发送请求到后台,处理审核结果
                this.$http.put("/searchMasterMsg/auditResult", this.searchMsgAuditLog).then(res => {
                    if (res.data.success) {
                        this.$message({
                            message: '审核成功',
                            type: 'success'
                        });
                    } else {
                        this.$message({
                            message: '审核失败',
                            type: 'error'
                        });
                    }
                    this.searchMsgAuditMsgLoading = false;
                    this.searchMsgAuditVisible = false;
                    this.currentPage = 1;
                    this.getSearchMasterMsgs();
                }).catch(result => {
                    this.$message.error("获取分页数据失败!!!");
                    this.searchMsgAuditMsgLoading = false;
                });
            },

            selsChange: function (sels) {
                this.sels = sels;
            }

        },
        mounted() {
            this.getSearchMasterMsgs();
        }
    }

</script>

<style scoped>

</style>