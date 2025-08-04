package com.pmpsdk.config;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    /**
     * 过滤方法，对请求进行XSS过滤
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        XssHttpServletRequestWrapper wrapper = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(wrapper, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
