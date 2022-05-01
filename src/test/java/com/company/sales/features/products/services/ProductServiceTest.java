package com.company.sales.features.products.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Order.asc;

import com.company.sales.features.products.entities.TestProduct;
import com.company.sales.features.products.repositories.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepo productRepo;

  @Test
  void save() {
    var product = TestProduct.airMaxShoes();

    productService.save(product);

    verify(productRepo, times(1)).save(product);
  }

  @Test
  void getAll() {
    var pageable = PageRequest.of(0, 20, Sort.by(asc("orderedOn")));

    productService.getAll(pageable);

    verify(productRepo, times(1)).findAll(pageable);
  }
}