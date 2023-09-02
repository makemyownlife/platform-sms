package cn.emay.sdk.core.service.security;

import java.util.HashMap;
import java.util.Map;

import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.BalanceRequest;
import cn.emay.sdk.core.dto.sms.request.MoRequest;
import cn.emay.sdk.core.dto.sms.request.ReportRequest;
import cn.emay.sdk.core.dto.sms.request.RetrieveReportRequest;
import cn.emay.sdk.core.dto.sms.request.SmsBatchOnlyRequest;
import cn.emay.sdk.core.dto.sms.request.SmsBatchRequest;
import cn.emay.sdk.core.dto.sms.request.SmsPersonalityAllRequest;
import cn.emay.sdk.core.dto.sms.request.SmsPersonalityRequest;
import cn.emay.sdk.core.dto.sms.request.SmsSingleRequest;
import cn.emay.sdk.core.dto.sms.response.BalanceResponse;
import cn.emay.sdk.core.dto.sms.response.MoResponse;
import cn.emay.sdk.core.dto.sms.response.ReportResponse;
import cn.emay.sdk.core.dto.sms.response.RetrieveReportResponse;
import cn.emay.sdk.core.dto.sms.response.SmsResponse;
import cn.emay.sdk.core.service.SDKService;
import cn.emay.sdk.util.HttpUtil;
import cn.emay.sdk.util.exception.SDKParamsException;

public class SDKSecurityServiceImpl implements SDKService {

	@Override
	public ResultModel<SmsResponse> sendSingleSms(String appId, String secretKey, String host, SmsSingleRequest request) {
		ResultModel<SmsResponse> result = HttpUtil.request(appId, secretKey, host + "/inter/sendSingleSMS", request, SmsResponse.class);
		return result;
	}

	@Override
	public ResultModel<SmsResponse[]> sendBatchOnlySms(String appId, String secretKey, String host, SmsBatchOnlyRequest request) {
		ResultModel<SmsResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/sendBatchOnlySMS", request, SmsResponse[].class);
		return result;
	}

	@Override
	public ResultModel<SmsResponse[]> sendBatchSms(String appId, String secretKey, String host, SmsBatchRequest request) throws SDKParamsException {
		ResultModel<SmsResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/sendBatchSMS", request, SmsResponse[].class);
		return result;
	}

	@Override
	public ResultModel<SmsResponse[]> sendPersonalitySms(String appId, String secretKey, String host, SmsPersonalityRequest request) throws SDKParamsException {
		ResultModel<SmsResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/sendPersonalitySMS", request, SmsResponse[].class);
		return result;
	}

	@Override
	public ResultModel<SmsResponse[]> sendPersonalityAllSms(String appId, String secretKey, String host, SmsPersonalityAllRequest request) throws SDKParamsException {
		ResultModel<SmsResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/sendPersonalityAllSMS", request, SmsResponse[].class);
		return result;
	}

	@Override
	public ResultModel<BalanceResponse> getBalance(String appId, String secretKey, String host, BalanceRequest request) {
		ResultModel<BalanceResponse> result = HttpUtil.request(appId, secretKey, host + "/inter/getBalance", request, BalanceResponse.class);
		return result;
	}

	@Override
	public ResultModel<ReportResponse[]> getReport(String appId, String secretKey, String host, ReportRequest reportRequest) {
		ResultModel<ReportResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/getReport", reportRequest, ReportResponse[].class);
		return result;
	}

	@Override
	public ResultModel<MoResponse[]> getMo(String appId, String secretKey, String host, MoRequest request) {
		ResultModel<MoResponse[]> result = HttpUtil.request(appId, secretKey, host + "/inter/getMo", request, MoResponse[].class);
		return result;
	}

	@Override
	public ResultModel<RetrieveReportResponse> retrieveReport(String appId, String timestamp, String sign, String host, RetrieveReportRequest reportRequest) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("timestamp", timestamp);
		params.put("sign", sign);
		params.put("startTime", reportRequest.getStartTime());
		params.put("endTime", reportRequest.getEndTime());
		params.put("smsId", reportRequest.getSmsId());
		ResultModel<String> result = HttpUtil.request(params, host + "/report/retrieveReport", "UTF-8", String.class);
		ResultModel<RetrieveReportResponse> resultModel = new ResultModel<RetrieveReportResponse>(result.getCode(), new RetrieveReportResponse(result.getCode()));
		return resultModel;
	}

}
