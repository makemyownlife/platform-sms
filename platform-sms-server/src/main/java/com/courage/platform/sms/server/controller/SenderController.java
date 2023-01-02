package com.courage.platform.sms.server.controller;

import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class SenderController {

    private final static Logger logger = LoggerFactory.getLogger(SenderController.class);

    public SmsSenderResult sendSingle(HttpServletRequest request) {
        String q = request.getParameter("q");
        String appKey = request.getParameter("appKey");
        return null;
    }

}
