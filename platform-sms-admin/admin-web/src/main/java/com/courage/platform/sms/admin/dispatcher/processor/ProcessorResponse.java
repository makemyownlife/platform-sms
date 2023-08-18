package com.courage.platform.sms.admin.dispatcher.processor;

public class ProcessorResponse<T> {

    private int code;

    private String message;

    private T data;

    public static final int SUCCESS = ProcessorResponseCode.SUCCESS.getCode();

    public static final int ERROR = ProcessorResponseCode.ERROR.getCode();

    private ProcessorResponse() {
    }

    public static <T> ProcessorResponse<T> build(int code, String message, T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.message = message;
        responseEntity.code = code;
        responseEntity.data = data;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> build(int code, String message) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.message = message;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> build(int code, T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.data = data;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> success(T data) {
        ProcessorResponse<T> responseEntity = new ProcessorResponse<>();
        responseEntity.data = data;
        responseEntity.code = SUCCESS;
        return responseEntity;
    }

    public static <T> ProcessorResponse<T> fail(String message) {
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

