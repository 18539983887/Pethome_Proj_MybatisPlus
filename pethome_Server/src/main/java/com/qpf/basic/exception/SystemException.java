package com.qpf.basic.exception;

/**
 * 自定义系统异常
 */
public class SystemException extends RuntimeException{

    //引起错误的对象
    private Object errObj;

    public Object getErrObj() {
        return errObj;
    }

    public SystemException() {
        
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message,Object errObj) {
        super(message);
    }
}