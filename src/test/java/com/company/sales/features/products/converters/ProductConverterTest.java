package com.company.sales.features.products.converters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Order.asc;

import com.company.sales.features.products.dtos.TestProductDto;
import com.company.sales.features.products.entities.TestProduct;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProductConverterTest {

  @InjectMocks
  private ProductConverter productConverter;

  @Test
  void toDto() {
    var dto = productConverter.toDto(TestProduct.airMaxShoes().withId(1L));

    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.sku()).isEqualTo("DJ3624-100");
    assertThat(dto.name()).isEqualTo("Nike Air Max Real Dawn");
    assertThat(dto.description()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(dto.price()).isEqualTo(BigDecimal.valueOf(104.95));
    assertThat(dto.warrantyDays()).isEqualTo(365);
    assertThat(dto.listed()).isTrue();
  }

  @Test
  void toEntity() {
    var entity = productConverter.toEntity(TestProductDto.airMaxShoes().withId(1L));

    assertThat(entity.getId()).isEqualTo(1L);
    assertThat(entity.getSku()).isEqualTo("DJ3624-100");
    assertThat(entity.getName()).isEqualTo("Nike Air Max Real Dawn");
    assertThat(entity.getDescription()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(entity.getPrice()).isEqualTo(BigDecimal.valueOf(104.95));
    assertThat(entity.getWarrantyDays()).isEqualTo(365);
    assertThat(entity.isListed()).isTrue();
  }

  @Test
  void toDtoPage() {
    var pageable = PageRequest.of(0, 3, Sort.by(asc("price")));
    var entitiesPage = new PageImpl<>(List.of(TestProduct.airMaxShoes().withId(1L)), pageable, 1);

    var dtoPage = productConverter.toDto(entitiesPage, pageable);

    assertThat(dtoPage.getTotalElements()).isEqualTo(1);
  }
}