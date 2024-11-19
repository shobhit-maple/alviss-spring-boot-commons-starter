package com.alviss.commons.exception;

public class NotFoundException extends RuntimeException implements BaseException {

  private final String message;

  public NotFoundException(final String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public int getCode() {
    return 404;
  }

  @Override
  public String getStatus() {
    return "NOT_FOUND";
  }
}
