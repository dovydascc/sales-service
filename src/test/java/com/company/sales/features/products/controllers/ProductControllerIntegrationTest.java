package com.company.sales.features.products.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.company.sales.IntegrationTest;
import com.company.sales.errorhandling.ErrorResponse;
import com.company.sales.features.products.dtos.ProductDto;
import com.company.sales.features.products.dtos.TestProductDto;
import com.company.sales.features.products.entities.Product;
import com.company.sales.features.products.entities.TestProduct;
import com.company.sales.features.products.repositories.ProductRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductControllerIntegrationTest extends IntegrationTest {

  @Autowired
  private ProductRepo productRepo;

  private final ProductDto invalidProductDto = TestProductDto.airMaxShoes().withPrice(BigDecimal.valueOf(-10));

  private final ProductDto airMaxShoesDto = TestProductDto.airMaxShoes();

  private final Product airMaxShoes = TestProduct.airMaxShoes();

  @Test
  void save_invalidRequest() throws Exception {
    // arrange
    var json = objectMapper.writeValueAsString(invalidProductDto);

    // act
    var response = mockMvc.perform(post("/api/v1/products").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    // assert
    assertThat(errorResponse.message()).isEqualTo("Invalid request, see data");
    assertThat(errorResponse.data()).asList().containsExactly("price: must be greater than or equal to 0");
    assertThat(errorResponse.timestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void save_success() throws Exception {
    // arrange
    var json = objectMapper.writeValueAsString(airMaxShoesDto);

    // act
    var response = mockMvc.perform(post("/api/v1/products").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var product = objectMapper.readValue(response, ProductDto.class);

    // assert
    assertThat(product.id()).isPositive();
    assertThat(product.sku()).isEqualTo("DJ3624-100");
    assertThat(product.name()).isEqualTo("Nike Air Max Real Dawn");
    assertThat(product.description()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(product.price()).isEqualTo(BigDecimal.valueOf(104.95));
    assertThat(product.warrantyDays()).isEqualTo(365);
    assertThat(product.listed()).isTrue();
  }

  @Test
  void get_paginatesResult() throws Exception {
    // arrange
    productRepo.saveAll(List.of(
        TestProduct.airMaxShoes(),
        TestProduct.craterShoes(),
        TestProduct.jordanMaShoes(),
        TestProduct.zoomShoes()
    ));

    // act
    var response = mockMvc.perform(get("/api/v1/products")
            .param("page", "0")
            .param("size", "2")
            .param("dateFrom", "2022-04-01"))
        .andExpect(status().isPartialContent()).andReturn().getResponse().getContentAsString();
    var products = objectMapper.readValue(response, new TypeReference<List<ProductDto>>() {
    });

    // assert
    assertThat(products.size()).isEqualTo(2);
    var product = products.get(0);

    assertThat(product.id()).isPositive();
    assertThat(product.sku()).isEqualTo("DJ3624-100");
    assertThat(product.name()).isEqualTo("Nike Air Max Real Dawn");
    assertThat(product.description()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(product.price()).isEqualTo(BigDecimal.valueOf(104.95));
    assertThat(product.warrantyDays()).isEqualTo(365);
    assertThat(product.listed()).isTrue();
  }

  @Test
  void update_invalidRequest() throws Exception {
    // arrange
    var json = objectMapper.writeValueAsString(invalidProductDto);

    // act
    var response = mockMvc.perform(put("/api/v1/products/1").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
    var errorResponse = objectMapper.readValue(response, ErrorResponse.class);

    // assert
    assertThat(errorResponse.message()).isEqualTo("Invalid request, see data");
    assertThat(errorResponse.data()).asList().containsExactly("price: must be greater than or equal to 0");
    assertThat(errorResponse.timestamp()).isCloseTo(ZonedDateTime.now(), within(1, ChronoUnit.MINUTES));
  }

  @Test
  void update_success() throws Exception {
    // arrange
    var saved = productRepo.save(airMaxShoes);
    var update = airMaxShoesDto.withId(saved.getId())
        .withSku("update-sku")
        .withName("Updated name")
        .withPrice(BigDecimal.valueOf(55));
    var json = objectMapper.writeValueAsString(update);

    // act
    var response = mockMvc.perform(put("/api/v1/products/1").contentType(APPLICATION_JSON).content(json))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var product = objectMapper.readValue(response, ProductDto.class);

    // assert
    assertThat(product.id()).isPositive();
    assertThat(product.sku()).isEqualTo("update-sku");
    assertThat(product.name()).isEqualTo("Updated name");
    assertThat(product.description()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(product.price()).isEqualTo(BigDecimal.valueOf(55));
    assertThat(product.warrantyDays()).isEqualTo(365);
    assertThat(product.listed()).isTrue();
  }
}