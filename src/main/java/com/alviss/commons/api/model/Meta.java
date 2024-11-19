package com.alviss.commons.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Meta {

  @JsonProperty("created_date")
  private LocalDateTime createdDate;
  @JsonProperty("last_modified_date")
  private LocalDateTime lastModifiedDate;
}