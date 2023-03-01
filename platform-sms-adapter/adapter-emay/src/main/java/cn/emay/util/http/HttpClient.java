package cn.emay.util.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * EMAY http客户端
 * 
 * @author Frank
 *
 */
public class HttpClient {

	/**
	 * 链接超时时间(s)
	 */
	private int httpConnectionTimeOut = 30;

	/**
	 * 数据传输超时时间(s)
	 */
	private int httpReadTimeOut = 30;

	public HttpClient() {

	}

	/**
	 * 
	 * @param httpConnectionTimeOut
	 *            链接超时时间(s)
	 * @param httpReadTimeOut
	 *            数据传输超时时间(s)
	 */
	public HttpClient(int httpConnectionTimeOut, int httpReadTimeOut) {
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
	public <T> T service(HttpRequest<?> request, HttpResponsePraser<T> praser) {
		HttpResultCode code = HttpResultCode.SUCCESS;
		if (request.getHttpParams().getUrl() == null || request.getHttpParams().getUrl().length() == 0) {
			code = HttpResultCode.ERROR_URL_NULL;
			return praser.prase(code, 0, null, null, request.getHttpParams().getCharSet(), null);
		}
		HttpURLConnection conn = null;
		int httpCode = 0;
		Map<String, String> headers = null;
		List<String> cookies = null;
		ByteArrayOutputStream outputStream = null;
		try {
			String realUrl = this.genUrl(request);
			conn = this.createConnection(request, realUrl);
			this.fillConnection(conn, request);
			this.request(conn, request);
			httpCode = conn.getResponseCode();
			headers = this.getHeaders(conn, request.getHttpParams().getCharSet());
			cookies = this.getCookies(conn, request.getHttpParams().getCharSet());
			outputStream = this.getResultOutputStream(conn);
		} catch (SocketTimeoutException e) {
			code = HttpResultCode.ERROR_TIMEOUT;
			e.printStackTrace();
		} catch (KeyManagementException e) {
			code = HttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			code = HttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (ProtocolException e) {
			code = HttpResultCode.ERROR_METHOD;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			code = HttpResultCode.ERROR_CHARSET;
			e.printStackTrace();
		} catch (MalformedURLException e) {
			code = HttpResultCode.ERROR_URL;
			httpCode = 500;
			e.printStackTrace();
		} catch (IOException e) {
			code = HttpResultCode.ERROR_CONNECT;
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			code = HttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (KeyStoreException e) {
			code = HttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} catch (CertificateException e) {
			code = HttpResultCode.ERROR_HTTPS_SSL;
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		T t = null;
		try {
			t = praser.prase(code, httpCode, headers, cookies, request.getHttpParams().getCharSet(), outputStream);
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

	private <T> String genUrl(HttpRequest<T> request) {
		if (request.getHttpParams().getMethod().equalsIgnoreCase("GET")) {
			String getprams = request.getContentPraser().praseRqeuestContentToString(request.getHttpParams());
			if (getprams != null) {
				String url = null;
				if (request.getHttpParams().getUrl().indexOf("?") > 0) {
					url = request.getHttpParams().getUrl() + "&" + getprams;
				} else {
					url = request.getHttpParams().getUrl() + "?" + getprams;
				}
				return url;
			} else {
				return request.getHttpParams().getUrl();
			}
		} else {
			return request.getHttpParams().getUrl();
		}
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
	private <T> void request(HttpURLConnection conn, HttpRequest<T> request) throws IOException {
		if (request.getHttpParams().getMethod().equalsIgnoreCase("POST")) {
			conn.setDoOutput(true);
			// conn.connect();
			if (request.getHttpParams().getParams() != null) {
				byte[] content = request.getContentPraser().praseRqeuestContentToBytes(request.getHttpParams());
				fillHeader(conn, "Content-Length", String.valueOf(request.getContentPraser().praseRqeuestContentLength(request.getHttpParams())));
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.write(content);
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
	private void fillConnection(HttpURLConnection conn, HttpRequest<?> request) throws ProtocolException {
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
	private void filleMethod(HttpURLConnection conn, HttpRequest<?> request) throws ProtocolException {
		conn.setRequestMethod(request.getHttpParams().getMethod().toUpperCase());
	}

	/**
	 * 添加头信息
	 * 
	 * @param conn
	 * @param request
	 */
	private void fillHeaders(HttpURLConnection conn, HttpRequest<?> request) {
		if (request.getHttpParams().getHeaders() != null) {
			for (Entry<String, String> entry : request.getHttpParams().getHeaders().entrySet()) {
				fillHeader(conn, entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * 添加头信息
	 * 
	 * @param conn
	 * @param request
	 */
	private void fillHeader(HttpURLConnection conn, String key, String value) {
		conn.setRequestProperty(key, value);
	}

	/**
	 * 添加Cookies
	 * 
	 * @param conn
	 * @param request
	 */
	private void fillCookies(HttpURLConnection conn, HttpRequest<?> request) {
		if (request.getHttpParams().getCookies() != null) {
			conn.setRequestProperty("Cookie", request.getHttpParams().getCookies());
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
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	private HttpURLConnection createConnection(HttpRequest<?> request, String realUrl)
			throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException, UnrecoverableKeyException, KeyStoreException, CertificateException {
		URL console = new URL(realUrl);
		HttpURLConnection conn;
		if (request.isHttps()) {
			conn = genHttpsConn(console, request);
		} else {
			conn = (HttpURLConnection) console.openConnection();
		}
		return conn;
	}

	private HttpURLConnection genHttpsConn(URL console, HttpRequest<?> request)
			throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
		SSLContext ctx = getSSLContext(request.getHttpsParams());
		HttpsURLConnection sconn = (HttpsURLConnection) console.openConnection();
		sconn.setSSLSocketFactory(ctx.getSocketFactory());
		sconn.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return sconn;
	}

	/**
	 * 获得KeyStore.
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param password
	 *            密码
	 * @return 密钥库
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	private KeyStore getKeyStore(HttpsParams params) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		// 实例化密钥库 KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
		// 指定交换数字证书的加密标准
		KeyStore ks = KeyStore.getInstance(params.getAlgorithm());
		// 获得密钥库文件流
		FileInputStream is = new FileInputStream(params.getKeyStorePath());
		// 加载密钥库
		ks.load(is, params.getPassword().toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}

	/**
	 * 获得SSLSocketFactory.
	 * 
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * @param trustStorePath
	 *            信任库路径
	 * @return SSLSocketFactory
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 * @throws Exception
	 */
	private SSLContext getSSLContext(HttpsParams params) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
		// 实例化SSL上下文
		SSLContext ctx = SSLContext.getInstance("TLS");
		if (params != null) {
			// 实例化密钥库 KeyManager选择证书证明自己的身份
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			// 实例化信任库 TrustManager决定是否信任对方的证书
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			// 获得密钥库
			KeyStore keyStore = getKeyStore(params);
			// 初始化密钥工厂
			keyManagerFactory.init(keyStore, params.getPassword().toCharArray());
			// 获得信任库
			KeyStore trustStore = getKeyStore(params);
			// 初始化信任库
			trustManagerFactory.init(trustStore);
			// 初始化SSL上下文
			ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());
		} else {
			ctx.init(null, new TrustManager[] { myX509TrustManager }, new java.security.SecureRandom());
		}
		return ctx;
	}

	private TrustManager myX509TrustManager = new X509TrustManager() {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	};

}
