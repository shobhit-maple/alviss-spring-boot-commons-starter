package com.alviss.commons.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alviss.commons.api.model.ErrorResponse;
import com.alviss.commons.exception.BadRequestException;
import com.alviss.commons.exception.BaseException;
import com.alviss.commons.exception.ForbiddenException;
import com.alviss.commons.exception.InternalServerErrorException;
import com.alviss.commons.exception.NotFoundException;
import com.alviss.commons.exception.ResponseAware;
import com.alviss.commons.exception.UnauthorizedException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  private final ObjectMapper objectMapper;

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleValidationError(
      final MethodArgumentNotValidException exception) {
    val errorList =
        exception.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();
    return toError(new BadRequestException(exception.getMessage(), errorList));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBadCredentialsError(
      final BadCredentialsException exception) {
    return toError(new BadRequestException(exception.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBadRequestError(
      final HttpMessageNotReadableException exception) {
    return toError(new BadRequestException(exception.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleUnauthorizedError(
      final UnauthorizedException exception) {
    return toError(exception);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleMethodNotSupportedError(
      final HttpRequestMethodNotSupportedException exception) {
    return toError(new BadRequestException(exception.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleAccessDeniedError(
      final AccessDeniedException exception) {
    return toError(new ForbiddenException(exception.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleBadRequestError(final BadRequestException exception) {
    return toError(exception);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleNotFoundError(final NotFoundException exception) {
    return toError(exception);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleInternalError(
      final InternalServerErrorException exception) {
    return toError(exception);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> fallbackExceptionHandler(final Exception exception) {
    log.error("Unknown error", exception);
    return toError(new InternalServerErrorException(exception.getMessage()));
  }

  @SneakyThrows
  private ResponseEntity<ErrorResponse> toError(final RuntimeException exception) {
    val base = (BaseException) exception;
    val errorCode = Optional.of(base.getCode()).orElse(500);
    return ResponseEntity.status(HttpStatus.resolve(errorCode))
        .body(
            ErrorResponse.builder()
                .id(UUID.randomUUID().toString())
                .status(base.getStatus())
                .code(base.getCode())
                .message(base.getMessage())
                .timestamp(Instant.now())
                .details(
                    (exception instanceof ResponseAware resp && resp.getResponseObject() != null)
                        ? objectMapper.writeValueAsString(resp.getResponseObject())
                        : null)
                .build());
  }
}
