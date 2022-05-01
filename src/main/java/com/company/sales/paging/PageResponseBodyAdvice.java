package com.company.sales.paging;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * PageResponseBodyAdvice intercepts Page<SomeClass> responses.
 *
 * 1. It converts Page<SomeClass> to List<SomeClass>.
 * 2. It adds special HTTP headers
 *   X-Current-Page - Current page number, which is returned
 *   X-Total-Pages - Total number of pages exists in the database
 *   X-Total-Count - Total number of entries that exist in the database
 * 3. It applies HTTP status 206 - PARTIAL-CONTENT
 *
 * Motivation behind this: Unwrap Spring's Page responses to avoid Page class serialization. Otherwise actual content is
 * wrapped under "content" field and 10 pagination related fields are serialized to response body. This would clutter
 * JSON response that is sent to frontend.
 */
@ControllerAdvice
@SuppressWarnings("rawtypes") // Specific type in the Iterable is not known
public class PageResponseBodyAdvice implements ResponseBodyAdvice<Iterable> {

  public static final String X_CURRENT_PAGE = "X-Current-Page";
  public static final String X_TOTAL_PAGES = "X-Total-Pages";
  public static final String X_TOTAL_COUNT = "X-Total-Count";

  /**
   * This advice supports only Page<SomeClass> responses. Page may contain more generics. E.G.
   * Page<SomeType<SomeOtherType>>
   */
  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Page.class.isAssignableFrom(returnType.getParameterType());
  }

  @Override
  public Iterable<?> beforeBodyWrite(
      Iterable body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response
  ) {
    response.setStatusCode(HttpStatus.PARTIAL_CONTENT);
    HttpHeaders headers = response.getHeaders();

    Page<?> page = (Page<?>) body;
    addPaginationHeaders(headers, page);
    return page.getContent();
  }

  private void addPaginationHeaders(HttpHeaders headers, Page<?> page) {
    headers.add(X_CURRENT_PAGE, String.valueOf(page.getNumber()));
    headers.add(X_TOTAL_PAGES, String.valueOf(page.getTotalPages()));
    headers.add(X_TOTAL_COUNT, String.valueOf(page.getTotalElements()));
  }
}
