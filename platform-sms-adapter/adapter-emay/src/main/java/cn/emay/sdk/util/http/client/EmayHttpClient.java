package cn.emay.sdk.util.http.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.emay.sdk.util.http.common.EmayHttpResultCode;
import cn.emay.sdk.util.http.request.EmayHttpRequest;
import cn.emay.sdk.util.http.response.EmayHttpResponsePraser;

/**
 * EMAY http客户端
 * 
 * @author Frank
 *
 */
public class EmayHttpClient {

	/**
	 * 链接超时时间(s)
	 */
	private int httpConnectionTimeOut = 30;

	/**
	 * 数据传输超时时间(s)
	 */
	private int httpReadTimeOut = 30;

	public EmayHttpClient() {

	}

	/**
	 * 
	 * @param httpConnectionTimeOut
	 *            链接超时时间(s)
	 * @param httpReadTimeOut
	 *            数据传输超时时间(s)
	 */
	public EmayHttpClient(int httpConnectionTimeOut, int httpReadTimeOut) {
		this.httpConnectionTimeOut = httpConnectionTimeOut;
		this.httpReadTimeOut = httpReadTimeOut;
	}

	/**
	 * 发送HTTP请求
	 * 
	 * @param request
	 *            请求
	 * @param praser
	 *            响应解析器
	 * @return T 响应
	 */
	public <T> T service(EmayHttpRequest<?> request, EmayHttpResponsePraser<T> praser) {
		EmayHttpResultCode code = EmayHttpResultCode.SUCCESS;
		if (request.getUrl() == null || request.getUrl().length() == 0) {
			code = EmayHttpResultCode.ERROR_URL_NULL;
			return praser.prase(request.getCharSet(), code, 0, null, null, null);
		}
		HttpURLConnection conn = null;
		int httpCode = 0;
		Map<String, String> headers = null;
		List<String> cookies = null;
		ByteArrayOutputStream outputStream = null;
		try {
			conn = this.createConnection(request);
			this.fillConnection(conn, request);
			this.request(conn, request);
			httpCode = conn.getResponseCode();
			headers = this.getHeaders(conn, request.getCharSet());
			cookies = this.getCookies(conn, request.getCharSet());
			outputStream = this.getResultOutputStream(conn);
		} catch (KeyManagementException e) {
			code = EmayHttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			code = EmayHttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (ProtocolException e) {
			code = EmayHttpResultCode.ERROR_METHOD;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			code = EmayHttpResultCode.ERROR_CHARSET;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			code = EmayHttpResultCode.ERROR_URL;
			httpCode = 500;
			e.printStackTrace();
		} catch (IOException e) {
			code = EmayHttpResultCode.ERROR_CONNECT;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		T t = null;
		try {
			t = praser.prase(request.getCharSet(), code, httpCode, headers, cookies, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return t;
	}

	/**
	 * 获取HTTP响应头
	 * 
	 * @param conn
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> getHeaders(HttpURLConnection conn, String charSet) throws UnsupportedEncodingException {
		Map<String, String> resultHeaders = new HashMap<String, String>();
		Map<String, List<String>> header = conn.getHeaderFields();
		if (header != null && header.size() > 0) {
			for (Entry<String, List<String>> entry : header.entrySet()) {
				if (!"Set-Cookie".equalsIgnoreCase(entry.getKey())) {
					String valuer = "";
					if (entry.getValue() != null && entry.getValue().size() > 0) {
						for (String value : entry.getValue()) {
							valuer += new String(value.getBytes("ISO-8859-1"), charSet) + ",";
						}
						valuer = valuer.substring(0, valuer.length() - 1);
					}
					resultHeaders.put(entry.getKey(), valuer);
				}
			}
		}
		return resultHeaders;
	}

	/**
	 * 获取HTTP响应Cookies
	 * 
	 * @param conn
	 * @param charSet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private List<String> getCookies(HttpURLConnection conn, String charSet) throws UnsupportedEncodingException {
		List<String> resultC = new ArrayList<String>();
		List<String> cookies = null;
		Map<String, List<String>> header = conn.getHeaderFields();
		if (header != null && header.size() > 0) {
			cookies = header.get("Set-Cookie");
		}
		if (cookies != null) {
			for (String cookie : cookies) {
				resultC.add(new String(cookie.getBytes("ISO-8859-1"), charSet));
			}
		}
		return cookies;
	}

	/**
	 * 获取HTTP响应数据流
	 * 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	private ByteArrayOutputStream getResultOutputStream(HttpURLConnection conn) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		InputStream is = conn.getInputStream();
		try {
			if (is != null) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return outStream;
	}

	/**
	 * 发送Http请求
	 * 
	 * @param conn
	 * @param request
	 * @throws IOException
	 */
	private void request(HttpURLConnection conn, EmayHttpRequest<?> request) throws IOException {
		if (request.getMethod().equalsIgnoreCase("POST")) {
			conn.setDoOutput(true);
			// conn.connect();
			if (request.getParams() != null) {
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.write(request.paramsToBytesForPost());
				out.flush();
				out.close();
			}
		} else {
			conn.connect();
		}
	}

	/**
	 * 添加请求信息
	 * 
	 * @param conn
	 * @param request
	 * @throws ProtocolException
	 */
	private void fillConnection(HttpURLConnection conn, EmayHttpRequest<?> request) throws ProtocolException {
		this.fillTimeout(conn);
		this.filleMethod(conn, request);
		this.fillHeaders(conn, request);
		this.fillCookies(conn, request);
	}

	/**
	 * 添加超时时间
	 * 
	 * @param conn
	 */
	private void fillTimeout(HttpURLConnection conn) {
		if (httpConnectionTimeOut != 0) {
			conn.setConnectTimeout(httpConnectionTimeOut * 1000);
		}
		if (httpReadTimeOut != 0) {
			conn.setReadTimeout(httpReadTimeOut * 1000);
		}
	}

	/**
	 * 指定HTTP方法
	 * 
	 * @param conn
	 * @param request
	 * @throws ProtocolException
	 */
	private void filleMethod(HttpURLConnection conn, EmayHttpRequest<?> request) throws ProtocolException {
		conn.setRequestMethod(request.getMethod().toUpperCase());
	}

	/**
	 * 添加头信息
	 * 
	 * @param conn
	 * @param request
	 */
	private void fillHeaders(HttpURLConnection conn, EmayHttpRequest<?> request) {
		if (request.getHeaders() != null) {
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 添加Cookies
	 * 
	 * @param conn
	 * @param request
	 */
	private void fillCookies(HttpURLConnection conn, EmayHttpRequest<?> request) {
		if (request.getCookies() != null) {
			conn.setRequestProperty("Cookie", request.getCookies());
		}
	}

	/**
	 * 创建Http链接
	 * 
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection createConnection(EmayHttpRequest<?> request) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException {
		URL console = new URL(request.getUrl());
		HttpURLConnection conn;
		if (request.isHttps()) {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new X509TrustManager() {

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

				}

				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}

			} }, new java.security.SecureRandom());
			HttpsURLConnection sconn = (HttpsURLConnection) console.openConnection();
			sconn.setSSLSocketFactory(sc.getSocketFactory());
			sconn.setHostnameVerifier(new HostnameVerifier() {

				public boolean verify(String hostname, SSLSession session) {
					return true;
				}

			});
			conn = sconn;
		} else {
			conn = (HttpURLConnection) console.openConnection();
		}
		return conn;
	}

}
