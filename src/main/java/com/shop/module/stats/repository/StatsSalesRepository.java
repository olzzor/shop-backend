package com.shop.module.stats.repository;

import com.shop.module.stats.entity.StatsSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsSalesRepository extends JpaRepository<StatsSales, Long>, StatsSalesRepositoryCustom {
}
