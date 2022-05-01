package com.company.sales.features.orders.entities;

import com.company.sales.features.products.entities.TestProduct;
import java.math.BigDecimal;

public class TestPurchasedProduct {

  public static PurchasedProduct fiveAirMaxes() {
    return new PurchasedProduct(
        null,
        TestProduct.airMaxShoes(),
        5,
        BigDecimal.valueOf(10)
    );
  }
}