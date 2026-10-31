package com.subscript.billing.controller;
import com.subscript.billing.dto.*;import com.subscript.billing.service.*;import com.subscript.billing.entity.Invoice;
import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/invoices")
public class InvoiceController {
 private final InvoiceService service;private final InvoicePdfService pdf;
 public InvoiceController(InvoiceService s,InvoicePdfService p){service=s;pdf=p;}
 @GetMapping public List<InvoiceResponse> list(){return service.list().stream().map(InvoiceResponse::from).toList();}
 @GetMapping("/{id}") public InvoiceResponse get(@PathVariable Long id){return InvoiceResponse.from(service.get(id));}
 @PostMapping("/from-subscription/{subscriptionId}") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
 public InvoiceResponse create(@PathVariable Long subscriptionId){return InvoiceResponse.from(service.createForSubscription(subscriptionId));}
 @PostMapping("/{id}/confirm") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
 public InvoiceResponse confirm(@PathVariable Long id){return InvoiceResponse.from(service.confirm(id));}
 @GetMapping(value="/{id}/pdf",produces=MediaType.APPLICATION_PDF_VALUE)
 public ResponseEntity<byte[]> pdf(@PathVariable Long id){Invoice i=service.get(id);return ResponseEntity.ok()
  .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+i.getInvoiceNumber()+".pdf\"")
  .contentType(MediaType.APPLICATION_PDF).body(pdf.generate(i));}
}
