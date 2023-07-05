package com.qpf.basic.exception;

public class BusinessException extends RuntimeException {

    private Object errObj;

    public Object getErrObj(){
        return errObj;
    }
    public BusinessException(){

    }
    public BusinessException(String message){
        super(message);
    }

    public BusinessException(String message,Object errObj) {
        super(message);
    }


}
