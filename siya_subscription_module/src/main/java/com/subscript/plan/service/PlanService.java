package com.subscript.plan.service;
import com.subscript.plan.dto.PlanRequest;
import com.subscript.plan.entity.SubscriptionPlan;
import com.subscript.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service public class PlanService {
 private final PlanRepository repo; public PlanService(PlanRepository r){repo=r;}
 @Transactional(readOnly=true) public List<SubscriptionPlan> list(){return repo.findAll().stream().filter(SubscriptionPlan::isActive).toList();}
 @Transactional(readOnly=true) public SubscriptionPlan get(Long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Plan not found."));}
 @Transactional public SubscriptionPlan create(PlanRequest r){SubscriptionPlan p=new SubscriptionPlan();apply(p,r);return repo.save(p);}
 @Transactional public SubscriptionPlan update(Long id,PlanRequest r){SubscriptionPlan p=get(id);apply(p,r);return repo.save(p);}
 @Transactional public SubscriptionPlan archive(Long id){SubscriptionPlan p=get(id);p.setActive(false);return repo.save(p);}
 private void apply(SubscriptionPlan p,PlanRequest r){
  if(r.availableFrom()!=null&&r.availableUntil()!=null&&r.availableUntil().isBefore(r.availableFrom()))
   throw new IllegalArgumentException("Availability end date cannot be before start date.");
  p.setName(r.name().trim());p.setBillingInterval(r.billingInterval());p.setIntervalCount(r.intervalCount());
  p.setMinimumQuantity(r.minimumQuantity());p.setAvailableFrom(r.availableFrom());p.setAvailableUntil(r.availableUntil());
  p.setAutoClose(r.autoClose());p.setAllowPause(r.allowPause());p.setAllowRenew(r.allowRenew());p.setDescription(r.description());
 }
}
