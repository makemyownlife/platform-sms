package com.alibaba.otter.canal.admin.service.impl;

import com.alibaba.otter.canal.admin.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息业务层
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static byte[] seeds = "canal is best!".getBytes();

    private static final Integer PASSWORD_LENGTH = 6;

}
