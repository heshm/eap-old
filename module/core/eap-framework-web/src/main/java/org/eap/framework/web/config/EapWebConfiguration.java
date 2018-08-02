package org.eap.framework.web.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@Import({ EapMvcConfiguration.class,EapSecConfiguration.class})
public class EapWebConfiguration {

	@Bean
	public ServletRegistrationBean<DispatcherServlet> restApiServlet() {
		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(EapRestMvcConfiguration.class);
		dispatcherServlet.setApplicationContext(applicationContext);
		ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet, "/api/*");
		servletRegistrationBean.setName("restApiServlet");
		return servletRegistrationBean;
	}
}
