package com.subscript.billing.controller;
import com.subscript.billing.dto.DiscountRequest;import com.subscript.billing.entity.Discount;import com.subscript.billing.repository.DiscountRepository;
import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/discounts") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class DiscountController {
 private final DiscountRepository repo;public DiscountController(DiscountRepository r){repo=r;}
 @GetMapping public List<Discount> list(){return repo.findAll();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public Discount create(@Valid @RequestBody DiscountRequest r){Discount d=new Discount();apply(d,r);return repo.save(d);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Discount update(@PathVariable Long id,@Valid @RequestBody DiscountRequest r){Discount d=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Discount not found."));apply(d,r);return repo.save(d);}
 @PatchMapping("/{id}/archive") @PreAuthorize("hasRole('ADMIN')") public Discount archive(@PathVariable Long id){Discount d=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Discount not found."));d.setActive(false);return repo.save(d);}
 private void apply(Discount d,DiscountRequest r){if(r.endsAt()!=null&&r.startsAt()!=null&&r.endsAt().isBefore(r.startsAt()))throw new IllegalArgumentException("Discount end must be after start.");
  d.setName(r.name().trim());d.setType(r.type());d.setValue(r.value());d.setMinimumPurchase(r.minimumPurchase());d.setMinimumQuantity(r.minimumQuantity());
  d.setStartsAt(r.startsAt());d.setEndsAt(r.endsAt());d.setUsageLimit(r.usageLimit());d.setActive(r.active());}
}
