package com.djh.common.exception;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.djh.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
* 全局异常统一处理：
*   1.@ResponseStatus(HttpStatus.BAD_REQUEST):改变服务器响应的状态码,HttpStatus.BAD_REQUEST = 400
*   2.@ExceptionHandler:用来统一处理方法抛出的异常,参数是某个异常类的class,
* */

@Slf4j
public class serveException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)/*非法参数异常*/
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常: ----------------{}",e.getMessage());
        return Result.fail(e.getMessage());
    }

//    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常: ----------------{}",e.getMessage());
        System.out.println("异常runtime");
        return Result.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        BindingResult result =e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常: ----------------{}",objectError.getDefaultMessage());
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MybatisPlusException.class)
    public Result handler(MybatisPlusException e){
        log.error("mybatisPlus异常: ----------------{}",e.getMessage());
        System.out.println("异常发生");
        return Result.fail(e.getMessage());
    }

}
