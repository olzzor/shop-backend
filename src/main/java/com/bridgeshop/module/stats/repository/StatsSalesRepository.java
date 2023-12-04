package com.bridgeshop.module.stats.repository;

import com.bridgeshop.module.stats.entity.StatsSales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsSalesRepository extends JpaRepository<StatsSales, Long>, StatsSalesRepositoryCustom {
}
