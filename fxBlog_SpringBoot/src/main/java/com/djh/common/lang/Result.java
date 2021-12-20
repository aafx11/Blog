package com.djh.common.lang;

import lombok.Data;
import java.io.Serializable;

/*
*统一的结果放回封装类：
*   1.包括返回的代码，返回结果的信息，返回结果的数据
*   2.操作成功返回的结果
*   3.操作失败返回的结果
*
* */

@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

    /*成功结果*/
    public static Result success(Object data){
        return success(200,"操作成功",data);
    }
    public static Result success(String msg){
        return success(200,msg,null);
    }
    public static Result success(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result success(String msg,Object data){
        return success(200,msg,data);
    }


    /*失败结果*/
    public static Result fail(String msg){
        return fail(400,msg,null);
    }

    public static Result fail(String msg,Object data){
        return fail(400,msg,data);
    }

    public static Result fail(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
