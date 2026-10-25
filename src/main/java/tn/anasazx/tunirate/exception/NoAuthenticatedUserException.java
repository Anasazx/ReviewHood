package tn.anasazx.tunirate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoAuthenticatedUserException extends RuntimeException {

  public NoAuthenticatedUserException(String message) {
    super(message);
  }

  public NoAuthenticatedUserException() {
    super("No authenticated user found");
  }
}