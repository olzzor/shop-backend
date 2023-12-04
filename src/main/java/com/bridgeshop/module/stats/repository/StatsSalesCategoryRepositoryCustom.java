package com.bridgeshop.module.stats.repository;


import com.bridgeshop.module.stats.dto.StatsSalesCategoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface StatsSalesCategoryRepositoryCustom {

    List<StatsSalesCategoryResponse> findSalesCategory(LocalDate startDate, LocalDate endDate);
}
