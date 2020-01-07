package com.courage.platform.sms.api.domain;

/**
 * 系统错误日志枚举
 * Created by zhangyong on 2018/3/22.
 */
public enum SystemErrorInfoEnum implements ErrorInfoEnum {

    SYSTEM_ERROR("000001", "系统异常，请稍后再试"),
    SYSTEM_PARAM_ERROR("-1", "系统参数错误"),
    SYSTEM_INVOKING_SERVICE_ERROR("000002", "调用第三方服务异常"),
    SYSTEM_INTERFACE_PARAM_ERROR("000003", "接口参数错误"),
    SYSTEM_CURRENT_LONGI_INVALID_ERROR("000004", "当前登录信息已失效，请重新登录"),
    SYSTEM_REQUEST_FAILURE_ERROR("000005", "请求数据失败，请稍候再试"),
    SYSTEM_NO_URL_MAPPING_ERROR("000006","URL不存在，请确认链接是否正确"),
    SYSTEM_BASE_VERTIFY_ERROR("000007","基础校验失败，请确认基础信息"),
    SYSTEM_ENCRYPTION_VERTIFY_ERROR("000008","加密校验失败,请确认appKey"),
    TEMPLATE_MISMATCH_ERROR("000009","关键字与模板内容不匹配"),
    APPKEY_INVALID_EXPIRED_ERROR("000010","无效的AppKey，请联系架构组确认账号状态"),
    RESTRICT_OVER_ERROR("000011","超出发送频率限制"),
    NO_VAILD_PHONENUMBER_ERROR("000012","手机号码不正确");

    SystemErrorInfoEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private String errorCode;

    private String errorMsg;

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    public ErrorInfoEnum getEnumByCode(String errorCode) {
        for (ErrorInfoEnum enu : SystemErrorInfoEnum.values()) {
            if (errorCode.equals(enu.getErrorCode())) {
                return enu;
            }
        }
        return null;
    }

}
