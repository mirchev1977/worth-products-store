package com.w.prod.config;

import com.w.prod.interceptor.SessionTimerInterceptor;
import com.w.prod.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserInterceptor userInterceptor;
    private SessionTimerInterceptor sessionTimerInterceptor;

    public WebConfig(UserInterceptor userInterceptor, SessionTimerInterceptor sessionTimerInterceptor) {
        this.userInterceptor = userInterceptor;
        this.sessionTimerInterceptor = sessionTimerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor);
        registry.addInterceptor(sessionTimerInterceptor);
    }
}
