package demo.longvh.demowebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class WebAppJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAppJavaApplication.class, args);
	}

}
