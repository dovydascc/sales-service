package com.company.sales.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Test execution clears all the database before running. Note that pointing test execution on main database could bring
 * unintended consequences.
 */
@Configuration
class FlywayConfig {

  @Bean
  FlywayMigrationStrategy clean() {
    return flyway -> {
      flyway.clean();
      flyway.migrate();
    };
  }
}
