package com.shop.module.stats.repository;


import com.shop.module.stats.dto.StatsSalesCategoryResponse;

import java.time.LocalDate;
import java.util.List;

public interface StatsSalesCategoryRepositoryCustom {

    List<StatsSalesCategoryResponse> findSalesCategory(LocalDate startDate, LocalDate endDate);
}
