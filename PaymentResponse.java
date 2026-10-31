package com.subscript.billing.dto;
import com.subscript.billing.entity.Payment;import java.math.BigDecimal;import java.time.LocalDateTime;
public record PaymentResponse(Long id,Long invoiceId,String invoiceNumber,Long customerId,String method,
 BigDecimal amount,LocalDateTime paidAt,String reference,String note){
 public static PaymentResponse from(Payment p){return new PaymentResponse(p.getId(),p.getInvoice().getId(),
 p.getInvoice().getInvoiceNumber(),p.getCustomerId(),p.getMethod().name(),p.getAmount(),p.getPaidAt(),p.getReference(),p.getNote());}
}
