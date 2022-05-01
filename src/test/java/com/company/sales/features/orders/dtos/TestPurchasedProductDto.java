package com.company.sales.features.orders.dtos;

import java.math.BigDecimal;

public class TestPurchasedProductDto {

  public static PurchasedProductDto fiveAirMaxes() {
    return new PurchasedProductDto(
        null,
        1L,
        5,
        BigDecimal.valueOf(10)
    );
  }
}