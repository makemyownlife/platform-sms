package com.courage.platform.sms.admin.loader.processors;

public class ProcessorResponse<T> {

    private int code;

    private String message;

    private T data;

    public static final int SUCCESS = 200;

    public static final int FAIL = 400;

    public static final int NO_PERMISSION = 403;

    public static final int ERROR = 500;

    public static final int SIGN_ERROR = 600;

    private ProcessorResponse() {
    }

    public static <T> ProcessorResponse<T> custom(int code, String message, T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.message = message;
        responseEntity.code = code;
        responseEntity.data = data;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> custom(int code, String message) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.message = message;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> custom(int code, T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.data = data;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> successResult(T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.data = data;
        responseEntity.code = SUCCESS;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> failResult(String message) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.code = FAIL;
        responseEntity.message = message;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> failCustom(String message, T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.message = message;
        responseEntity.code = FAIL;
        responseEntity.data = data;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> noPermissionResult(String message) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.code = NO_PERMISSION;
        responseEntity.message = message;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> errorResult(String message) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
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

