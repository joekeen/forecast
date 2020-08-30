package au.id.keen.forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

// exclude class that causes restart loop (bug)
// https://vaadin.com/forum/thread/17784869/vaadin-14-with-spring-security-login-page-not-loading
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class ForecastApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForecastApplication.class, args);
	}

}
