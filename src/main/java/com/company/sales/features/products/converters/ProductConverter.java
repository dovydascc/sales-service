package com.company.sales.features.products.converters;

import com.company.sales.features.products.dtos.ProductDto;
import com.company.sales.features.products.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductConverter {

  public ProductDto toDto(Product product) {
    return new ProductDto(
        product.getId(),
        product.getSku(),
        product.getName(),
        product.getDescription(),
        product.getPrice(),
        product.getWarrantyDays(),
        product.isListed()
    );
  }

  public Product toEntity(ProductDto product) {
    return new Product(
        product.id(),
        product.sku(),
        product.name(),
        product.description(),
        product.price(),
        product.warrantyDays(),
        product.listed()
    );
  }

  public Page<ProductDto> toDto(Page<Product> products, Pageable pageable) {
    var productDtos = products.getContent().stream().map(this::toDto).toList();
    return new PageImpl<>(productDtos, pageable, products.getTotalElements());
  }
}
