package com.company.sales.features.orders.converters;

import com.company.sales.features.orders.dtos.OrderDto;
import com.company.sales.features.orders.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConverter {

  private final PurchasedProductConverter purchasedProductConverter;

  public OrderDto toDto(Order order) {
    var purchasedProducts = order.getPurchasedProducts()
        .stream()
        .map(purchasedProductConverter::toDto)
        .toList();

    return new OrderDto(
        order.getId(),
        order.getEmail(),
        order.getAddress(),
        order.getOrderedOn(),
        order.getTotalValue(),
        purchasedProducts
    );
  }

  public Order toEntity(OrderDto orderDto) {
    var purchasedProducts = orderDto.purchasedProducts()
        .stream()
        .map(purchasedProductConverter::toEntity)
        .toList();

    return new Order(
        orderDto.id(),
        orderDto.email(),
        orderDto.address(),
        orderDto.orderedOn(),
        purchasedProducts
    );
  }

  public Page<OrderDto> toDto(Page<Order> orders, Pageable pageable) {
    var orderDtos = orders.getContent().stream().map(this::toDto).toList();
    return new PageImpl<>(orderDtos, pageable, orders.getTotalElements());
  }
}
