package com.subscript.billing.controller;
import com.subscript.billing.dto.TaxRequest;import com.subscript.billing.entity.TaxRate;import com.subscript.billing.repository.TaxRateRepository;
import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/taxes") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class TaxController {
 private final TaxRateRepository repo;public TaxController(TaxRateRepository r){repo=r;}
 @GetMapping public List<TaxRate> list(){return repo.findAll();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public TaxRate create(@Valid @RequestBody TaxRequest r){TaxRate t=new TaxRate();apply(t,r);return repo.save(t);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public TaxRate update(@PathVariable Long id,@Valid @RequestBody TaxRequest r){TaxRate t=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Tax rate not found."));apply(t,r);return repo.save(t);}
 @PatchMapping("/{id}/archive") @PreAuthorize("hasRole('ADMIN')") public TaxRate archive(@PathVariable Long id){TaxRate t=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Tax rate not found."));t.setActive(false);return repo.save(t);}
 private void apply(TaxRate t,TaxRequest r){t.setName(r.name().trim());t.setRatePercent(r.ratePercent());t.setActive(r.active());}
}
