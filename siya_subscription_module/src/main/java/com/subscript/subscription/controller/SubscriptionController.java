package com.subscript.subscription.controller;
import com.subscript.subscription.dto.*;import com.subscript.subscription.service.SubscriptionService;
import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/subscriptions")
public class SubscriptionController {
 private final SubscriptionService service;public SubscriptionController(SubscriptionService s){service=s;}
 @GetMapping public List<SubscriptionResponse> list(){return service.list().stream().map(SubscriptionResponse::from).toList();}
 @GetMapping("/{id}") public SubscriptionResponse get(@PathVariable Long id){return SubscriptionResponse.from(service.get(id));}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF','CUSTOMER')")
 public SubscriptionResponse create(@Valid @RequestBody SubscriptionRequest r){return SubscriptionResponse.from(service.create(r));}
 @PostMapping("/{id}/confirm") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
 public SubscriptionResponse confirm(@PathVariable Long id){return change(id,"confirm");}
 @PostMapping("/{id}/activate") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
 public SubscriptionResponse activate(@PathVariable Long id){return change(id,"activate");}
 @PostMapping("/{id}/pause") public SubscriptionResponse pause(@PathVariable Long id){return change(id,"pause");}
 @PostMapping("/{id}/resume") public SubscriptionResponse resume(@PathVariable Long id){return change(id,"resume");}
 @PostMapping("/{id}/close") public SubscriptionResponse close(@PathVariable Long id){return change(id,"close");}
 @PostMapping("/{id}/renew") public SubscriptionResponse renew(@PathVariable Long id){return change(id,"renew");}
 private SubscriptionResponse change(Long id,String action){return SubscriptionResponse.from(service.transition(id,action));}
}
