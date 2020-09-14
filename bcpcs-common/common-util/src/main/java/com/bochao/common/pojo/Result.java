package com.bochao.common.pojo;

import lombok.Data;

/**
 * 通用响应
 * @param <T>
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private int status;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    private boolean success;

    public static Result successMsg(String message) {
        Result result = new Result<>();
        result.setStatus(Constant.SUCCESS_CODE);
        result.success = true;
        result.message = message;
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus(Constant.SUCCESS_CODE);
        result.success = true;
        result.data = data;
        return result;
    }

    public static Result failure(String message) {
        Result result = new Result();
        result.setStatus(Constant.FAIL_CODE);
        result.success = false;
        result.message = message;
        return result;
    }

    public static Result failure(String message, Object data) {
        Result result = new Result();
        result.setStatus(Constant.FAIL_CODE);
        result.success = false;
        result.data = data;
        result.message = message;
        return result;
    }
    /**
     * 无返回数据成功响应
     * @return
     */
    public static Result successResponse() {
        Result response = new Result();
        response.success = true;
        response.setStatus(Constant.SUCCESS_CODE);
        response.setMessage(Constant.SUCCESS_MSG);
        return response;
    }

    /**
     * 有返回数据成功响应
     * @param data 返回数据内容
     * @return
     */
    public static <T> Result<T> successResponse(T data) {
        Result<T> response = new Result<>();
        response.success = true;
        response.setStatus(Constant.SUCCESS_CODE);
        response.setMessage(Constant.SUCCESS_MSG);
        response.setData(data);
        return response;
    }

    /**
     * 请求失败响应
     * @return
     */
    public static Result failResponse() {
        Result response = new Result();
        response.setStatus(Constant.FAIL_CODE);
        response.setMessage(Constant.ERROR_MSG);
        return response;
    }

    /**
     * 请求失败
     * @param data
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Result<T> failResponse(T data, Integer code, String message){
        Result<T> response = new Result<>();
        response.setStatus(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

}
