package com.pmpsdk.service;

import com.pmpsdk.domain.Result;

/**
 * @Description: // 类说明
 * @ClassName: UserService    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:31   // 时间
 * @Version: 1.0     // 版本
 */
public interface UserService {
    Result getUser(Long id);
}
