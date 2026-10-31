package com.subscript.billing.dto;
import com.subscript.billing.entity.DiscountType;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.time.LocalDateTime;
public record DiscountRequest(@NotBlank @Size(max=120) String name,@NotNull DiscountType type,
 @NotNull @DecimalMin("0.01") @Digits(integer=12,fraction=2) BigDecimal value,
 @NotNull @DecimalMin("0.00") BigDecimal minimumPurchase,@Min(1) int minimumQuantity,
 LocalDateTime startsAt,LocalDateTime endsAt,@Min(1) Integer usageLimit,boolean active){}
