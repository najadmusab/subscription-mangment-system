package com.subscript.product.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="products", indexes=@Index(name="idx_products_name", columnList="name"))
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=150) private String name;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private ProductType type;
    @Column(nullable=false, precision=14, scale=2) private BigDecimal salesPrice;
    @Column(nullable=false, precision=14, scale=2) private BigDecimal costPrice;
    @Column(length=1000) private String notes;
    @Column(nullable=false) private boolean active=true;
    @OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<ProductVariant> variants=new ArrayList<>();
    @Column(nullable=false, updatable=false) private Instant createdAt;
    @Column(nullable=false) private Instant updatedAt;
    protected Product() {}
    @PrePersist void create(){ Instant n=Instant.now(); createdAt=n; updatedAt=n; }
    @PreUpdate void update(){ updatedAt=Instant.now(); }
    public void replaceVariants(List<ProductVariant> values) {
        variants.clear();
        if(values!=null) values.forEach(v->{v.setProduct(this); variants.add(v);});
    }
    public void addVariant(ProductVariant v){v.setProduct(this);variants.add(v);}
    public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;}
    public ProductType getType(){return type;} public void setType(ProductType v){type=v;}
    public BigDecimal getSalesPrice(){return salesPrice;} public void setSalesPrice(BigDecimal v){salesPrice=v;}
    public BigDecimal getCostPrice(){return costPrice;} public void setCostPrice(BigDecimal v){costPrice=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
    public List<ProductVariant> getVariants(){return variants;}
    public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
