package com.subscript.billing.service;
import com.subscript.subscription.entity.*;import com.subscript.subscription.repository.SubscriptionRepository;
import com.subscript.billing.entity.Invoice;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;import java.util.*;
@Service public class RecurringBillingService {
 private final SubscriptionRepository subscriptions;private final InvoiceService invoices;
 public RecurringBillingService(SubscriptionRepository s,InvoiceService i){subscriptions=s;invoices=i;}
 @Transactional public List<Invoice> runDue(){List<Invoice> created=new ArrayList<>();LocalDate today=LocalDate.now();
  for(Subscription s:subscriptions.findAll()){
   if(s.getStatus()==SubscriptionStatus.ACTIVE&&s.getNextBillingDate()!=null&&!s.getNextBillingDate().isAfter(today)){
    created.add(invoices.createForPeriod(s,s.getNextBillingDate()));
   }
  }return created;
 }
 @Transactional(readOnly=true) public long dueCount(){return subscriptions.findAll().stream().filter(s->s.getStatus()==SubscriptionStatus.ACTIVE&&s.getNextBillingDate()!=null&&!s.getNextBillingDate().isAfter(LocalDate.now())).count();}
}
