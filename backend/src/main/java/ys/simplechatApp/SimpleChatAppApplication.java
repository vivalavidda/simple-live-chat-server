package ys.simplechatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import ys.simplechatApp.filter.RequestLogging;

@SpringBootApplication
public class SimpleChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleChatAppApplication.class, args);
	}

//	@Bean
	public FilterRegistrationBean firstFilterRegister() {
		return new FilterRegistrationBean(new RequestLogging());
	}
}
