package com.subscript.billing.service;
import com.subscript.billing.entity.*;import com.subscript.billing.repository.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.math.*;import java.time.*;import java.util.*;
@Service public class PaymentService {
 private final PaymentRepository payments;private final InvoiceRepository invoices;private final BillingAccessService access;
 public PaymentService(PaymentRepository p,InvoiceRepository i,BillingAccessService a){payments=p;invoices=i;access=a;}
 @Transactional(readOnly=true) public List<Payment> list(){var u=access.actor();return access.staff()?payments.findAll():payments.findByCustomerIdOrderByPaidAtDesc(u.getId());}
 @Transactional(readOnly=true) public Payment get(Long id){Payment p=payments.findById(id).orElseThrow(()->new IllegalArgumentException("Payment not found."));access.payment(p);return p;}
 @Transactional(readOnly=true) public List<Payment> forInvoice(Long invoiceId){Invoice i=invoices.findById(invoiceId).orElseThrow(()->new IllegalArgumentException("Invoice not found."));access.invoice(i);return payments.findByInvoice_IdOrderByPaidAtDesc(invoiceId);}
 @Transactional public Payment record(Long invoiceId,PaymentMethod method,BigDecimal amount,LocalDateTime paidAt,String reference,String note){
  Invoice i=invoices.findById(invoiceId).orElseThrow(()->new IllegalArgumentException("Invoice not found."));
  if(i.getStatus()!=InvoiceStatus.CONFIRMED)throw new IllegalArgumentException("Payments can only be recorded for confirmed invoices.");
  if(amount==null||amount.compareTo(BigDecimal.ZERO)<=0)throw new IllegalArgumentException("Payment amount must be greater than zero.");
  BigDecimal remaining=i.getBalanceDue();if(amount.compareTo(remaining)>0)throw new IllegalArgumentException("Payment exceeds the invoice balance due.");
  Payment p=new Payment();p.setInvoice(i);p.setCustomerId(i.getCustomerId());p.setMethod(method);p.setAmount(amount.setScale(2,RoundingMode.HALF_UP));
  p.setPaidAt(paidAt==null?LocalDateTime.now():paidAt);p.setReference(reference);p.setNote(note);
  i.setAmountPaid(i.getAmountPaid().add(p.getAmount()).setScale(2,RoundingMode.HALF_UP));
  if(i.getBalanceDue().compareTo(BigDecimal.ZERO)==0)i.setStatus(InvoiceStatus.PAID);
  invoices.save(i);return payments.save(p);
 }
}
