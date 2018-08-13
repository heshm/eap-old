package org.eap.framework.config;

//import org.flowable.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.eap.**.mapper")
@ComponentScan("org.eap.**.service,org.eap.**.manager")
@EnableCaching
//@EnableAutoConfiguration(exclude={SecurityAutoConfiguration.class})
public class EapConfiguration {

}
