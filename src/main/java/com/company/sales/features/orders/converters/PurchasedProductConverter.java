package com.company.sales.features.orders.converters;

import com.company.sales.errorhandling.NotFoundException;
import com.company.sales.features.orders.dtos.PurchasedProductDto;
import com.company.sales.features.orders.entities.PurchasedProduct;
import com.company.sales.features.products.repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchasedProductConverter {

  private final ProductRepo productRepo;

  public PurchasedProductDto toDto(PurchasedProduct purchasedProduct) {
    return new PurchasedProductDto(
        purchasedProduct.getId(),
        purchasedProduct.getProduct().getId(),
        purchasedProduct.getQuantity(),
        purchasedProduct.getPrice()
    );
  }

  public PurchasedProduct toEntity(PurchasedProductDto purchasedProductDto) {
    var product = productRepo.findById(purchasedProductDto.productId())
        .orElseThrow(() -> new NotFoundException("There is no product with an id: " + purchasedProductDto.productId()));

    return new PurchasedProduct(
        purchasedProductDto.id(),
        product,
        purchasedProductDto.quantity(),
        purchasedProductDto.price()
    );
  }
}
