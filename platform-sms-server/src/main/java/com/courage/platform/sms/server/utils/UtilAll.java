package com.courage.platform.sms.server.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by zhangyong on 2023/1/5.
 */
public class UtilAll {

    public static void responseJSONToClient(HttpServletResponse httpServletResponse, String result) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(result);
    }

}
