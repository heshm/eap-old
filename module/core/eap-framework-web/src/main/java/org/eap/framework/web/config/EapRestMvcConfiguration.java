package org.eap.framework.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
@ComponentScan("org.eap.*.*.endpoint")
public class EapRestMvcConfiguration implements WebMvcConfigurer{
	
	
}
