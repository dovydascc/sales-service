package com.company.sales.features.products.services;

import com.company.sales.features.products.entities.Product;
import com.company.sales.features.products.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepo productRepo;

  @Transactional
  public Product save(Product product) {
    return productRepo.save(product);
  }

  public Page<Product> getAll(Pageable pageable) {
    return productRepo.findAll(pageable);
  }
}
