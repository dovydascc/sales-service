package com.company.sales.features.products.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.company.sales.IntegrationTest;
import com.company.sales.features.products.entities.Product;
import com.company.sales.features.products.entities.TestProduct;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRepoIntegrationTest extends IntegrationTest {

  @Autowired
  private ProductRepo productRepo;

  private final Product airMaxShoes = TestProduct.airMaxShoes();

  @Test
  void findAll_noProducts() {
    var products = productRepo.findAll();

    assertThat(products).isEmpty();
  }

  @Test
  void save_newProduct() {
    var product = productRepo.save(airMaxShoes);

    assertThat(product.getId()).isPositive();
    assertThat(product.getSku()).isEqualTo("DJ3624-100");
    assertThat(product.getName()).isEqualTo("Nike Air Max Real Dawn");
    assertThat(product.getDescription()).isEqualTo(
        "Rooted to athletics DNA, the Air Max Dawn is made from at least 20% recycled material by weight.");
    assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(104.95));
    assertThat(product.getWarrantyDays()).isEqualTo(365);
    assertThat(product.isListed()).isTrue();
  }

  @Test
  void getById_success() {
    var product = productRepo.save(airMaxShoes);

    product = productRepo.getById(product.getId());

    assertThat(product.getId()).isPositive();
    assertThat(product.getName()).isEqualTo("Nike Air Max Real Dawn");
  }
}