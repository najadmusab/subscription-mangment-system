package com.subscript.billing.repository;
import com.subscript.billing.entity.TaxRate;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface TaxRateRepository extends JpaRepository<TaxRate,Long>{List<TaxRate> findByActiveTrue();}
