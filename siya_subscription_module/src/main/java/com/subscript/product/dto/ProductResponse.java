package com.subscript.product.dto;
import com.subscript.product.entity.*;
import java.math.BigDecimal;
import java.util.List;
public record ProductResponse(Long id,String name,String type,BigDecimal salesPrice,BigDecimal costPrice,
 String notes,boolean active,List<VariantResponse> variants) {
 public record VariantResponse(Long id,String name,BigDecimal additionalPrice,boolean active){}
 public static ProductResponse from(Product p){return new ProductResponse(p.getId(),p.getName(),p.getType().name(),
 p.getSalesPrice(),p.getCostPrice(),p.getNotes(),p.isActive(),p.getVariants().stream()
 .map(v->new VariantResponse(v.getId(),v.getName(),v.getAdditionalPrice(),v.isActive())).toList());}
}
