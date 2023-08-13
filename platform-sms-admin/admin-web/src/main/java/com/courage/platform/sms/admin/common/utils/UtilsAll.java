package com.courage.platform.sms.admin.common.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class UtilsAll {

    public static void responseJSONToClient(HttpServletResponse httpServletResponse, String result) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(result);
    }
    
}
