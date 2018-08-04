package org.eap.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.eap.**.mapper")
@ComponentScan("org.eap.**.service,org.eap.**.manager")
@EnableCaching
public class EapConfiguration {

}
