package com.pmpsdk.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @Description: 文件的工具类  // 类说明
 * @ClassName: FileUtil    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/6 10:53   // 时间
 * @Version: 1.0     // 版本
 */
public class FileUtil {

    private FileUtil() {
        // TODO：私有构造函数，防止实例化
    }

    /**
     * @return void
     * @Author lrt
     * @Description //TODO创建文件
     * @Date 10:56 2025/8/6
     * @Param
     **/
    public static void create(String filePath, Path path) {
        File file = new File(filePath);
        try {
            file.createNewFile();
            System.out.println("文件创建成功");
        } catch (IOException ignored) {}
    }

    /**
     * @param path
     * @return java.util.List<java.lang.String>
     * @Author lrt
     * @Description //TODO 读取文件内容
     * @Date 10:58 2025/8/6
     * @Param
     **/
    public static List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return List.of();
        }
    }

    /**
     * 覆盖写入文件内容
     * @param path
     * @param lines
     */
    public static void writeLines(Path path, List<String> lines) {
        try {
            Files.write(path, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("写入文件失败: " + e.getMessage());
        }
    }

    /**
     * 追加写入单行内容
     * @param path
     * @param line
     */
    public static void appendLine(Path path, String line) {
        try {
            Files.writeString(path, line + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("追加文件失败: " + e.getMessage());
        }
    }

}
