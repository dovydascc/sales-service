package com.company.sales.features.orders.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.sales.IntegrationTest;
import com.company.sales.errorhandling.ErrorResponse;
import com.company.sales.features.orders.dtos.OrderDto;
import com.company.sales.features.orders.dtos.TestOrderDto;
import com.company.sales.features.orders.dtos.TestPurchasedProductDto;
import com.company.sales.features.orders.entities.TestOrder;
import com.company.sales.features.orders.entities.TestPurchasedProduct;
import com.company.sales.features.orders.repositories.OrderRepo;
import com.company.sales.features.products.entities.Product;
import com.company.sales.features.products.entities.TestProduct;
import com.company.sales.features.products.repositories.ProductRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderControllerIntegrationTest extends IntegrationTest {

  @Autowired
  private ProductRepo productRepo;

  @Autowired
  private OrderRepo orderRepo;

  private final Product airMaxShoes = TestProduct.airMaxShoes();

  private final OrderDto invalidOrderDto = TestOrderDto.orderToDenmark().withEmail("");

  private OrderDto orderToDenmarkDto;

  @BeforeEach
  void setUp() {
    productRepo.save(airMaxShoes);

    orderToDenmarkDto = TestOrderDto.orderToDenmark().withPurchasedProducts(List.of(
        TestPurchasedProductDto.fiveAirMaxes().withProductId(airMaxShoes.getId())
    ));
  }

  @Test
  void save_invalidRequest() throws Exception {
    // arrange
    var json = objectMapper.writeValueAsString(invalidOrderDto);

    // act
    var response = mockMvc.perform(post("/api/v1/orders").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    // assert
    assertThat(errorResponse.message()).isEqualTo("Invalid request, see data");
    assertThat(errorResponse.data()).asList().containsExactly("email: must not be empty");
    assertThat(errorResponse.timestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void save_success() throws Exception {
    // arrange
    var json = objectMapper.writeValueAsString(orderToDenmarkDto);

    // act
    var response = mockMvc.perform(post("/api/v1/orders").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var order = objectMapper.readValue(response, OrderDto.class);

    // assert
    assertThat(order.id()).isPositive();
    assertThat(order.email()).isEqualTo("john@somemail.com");
    assertThat(order.address()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(order.orderedOn()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
    assertThat(order.totalValue()).isEqualTo(BigDecimal.valueOf(50));
    assertThat(order.purchasedProducts().size()).isEqualTo(1);

    var purchasedProduct = order.purchasedProducts().get(0);
    assertThat(purchasedProduct.id()).isPositive();
    assertThat(purchasedProduct.productId()).isPositive();
    assertThat(purchasedProduct.quantity()).isEqualTo(5);
    assertThat(purchasedProduct.price()).isEqualTo(BigDecimal.valueOf(10));
  }

  @Test
  void get_filtersByDate_paginatesResult() throws Exception {
    // arrange
    var purchasedProducts = List.of(TestPurchasedProduct.fiveAirMaxes().withProduct(airMaxShoes));

    orderRepo.saveAll(List.of(
        TestOrder.orderToDenmark()
            .withPurchasedProducts(purchasedProducts)
            .withOrderedOn(ZonedDateTime.parse("2019-01-01T00:00:00Z")),
        TestOrder.orderToDenmark()
            .withPurchasedProducts(purchasedProducts)
            .withOrderedOn(ZonedDateTime.parse("2020-04-01T00:00:00Z")),
        TestOrder.orderToDenmark()
            .withPurchasedProducts(purchasedProducts)
            .withOrderedOn(ZonedDateTime.parse("2022-04-01T16:00:00Z")),
        TestOrder.orderToDenmark()
            .withPurchasedProducts(purchasedProducts)
            .withOrderedOn(ZonedDateTime.parse("2022-04-30T10:00:00Z")),
        TestOrder.orderToDenmark()
            .withPurchasedProducts(purchasedProducts)
            .withOrderedOn(ZonedDateTime.parse("2022-05-01T10:00:00Z"))
    ));

    // act
    var response = mockMvc.perform(get("/api/v1/orders")
            .param("page", "0")
            .param("size", "2")
            .param("dateFrom", "2022-04-01")
            .param("dateTo", "2022-04-30"))
        .andExpect(status().isPartialContent()).andReturn().getResponse().getContentAsString();
    var orders = objectMapper.readValue(response, new TypeReference<List<OrderDto>>() {
    });

    // assert
    assertThat(orders.size()).isEqualTo(2);
    var order = orders.get(0);

    assertThat(order.id()).isPositive();
    assertThat(order.email()).isEqualTo("john@somemail.com");
    assertThat(order.address()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(order.orderedOn()).isEqualTo(ZonedDateTime.parse("2022-04-01T16:00:00Z"));
    assertThat(order.totalValue()).isEqualTo(BigDecimal.valueOf(50));
    assertThat(order.purchasedProducts().size()).isEqualTo(1);

    var purchasedProduct = order.purchasedProducts().get(0);
    assertThat(purchasedProduct.id()).isPositive();
    assertThat(purchasedProduct.productId()).isPositive();
    assertThat(purchasedProduct.quantity()).isEqualTo(5);
    assertThat(purchasedProduct.price()).isEqualTo(BigDecimal.valueOf(10));
  }
}