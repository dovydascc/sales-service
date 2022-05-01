package com.company.sales.features.products.entities;

import java.math.BigDecimal;

public class TestProduct {

  public static Product airMaxShoes() {
    return new Product()
        .withSku("DJ3624-100")
        .withName("Nike Air Max Real Dawn")
        .withDescription(
            "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.")
        .withPrice(BigDecimal.valueOf(104.95))
        .withWarrantyDays(365)
        .withListed(true);
  }

  public static Product jordanMaShoes() {
    return new Product()
        .withSku("DO6727-100")
        .withName("Jordan MA2")
        .withDescription(
            "Shatter the sneaker status quo in the Jordan MA2. It's built from a mix of diverse materials and design details.")
        .withPrice(BigDecimal.valueOf(124.50))
        .withWarrantyDays(14)
        .withListed(true);
  }

  public static Product craterShoes() {
    return new Product()
        .withSku("DJ6308-100")
        .withName("Crater Impact SE")
        .withDescription(
            "Crater Impact is part of our sustainability journey to transform trash into shoes that tread a little lighter.")
        .withPrice(BigDecimal.valueOf(50.99))
        .withWarrantyDays(365)
        .withListed(true);
  }

  public static Product zoomShoes() {
    return new Product()
        .withSku("DH3392-010")
        .withName("Nike Zoom Metcon Turbo 2")
        .withDescription("The Nike Zoom Metcon Turbo 2 puts a shot of adrenalizing speed into your everyday workout.")
        .withPrice(BigDecimal.valueOf(99.35))
        .withWarrantyDays(365)
        .withListed(true);
  }
}