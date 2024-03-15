package com.shop.module.stats.service;

import com.shop.module.stats.dto.StatsSalesCategoryRequest;
import com.shop.module.stats.dto.StatsSalesCategoryResponse;
import com.shop.module.stats.dto.StatsSalesRequest;
import com.shop.module.stats.dto.StatsSalesResponse;
import com.shop.module.stats.repository.StatsSalesCategoryRepository;
import com.shop.module.stats.repository.StatsSalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsSalesRepository statsSalesRepository;
    private final StatsSalesCategoryRepository statsSalesCategoryRepository;

    public List<StatsSalesResponse> getYearlySales(StatsSalesRequest statsSalesRequest) {
        return statsSalesRepository.findYearlySales(statsSalesRequest.getStartDate(), statsSalesRequest.getEndDate());
    }

    public List<StatsSalesResponse> getMonthlySales(StatsSalesRequest statsSalesRequest) {
        return statsSalesRepository.findMonthlySales(statsSalesRequest.getStartDate(), statsSalesRequest.getEndDate());
    }

    public List<StatsSalesResponse> getWeeklySales(StatsSalesRequest statsSalesRequest) {
        return statsSalesRepository.findWeeklySales(statsSalesRequest.getStartDate(), statsSalesRequest.getEndDate());
    }

    public List<StatsSalesResponse> getDailySales(StatsSalesRequest statsSalesRequest) {
        return statsSalesRepository.findDailySales(statsSalesRequest.getStartDate(), statsSalesRequest.getEndDate());
    }

    public List<StatsSalesCategoryResponse> getSalesCategory(StatsSalesCategoryRequest statsSalesCategoryRequest) {
        return statsSalesCategoryRepository.findSalesCategory(statsSalesCategoryRequest.getStartDate(), statsSalesCategoryRequest.getEndDate());
    }
}
