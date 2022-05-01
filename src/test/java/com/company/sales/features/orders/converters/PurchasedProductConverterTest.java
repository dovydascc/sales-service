package com.company.sales.features.orders.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.company.sales.features.orders.dtos.TestPurchasedProductDto;
import com.company.sales.features.orders.entities.TestPurchasedProduct;
import com.company.sales.features.products.entities.Product;
import com.company.sales.features.products.entities.TestProduct;
import com.company.sales.features.products.repositories.ProductRepo;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PurchasedProductConverterTest {

  @InjectMocks
  private PurchasedProductConverter purchasedProductConverter;

  @Mock
  private ProductRepo productRepo;

  @Test
  void toDto() {
    // arrange
    var entity = TestPurchasedProduct.fiveAirMaxes()
        .withId(1L)
        .withProduct(TestProduct.airMaxShoes().withId(2L));

    // act
    var dto = purchasedProductConverter.toDto(entity);

    // assert
    assertThat(dto.id()).isEqualTo(1);
    assertThat(dto.productId()).isEqualTo(2);
    assertThat(dto.quantity()).isEqualTo(5);
    assertThat(dto.price()).isEqualTo(BigDecimal.valueOf(10));
  }

  @Test
  void toEntity() {
    // arrange
    var product = TestProduct.airMaxShoes();
    when(productRepo.findById(anyLong())).thenReturn(Optional.of(product));
    var dto = TestPurchasedProductDto.fiveAirMaxes().withId(1L);

    // act
    var entity = purchasedProductConverter.toEntity(dto);

    // assert
    assertThat(entity.getId()).isEqualTo(1);
    assertThat(entity.getProduct()).isEqualTo(product);
    assertThat(entity.getQuantity()).isEqualTo(5);
    assertThat(entity.getPrice()).isEqualTo(BigDecimal.valueOf(10));
  }
}