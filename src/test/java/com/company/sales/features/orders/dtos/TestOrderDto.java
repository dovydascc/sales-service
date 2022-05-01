package com.company.sales.features.orders.dtos;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class TestOrderDto {

  public static OrderDto orderToDenmark() {
    return new OrderDto(
        null,
        "john@somemail.com",
        "Some str. 10, Copenhagen, Denmark",
        ZonedDateTime.parse("2022-04-01T15:00:00Z"),
        BigDecimal.valueOf(50),
        List.of(TestPurchasedProductDto.fiveAirMaxes())
    );
  }
}