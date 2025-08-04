package com.pmpsdk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pmpsdk.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: // 类说明
 * @ClassName: UserMapper    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:31   // 时间
 * @Version: 1.0     // 版本
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
