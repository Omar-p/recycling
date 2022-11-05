package eg.edu.tanta.recycling.registration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping
  public Map<String, Long> register(@Valid RegistrationForm registrationForm) {
    final long id = registrationService.registerUser(registrationForm);
    return Map.of("id", id);
  }

  @GetMapping
  public String hello(Principal principal) {
    return "Hello, " + principal.getName();
  }
}
