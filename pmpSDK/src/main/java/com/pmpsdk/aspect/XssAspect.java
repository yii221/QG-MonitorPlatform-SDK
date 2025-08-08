package com.pmpsdk.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

@Order(5)
@Aspect
@Component
public class XssAspect {

    /**
     * 拦截所有不带@FreeForXssFilter且带@PutMapping、@PostMapping注解的方法
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("(@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)) && " +
            "!@annotation(com.pmpsdk.annotation.FreeForXssFilter)")
    public Object cleanXssAroundAPIMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // TODO：获取方法签名和参数信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = signature.getMethod().getParameters();

        // TODO：遍历所有参数，处理标记了@RequestBody的参数
        for (int i = 0; i < parameters.length; i++) {
            if (isRequestBodyParameter(parameters[i])) {
                args[i] = processXss(args[i]);
            }
        }
        return joinPoint.proceed(args);
    }

    /**
     * 检查参数是否标记了@RequestBody注解
     * @param parameter
     * @return
     */
    private boolean isRequestBodyParameter(Parameter parameter) {
        return parameter.getAnnotationsByType(org.springframework.web.bind.annotation.RequestBody.class).length > 0;
    }

    /**
     * 递归处理XSS
     * @param target
     * @return
     */
    private Object processXss(Object target) {
        if (target == null) {
            return null;
        }

        // TODO：基本数据类型不需要处理
        if (target.getClass().isPrimitive() ||
                target instanceof Number ||
                target instanceof Boolean ||
                target instanceof Character) {
            return target;
        }

        // TODO：处理字符串类型
        if (target instanceof String) {
            return cleanXss((String) target);
        }

        // TODO：处理Map类型，递归处理每个value
        if (target instanceof Map<?, ?> map) {
            Map<Object, Object> result = new LinkedHashMap<>(map.size());
            map.forEach((k, v) -> result.put(k, processXss(v)));
            return result;
        }

        // TODO：处理集合类型，递归处理每个元素
        if (target instanceof Collection<?> collection) {
            List<Object> result = new ArrayList<>(collection.size());
            collection.forEach(item -> result.add(processXss(item)));
            return result;
        }

        // TODO：处理自定义对象
        try {
            for (Field field : target.getClass().getDeclaredFields()) {
                // TODO：设置字段可访问
                field.setAccessible(true);
                Object value = field.get(target);

                if (value instanceof String) {
                    // TODO：字符串字段直接过滤
                    field.set(target, cleanXss((String) value));
                } else if (value != null && !value.getClass().isPrimitive()) {
                    // TODO：非基本类型的对象字段递归处理
                    field.set(target, processXss(value));
                }
            }
            return target;
        } catch (Exception e) {
            return target;
        }
    }

    /**
     * 过滤XSS
     * @param value
     * @return
     */
    private String cleanXss(String value) {
        // TODO：HuTool的HtmlUtil.filter()会移除HTML标签并转义特殊字符
        return StrUtil.isBlank(value) ? value : HtmlUtil.filter(value);
    }
}