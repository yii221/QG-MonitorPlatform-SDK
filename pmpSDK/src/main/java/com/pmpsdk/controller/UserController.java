package com.pmpsdk.controller;

import com.pmpsdk.annotation.Model;
import com.pmpsdk.annotation.Monitor;
import com.pmpsdk.domain.Result;
import com.pmpsdk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 用户业务  // 类说明
 * @ClassName: UserController    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:19   // 时间
 * @Version: 1.0     // 版本
 */
@Model(type = "user")
@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/getUser/{id}")
    @Monitor
    public Result getUser(@PathVariable("id") Integer id) {
        Long userID = id.longValue();
        int i= 1/0; // 模拟异常
        return userService.getUser(userID);
    }

}
