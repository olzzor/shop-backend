package com.bridgeshop.module.stats.repository;

import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsSalesCategoryRepository extends JpaRepository<StatsSalesCategory, Long>, StatsSalesCategoryRepositoryCustom {
}
