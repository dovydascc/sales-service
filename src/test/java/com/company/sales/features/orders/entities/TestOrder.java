package com.company.sales.features.orders.entities;

import java.time.ZonedDateTime;
import java.util.List;

public class TestOrder {

  public static Order orderToDenmark() {
    return new Order(
        null,
        "john@somemail.com",
        "Some str. 10, Copenhagen, Denmark",
        ZonedDateTime.parse("2022-04-01T15:00:00Z"),
        List.of(TestPurchasedProduct.fiveAirMaxes())
    );
  }
}