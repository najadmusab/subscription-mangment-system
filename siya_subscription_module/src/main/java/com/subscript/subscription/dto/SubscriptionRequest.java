package com.subscript.subscription.dto;
import jakarta.validation.Valid;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.time.LocalDate;import java.util.List;
public record SubscriptionRequest(Long customerId,@NotNull Long planId,@NotNull LocalDate startDate,
 @Min(0) @Max(365) int paymentTermsDays,@Size(max=1000) String notes,
 @NotEmpty List<@Valid LineRequest> lines){
 public record LineRequest(@NotNull Long productId,Long variantId,@Min(1) @Max(100000) int quantity,
 @NotNull @DecimalMin("0.00") @Digits(integer=12,fraction=2) BigDecimal unitPrice){}
}
