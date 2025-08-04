package com.pmpsdk.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: // 类说明
 * @ClassName: User    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/4 16:21   // 时间
 * @Version: 1.0     // 版本
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class User {

    private Long id;

    private String name;

    private Integer age;

}
