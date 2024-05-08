package cn.javayong.platform.sms.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class SmsHttpClientUtils {

    public static final Logger LOG = LoggerFactory.getLogger(SmsHttpClientUtils.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final String METHOD_POST = "POST";

    private static final String METHOD_GET = "GET";

    private static final int MAX_SIZE = 1024;

    private SmsHttpClientUtils() {
    }

    public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws Exception {
        return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
    }

    private static void fillHeaders(HttpURLConnection conn, Map<String, String> headers) {
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                fillHeader(conn, entry.getKey(), entry.getValue());
            }
        }
    }

    private static void fillHeader(HttpURLConnection conn, String key, String value) {
        conn.setRequestProperty(key, value);
    }

    private static ByteArrayOutputStream getResultOutputStream(HttpURLConnection conn) throws IOException {
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

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws Exception {
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = {};
        if (query != null) {
            try {
                content = query.getBytes(charset);
            } catch (IOException e) {
                throw new Exception(e);
            }
        }
        return doPost(url, ctype, content, connectTimeout, readTimeout);
    }

    public static String doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout) throws Exception {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = getConnection(new URL(url), METHOD_POST, ctype);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } catch (IOException e) {
            LOG.error("调用 Http地址：" + url + " 访问出现错误 : ", e);
            throw new Exception("访问远程服务失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, params, DEFAULT_CHARSET);
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws Exception {
        HttpURLConnection conn = null;
        String rsp = null;
        try {
            String ctype = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype);
            } catch (IOException e) {
                LOG.error("调用 Http地址：" + url + " 访问出现错误 : ", e);
                throw new Exception(e);
            }
            rsp = getResponseAsString(conn);

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static HttpURLConnection getConnection(URL url, String method, String ctype) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,text/plain,*/*;");
            // conn.setRequestProperty("User-Agent", "remote-java");
            conn.setRequestProperty("Content-Type", ctype);
            conn.setRequestProperty("Accept-Language", "zh-CN");
            return conn;
        } catch (IOException e) {
            LOG.error("execute open connection error", e);
        }
        return null;
    }

    private static URL buildGetUrl(String strUrl, String query) throws IOException {
        StringBuffer buffer = new StringBuffer(strUrl);
        URL url = new URL(strUrl);
        if (SmsStringUtils.isEmpty(query)) {
            return url;
        }

        if (SmsStringUtils.isEmpty(url.getQuery())) {
            if (strUrl.endsWith("?")) {
                buffer.append(query);
            } else {
                buffer.append("?").append(query);
            }
        } else {
            if (strUrl.endsWith("&")) {
                buffer.append(query);
            } else {
                buffer.append("&").append(query);
            }
        }

        return new URL(buffer.toString());
    }

    public static String buildQuery(Map<String, String> params, String charset) throws Exception {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;

        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (SmsStringUtils.areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                try {
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
        }

        return query.toString();
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws Exception {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        String msg;
        if (es == null) {
            try {
                return getStreamAsString(conn.getInputStream(), charset);
            } catch (IOException e) {
                throw new Exception(e);
            }
        } else {
            msg = getStreamAsString(es, charset);
            if (SmsStringUtils.isEmpty(msg)) {
                throw new Exception("返回的内容为空" + msg);
            }
        }
        return msg;
    }

    private static String getStreamAsString(InputStream stream, String charset) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[MAX_SIZE];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;
        if (!SmsStringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2 && !SmsStringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }

        return charset;
    }

    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }


    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }


    public static String decode(String value, String charset) {
        String result = null;
        if (!SmsStringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if (!SmsStringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    private static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf('?') != -1) {
            map = splitUrlQuery(url.substring(url.indexOf('?') + 1));
        }
        if (map == null) {
            map = new HashMap<String, String>();
        }
        return map;
    }

      public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap<String, String>();

        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }

}
