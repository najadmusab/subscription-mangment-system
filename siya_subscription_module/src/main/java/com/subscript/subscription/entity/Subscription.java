package com.subscript.subscription.entity;
import com.subscript.auth.entity.User;import com.subscript.plan.entity.SubscriptionPlan;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.*;import java.util.*;
@Entity @Table(name="subscriptions",indexes={@Index(name="idx_sub_customer",columnList="customer_id"),@Index(name="idx_sub_status_due",columnList="status,next_billing_date")})
public class Subscription {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=40) private String subscriptionNumber;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="customer_id") private User customer;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="plan_id") private SubscriptionPlan plan;
 @Column(nullable=false) private LocalDate startDate;
 private LocalDate expiryDate;private LocalDate nextBillingDate;
 @Column(nullable=false) private int paymentTermsDays=0;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private SubscriptionStatus status=SubscriptionStatus.DRAFT;
 @Column(precision=14,scale=2,nullable=false) private BigDecimal subtotal=BigDecimal.ZERO;
 @Column(length=1000) private String notes;
 @OneToMany(mappedBy="subscription",cascade=CascadeType.ALL,orphanRemoval=true) private List<SubscriptionLine> lines=new ArrayList<>();
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected Subscription(){}
 @PrePersist void created(){createdAt=Instant.now();}
 public void addLine(SubscriptionLine l){l.setSubscription(this);lines.add(l);subtotal=subtotal.add(l.getLineTotal());}
 public void clearLines(){lines.clear();subtotal=BigDecimal.ZERO;}
 public Long getId(){return id;}public String getSubscriptionNumber(){return subscriptionNumber;}public void setSubscriptionNumber(String v){subscriptionNumber=v;}
 public User getCustomer(){return customer;}public void setCustomer(User v){customer=v;}public SubscriptionPlan getPlan(){return plan;}public void setPlan(SubscriptionPlan v){plan=v;}
 public LocalDate getStartDate(){return startDate;}public void setStartDate(LocalDate v){startDate=v;}public LocalDate getExpiryDate(){return expiryDate;}public void setExpiryDate(LocalDate v){expiryDate=v;}
 public LocalDate getNextBillingDate(){return nextBillingDate;}public void setNextBillingDate(LocalDate v){nextBillingDate=v;}
 public int getPaymentTermsDays(){return paymentTermsDays;}public void setPaymentTermsDays(int v){paymentTermsDays=v;}
 public SubscriptionStatus getStatus(){return status;}public void setStatus(SubscriptionStatus v){status=v;}
 public BigDecimal getSubtotal(){return subtotal;}public String getNotes(){return notes;}public void setNotes(String v){notes=v;}
 public List<SubscriptionLine> getLines(){return lines;}
}
