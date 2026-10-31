package com.subscript.report.controller;
import com.subscript.billing.service.ReportService;import org.springframework.format.annotation.DateTimeFormat;import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;import java.util.*;
@RestController @RequestMapping("/api/reports")
public class ReportController {
 private final ReportService service;public ReportController(ReportService s){service=s;}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){return service.dashboard();}
 @GetMapping("/revenue") public List<Map<String,Object>> revenue(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
 @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to){return service.revenue(from,to);}
}
