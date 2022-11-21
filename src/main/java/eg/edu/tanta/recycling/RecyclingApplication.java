package eg.edu.tanta.recycling;

import eg.edu.tanta.recycling.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RecyclingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecyclingApplication.class, args);
	}

}

@RestController
class HomeController {

	@GetMapping("/")
	public String hello(Authentication a) {
		return "Hello, " + a.getName();
	}
}
