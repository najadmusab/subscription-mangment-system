package com.subscript.billing.repository;
import com.subscript.billing.entity.Payment;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface PaymentRepository extends JpaRepository<Payment,Long>{
 List<Payment> findByCustomerIdOrderByPaidAtDesc(Long customerId);
 List<Payment> findByInvoice_IdOrderByPaidAtDesc(Long invoiceId);
}
