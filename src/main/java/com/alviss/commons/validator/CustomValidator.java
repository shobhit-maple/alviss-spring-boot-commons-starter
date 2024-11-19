package com.alviss.commons.validator;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CustomValidator {

  private final Validator validator;

  public Map<Path, List<String>> validate(final Object object) {
    val violations = validator.validate(object);
    if (!violations.isEmpty()) {
      return violations
          .stream()
          .collect(groupingBy(ConstraintViolation::getPropertyPath, mapping(ConstraintViolation::getMessage, toList())));
    }
    return new HashMap<>();
  }
}
