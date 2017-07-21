package com.mezhou887.system.core;

import org.apache.http.HttpStatus;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(HttpStatus.SC_OK),//成功
    FAIL(HttpStatus.SC_BAD_REQUEST),//失败
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED),//未认证（签名错误）
    NOT_FOUND(HttpStatus.SC_NOT_FOUND),//接口不存在
    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR);//服务器内部错误

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
