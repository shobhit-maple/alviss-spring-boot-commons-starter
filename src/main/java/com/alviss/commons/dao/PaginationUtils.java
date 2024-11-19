package com.alviss.commons.dao;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@UtilityClass
public class PaginationUtils {

  private static final int MAX_PAGE_SIZE = 20;
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int MIN_PAGE_NUMBER = 0;

  public static Pageable validateAndGetPageable(final Pageable pageable) {

    if (pageable == null) {

      return PageRequest.of(MIN_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
    }
    return PageRequest.of(
        pageable.getPageNumber(), validatePageSize(pageable.getPageSize()), pageable.getSort());
  }

  private static Integer validatePageSize(final Integer size) {

    if (size > MAX_PAGE_SIZE) {

      return MAX_PAGE_SIZE;
    }

    return size;
  }
}
