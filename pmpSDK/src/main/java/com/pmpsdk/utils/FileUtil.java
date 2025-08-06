package com.pmpsdk.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.createFile;

/**
 * @Description: 文件的工具类  // 类说明
 * @ClassName: FileUtil    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/6 10:53   // 时间
 * @Version: 1.0     // 版本
 */
public class FileUtil {

    private FileUtil() {
        // 私有构造函数，防止实例化
    }


    /**
     * @Author lrt
     * @Description //TODO创建文件
     * @Date 10:56 2025/8/6
     * @Param
     * @return void
     **/
    public static void create(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
            System.out.println("文件创建成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @Author lrt
     * @Description //TODO 读取文件内容
     * @Date 10:58 2025/8/6
     * @Param
 * @param path
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * @Author lrt
     * @Description //TODO 写入文件内容
     * @Date 10:59 2025/8/6
     * @Param
 * @param path
 * @param lines
     * @return void
     **/
    public static void writeLines(Path path, List<String> lines) {
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
