package com.subscript.subscription.dto;
import com.subscript.subscription.entity.*;import java.math.BigDecimal;import java.time.LocalDate;import java.util.List;
public record SubscriptionResponse(Long id,String number,Long customerId,String customerName,Long planId,
 String planName,LocalDate startDate,LocalDate expiryDate,LocalDate nextBillingDate,int paymentTermsDays,
 String status,BigDecimal subtotal,String notes,List<LineResponse> lines){
 public record LineResponse(Long id,Long productId,String productName,Long variantId,String variantName,
 int quantity,BigDecimal unitPrice,BigDecimal lineTotal){}
 public static SubscriptionResponse from(Subscription s){return new SubscriptionResponse(s.getId(),s.getSubscriptionNumber(),
 s.getCustomer().getId(),s.getCustomer().getFullName(),s.getPlan().getId(),s.getPlan().getName(),s.getStartDate(),
 s.getExpiryDate(),s.getNextBillingDate(),s.getPaymentTermsDays(),s.getStatus().name(),s.getSubtotal(),s.getNotes(),
 s.getLines().stream().map(l->new LineResponse(l.getId(),l.getProduct().getId(),l.getProduct().getName(),
 l.getVariant()==null?null:l.getVariant().getId(),l.getVariant()==null?null:l.getVariant().getName(),
 l.getQuantity(),l.getUnitPrice(),l.getLineTotal())).toList());}
}
