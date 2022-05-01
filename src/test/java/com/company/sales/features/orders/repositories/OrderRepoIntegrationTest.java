package com.company.sales.features.orders.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Order.asc;

import com.company.sales.IntegrationTest;
import com.company.sales.features.orders.entities.Order;
import com.company.sales.features.orders.entities.TestOrder;
import com.company.sales.features.products.repositories.ProductRepo;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class OrderRepoIntegrationTest extends IntegrationTest {

  @Autowired
  private OrderRepo orderRepo;

  @Autowired
  private ProductRepo productRepo;

  private final Order orderToDenmark = TestOrder.orderToDenmark();

  @BeforeEach
  void setUp() {
    orderToDenmark.getPurchasedProducts()
        .forEach(purchasedProduct -> productRepo.save(purchasedProduct.getProduct()));
  }

  @Test
  void findAll_noOrders() {
    var orders = orderRepo.findAll();

    assertThat(orders).isEmpty();
  }

  @Test
  void save_newOrder() {
    // act
    var order = orderRepo.save(orderToDenmark);

    // assert
    assertThat(order.getId()).isPositive();
    assertThat(order.getEmail()).isEqualTo("john@somemail.com");
    assertThat(order.getAddress()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(order.getOrderedOn()).isEqualTo(ZonedDateTime.parse("2022-04-01T15:00:00Z"));
    assertThat(order.getPurchasedProducts()).hasSize(1);

    var purchasedProduct = order.getPurchasedProducts().get(0);
    assertThat(purchasedProduct.getId()).isPositive();
    assertThat(purchasedProduct.getProduct()).isNotNull();
    assertThat(purchasedProduct.getQuantity()).isEqualTo(5);
    assertThat(purchasedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10));
  }

  @Test
  void getById_success() {
    // arrange
    var order = orderRepo.save(orderToDenmark);

    // act
    order = orderRepo.getById(order.getId());

    // assert
    assertThat(order.getId()).isPositive();
    assertThat(order.getEmail()).isEqualTo("john@somemail.com");
  }

  @Test
  void findAllByOrderedOnAfterAndOrderedOnBefore_noResults() {
    // arrange
    var pageable = PageRequest.of(0, 3, Sort.by(asc("orderedOn")));
    var dateFrom = ZonedDateTime.parse("2022-04-01T15:00:00Z");
    var dateTo = ZonedDateTime.parse("2022-04-30T15:00:00Z");

    // act
    var ordersPage = orderRepo.findAllByOrderedOnAfterAndOrderedOnBefore(pageable, dateFrom, dateTo);

    // assert
    assertThat(ordersPage.hasContent()).isFalse();
  }

  @Test
  void findAllByOrderedOnAfterAndOrderedOnBefore_filtersByDate_paginates() {
    // arrange
    orderRepo.save(orderToDenmark.withOrderedOn(ZonedDateTime.parse("2019-01-01T00:00:00Z")));
    orderRepo.save(orderToDenmark.withOrderedOn(ZonedDateTime.parse("2020-03-01T00:00:00Z")));
    orderRepo.save(orderToDenmark.withOrderedOn(ZonedDateTime.parse("2022-04-01T16:00:00Z")));

    var pageable = PageRequest.of(0, 2, Sort.by(asc("orderedOn")));
    var dateFrom = ZonedDateTime.parse("2022-04-01T15:00:00Z");
    var dateTo = ZonedDateTime.parse("2022-04-30T15:00:00Z");

    // act
    var count = orderRepo.count();
    var ordersPage = orderRepo.findAllByOrderedOnAfterAndOrderedOnBefore(pageable, dateFrom, dateTo);

    // assert
    assertThat(count).isEqualTo(3);
    assertThat(ordersPage.getTotalElements()).isEqualTo(1);

    var order = ordersPage.getContent().get(0);

    assertThat(order.getId()).isPositive();
    assertThat(order.getEmail()).isEqualTo("john@somemail.com");
    assertThat(order.getAddress()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(order.getOrderedOn()).isEqualTo(ZonedDateTime.parse("2022-04-01T16:00:00Z"));
    assertThat(order.getTotalValue()).isEqualTo(BigDecimal.valueOf(50));
    assertThat(order.getPurchasedProducts().size()).isEqualTo(1);
  }
}
