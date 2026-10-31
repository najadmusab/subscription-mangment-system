package com.subscript.product.dto;
import com.subscript.product.entity.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
public record ProductRequest(
 @NotBlank @Size(max=150) String name,
 @NotNull ProductType type,
 @NotNull @DecimalMin("0.00") @Digits(integer=12,fraction=2) BigDecimal salesPrice,
 @NotNull @DecimalMin("0.00") @Digits(integer=12,fraction=2) BigDecimal costPrice,
 @Size(max=1000) String notes,
 List<@Valid VariantRequest> variants
) {
 public record VariantRequest(@NotBlank @Size(max=120) String name,
   @NotNull @DecimalMin("0.00") @Digits(integer=12,fraction=2) BigDecimal additionalPrice,
   Boolean active) {}
}
