package com.subscript.billing.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.*;
@Entity @Table(name="discounts")
public class Discount {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String name;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private DiscountType type;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal value;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal minimumPurchase=BigDecimal.ZERO;
 @Column(nullable=false) private int minimumQuantity=1;
 private LocalDateTime startsAt;private LocalDateTime endsAt;
 private Integer usageLimit;
 @Column(nullable=false) private int usedCount=0;
 @Column(nullable=false) private boolean active=true;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected Discount(){} @PrePersist void created(){createdAt=Instant.now();}
 public boolean eligible(LocalDateTime now,BigDecimal subtotal,int qty){return active&&(startsAt==null||!now.isBefore(startsAt))&&(endsAt==null||!now.isAfter(endsAt))&&subtotal.compareTo(minimumPurchase)>=0&&qty>=minimumQuantity&&(usageLimit==null||usedCount<usageLimit);}
 public Long getId(){return id;}public String getName(){return name;}public void setName(String v){name=v;}
 public DiscountType getType(){return type;}public void setType(DiscountType v){type=v;}
 public BigDecimal getValue(){return value;}public void setValue(BigDecimal v){value=v;}
 public BigDecimal getMinimumPurchase(){return minimumPurchase;}public void setMinimumPurchase(BigDecimal v){minimumPurchase=v;}
 public int getMinimumQuantity(){return minimumQuantity;}public void setMinimumQuantity(int v){minimumQuantity=v;}
 public LocalDateTime getStartsAt(){return startsAt;}public void setStartsAt(LocalDateTime v){startsAt=v;}
 public LocalDateTime getEndsAt(){return endsAt;}public void setEndsAt(LocalDateTime v){endsAt=v;}
 public Integer getUsageLimit(){return usageLimit;}public void setUsageLimit(Integer v){usageLimit=v;}
 public int getUsedCount(){return usedCount;}public void setUsedCount(int v){usedCount=v;}public boolean isActive(){return active;}public void setActive(boolean v){active=v;}
}
