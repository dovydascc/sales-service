package com.company.sales.features.products.controllers;

import com.company.sales.features.products.converters.ProductConverter;
import com.company.sales.features.products.dtos.ProductDto;
import com.company.sales.features.products.services.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for products entity.
 */
@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductConverter productConverter;

  /**
   * API to create a new product. This method expects ProductDto as a request body. Request body will be validated by
   * Spring & JSR303 validator
   *
   * @param productDto The payload
   * @return Created product
   */
  @PostMapping
  ProductDto save(@RequestBody @Valid ProductDto productDto) {
    var product = productConverter.toEntity(productDto);
    product = productService.save(product);
    return productConverter.toDto(product);
  }

  /**
   * API to get a Page of products. The response is paginated ProductDto list.
   *
   * @param pageable Spring's default pagination support. Example: ?page=0&size=20&sort=price,DESC&sort=sku,ASC
   * @return A Page of ProductDtos. Max page size can be 2000 items (defined in configuration)
   */
  @GetMapping
  Page<ProductDto> get(Pageable pageable) {
    var products = productService.getAll(pageable);
    return productConverter.toDto(products, pageable);
  }

  /**
   * API to update a product. This method expects ProductDto as a request body. Request body will be validated by Spring
   * & JSR303 validator
   *
   * @param id         Product id to update
   * @param productDto The payload
   * @return Updated product
   */
  @PutMapping("/{id}")
  ProductDto update(@PathVariable long id, @RequestBody @Valid ProductDto productDto) {
    var product = productConverter.toEntity(productDto);
    product.setId(id);
    product = productService.save(product);
    return productConverter.toDto(product);
  }
}
