package com.subscript.plan.dto;
import com.subscript.plan.entity.SubscriptionPlan;
import java.time.LocalDate;
public record PlanResponse(Long id,String name,String billingInterval,int intervalCount,int minimumQuantity,
 LocalDate availableFrom,LocalDate availableUntil,boolean autoClose,boolean allowPause,
 boolean allowRenew,String description,boolean active){
 public static PlanResponse from(SubscriptionPlan p){return new PlanResponse(p.getId(),p.getName(),
 p.getBillingInterval().name(),p.getIntervalCount(),p.getMinimumQuantity(),p.getAvailableFrom(),
 p.getAvailableUntil(),p.isAutoClose(),p.isAllowPause(),p.isAllowRenew(),p.getDescription(),p.isActive());}
}
