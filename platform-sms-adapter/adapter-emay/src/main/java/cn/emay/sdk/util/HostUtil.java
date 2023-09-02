package cn.emay.sdk.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.emay.sdk.common.CommonConstants;
import cn.emay.sdk.core.dto.cache.UrlDTO;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.BalanceRequest;
import cn.emay.sdk.core.dto.sms.response.BalanceResponse;
import cn.emay.sdk.util.http.client.EmayHttpClient;
import cn.emay.sdk.util.http.request.impl.EmayHttpRequestString;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseString;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseStringPraser;
import cn.emay.sdk.util.json.JsonHelper;
import cn.emay.sdk.util.json.gson.JsonArray;
import cn.emay.sdk.util.json.gson.JsonElement;
import cn.emay.sdk.util.json.gson.JsonParser;
import cn.emay.sdk.util.json.gson.reflect.TypeToken;

public class HostUtil {

	public static String getUrl(String appId, String secretkey) {
		String host = "";
		List<String> list = CommonConstants.interList;
		try {
			if (list != null) {
				long minUseTime = -1l;
				for (String url : list) {
					long useTime = getBalance(appId, secretkey, url);
					if (useTime == -1) {
						continue;
					} else if (minUseTime == -1 || minUseTime > useTime) {
						minUseTime = useTime;
						host = url;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return host;
	}

	public static void getSDKInter() {

		EmayHttpClient client = new EmayHttpClient();
		EmayHttpRequestString request = null;
		try {
			request = new EmayHttpRequestString(CommonConstants.getSdkUrl, "UTF-8", "POST", null, null, null);
			EmayHttpResponseString res = client.service(request, new EmayHttpResponseStringPraser());
			if (res != null && res.getResultCode().getCode().equals("SUCCESS")) {
				String json = res.getResultString();
				if (json != null && !json.equals("")) {
					List<String> list = new ArrayList<String>();
					JsonArray array = new JsonParser().parse(json).getAsJsonObject().getAsJsonArray("result");
					Iterator<JsonElement> iterator = array.iterator();
					while (iterator.hasNext()) {
						JsonElement element = iterator.next();
						String ip = element.getAsJsonObject().get("ip").getAsString();
						Integer port = element.getAsJsonObject().get("port").getAsInt();
						list.add("http://" + ip + ":" + port);
					}
					CommonConstants.interList = list;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static long getBalance(String appId, String secretKey, String host) {
		long useTime = 0l;
		try {
			long time = System.currentTimeMillis();
			BalanceRequest request = new BalanceRequest();
			ResultModel<BalanceResponse> result = HttpUtil.request(appId, secretKey, host + "/inter/getBalance", request, BalanceResponse.class);
			if (result != null && result.getCode().equals("SUCCESS")) {
				useTime = System.currentTimeMillis() - time;
			} else {
				useTime = -1l;
			}
		} catch (Exception e) {
			e.printStackTrace();
			useTime = -1l;
		}
		return useTime;

	}

	public static List<Entry<String, Integer>> sortMap(Map<String, Integer> map) {
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		return list;
	}

	public static File creatCacheFile(String filePath) {
		File file = null;
		try {
			if (StringUtil.isEmpty(filePath)) {
				file = new File(CommonConstants.fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				return file;
			}
			file = new File(filePath);
			if (!file.exists()) {
				File fileParent = file.getParentFile();
				if (!fileParent.exists()) {
					fileParent.mkdirs();
				}
				file.createNewFile();
			}
			if (!file.canWrite() || !file.canRead()) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}

	public static boolean writeFile(File file, String content) {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(content.getBytes("UTF-8"));
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static String readFile(File file) {
		long fileSize = file.length();
		String json = "";
		if (fileSize > Integer.MAX_VALUE) {
			return null;
		}
		byte[] by = new byte[(int) fileSize];
		try {
			FileInputStream in = new FileInputStream(file);
			int offset = 0;
			int numRead = 0;
			while (offset < by.length && (numRead = in.read(by, offset, by.length - offset)) >= 0) {
				offset += numRead;
			}
			json = new String(by, "UTF-8");
			in.close();
		} catch (Exception e) {
			return null;
		}
		return json;
	}

	public static void contrastWrite(File file, String appId, String host) {
		List<UrlDTO> list = new ArrayList<UrlDTO>();
		String json = readFile(file);
		if (StringUtil.isEmpty(json)) {
			UrlDTO dto = new UrlDTO(appId, host);
			list.add(dto);
			json = JsonHelper.toJsonString(list);
			writeFile(file, json);
		} else {
			list = JsonHelper.fromJson(new TypeToken<List<UrlDTO>>() {
			}, json);
			Map<String, String> map = new HashMap<String, String>();
			for (UrlDTO urlDTO : list) {
				map.put(urlDTO.getAppId(), urlDTO.getUrl());
				if (urlDTO.getAppId().equals(appId) && !urlDTO.getUrl().equals(host)) {
					urlDTO.setUrl(host);
					json = JsonHelper.toJsonString(list);
					writeFile(file, json);
					break;
				}

			}
			if (!map.containsKey(appId)) {
				list.add(new UrlDTO(appId, host));
				writeFile(file, JsonHelper.toJsonString(list));
			}
		}
	}

	public static String getFileUrl(File file, String appId) {
		String url = "";
		String json = readFile(file);
		if (StringUtil.isEmpty(json)) {
			return url;
		} else {
			List<UrlDTO> list = JsonHelper.fromJson(new TypeToken<List<UrlDTO>>() {
			}, json);
			for (UrlDTO urlDTO : list) {
				if (urlDTO.getAppId().equals(appId)) {
					url = urlDTO.getUrl();
					break;
				}
			}
		}
		return url;
	}
}
