package com.company.sales.errorhandling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.sales.IntegrationTest;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class ExceptionHandlingAdviceIntegrationTest extends IntegrationTest {

  @Test
  void validRequest_noException() throws Exception {
    mockMvc.perform(get("/api/v1/test/ok"))
        .andExpect(status().isOk());
  }

  @Test
  void controllerThrowsArgumentNotValid_http400() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/argument-not-valid"))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.error()).isEqualTo("MethodArgumentNotValidException");
    assertThat(errorResponse.message()).isEqualTo("Invalid request, see data");
    assertThat(errorResponse.data()).asList()
        .containsExactly("name: Please enter unique name", "warrantyDays: Please enter a valid warranty");
    assertThat(errorResponse.timestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void controllerThrowsException_http500() throws Exception {
    var response = mockMvc.perform(get("/api/v1/test/some-throwable"))
        .andExpect(status().isInternalServerError())
        .andReturn()
        .getResponse()
        .getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    assertThat(errorResponse.error()).isEqualTo("SalesServiceException");
    assertThat(errorResponse.message()).isEqualTo("Internal server error, please contact support");
    assertThat(errorResponse.data()).isNull();
    assertThat(errorResponse.timestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }
}