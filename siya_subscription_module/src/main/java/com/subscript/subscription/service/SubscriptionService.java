package com.subscript.subscription.service;
import com.subscript.auth.entity.*;import com.subscript.auth.repository.UserRepository;import com.subscript.auth.service.CurrentUserService;
import com.subscript.plan.entity.SubscriptionPlan;import com.subscript.plan.repository.PlanRepository;
import com.subscript.product.entity.*;import com.subscript.product.repository.ProductRepository;
import com.subscript.subscription.dto.SubscriptionRequest;import com.subscript.subscription.entity.*;import com.subscript.subscription.repository.SubscriptionRepository;
import org.springframework.security.access.AccessDeniedException;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;import java.util.*;
@Service public class SubscriptionService {
 private final SubscriptionRepository subs;private final UserRepository users;private final PlanRepository plans;private final ProductRepository products;private final CurrentUserService current;
 public SubscriptionService(SubscriptionRepository s,UserRepository u,PlanRepository p,ProductRepository pr,CurrentUserService c){subs=s;users=u;plans=p;products=pr;current=c;}
 @Transactional(readOnly=true) public List<Subscription> list(){User u=current.requireUser();return u.getRole()==Role.CUSTOMER?subs.findByCustomer_IdOrderByCreatedAtDesc(u.getId()):subs.findAll();}
 @Transactional(readOnly=true) public Subscription get(Long id){Subscription s=subs.findById(id).orElseThrow(()->new IllegalArgumentException("Subscription not found."));ensureOwnerOrStaff(s);return s;}
 @Transactional public Subscription create(SubscriptionRequest r){
  User actor=current.requireUser();User customer;
  if(actor.getRole()==Role.CUSTOMER){if(r.customerId()!=null&&!r.customerId().equals(actor.getId()))throw new AccessDeniedException("You can only create your own subscription.");customer=actor;}
  else customer=users.findById(Objects.requireNonNull(r.customerId(),"customerId is required for staff/admin")).filter(u->u.getRole()==Role.CUSTOMER).orElseThrow(()->new IllegalArgumentException("Customer not found."));
  SubscriptionPlan plan=plans.findById(r.planId()).filter(SubscriptionPlan::isActive).orElseThrow(()->new IllegalArgumentException("Active plan not found."));
  LocalDate start=r.startDate();if(plan.getAvailableFrom()!=null&&start.isBefore(plan.getAvailableFrom()))throw new IllegalArgumentException("Plan is not available on the selected start date.");
  if(plan.getAvailableUntil()!=null&&start.isAfter(plan.getAvailableUntil()))throw new IllegalArgumentException("Plan is not available on the selected start date.");
  Subscription s=new Subscription();s.setSubscriptionNumber("SUB-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());
  s.setCustomer(customer);s.setPlan(plan);s.setStartDate(start);s.setNextBillingDate(start);s.setPaymentTermsDays(r.paymentTermsDays());s.setNotes(r.notes());
  for(var line:r.lines()){
   if(line.quantity()<plan.getMinimumQuantity())throw new IllegalArgumentException("Quantity is below the plan minimum.");
   Product product=products.findById(line.productId()).filter(Product::isActive).orElseThrow(()->new IllegalArgumentException("Active product not found."));
   ProductVariant variant=null;
   if(line.variantId()!=null){variant=product.getVariants().stream().filter(v->v.getId().equals(line.variantId())&&v.isActive()).findFirst().orElseThrow(()->new IllegalArgumentException("Active variant does not belong to this product."));}
   s.addLine(new SubscriptionLine(product,variant,line.quantity(),line.unitPrice()));
  }
  s.setStatus(SubscriptionStatus.DRAFT);return subs.save(s);
 }
 @Transactional public Subscription transition(Long id,String action){
  Subscription s=get(id);SubscriptionStatus st=s.getStatus();SubscriptionPlan p=s.getPlan();
  switch(action){
   case "confirm"->{if(st!=SubscriptionStatus.QUOTATION&&st!=SubscriptionStatus.DRAFT)bad();s.setStatus(SubscriptionStatus.CONFIRMED);}
   case "activate"->{if(st!=SubscriptionStatus.CONFIRMED)bad();s.setStatus(SubscriptionStatus.ACTIVE);}
   case "pause"->{if(!p.isAllowPause()||st!=SubscriptionStatus.ACTIVE)throw new IllegalArgumentException("This subscription cannot be paused.");s.setStatus(SubscriptionStatus.PAUSED);}
   case "resume"->{if(st!=SubscriptionStatus.PAUSED)bad();s.setStatus(SubscriptionStatus.ACTIVE);}
   case "close"->{if(st==SubscriptionStatus.CLOSED)bad();s.setStatus(SubscriptionStatus.CLOSED);s.setExpiryDate(LocalDate.now());}
   case "renew"->{if(!p.isAllowRenew()||(st!=SubscriptionStatus.ACTIVE&&st!=SubscriptionStatus.CLOSED))throw new IllegalArgumentException("This subscription cannot be renewed.");LocalDate next=s.getExpiryDate()!=null?s.getExpiryDate().plusDays(1):advance(s.getNextBillingDate(),p);s.setStartDate(next);s.setNextBillingDate(next);s.setExpiryDate(null);s.setStatus(SubscriptionStatus.ACTIVE);}
   default->throw new IllegalArgumentException("Unknown subscription action.");
  }
  return subs.save(s);
 }
 private void ensureOwnerOrStaff(Subscription s){User actor=current.requireUser();if(actor.getRole()==Role.CUSTOMER&&!s.getCustomer().getId().equals(actor.getId()))throw new AccessDeniedException("You cannot access another customer's subscription.");}
 private static void bad(){throw new IllegalArgumentException("Invalid subscription status transition.");}
 private static LocalDate advance(LocalDate d,SubscriptionPlan p){return switch(p.getBillingInterval()){case DAILY->d.plusDays(p.getIntervalCount());case WEEKLY->d.plusWeeks(p.getIntervalCount());case MONTHLY->d.plusMonths(p.getIntervalCount());case YEARLY->d.plusYears(p.getIntervalCount());};}
}
