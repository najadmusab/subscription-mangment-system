package com.subscript.billing.dto;
import jakarta.validation.constraints.*;import java.math.BigDecimal;
public record TaxRequest(@NotBlank @Size(max=100) String name,@NotNull @DecimalMin("0.000") @DecimalMax("100.000") @Digits(integer=3,fraction=3) BigDecimal ratePercent,boolean active){}
