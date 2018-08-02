package org.eap.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.eap.**.mapper")
@ComponentScan("org.eap.**.service,org.eap.**.manager")
public class EapConfiguration {

}
