package com.shop.module.stats.repository;

import com.shop.module.stats.dto.StatsSalesResponse;

import java.time.LocalDate;
import java.util.List;

public interface StatsSalesRepositoryCustom {

    List<StatsSalesResponse> findYearlySales(LocalDate startDate, LocalDate endDate);

    List<StatsSalesResponse> findMonthlySales(LocalDate startDate, LocalDate endDate);

    List<StatsSalesResponse> findWeeklySales(LocalDate startDate, LocalDate endDate);

    List<StatsSalesResponse> findDailySales(LocalDate startDate, LocalDate endDate);
}
