package com.example.ccb.exception;

public enum ErrorCode implements IErrorCode{
    GRADUATE_INFO_NOT_FOUNT(201, "无法找到毕业信息"),
    SIGN_OTHER_FAILED(202, "无法对他人的信息进行签名"),
    SIGN_FAILED(203, "当前状态下无法签名"),
    DELETE_FAILED(204, "删除记录失败"),
    REGISTER_FAILED(205, "注册失败"),
    LOGIN_FAILED(206, "登陆失败"),
    KEY_NOT_FOUND(207, "密钥已失效，请联系学校重新签名"),
    DATA_UNBELIEVABLE(208, "查询的数据已经被篡改"),
    INNER_ERROR(209, "系统内部错误"),
    DATA_NOT_FOUND(210, "查询结果为空");


    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
