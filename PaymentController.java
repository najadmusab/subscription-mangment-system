package com.subscript.billing.controller;
import com.subscript.billing.dto.*;import com.subscript.billing.service.PaymentService;
import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/payments")
public class PaymentController {
 private final PaymentService service;public PaymentController(PaymentService s){service=s;}
 @GetMapping public List<PaymentResponse> list(){return service.list().stream().map(PaymentResponse::from).toList();}
 @GetMapping("/{id}") public PaymentResponse get(@PathVariable Long id){return PaymentResponse.from(service.get(id));}
 @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") public PaymentResponse create(@Valid @RequestBody PaymentRequest r){return PaymentResponse.from(service.record(r.invoiceId(),r.method(),r.amount(),r.paidAt(),r.reference(),r.note()));}
 @GetMapping("/invoice/{invoiceId}") public List<PaymentResponse> byInvoice(@PathVariable Long invoiceId){return service.forInvoice(invoiceId).stream().map(PaymentResponse::from).toList();}
}
