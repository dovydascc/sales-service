package com.company.sales.features.products.dtos;

import java.math.BigDecimal;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.With;

@With
public record ProductDto(
    Long id,
    @NotEmpty @Size(max = 16) String sku,
    @NotEmpty @Size(max = 32) String name,
    @NotEmpty @Size(max = 256) String description,
    @NotNull @Min(0) @Max(999_999_999) BigDecimal price,
    @Min(0) @Max(365 * 100) int warrantyDays, // Warranty stored in days. Max is 100 years
    boolean listed
) {

}
