package com.courage.platform.sms.admin.dispatcher.processor;

public class ResponseCommand<T> {

    private int code;

    private String message;

    private T data;

    public static final int SUCCESS = ResponseCode.SUCCESS.getCode();

    public static final int ERROR = ResponseCode.ERROR.getCode();

    private ResponseCommand() {
    }

    public static <T> ResponseCommand<T> build(int code, String message, T data) {
        ResponseCommand<T> responseEntity = new ResponseCommand<>();
        responseEntity.message = message;
        responseEntity.code = code;
        responseEntity.data = data;
        return responseEntity;
    }

    public static <T> ResponseCommand<T> build(int code, String message) {
        ResponseCommand<T> responseEntity = new ResponseCommand<>();
        responseEntity.message = message;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ResponseCommand<T> build(int code, T data) {
        ResponseCommand<T> responseEntity = new ResponseCommand<>();
        responseEntity.data = data;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ResponseCommand<T> success(T data) {
        ResponseCommand<T> responseEntity = new ResponseCommand<>();
        responseEntity.data = data;
        responseEntity.code = SUCCESS;
        return responseEntity;
    }

    public static <T> ResponseCommand<T> fail(String message) {
        ResponseCommand<T> responseEntity = new ResponseCommand<>();
        responseEntity.code = ERROR;
        responseEntity.message = message;
        return responseEntity;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}

