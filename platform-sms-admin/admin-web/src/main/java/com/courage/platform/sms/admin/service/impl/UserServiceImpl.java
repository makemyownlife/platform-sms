package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息业务层
 */
@Service
public class UserServiceImpl implements UserService {

    private static byte[] seeds = "admin is best!".getBytes();

    private static final Integer PASSWORD_LENGTH = 6;

}
