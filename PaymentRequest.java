package com.subscript.billing.dto;
import com.subscript.billing.entity.PaymentMethod;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.time.LocalDateTime;
public record PaymentRequest(@NotNull Long invoiceId,@NotNull PaymentMethod method,
 @NotNull @DecimalMin("0.01") @Digits(integer=12,fraction=2) BigDecimal amount,
 LocalDateTime paidAt,@Size(max=140) String reference,@Size(max=500) String note){}
