package org.example.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author huang
 */
@Component
public class FirstHandlerInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(FirstHandlerInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            logger.info("first-preHandle当前接口：{}", ((HandlerMethod) handler).getMethod().getName());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            logger.info("first-postHandle当前接口：{}", ((HandlerMethod) handler).getMethod().getName());
        }
    }
}
