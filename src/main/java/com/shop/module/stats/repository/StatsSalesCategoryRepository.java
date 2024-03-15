package com.shop.module.stats.repository;

import com.shop.module.stats.entity.StatsSalesCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsSalesCategoryRepository extends JpaRepository<StatsSalesCategory, Long>, StatsSalesCategoryRepositoryCustom {
}
