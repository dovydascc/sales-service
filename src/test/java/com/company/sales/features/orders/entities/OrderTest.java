package com.company.sales.features.orders.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void getTotalValue() {
    var order = TestOrder.orderToDenmark();

    assertThat(order.getTotalValue()).isEqualTo(BigDecimal.valueOf(50));
  }
}