package com.subscript.billing.service;
import com.subscript.auth.entity.User;import com.subscript.billing.entity.*;import com.subscript.billing.repository.*;
import com.subscript.subscription.entity.*;import com.subscript.subscription.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.math.*;import java.time.*;import java.util.*;
@Service public class InvoiceService {
 private final InvoiceRepository invoices;private final SubscriptionRepository subscriptions;private final TaxDiscountService calculations;private final BillingAccessService access;
 @Value("${app.billing.invoice-prefix:INV}") private String prefix;
 @Value("${app.billing.default-due-days:7}") private int defaultDueDays;
 public InvoiceService(InvoiceRepository i,SubscriptionRepository s,TaxDiscountService c,BillingAccessService a){invoices=i;subscriptions=s;calculations=c;access=a;}
 @Transactional(readOnly=true) public List<Invoice> list(){User u=access.actor();return access.staff()?invoices.findAll():invoices.findByCustomerIdOrderByInvoiceDateDesc(u.getId());}
 @Transactional(readOnly=true) public Invoice get(Long id){Invoice i=invoices.findById(id).orElseThrow(()->new IllegalArgumentException("Invoice not found."));access.invoice(i);return i;}
 @Transactional public Invoice createForSubscription(Long subscriptionId){Subscription s=subscriptions.findById(subscriptionId).orElseThrow(()->new IllegalArgumentException("Subscription not found."));
  LocalDate period=s.getNextBillingDate()==null?s.getStartDate():s.getNextBillingDate();
  return createForPeriod(s,period);
 }
 @Transactional public Invoice createForPeriod(Subscription s,LocalDate period){
  var existing=invoices.findBySubscription_IdAndBillingPeriodStart(s.getId(),period);if(existing.isPresent())return existing.get();
  if(s.getStatus()!=SubscriptionStatus.ACTIVE)throw new IllegalArgumentException("Only active subscriptions can be billed.");
  Invoice i=new Invoice();i.setInvoiceNumber(prefix+"-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());
  i.setSubscription(s);i.setCustomerId(s.getCustomer().getId());i.setCustomerName(s.getCustomer().getFullName());
  i.setCustomerEmail(s.getCustomer().getEmail());i.setInvoiceDate(LocalDate.now());i.setBillingPeriodStart(period);
  i.setBillingPeriodEnd(nextPeriod(period,s).minusDays(1));
  int terms=s.getPaymentTermsDays()>0?s.getPaymentTermsDays():defaultDueDays;i.setDueDate(i.getInvoiceDate().plusDays(terms));
  BigDecimal subtotal=BigDecimal.ZERO;int totalQty=0;
  for(var line:s.getLines()){String desc=line.getProduct().getName()+(line.getVariant()==null?"":" — "+line.getVariant().getName());
   i.addLine(new InvoiceLine(desc,line.getQuantity(),line.getUnitPrice()));subtotal=subtotal.add(line.getLineTotal());totalQty+=line.getQuantity();}
  subtotal=subtotal.setScale(2,RoundingMode.HALF_UP);i.setSubtotal(subtotal);
  Discount d=calculations.bestDiscount(subtotal,totalQty);BigDecimal discount=BigDecimal.ZERO;
  if(d!=null){discount=calculations.discountValue(d,subtotal);i.setDiscountName(d.getName());d.setUsedCount(d.getUsedCount()+1);}
  i.setDiscountAmount(discount);
  TaxRate tax=calculations.currentTax();BigDecimal rate=tax==null?BigDecimal.ZERO:tax.getRatePercent();
  BigDecimal taxable=subtotal.subtract(discount).max(BigDecimal.ZERO);
  BigDecimal taxAmount=taxable.multiply(rate).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP);
  i.setTaxRatePercent(rate);i.setTaxAmount(taxAmount);i.setTotal(taxable.add(taxAmount).setScale(2,RoundingMode.HALF_UP));
  i.setNotes(s.getNotes());Invoice saved=invoices.saveAndFlush(i);
  // Advance the subscription only after invoice has been persisted successfully.
  s.setNextBillingDate(nextPeriod(period,s));subscriptions.save(s);
  return saved;
 }
 @Transactional public Invoice confirm(Long id){Invoice i=invoices.findById(id).orElseThrow(()->new IllegalArgumentException("Invoice not found."));
  if(i.getStatus()!=InvoiceStatus.DRAFT)throw new IllegalArgumentException("Only draft invoices can be confirmed.");i.setStatus(InvoiceStatus.CONFIRMED);return invoices.save(i);}
 private LocalDate nextPeriod(LocalDate date,Subscription s){var p=s.getPlan();return switch(p.getBillingInterval()){
  case DAILY->date.plusDays(p.getIntervalCount());case WEEKLY->date.plusWeeks(p.getIntervalCount());
  case MONTHLY->date.plusMonths(p.getIntervalCount());case YEARLY->date.plusYears(p.getIntervalCount());};}
}
