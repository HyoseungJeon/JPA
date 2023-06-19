package jpabook.jpashop.interceptor;

import jpabook.jpashop.wrapper.MultiReadableRequestBodyHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MultiReadableRequestBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (ObjectUtils.isEmpty(servletRequest.getContentType())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (servletRequest.getContentType().startsWith("multipart/form-data")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            MultiReadableRequestBodyHttpServletRequest wrappedRequest = new MultiReadableRequestBodyHttpServletRequest((HttpServletRequest) servletRequest);
            filterChain.doFilter(wrappedRequest, servletResponse);
        }
    }
}
