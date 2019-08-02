package com.chegy.configer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 和springmvc的webmvc拦截配置一样
 * 
 * @author BIANP
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 拦截所有请求
		registry.addInterceptor(LoginInterceptor()).addPathPatterns("**").excludePathPatterns("/static/**");

	}

	@Bean
	public LoginInterceptor LoginInterceptor() {
		return new LoginInterceptor();
	}

}
