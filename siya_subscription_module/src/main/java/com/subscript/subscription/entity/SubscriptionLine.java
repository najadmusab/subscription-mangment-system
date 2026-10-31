package com.subscript.subscription.entity;
import com.subscript.product.entity.Product;import com.subscript.product.entity.ProductVariant;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="subscription_lines")
public class SubscriptionLine {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="subscription_id") private Subscription subscription;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="product_id") private Product product;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="variant_id") private ProductVariant variant;
 @Column(nullable=false) private int quantity;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal unitPrice;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal lineTotal;
 protected SubscriptionLine(){}
 public SubscriptionLine(Product p,ProductVariant v,int q,BigDecimal price){product=p;variant=v;quantity=q;unitPrice=price;lineTotal=price.multiply(BigDecimal.valueOf(q));}
 void setSubscription(Subscription s){subscription=s;}
 public Long getId(){return id;}public Product getProduct(){return product;}public ProductVariant getVariant(){return variant;}
 public int getQuantity(){return quantity;}public BigDecimal getUnitPrice(){return unitPrice;}public BigDecimal getLineTotal(){return lineTotal;}
}
