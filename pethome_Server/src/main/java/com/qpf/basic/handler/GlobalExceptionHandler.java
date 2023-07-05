package com.qpf.basic.handler;

import com.qpf.basic.exception.BusinessException;
import com.qpf.basic.exception.SystemException;
import com.qpf.basic.vo.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice    //设置当前类为异常处理器类
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public AjaxResult doBusinessException(BusinessException ex) {
        log.error("业务异常：", ex);
        log.error("异常对象：{}", ex.getErrObj());
        return AjaxResult.me().setSuccess(false).setMessage(ex.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    @ResponseBody
    public AjaxResult doSystemException(SystemException ex) {
        log.error("系统异常", ex);
        log.error("异常对象：{}", ex.getErrObj());
        return AjaxResult.me().setSuccess(false).setMessage(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResult doOtherException(Exception ex) {
        log.error("系统异常", ex);
        return AjaxResult.me().setSuccess(false).setMessage("系统繁忙，稍后重试!");
    }

}