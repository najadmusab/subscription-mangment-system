package com.subscript.billing.service;
import com.subscript.auth.entity.*;import com.subscript.auth.service.CurrentUserService;
import com.subscript.billing.entity.*;import org.springframework.security.access.AccessDeniedException;import org.springframework.stereotype.Service;
@Service public class BillingAccessService {
 private final CurrentUserService current;public BillingAccessService(CurrentUserService c){current=c;}
 public User actor(){return current.requireUser();}
 public boolean staff(){Role r=actor().getRole();return r==Role.ADMIN||r==Role.STAFF;}
 public void invoice(Invoice i){User u=actor();if(u.getRole()==Role.CUSTOMER&&!i.getCustomerId().equals(u.getId()))throw new AccessDeniedException("You cannot access this invoice.");}
 public void payment(Payment p){User u=actor();if(u.getRole()==Role.CUSTOMER&&!p.getCustomerId().equals(u.getId()))throw new AccessDeniedException("You cannot access this payment.");}
}
