package com.subscript.plan.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
@Entity @Table(name="subscription_plans")
public class SubscriptionPlan {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=140) private String name;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private BillingInterval billingInterval;
 @Column(nullable=false) private int intervalCount=1;
 @Column(nullable=false) private int minimumQuantity=1;
 private LocalDate availableFrom; private LocalDate availableUntil;
 @Column(nullable=false) private boolean autoClose=false;
 @Column(nullable=false) private boolean allowPause=true;
 @Column(nullable=false) private boolean allowRenew=true;
 @Column(length=1000) private String description;
 @Column(nullable=false) private boolean active=true;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected SubscriptionPlan(){}
 @PrePersist void created(){createdAt=Instant.now();}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
 public BillingInterval getBillingInterval(){return billingInterval;} public void setBillingInterval(BillingInterval v){billingInterval=v;}
 public int getIntervalCount(){return intervalCount;} public void setIntervalCount(int v){intervalCount=v;}
 public int getMinimumQuantity(){return minimumQuantity;} public void setMinimumQuantity(int v){minimumQuantity=v;}
 public LocalDate getAvailableFrom(){return availableFrom;} public void setAvailableFrom(LocalDate v){availableFrom=v;}
 public LocalDate getAvailableUntil(){return availableUntil;} public void setAvailableUntil(LocalDate v){availableUntil=v;}
 public boolean isAutoClose(){return autoClose;} public void setAutoClose(boolean v){autoClose=v;}
 public boolean isAllowPause(){return allowPause;} public void setAllowPause(boolean v){allowPause=v;}
 public boolean isAllowRenew(){return allowRenew;} public void setAllowRenew(boolean v){allowRenew=v;}
 public String getDescription(){return description;} public void setDescription(String v){description=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
