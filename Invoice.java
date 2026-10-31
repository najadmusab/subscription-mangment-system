package com.subscript.billing.entity;
import com.subscript.subscription.entity.Subscription;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.*;import java.util.*;
@Entity @Table(name="invoices",uniqueConstraints=@UniqueConstraint(name="uk_invoice_subscription_period",columnNames={"subscription_id","billing_period_start"}),indexes={@Index(name="idx_invoice_customer",columnList="customer_id"),@Index(name="idx_invoice_status_due",columnList="status,due_date")})
public class Invoice {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=40) private String invoiceNumber;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="subscription_id") private Subscription subscription;
 @Column(name="customer_id",nullable=false) private Long customerId;
 @Column(nullable=false,length=180) private String customerName;
 @Column(length=180) private String customerEmail;
 @Column(nullable=false) private LocalDate invoiceDate;
 @Column(nullable=false) private LocalDate dueDate;
 @Column(nullable=false) private LocalDate billingPeriodStart;
 private LocalDate billingPeriodEnd;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private InvoiceStatus status=InvoiceStatus.DRAFT;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal subtotal=BigDecimal.ZERO;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal discountAmount=BigDecimal.ZERO;
 @Column(nullable=false,precision=7,scale=3) private BigDecimal taxRatePercent=BigDecimal.ZERO;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal taxAmount=BigDecimal.ZERO;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal total=BigDecimal.ZERO;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal amountPaid=BigDecimal.ZERO;
 @Column(length=140) private String discountName;
 @Column(length=1000) private String notes;
 @OneToMany(mappedBy="invoice",cascade=CascadeType.ALL,orphanRemoval=true) private List<InvoiceLine> lines=new ArrayList<>();
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected Invoice(){} @PrePersist void created(){createdAt=Instant.now();}
 public void addLine(InvoiceLine l){l.setInvoice(this);lines.add(l);}
 public BigDecimal getBalanceDue(){return total.subtract(amountPaid).max(BigDecimal.ZERO);}
 public Long getId(){return id;}public String getInvoiceNumber(){return invoiceNumber;}public void setInvoiceNumber(String v){invoiceNumber=v;}
 public Subscription getSubscription(){return subscription;}public void setSubscription(Subscription v){subscription=v;}
 public Long getCustomerId(){return customerId;}public void setCustomerId(Long v){customerId=v;}
 public String getCustomerName(){return customerName;}public void setCustomerName(String v){customerName=v;}
 public String getCustomerEmail(){return customerEmail;}public void setCustomerEmail(String v){customerEmail=v;}
 public LocalDate getInvoiceDate(){return invoiceDate;}public void setInvoiceDate(LocalDate v){invoiceDate=v;}
 public LocalDate getDueDate(){return dueDate;}public void setDueDate(LocalDate v){dueDate=v;}
 public LocalDate getBillingPeriodStart(){return billingPeriodStart;}public void setBillingPeriodStart(LocalDate v){billingPeriodStart=v;}
 public LocalDate getBillingPeriodEnd(){return billingPeriodEnd;}public void setBillingPeriodEnd(LocalDate v){billingPeriodEnd=v;}
 public InvoiceStatus getStatus(){return status;}public void setStatus(InvoiceStatus v){status=v;}
 public BigDecimal getSubtotal(){return subtotal;}public void setSubtotal(BigDecimal v){subtotal=v;}
 public BigDecimal getDiscountAmount(){return discountAmount;}public void setDiscountAmount(BigDecimal v){discountAmount=v;}
 public BigDecimal getTaxRatePercent(){return taxRatePercent;}public void setTaxRatePercent(BigDecimal v){taxRatePercent=v;}
 public BigDecimal getTaxAmount(){return taxAmount;}public void setTaxAmount(BigDecimal v){taxAmount=v;}
 public BigDecimal getTotal(){return total;}public void setTotal(BigDecimal v){total=v;}
 public BigDecimal getAmountPaid(){return amountPaid;}public void setAmountPaid(BigDecimal v){amountPaid=v;}
 public String getDiscountName(){return discountName;}public void setDiscountName(String v){discountName=v;}
 public String getNotes(){return notes;}public void setNotes(String v){notes=v;}
 public List<InvoiceLine> getLines(){return lines;}
}
