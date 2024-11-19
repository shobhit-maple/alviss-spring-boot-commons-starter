package com.alviss.commons.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@JsonInclude(Include.NON_NULL)
public class Response<T> {

  private T content;
  private Pagination pagination;

  public Response(final T content) {
    this.content = content;
  }

  public Response(final T content, final Page page) {
    this.content = content;
    if (page != null) {
      pagination = new Pagination(page);
    }
  }

  @Data
  @AllArgsConstructor
  @JsonInclude(Include.NON_NULL)
  public static class Pagination {

    public Pagination(final Page page) {
      this.page = page.getNumber();
      this.size = page.getSize();
      this.first = page.isFirst();
      this.last = page.isLast();
      this.numberOfElements = page.getNumberOfElements();
      this.totalElements = page.getTotalElements();
      this.totalPages = page.getTotalPages();
      this.sort = new HashMap<>();
      page.getSort()
          .forEach(
              order -> {
                sort.put(order.getProperty(), order.getDirection().toString());
              });
    }

    public int page;
    public int size;
    public boolean first;
    public boolean last;
    @JsonProperty("number_of_elements")
    public int numberOfElements;
    @JsonProperty("total_elements")
    public long totalElements;
    @JsonProperty("total_pages")
    public long totalPages;
    public Map<String, String> sort;
  }
}
