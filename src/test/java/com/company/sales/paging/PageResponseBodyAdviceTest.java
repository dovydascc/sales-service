package com.company.sales.paging;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.ReflectionUtils;

class PageResponseBodyAdviceTest {

  private final PageResponseBodyAdvice pageResponseBodyAdvice = new PageResponseBodyAdvice();
  private final ServletServerHttpRequest request = new ServletServerHttpRequest(new MockHttpServletRequest());
  private final ServletServerHttpResponse response = new ServletServerHttpResponse(new MockHttpServletResponse());

  // In these tests we must imitate Controller's method, that returns a Page<SomeEntity>. See private empty method below
  private final Method someMethod =
      ReflectionUtils.findMethod(PageResponseBodyAdviceTest.class, "someMethodThatReturnsPage");

  @Test
  void supports() {
    var supports = pageResponseBodyAdvice.supports(
        new MethodParameter(Objects.requireNonNull(someMethod), -1),
        MappingJackson2HttpMessageConverter.class
    );
    assertThat(supports).isTrue();
  }

  @Test
  void beforeBodyWrite() {
    var content = List.of("some", "content", "with", "many", "items", "and", "even", "more", "items");
    var paginatedContent = new PageImpl<>(content, PageRequest.of(2, 3), content.size());

    pageResponseBodyAdvice.beforeBodyWrite(paginatedContent,
        new MethodParameter(Objects.requireNonNull(someMethod), -1),
        MediaType.APPLICATION_JSON,
        MappingJackson2HttpMessageConverter.class,
        request,
        response);

    assertThat(response.getServletResponse().getStatus()).isEqualTo(HttpStatus.PARTIAL_CONTENT.value());

    var headers = response.getHeaders();
    assertThat(headers.get(PageResponseBodyAdvice.X_CURRENT_PAGE)).containsExactly("2");
    assertThat(headers.get(PageResponseBodyAdvice.X_TOTAL_PAGES)).containsExactly("3");
    assertThat(headers.get(PageResponseBodyAdvice.X_TOTAL_COUNT)).containsExactly("9");
  }

  @SuppressWarnings("unused") // Mock method of imaginary RestController. Method is used in test by reflection utils
  private Page<?> someMethodThatReturnsPage() {
    return new PageImpl<>(List.of());
  }
}
