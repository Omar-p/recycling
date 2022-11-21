package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.domain.User;
import eg.edu.tanta.recycling.domain.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class RegistrationListener implements
    ApplicationListener<OnRegistrationCompleteEvent> {
  private final JavaMailSender mailSender;
  private final UserService userService;
  private final MessageSource messages;

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    User user = event.getUser();
    String token = userService.findVerificationTokenByUser(user);

    String recipientAddress = user.getEmail();
    String subject = "Registration Confirmation";
    String confirmationUrl
        = event.getAppUrl() + "/registrationConfirm?token=" + token;

    String message = "succ"; //messages.getMessage("message.regSucc", null, event.getLocale());
    SimpleMailMessage email = new SimpleMailMessage();
    email.setTo(recipientAddress);
    email.setSubject(subject);
    email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
    mailSender.send(email);
  }
}
