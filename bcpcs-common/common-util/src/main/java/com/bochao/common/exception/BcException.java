package com.bochao.common.exception;

/**
 * 自定义异常的父类
 * @author
 */
public class BcException extends RuntimeException {

    private static final long serialVersionUID = -8879123682017730252L;
    protected Integer code;
    protected String title = "Exception";

    public BcException(String message) {
        super(message);
    }

    public BcException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BcException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}