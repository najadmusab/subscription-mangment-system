package com.subscript.product.service;
import com.subscript.product.dto.ProductRequest;
import com.subscript.product.entity.*;
import com.subscript.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
public class ProductService {
 private final ProductRepository repo;
 public ProductService(ProductRepository repo){this.repo=repo;}
 @Transactional(readOnly=true) public List<Product> list(){return repo.findAll().stream().filter(Product::isActive).toList();}
 @Transactional(readOnly=true) public Product get(Long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found."));}
 @Transactional public Product create(ProductRequest r){Product p=new Product();apply(p,r);return repo.save(p);}
 @Transactional public Product update(Long id,ProductRequest r){Product p=get(id);if(!p.isActive())throw new IllegalArgumentException("Archived product cannot be edited.");apply(p,r);return repo.save(p);}
 @Transactional public Product archive(Long id){Product p=get(id);p.setActive(false);return repo.save(p);}
 private void apply(Product p,ProductRequest r){
  p.setName(r.name().trim());p.setType(r.type());p.setSalesPrice(r.salesPrice());p.setCostPrice(r.costPrice());p.setNotes(r.notes());
  List<ProductVariant> variants=r.variants()==null?List.of():r.variants().stream()
   .map(v->new ProductVariant(v.name().trim(),v.additionalPrice(),v.active()==null||v.active())).toList();
  p.replaceVariants(variants);
 }
}
