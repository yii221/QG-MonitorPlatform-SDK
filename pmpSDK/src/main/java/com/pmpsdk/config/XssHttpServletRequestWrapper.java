package com.pmpsdk.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 用于清除html标签
     *
     * @param value
     * @return
     */
    private String cleanXss(String value) {
        return StrUtil.isBlank(value) ? value : HtmlUtil.filter(value);
    }

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    /**
     * 获取参数值时
     * 进行html标签清理
     *
     * @param name
     * @return
     */
    @Override
    public String getParameter(String name) {
        // 获得该请求中参数的值
        String value = super.getParameter(name);
        return cleanXss(value);
    }


    /**
     * 获取参数值数组时
     * 进行html标签清理
     *
     * @param name
     * @return
     */
    @Override
    public String[] getParameterValues(String name) {
        // 获得该请求中参数的值
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] cleanedValues = new String[values.length];

        // 遍历清理html标签
        for (int i = 0; i < values.length; i++) {
            cleanedValues[i] = cleanXss(values[i]);
        }
        return cleanedValues;
    }

    /**
     * 对参数值映射进行html标签清理
     *
     * @return
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        // 获得请求中键值对参数
        Map<String, String[]> parameterMap = super.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String[]> cleanedMap = new LinkedHashMap<>(parameterMap.size());

        // 遍历清理html标签
        parameterMap.forEach((key, values) -> {
            String[] cleanedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                cleanedValues[i] = cleanXss(values[i]);
            }
            cleanedMap.put(key, cleanedValues);
        });
        return cleanedMap;
    }

    /**
     * 对请求头进行html标签清理
     *
     * @param name
     * @return
     */
    @Override
    public String getHeader(String name) {
        // 获得该请求中参数的值
        String value = super.getHeader(name);
        return cleanXss(value);
    }

    /**
     * 对请求体进行html标签清理
     *
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader buffer = null;
        StringBuffer body;

        try {
            // 获得输入流
            in = super.getInputStream();
            // 指定输入流为utf-8
            reader = new InputStreamReader(in, StandardCharsets.UTF_8);

            // 对数据流进行读操作
            buffer = new BufferedReader(reader);
            body = new StringBuffer();

            // 循环读取文本
            String line = buffer.readLine();
            while (line != null) {
                body.append(line);
                line = buffer.readLine();
            }
        } finally {
            if (buffer != null) {
                buffer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (in != null) {
                in.close();
            }
        }


        // 从json中获取参数键值
        Map<String, Object> map = JSONUtil.parseObj(body.toString());
        Map<String, Object> result = new LinkedHashMap<>();
        for (String key : map.keySet()) {
            Object values = map.get(key);
            // 若为字符串类型
            if (values instanceof String) {
                if (!StrUtil.hasEmpty(values.toString())) {
                    result.put(key, HtmlUtil.cleanHtmlTag(values.toString()));
                }
            } else {
                result.put(key, values);
            }
        }

        // 重新生成json语句
        String json = JSONUtil.toJsonStr(result);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            /**
             * 始终不结束
             * @return
             */
            @Override
            public boolean isFinished() {
                return false;
            }

            /**
             * 始终不开始
             * @return
             */
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
