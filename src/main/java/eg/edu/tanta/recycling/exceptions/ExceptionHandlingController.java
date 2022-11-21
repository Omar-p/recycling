package eg.edu.tanta.recycling.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {

  @ExceptionHandler(EmailIsAlreadyExist.class)
  public ResponseEntity<ApiErrorResponse> emailIsAlreadyExistHandler(EmailIsAlreadyExist emailIsAlreadyExist) {
    return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.CONFLICT.value(), emailIsAlreadyExist.getMessage()),
        HttpStatus.CONFLICT);
  }


  @ExceptionHandler(UsernameIsAlreadyExist.class)
  public ResponseEntity<?> usernameIsAlreadyExistHandler(UsernameIsAlreadyExist usernameIsAlreadyExist) {
    return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.CONFLICT.value(), usernameIsAlreadyExist.getMessage()),
        HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
      ConstraintViolationException ex,
      HttpServletRequest request
  ) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "Validation error. Check 'errors' field for details."
    );

    for (var violation : ex.getConstraintViolations()) {
      errorResponse.addValidationError(violation.getPropertyPath().toString(),
          violation.getMessage());
    }
    return ResponseEntity.unprocessableEntity().body(errorResponse);
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    ApiErrorResponse errorResponse = new ApiErrorResponse(
        HttpStatus.UNPROCESSABLE_ENTITY.value(),
        "Validation error. Check 'errors' field for details."
    );

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errorResponse.addValidationError(fieldError.getField(),
          fieldError.getDefaultMessage());
    }
    return ResponseEntity.unprocessableEntity().body(errorResponse);
  }
}
