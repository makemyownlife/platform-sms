import com.courage.platform.sms.client.util.SmsHttpClientUtils;

import java.util.HashMap;

/**
 * Created by zhangyong on 2020/9/12.
 */
public class HttpUnitTest {

    public static void main(String[] args) throws Exception {
        String rtn = SmsHttpClientUtils.doGet("http://localhost:8080", new HashMap<String, String>());
        System.out.println(rtn);
    }

}
