package com.pmpsdk.service.impl;

import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.domain.Result;
import com.pmpsdk.domain.User;
import com.pmpsdk.mapper.UserMapper;
import com.pmpsdk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户service应用层  // 类说明
 * @ClassName: UserService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:20   // 时间
 * @Version: 1.0     // 版本
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Monitor
    public Result getUser(Long id) {
        Result result = new Result();

        User user = userMapper.selectById(id);
        result.setData(user);
        return result;
    }
}
