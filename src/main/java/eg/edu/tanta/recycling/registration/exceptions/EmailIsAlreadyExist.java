package eg.edu.tanta.recycling.registration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailIsAlreadyExist extends RuntimeException {
  public EmailIsAlreadyExist(String message) {
    super(message);
  }
}
