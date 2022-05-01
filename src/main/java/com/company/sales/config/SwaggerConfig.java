package com.company.sales.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

class SwaggerConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("sales-service")
        .pathsToMatch("/api/**")
        .build();
  }
}
