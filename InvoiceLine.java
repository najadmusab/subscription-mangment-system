package com.subscript.billing.entity;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="invoice_lines")
public class InvoiceLine {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="invoice_id") private Invoice invoice;
 @Column(nullable=false,length=180) private String description;
 @Column(nullable=false) private int quantity;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal unitPrice;
 @Column(nullable=false,precision=14,scale=2) private BigDecimal lineTotal;
 protected InvoiceLine(){} public InvoiceLine(String d,int q,BigDecimal p){description=d;quantity=q;unitPrice=p;lineTotal=p.multiply(BigDecimal.valueOf(q));}
 void setInvoice(Invoice i){invoice=i;}public Long getId(){return id;}public String getDescription(){return description;}
 public int getQuantity(){return quantity;}public BigDecimal getUnitPrice(){return unitPrice;}public BigDecimal getLineTotal(){return lineTotal;}
}
