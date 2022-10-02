package com.example.springbootinit.Utils;

public class MyResponse {

    private String code;
    private String msg;
    private Object data;

    public static MyResponse buildSuccess() {
        MyResponse response = new MyResponse();
        response.setCode("1");
        response.setData(true);
        return response;
    }

    public static MyResponse buildSuccess(Object data) {
        MyResponse response = new MyResponse();
        response.setCode("1");
        response.setData(data);
        return response;
    }


    public static MyResponse buildFailure(String msg) {
        MyResponse response = new MyResponse();
        response.setCode("-1");
        response.setMsg(msg);
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
