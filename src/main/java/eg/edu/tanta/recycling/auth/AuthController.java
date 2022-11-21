package eg.edu.tanta.recycling.auth;

import eg.edu.tanta.recycling.api.dto.AccessTokenDto;
import eg.edu.tanta.recycling.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {


  private final AccessTokenService accessTokenService;



  public AuthController(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

  @PostMapping("/auth")
  public ResponseEntity<ApiResponse> token(Authentication authentication) {
    log.info("Token requested for user: '{}'", authentication.getName());
    String token = accessTokenService.generateToken(authentication);
    log.info("Token granted: {}", token);
    return new ResponseEntity<>(
        new ApiResponse<>(HttpStatus.OK.value(),"successfully authenticated", new AccessTokenDto(token)),
        HttpStatus.OK
    );
  }

}