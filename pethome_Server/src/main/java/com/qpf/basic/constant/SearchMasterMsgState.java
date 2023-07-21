package com.qpf.basic.constant;

public class SearchMasterMsgState {
    //待审核：0【消息发布默认待审核】
    //审核通过待处理：1【人工审核通过】
    //审核失败驳回：-1【人工审核驳回】
    //已处理：2【门店拿走宠物之后就是已处理】
    //寻主池：3【没人要/方圆50公里没有店铺，如果没有找到店铺】
    public static final Integer ToAudit = 0;
    //微信应用的密钥
    public static final Integer PassAudit = 1;
    //请求授权url(第1次请求)
    public static final Integer RejectAudit = -1;
    //生成授权令牌url(第2次请求)
    public static final Integer Finished = 2;
    //获取用户信息url(第3次请求)
    public static final Integer SearchPool = 3;
}
