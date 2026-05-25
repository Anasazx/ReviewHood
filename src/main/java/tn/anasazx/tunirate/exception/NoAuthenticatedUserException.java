package tn.anasazx.tunirate.exception;


public class NoAuthenticatedUserException extends RuntimeException {

  public NoAuthenticatedUserException(String message) {
    super(message);
  }

  public NoAuthenticatedUserException() {
    super("No authenticated user found");
  }
}