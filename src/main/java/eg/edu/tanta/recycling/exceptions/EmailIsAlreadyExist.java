package eg.edu.tanta.recycling.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailIsAlreadyExist extends RuntimeException {
  public EmailIsAlreadyExist(String message) {
    super(message);
  }
}
