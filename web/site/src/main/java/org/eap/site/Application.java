package org.eap.site;

import org.eap.framework.config.EapConfiguration;
import org.eap.framework.web.config.EapWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({ EapWebConfiguration.class,EapConfiguration.class})
public class Application {

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);

	}

}
