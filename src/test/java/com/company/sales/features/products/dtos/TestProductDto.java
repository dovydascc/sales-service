package com.company.sales.features.products.dtos;

import java.math.BigDecimal;

public class TestProductDto {

  public static ProductDto airMaxShoes() {
    return new ProductDto(
        null,
        "DJ3624-100",
        "Nike Air Max Real Dawn",
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.",
        BigDecimal.valueOf(104.95),
        365,
        true);
  }
}

