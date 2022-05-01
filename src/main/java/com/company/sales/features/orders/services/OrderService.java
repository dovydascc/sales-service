package com.company.sales.features.orders.services;

import com.company.sales.features.orders.entities.Order;
import com.company.sales.features.orders.repositories.OrderRepo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepo orderRepo;

  @Transactional
  public Order save(Order order) {
    order.setOrderedOn(ZonedDateTime.now());
    return orderRepo.save(order);
  }

  public Page<Order> getAll(Pageable pageable, LocalDate dateFrom, LocalDate dateTo) {
    ZonedDateTime dateTimeFrom = dateFrom.atStartOfDay(ZoneOffset.UTC);
    ZonedDateTime dateTimeTo = dateTo.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC);
    return orderRepo.findAllByOrderedOnAfterAndOrderedOnBefore(pageable, dateTimeFrom, dateTimeTo);
  }
}
