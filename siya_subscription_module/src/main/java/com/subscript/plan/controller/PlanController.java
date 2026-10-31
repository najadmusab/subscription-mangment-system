package com.subscript.plan.controller;
import com.subscript.plan.dto.*;import com.subscript.plan.service.PlanService;
import jakarta.validation.Valid;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/plans") @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class PlanController {
 private final PlanService service;public PlanController(PlanService s){service=s;}
 @GetMapping public List<PlanResponse> list(){return service.list().stream().map(PlanResponse::from).toList();}
 @GetMapping("/{id}") public PlanResponse get(@PathVariable Long id){return PlanResponse.from(service.get(id));}
 @PostMapping public PlanResponse create(@Valid @RequestBody PlanRequest r){return PlanResponse.from(service.create(r));}
 @PutMapping("/{id}") public PlanResponse update(@PathVariable Long id,@Valid @RequestBody PlanRequest r){return PlanResponse.from(service.update(id,r));}
 @PatchMapping("/{id}/archive") @PreAuthorize("hasRole('ADMIN')") public PlanResponse archive(@PathVariable Long id){return PlanResponse.from(service.archive(id));}
}
