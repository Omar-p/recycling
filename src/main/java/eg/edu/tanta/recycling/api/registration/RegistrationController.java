package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.api.ApiResponse;
import eg.edu.tanta.recycling.api.dto.UserDto;
import eg.edu.tanta.recycling.domain.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Locale;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;
  private final UserService userService;

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> register(@RequestBody @Valid UserDto userDto, HttpServletRequest request) {
    final long id = registrationService.registerUser(userDto, request);

    final var headers = new HttpHeaders();
    headers.setLocation(URI.create("/api/users/" + id));

    return new ResponseEntity<>(
        new ApiResponse<>(HttpStatus.CREATED.value(),
            "please check your email and click on the verification link.",
            null),
        headers,
        HttpStatus.CREATED
    );
  }

  @GetMapping(value = "/registrationConfirmation", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token, HttpServletRequest request) {
    Locale locale = request.getLocale();
    final var result = userService.validateVerificationToken(token);

    return new ResponseEntity<>(result, HttpStatus.valueOf(result.status()));
  }

}
