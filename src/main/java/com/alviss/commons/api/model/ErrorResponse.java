package com.alviss.commons.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

  String id;
  String status;
  int code;
  String message;
  Instant timestamp;
  String details;
}