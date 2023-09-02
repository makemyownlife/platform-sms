package cn.emay.sdk.client;

import java.io.File;
import java.util.Date;

import cn.emay.sdk.common.CommonConstants;
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
import cn.emay.sdk.core.service.security.SDKSecurityServiceImpl;
import cn.emay.sdk.task.ContrastHostTask;
import cn.emay.sdk.util.DateUtil;
import cn.emay.sdk.util.HostUtil;
import cn.emay.sdk.util.Md5;
import cn.emay.sdk.util.StringUtil;
import cn.emay.sdk.util.exception.SDKParamsException;
import cn.emay.sdk.util.http.common.EmayHttpResultCode;

public class SmsSDKClient {

	/**
	 * 亿美服务帐号
	 */
	private String appId;
	/**
	 * 亿美服务密码
	 */
	private String secretkey;

	private SDKService service;
	/**
	 * 亿美http请求地址
	 */
	private String host;
	/**
	 * SDK缓存目录
	 */
	private File file;

	/**
	 * @param ip
	 *            亿美网关IP
	 * @param port
	 *            亿美网关端口
	 * @param appId
	 *            亿美服务帐号
	 * @param secretkey
	 *            亿美服务密码
	 * @throws SDKParamsException
	 */
	public SmsSDKClient(String ip, int port, String appId, String secretkey) throws SDKParamsException {
		if (StringUtil.isEmpty(appId) || StringUtil.isEmpty(secretkey) || StringUtil.isEmpty(ip) || port <= 0) {
			throw new SDKParamsException("SDK params error");
		}
		this.appId = appId;
		this.secretkey = secretkey;
		if (!ip.toLowerCase().startsWith("http://")) {
			ip = "http://" + ip;
		}
		host = ip + ":" + port;
		CommonConstants.isBest = true;
		CommonConstants.bestUrl = host;
		service = new SDKSecurityServiceImpl();
	}

	/**
	 * @param appId
	 *            亿美服务帐号
	 * @param secretkey
	 *            亿美服务密码
	 * @throws SDKParamsException
	 */
	public SmsSDKClient(String appId, String secretkey) throws SDKParamsException {
		this(appId, secretkey, "");
	}

	/**
	 * 
	 * @param appId
	 *            亿美服务帐号
	 * @param secretkey
	 *            亿美服务密码
	 * @param filePath
	 *            缓存数据目录(用于存储请求地址)
	 * @throws SDKParamsException
	 */
	public SmsSDKClient(String appId, String secretkey, String filePath) throws SDKParamsException {
		if (StringUtil.isEmpty(appId) || StringUtil.isEmpty(secretkey)) {
			throw new SDKParamsException("SDK params error");
		}
		this.appId = appId;
		this.secretkey = secretkey;
		file = HostUtil.creatCacheFile(filePath);
		String url = HostUtil.getFileUrl(file, appId);
		if (StringUtil.isEmpty(url)) {
			HostUtil.getSDKInter();
			if (CommonConstants.interList == null || CommonConstants.interList.isEmpty()) {
				throw new SDKParamsException("SDK Request interface address exception");
			}
			new Thread(new ContrastHostTask(file, appId, secretkey)).start();
			CommonConstants.bestUrl = CommonConstants.interList.get(0);
		} else {
			CommonConstants.isBest = true;
			CommonConstants.bestUrl = url;
		}
		service = new SDKSecurityServiceImpl();
	}

	/**
	 * 状态报告重新获取
	 * 
	 * @param reportRequest
	 * @return
	 */
	public ResultModel<RetrieveReportResponse> retrieveReport(RetrieveReportRequest reportRequest) {
		String timestamp = DateUtil.toString(new Date(), "yyyyMMddHHmmss");
		String sign = Md5.md5((appId + secretkey + timestamp).getBytes());
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.retrieveReport(appId, timestamp, sign, url, reportRequest);
	}

	/**
	 * 发送单条短信
	 * 
	 * @param request
	 * @return
	 */

	public ResultModel<SmsResponse> sendSingleSms(SmsSingleRequest request) {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.sendSingleSms(appId, secretkey, url, request);
	}

	/**
	 * 发送批次短信【非自定义smsid】
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendBatchOnlySms(SmsBatchOnlyRequest request) {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.sendBatchOnlySms(appId, secretkey, url, request);
	}

	/**
	 * 发送批次短信【自定义smsid】
	 * 
	 * @param request
	 * @return
	 * @throws SDKParamsException
	 */
	public ResultModel<SmsResponse[]> sendBatchSms(SmsBatchRequest request) throws SDKParamsException {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.sendBatchSms(appId, secretkey, url, request);
	}

	/**
	 * 发送个性短信
	 * 
	 * @param request
	 * @return
	 */

	public ResultModel<SmsResponse[]> sendPersonalitySms(SmsPersonalityRequest request) throws SDKParamsException {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.sendPersonalitySms(appId, secretkey, url, request);
	}

	/**
	 * 发送个性短信【全属性个性】
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<SmsResponse[]> sendPersonalityAllSMS(SmsPersonalityAllRequest request) throws SDKParamsException {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.sendPersonalityAllSms(appId, secretkey, url, request);
	}

	/**
	 * 获取余额
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<BalanceResponse> getBalance(BalanceRequest request) {
		String url = (host == null || host.equals("")) ? CommonConstants.bestUrl : host;
		return service.getBalance(appId, secretkey, url, request);
	}

	/**
	 * 获取状态报告
	 * 
	 * @param request
	 * @return
	 */

	public ResultModel<ReportResponse[]> getReport(ReportRequest reportRequest) {
		String url = "";
		if (CommonConstants.isBest) {
			url = CommonConstants.bestUrl;
		} else {
			return new ResultModel<ReportResponse[]>(EmayHttpResultCode.SUCCESS.getCode(), new ReportResponse[0]);
		}
		return service.getReport(appId, secretkey, url, reportRequest);
	}

	/**
	 * 获取上行短信
	 * 
	 * @param request
	 * @return
	 */
	public ResultModel<MoResponse[]> getMo(MoRequest request) {
		String url = "";
		if (CommonConstants.isBest) {
			url = CommonConstants.bestUrl;
		} else {
			return new ResultModel<MoResponse[]>(EmayHttpResultCode.SUCCESS.getCode(), new MoResponse[0]);
		}
		return service.getMo(appId, secretkey, url, request);
	}

}
