package com.bochao.common.exception;

/**
 * 权限相关异常
 * @author
 */
public class AuthException extends BcException {


    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, int code) {
        super(message);
        this.code = code;
    }

    public AuthException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}
