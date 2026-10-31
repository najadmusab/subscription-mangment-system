package com.subscript.plan.dto;
import com.subscript.plan.entity.BillingInterval;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record PlanRequest(@NotBlank @Size(max=140) String name,@NotNull BillingInterval billingInterval,
 @Min(1) @Max(365) int intervalCount,@Min(1) int minimumQuantity,
 LocalDate availableFrom,LocalDate availableUntil,boolean autoClose,boolean allowPause,
 boolean allowRenew,@Size(max=1000) String description){}
