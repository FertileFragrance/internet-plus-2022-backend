package com.example.springbootinit.Controller.ControllerAdvice;

import com.example.springbootinit.Exception.BussinessException;
import com.example.springbootinit.Utils.MyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class UniformErrorHandler {

    private static final String SYSTEM_FAILED = "系统出错";
    private static final String IO_FAILED = "输入输出错误";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MyResponse BindExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return MyResponse.buildFailure(e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResponse sendBussinessException(BussinessException e) {
        log.error(e.getMessage());
        return MyResponse.buildFailure(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResponse sendBussinessException(IOException e) {
        log.error(e.getMessage());
        return MyResponse.buildFailure(IO_FAILED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyResponse sendException(Exception e) {
        log.error(e.getMessage());
        return MyResponse.buildFailure(SYSTEM_FAILED);
    }

}
