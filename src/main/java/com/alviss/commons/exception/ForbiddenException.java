package com.alviss.commons.exception;

public class ForbiddenException extends RuntimeException implements BaseException, ResponseAware {

  private final String message;
  private Object responseObject;

  public ForbiddenException(final String message) {
    super(message);
    this.message = message;
  }

  public ForbiddenException(final String message, final Object responseObject) {
    super(message);
    this.message = message;
    this.responseObject = responseObject;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public int getCode() {
    return 403;
  }

  @Override
  public String getStatus() {
    return "FORBIDDEN";
  }

  @Override
  public Object getResponseObject() {
    return this.responseObject;
  }
}
