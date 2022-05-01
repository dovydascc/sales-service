package com.company.sales.features.orders.dtos;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.With;

@With
public record OrderDto(
    Long id,
    @NotEmpty @Size(max = 64) String email,
    @NotEmpty @Size(max = 256) String address,
    ZonedDateTime orderedOn,
    BigDecimal totalValue,
    @NotEmpty @Valid List<PurchasedProductDto> purchasedProducts
) {

}
