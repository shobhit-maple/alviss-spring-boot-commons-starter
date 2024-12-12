package com.alviss.commons.dao.tracing;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "tracing.db")
public class DatabaseTracingProperties {

  private boolean enabled = true;

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }
}