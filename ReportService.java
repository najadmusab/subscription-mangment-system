package com.subscript.billing.service;
import com.subscript.auth.entity.User;import com.subscript.billing.entity.*;import com.subscript.billing.repository.*;
import com.subscript.subscription.entity.SubscriptionStatus;import com.subscript.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.math.*;import java.time.*;import java.util.*;
@Service public class ReportService {
 private final InvoiceRepository invoices;private final PaymentRepository payments;private final SubscriptionRepository subscriptions;private final BillingAccessService access;
 public ReportService(InvoiceRepository i,PaymentRepository p,SubscriptionRepository s,BillingAccessService a){invoices=i;payments=p;subscriptions=s;access=a;}
 @Transactional(readOnly=true) public Map<String,Object> dashboard(){
  if(!access.staff())throw new org.springframework.security.access.AccessDeniedException("Staff access required.");
  List<Invoice> all=invoices.findAll();BigDecimal paid=all.stream().map(Invoice::getAmountPaid).reduce(BigDecimal.ZERO,BigDecimal::add);
  BigDecimal outstanding=all.stream().map(Invoice::getBalanceDue).reduce(BigDecimal.ZERO,BigDecimal::add);
  long overdue=all.stream().filter(i->i.getStatus()!=InvoiceStatus.PAID&&i.getDueDate().isBefore(LocalDate.now())&&i.getBalanceDue().signum()>0).count();
  long active=subscriptions.findAll().stream().filter(s->s.getStatus()==SubscriptionStatus.ACTIVE).count();
  Map<String,Object> m=new LinkedHashMap<>();m.put("activeSubscriptions",active);m.put("invoiceCount",all.size());m.put("overdueInvoices",overdue);m.put("paidRevenue",paid.setScale(2,RoundingMode.HALF_UP));m.put("outstandingBalance",outstanding.setScale(2,RoundingMode.HALF_UP));return m;
 }
 @Transactional(readOnly=true) public List<Map<String,Object>> revenue(LocalDate from,LocalDate to){
  if(!access.staff())throw new org.springframework.security.access.AccessDeniedException("Staff access required.");
  if(from==null||to==null||to.isBefore(from))throw new IllegalArgumentException("Provide a valid from/to date range.");
  Map<LocalDate,BigDecimal> daily=new TreeMap<>();
  for(Payment p:payments.findAll()){LocalDate day=p.getPaidAt().toLocalDate();if(!day.isBefore(from)&&!day.isAfter(to))daily.merge(day,p.getAmount(),BigDecimal::add);}
  return daily.entrySet().stream().map(e->{Map<String,Object> row=new LinkedHashMap<>();row.put("date",e.getKey());row.put("revenue",e.getValue().setScale(2,RoundingMode.HALF_UP));return row;}).toList();
 }
}
