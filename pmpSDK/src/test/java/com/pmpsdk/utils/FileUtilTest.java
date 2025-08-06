package com.pmpsdk.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

/**
 * @Description: 文件工具的测试  // 类说明
 * @ClassName: FileUtilTest    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/6 10:59   // 时间
 * @Version: 1.0     // 版本
 */
public class FileUtilTest {

    @Test
    public void testFile() {
        // 测试创建文件
//        FileUtil.create("test.txt");
        // 写入文件内容
        FileUtil.writeLines(Path.of("test.txt"), List.of("666", "This is a test file."));
        // 读取文件内容
        FileUtil.readLines(Path.of("test.txt")).forEach(System.out::println);
    }
}
