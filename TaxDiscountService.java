package com.subscript.billing.service;
import com.subscript.billing.entity.*;import com.subscript.billing.repository.*;
import org.springframework.stereotype.Service;import java.math.*;import java.time.*;import java.util.*;
@Service public class TaxDiscountService {
 private final TaxRateRepository taxes;private final DiscountRepository discounts;
 public TaxDiscountService(TaxRateRepository t,DiscountRepository d){taxes=t;discounts=d;}
 public TaxRate currentTax(){return taxes.findByActiveTrue().stream().findFirst().orElse(null);}
 public Discount bestDiscount(BigDecimal subtotal,int quantity){return discounts.findByActiveTrue().stream()
  .filter(d->d.eligible(LocalDateTime.now(),subtotal,quantity))
  .max(Comparator.comparing(d->discountValue(d,subtotal))).orElse(null);}
 public BigDecimal discountValue(Discount d,BigDecimal subtotal){BigDecimal v=d.getType()==DiscountType.PERCENTAGE?
  subtotal.multiply(d.getValue()).divide(new BigDecimal("100"),2,RoundingMode.HALF_UP):d.getValue();
  return v.min(subtotal).max(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);}
}
