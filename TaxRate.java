package com.subscript.billing.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.Instant;
@Entity @Table(name="tax_rates")
public class TaxRate {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String name;
 @Column(nullable=false,precision=7,scale=3) private BigDecimal ratePercent;
 @Column(nullable=false) private boolean active=true;
 @Column(nullable=false,updatable=false) private Instant createdAt;
 protected TaxRate(){} @PrePersist void created(){createdAt=Instant.now();}
 public Long getId(){return id;}public String getName(){return name;}public void setName(String v){name=v;}
 public BigDecimal getRatePercent(){return ratePercent;}public void setRatePercent(BigDecimal v){ratePercent=v;}
 public boolean isActive(){return active;}public void setActive(boolean v){active=v;}
}
