package com.shop.batch.controller;

import com.shop.batch.scheduler.StatsSalesCategoryScheduler;
import com.shop.batch.scheduler.StatsSalesScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final StatsSalesScheduler statsSalesScheduler;
    private final StatsSalesCategoryScheduler statsSalesCategoryScheduler;

    @GetMapping("/api/batch/stats-sales")
    public ResponseEntity<String> runBatchSales() {
        statsSalesScheduler.runJob();
        return ResponseEntity.ok("Batch job started");
    }

    @GetMapping("/api/batch/stats-sales-category")
    public ResponseEntity<String> runBatchSalesCategory() {
        statsSalesCategoryScheduler.runJob();
        return ResponseEntity.ok("Batch job started");
    }
}