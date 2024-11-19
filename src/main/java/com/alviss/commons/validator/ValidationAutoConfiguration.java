package com.alviss.commons.validator;

import com.alviss.commons.validator.CustomValidator;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationAutoConfiguration {

  @Bean
  public CustomValidator customValidator(final Validator validator) {
    return new CustomValidator(validator);
  }
}
