package eg.edu.tanta.recycling.api.registration;

import eg.edu.tanta.recycling.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private User user;
  private Locale locale;
  private String appUrl;

  public OnRegistrationCompleteEvent(
      User user, Locale locale, String appUrl) {
    super(user);
    this.user = user;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
