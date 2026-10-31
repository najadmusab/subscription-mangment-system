package com.subscript.billing.repository;
import com.subscript.billing.entity.Invoice;import org.springframework.data.jpa.repository.JpaRepository;import java.time.LocalDate;import java.util.*;
public interface InvoiceRepository extends JpaRepository<Invoice,Long>{
 Optional<Invoice> findBySubscription_IdAndBillingPeriodStart(Long subscriptionId,LocalDate periodStart);
 List<Invoice> findByCustomerIdOrderByInvoiceDateDesc(Long customerId);
 List<Invoice> findByStatusAndDueDateBeforeAndAmountPaidLessThan(com.subscript.billing.entity.InvoiceStatus status,LocalDate date,java.math.BigDecimal total);
}
