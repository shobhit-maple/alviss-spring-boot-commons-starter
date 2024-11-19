package com.alviss.commons.exception;

public class InternalServerErrorException extends RuntimeException implements BaseException {

  private final String message;

  public InternalServerErrorException(final String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public int getCode() {
    return 500;
  }

  @Override
  public String getStatus() {
    return "INTERNAL_SERVER_ERROR";
  }
}
