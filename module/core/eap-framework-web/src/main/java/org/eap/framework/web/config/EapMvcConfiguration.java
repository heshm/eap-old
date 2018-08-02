package org.eap.framework.web.config;

import org.eap.framework.web.interceptor.LogInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("org.eap.*.*.controller")
public class EapMvcConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/static/**");
	}
	
	/*@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}*/

}
