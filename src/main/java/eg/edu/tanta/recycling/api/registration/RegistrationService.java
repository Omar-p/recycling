package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.api.dto.UserDto;
import eg.edu.tanta.recycling.domain.User;
import eg.edu.tanta.recycling.domain.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

  private final ApplicationEventPublisher eventPublisher;
  private final UserService userService;

  @Transactional
  public long registerUser(UserDto userDto, HttpServletRequest request) {
    String appUrl = request.getContextPath();
    final User registeredUser = userService.registerNewUserAccount(userDto);

    eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), appUrl));

    return registeredUser.getId();
  }





}
