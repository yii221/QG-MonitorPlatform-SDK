package com.pmpsdk.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class IpBlacklistUtil {

    /**
     * 黑名单文件名称，存储路径为项目启动目录下的ip_blacklist.txt
     **/
    private static final String BLACKLIST_FILE =
            System.getProperty("user.dir") + "/config/ip_blacklist.txt";
    private static final Set<String> blacklist = new HashSet<>();

    /**
     * 读写锁
     **/
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static {
        // 初始化黑名单
        init();
    }

    /**
     * 确保父目录存在（如config目录），避免文件创建失败
     **/
    private static void ensureParentDirExists() {
        try {
            Path path = Paths.get(BLACKLIST_FILE);
            // 创建父目录（如果不存在）
            java.nio.file.Files.createDirectories(path.getParent());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create parent directory for blacklist file", e);
        }
    }

    /**
     * 从本地文件加载黑名单数据到内存集合
     */
    private static void loadBlacklist() {
        /** 加锁 **/
        lock.writeLock().lock();
        try {
            /** 构建文件路径对象 **/
            Path path = Paths.get(BLACKLIST_FILE);
            if (java.nio.file.Files.exists(path)) {
                List<String> lines = FileUtil.readLines(path);
                /** 先清空集合，再读取黑名单 **/
                blacklist.clear();
                blacklist.addAll(lines);
            } else {
                FileUtil.create(BLACKLIST_FILE, path);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            /** 释放锁 **/
            lock.writeLock().unlock();
        }
    }

    /**
     * 初始化黑名单
     */
    private static void init() {
        // 保证黑名单存在
        ensureParentDirExists();
        loadBlacklist();
    }

    // ---------------------------- ↑私有 ------------------------------------
    // ---------------------------- ↓公有 ------------------------------------


    /**
     * 将IP添加到黑名单（同时更新内存集合和本地文件）
     *
     * @param ip 新的黑名单的 ip地址
     */
    public static void addToBlacklist(String ip){
        init();
        // 校验IP格式
        if (!isValidIp(ip)) {
            return;
        }
        lock.writeLock().lock();
        try {
            if (blacklist.add(ip)) {
                FileUtil.appendLine(Paths.get(BLACKLIST_FILE), ip);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 从黑名单中移除IP（同时更新内存集合和本地文件）
     *
     * @param ip 待移除的IP地址
     */
    public static void removeFromBlacklist(String ip) {
        init();
        lock.writeLock().lock();
        try {
            // 从内存集合中移除IP，若移除成功则更新文件
            if (blacklist.remove(ip)) {
                Path path = Paths.get(BLACKLIST_FILE);
                // 直接将内存中最新的集合转为List，通过FileUtil写入文件
                FileUtil.writeLines(path, new ArrayList<>(blacklist));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 检查IP是否在黑名单中
     *
     * @param ip 待检查的IP地址
     * @return true：IP在黑名单中；false：IP不在黑名单中
     */
    public static boolean isBlacklisted(String ip) {
        init();
        lock.readLock().lock();
        try {
            return blacklist.contains(ip);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 校验 ip地址格式是否有效
     *
     * @param ip 待校验的IP地址
     * @return true：有效；false：无效
     */
    private static boolean isValidIp(String ip) {
        return ip != null
                && !ip.isBlank()
                && (ip.chars().filter(c -> c == '.').count() == 3
                || ip.contains(":"));
    }

}