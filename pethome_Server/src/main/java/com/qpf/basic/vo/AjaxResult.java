package com.qpf.basic.vo;

import lombok.Data;

/**
 * 工具类：封装增删改的结果
 */
@Data
public class AjaxResult {
    //接口状态（true：成功；false：失败）
    private Boolean success = true;
    //返回前端的提示信息（成功）
    private String message = "操作成功";
    //存储返回给前端的数据
    private Object resultObj;

    //无参构造
    public AjaxResult(){}
    //有参构造
    public AjaxResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }



    //链式语法改造
    public static AjaxResult me(){
        return new AjaxResult();
    }

    public AjaxResult setError(String message){
        this.success = false;
        this.message = message;
        return this;
    }
    public AjaxResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }
    public AjaxResult setResultObj(Object resultObj) {
        this.resultObj = resultObj;
        return this;
    }
}