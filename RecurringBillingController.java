package com.subscript.billing.controller;
import com.subscript.billing.dto.InvoiceResponse;import com.subscript.billing.service.RecurringBillingService;
import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;import java.util.Map;
@RestController @RequestMapping("/api/billing") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class RecurringBillingController {
 private final RecurringBillingService service;public RecurringBillingController(RecurringBillingService s){service=s;}
 @PostMapping("/run-due") @PreAuthorize("hasRole('ADMIN')") public List<InvoiceResponse> run(){return service.runDue().stream().map(InvoiceResponse::from).toList();}
 @GetMapping("/due-count") public Map<String,Long> count(){return Map.of("dueSubscriptions",service.dueCount());}
}
