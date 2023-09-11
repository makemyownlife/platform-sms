package com.courage.platform.sms.admin.dispatcher.processor.response;

public class ResponseEntity<T> {

    private int code;

    private String message;

    private T data;

    public static final int SUCCESS = ResponseCode.SUCCESS.getCode();

    public static final int ERROR = ResponseCode.ERROR.getCode();

    private ResponseEntity() {
    }

    public static <T> ResponseEntity<T> build(int code, String message, T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.message = message;
        responseEntity.code = code;
        responseEntity.data = data;
        return responseEntity;
    }

    public static <T> ResponseEntity<T> build(int code, String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.message = message;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ResponseEntity<T> build(int code, T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.data = data;
        responseEntity.code = code;
        return responseEntity;
    }

    public static <T> ResponseEntity<T> success(T data) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
        responseEntity.data = data;
        responseEntity.code = SUCCESS;
        return responseEntity;
    }

    public static <T> ResponseEntity<T> fail(String message) {
        ResponseEntity<T> responseEntity = new ResponseEntity<>();
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

