package com.subscript.billing.dto;
import com.subscript.billing.entity.*;import java.math.BigDecimal;import java.time.LocalDate;import java.util.List;
public record InvoiceResponse(Long id,String invoiceNumber,Long subscriptionId,Long customerId,String customerName,
 String customerEmail,LocalDate invoiceDate,LocalDate dueDate,LocalDate billingPeriodStart,LocalDate billingPeriodEnd,
 String status,BigDecimal subtotal,BigDecimal discountAmount,String discountName,BigDecimal taxRatePercent,
 BigDecimal taxAmount,BigDecimal total,BigDecimal amountPaid,BigDecimal balanceDue,boolean overdue,String notes,
 List<LineResponse> lines){
 public record LineResponse(String description,int quantity,BigDecimal unitPrice,BigDecimal lineTotal){}
 public static InvoiceResponse from(Invoice i){return new InvoiceResponse(i.getId(),i.getInvoiceNumber(),
 i.getSubscription().getId(),i.getCustomerId(),i.getCustomerName(),i.getCustomerEmail(),i.getInvoiceDate(),
 i.getDueDate(),i.getBillingPeriodStart(),i.getBillingPeriodEnd(),i.getStatus().name(),i.getSubtotal(),
 i.getDiscountAmount(),i.getDiscountName(),i.getTaxRatePercent(),i.getTaxAmount(),i.getTotal(),i.getAmountPaid(),
 i.getBalanceDue(),i.getStatus()!=InvoiceStatus.PAID&&i.getDueDate().isBefore(LocalDate.now())&&i.getBalanceDue().signum()>0,
 i.getNotes(),i.getLines().stream().map(l->new LineResponse(l.getDescription(),l.getQuantity(),l.getUnitPrice(),l.getLineTotal())).toList());}
}
