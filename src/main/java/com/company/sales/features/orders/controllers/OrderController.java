package com.company.sales.features.orders.controllers;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import com.company.sales.features.orders.converters.OrderConverter;
import com.company.sales.features.orders.dtos.OrderDto;
import com.company.sales.features.orders.services.OrderService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST APIs for Orders entities
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final OrderConverter orderConverter;

  /**
   * API to create a new order. This method expects OrderDto as a request body. It must contain valid
   * PurchasedProductDtos. Request body will be validated by Spring & JSR303 validator
   *
   * @param orderDto The payload
   * @return Created order
   */
  @PostMapping
  OrderDto save(@RequestBody @Validated OrderDto orderDto) {
    var order = orderConverter.toEntity(orderDto);
    order = orderService.save(order);
    return orderConverter.toDto(order);
  }

  /**
   * API to get a Page of orders between given dates. The response is paginated OrderDto list.
   *
   * @param pageable Spring's default pagination support. Example: ?page=0&size=20&sort=orderedOn,DESC&sort=email,ASC
   * @return A Page of OrderDtos. Max page size can be 2000 items (defined in configuration)
   */
  @GetMapping
  Page<OrderDto> get(
      Pageable pageable,
      @RequestParam(value = "dateFrom") @DateTimeFormat(iso = DATE) LocalDate dateFrom,
      @RequestParam(value = "dateTo") @DateTimeFormat(iso = DATE) LocalDate dateTo
  ) {
    var ordersPage = orderService.getAll(pageable, dateFrom, dateTo);
    return orderConverter.toDto(ordersPage, pageable);
  }
}
