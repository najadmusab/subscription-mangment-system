package com.subscript.product.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="product_variants")
public class ProductVariant {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="product_id") private Product product;
    @Column(nullable=false,length=120) private String name;
    @Column(nullable=false,precision=14,scale=2) private BigDecimal additionalPrice=BigDecimal.ZERO;
    @Column(nullable=false) private boolean active=true;
    protected ProductVariant(){}
    public ProductVariant(String name,BigDecimal additionalPrice,boolean active){this.name=name;this.additionalPrice=additionalPrice;this.active=active;}
    public Long getId(){return id;} public Product getProduct(){return product;} void setProduct(Product p){product=p;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public BigDecimal getAdditionalPrice(){return additionalPrice;} public void setAdditionalPrice(BigDecimal v){additionalPrice=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
