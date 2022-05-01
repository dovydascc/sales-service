package com.company.sales.features.orders.dtos;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.With;

@With
public record PurchasedProductDto(
    Long id,
    long productId,
    @Min(1) int quantity,
    @NotNull @Min(0) @Max(999_999_999) BigDecimal price
) {

}
