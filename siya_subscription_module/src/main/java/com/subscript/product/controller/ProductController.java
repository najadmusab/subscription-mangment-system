package com.subscript.product.controller;
import com.subscript.product.dto.*;
import com.subscript.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/products")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class ProductController {
 private final ProductService service;
 public ProductController(ProductService s){service=s;}
 @GetMapping public List<ProductResponse> list(){return service.list().stream().map(ProductResponse::from).toList();}
 @GetMapping("/{id}") public ProductResponse get(@PathVariable Long id){return ProductResponse.from(service.get(id));}
 @PostMapping public ProductResponse create(@Valid @RequestBody ProductRequest r){return ProductResponse.from(service.create(r));}
 @PutMapping("/{id}") public ProductResponse update(@PathVariable Long id,@Valid @RequestBody ProductRequest r){return ProductResponse.from(service.update(id,r));}
 @PatchMapping("/{id}/archive") @PreAuthorize("hasRole('ADMIN')")
 public ProductResponse archive(@PathVariable Long id){return ProductResponse.from(service.archive(id));}
}
