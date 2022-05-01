package com.company.sales.features.orders.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;

import com.company.sales.features.orders.entities.Order;
import com.company.sales.features.orders.entities.TestOrder;
import com.company.sales.features.orders.repositories.OrderRepo;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @InjectMocks
  private OrderService orderService;

  @Mock
  private OrderRepo orderRepo;

  @Captor
  private ArgumentCaptor<Order> orderCaptor;

  @Test
  void save() {
    // act
    orderService.save(TestOrder.orderToDenmark());

    // assert
    verify(orderRepo, times(1)).save(orderCaptor.capture());
    var order = orderCaptor.getValue();
    assertThat(order.getOrderedOn()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void getAll() {
    // arrange
    var pageable = PageRequest.of(0, 20, Sort.by(asc("orderedOn")));
    LocalDate dateFrom = LocalDate.parse("2022-04-01");
    LocalDate dateTo = LocalDate.parse("2022-04-30");

    Page<Order> paginatedContent = new PageImpl<>(List.of(), pageable, 0);
    when(orderRepo.findAllByOrderedOnAfterAndOrderedOnBefore(any(), any(), any())).thenReturn(paginatedContent);

    // act
    var orderPage = orderService.getAll(pageable, dateFrom, dateTo);

    // assert
    assertThat(orderPage).isEqualTo(paginatedContent);
  }
}