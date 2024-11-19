package com.alviss.commons.exception;

public class UnauthorizedException extends RuntimeException implements BaseException {

  private final String message;

  public UnauthorizedException(final String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public int getCode() {
    return 401;
  }

  @Override
  public String getStatus() {
    return "UNAUTHORIZED";
  }
}
