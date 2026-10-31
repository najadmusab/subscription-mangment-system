package com.subscript.billing.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.*;
@Entity @Table(name="payments",indexes=@Index(name="idx_payment_paid_at",columnList="paid_at"))
public class Payment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="invoice_id") private Invoice invoice;
 @Column(nullable=false) private Long customerId;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=25) private PaymentMethod method;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal amount;
 @Column(nullable=false) private LocalDateTime paidAt;
 @Column(length=140) private String reference;
 @Column(length=500) private String note;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected Payment(){} @PrePersist void created(){createdAt=Instant.now();}
 public Long getId(){return id;}public Invoice getInvoice(){return invoice;}public void setInvoice(Invoice v){invoice=v;}
 public Long getCustomerId(){return customerId;}public void setCustomerId(Long v){customerId=v;}
 public PaymentMethod getMethod(){return method;}public void setMethod(PaymentMethod v){method=v;}
 public BigDecimal getAmount(){return amount;}public void setAmount(BigDecimal v){amount=v;}
 public LocalDateTime getPaidAt(){return paidAt;}public void setPaidAt(LocalDateTime v){paidAt=v;}
 public String getReference(){return reference;}public void setReference(String v){reference=v;}
 public String getNote(){return note;}public void setNote(String v){note=v;}
}
