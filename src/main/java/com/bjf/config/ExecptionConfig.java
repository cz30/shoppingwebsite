package com.bjf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
//@ControllerAdvice
@ResponseBody
public class ExecptionConfig {
    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception e){
        //annotation抛出的异常
        if(e instanceof UndeclaredThrowableException) {
            e = (Exception) ((UndeclaredThrowableException)e).getUndeclaredThrowable();
        }
        log.error("[handleException]: ",e);
        return e.getLocalizedMessage()+"异常捕捉处理";
    }

}
