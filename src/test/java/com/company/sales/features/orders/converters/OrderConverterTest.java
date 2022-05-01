package com.company.sales.features.orders.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;

import com.company.sales.features.orders.converters.OrderConverter;
import com.company.sales.features.orders.converters.PurchasedProductConverter;
import com.company.sales.features.orders.dtos.TestOrderDto;
import com.company.sales.features.orders.dtos.TestPurchasedProductDto;
import com.company.sales.features.orders.entities.TestOrder;
import com.company.sales.features.orders.entities.TestPurchasedProduct;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class OrderConverterTest {

  @InjectMocks
  private OrderConverter orderConverter;

  @Mock
  private PurchasedProductConverter purchasedProductConverter;

  @Test
  void toDto() {
    // arrange
    var fiveAirMaxes = TestPurchasedProductDto.fiveAirMaxes();
    when(purchasedProductConverter.toDto(any())).thenReturn(fiveAirMaxes);

    // act
    var dto = orderConverter.toDto(TestOrder.orderToDenmark().withId(1L));

    // assert
    assertThat(dto.id()).isEqualTo(1);
    assertThat(dto.email()).isEqualTo("john@somemail.com");
    assertThat(dto.address()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(dto.orderedOn()).isEqualTo(ZonedDateTime.parse("2022-04-01T15:00:00Z"));
    assertThat(dto.totalValue()).isEqualTo(BigDecimal.valueOf(50));
    assertThat(dto.purchasedProducts()).isEqualTo(List.of(fiveAirMaxes));
  }

  @Test
  void toEntity() {
    // arrange
    var fiveAirMaxes = TestPurchasedProduct.fiveAirMaxes();
    when(purchasedProductConverter.toEntity(any())).thenReturn(fiveAirMaxes);

    // act
    var entity = orderConverter.toEntity(TestOrderDto.orderToDenmark().withId(1L));

    // assert
    assertThat(entity.getId()).isEqualTo(1);
    assertThat(entity.getEmail()).isEqualTo("john@somemail.com");
    assertThat(entity.getAddress()).isEqualTo("Some str. 10, Copenhagen, Denmark");
    assertThat(entity.getOrderedOn()).isEqualTo(ZonedDateTime.parse("2022-04-01T15:00:00Z"));
    assertThat(entity.getPurchasedProducts()).isEqualTo(List.of(fiveAirMaxes));
  }

  @Test
  void toDtoPage() {
    // arrange
    var pageable = PageRequest.of(0, 3, Sort.by(asc("orderedOn")));
    var entitiesPage = new PageImpl<>(List.of(TestOrder.orderToDenmark().withId(1L)), pageable, 1);

    // act
    var dtoPage = orderConverter.toDto(entitiesPage, pageable);

    // assert
    assertThat(dtoPage.getTotalElements()).isEqualTo(1);
  }
}