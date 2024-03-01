package org.example.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huang
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    private FirstHandlerInterceptor firstHandlerInterceptor;

    private SecondHandlerInterceptor secondHandlerInterceptor;

    @Autowired
    public WebAppConfigurer(FirstHandlerInterceptor firstHandlerInterceptor,
                            SecondHandlerInterceptor secondHandlerInterceptor) {
        this.firstHandlerInterceptor = firstHandlerInterceptor;
        this.secondHandlerInterceptor = secondHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 加入顺序就是执行顺序
//        registry.addInterceptor(firstHandlerInterceptor).addPathPatterns("/**");
//        registry.addInterceptor(secondHandlerInterceptor).addPathPatterns("/**");
    }
}
