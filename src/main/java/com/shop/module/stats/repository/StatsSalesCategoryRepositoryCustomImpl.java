package com.shop.module.stats.repository;

import com.shop.module.stats.dto.StatsSalesCategoryResponse;
import com.shop.module.stats.entity.QStatsSalesCategory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;


public class StatsSalesCategoryRepositoryCustomImpl implements StatsSalesCategoryRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StatsSalesCategoryResponse> findSalesCategory(LocalDate startDate, LocalDate endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStatsSalesCategory statsSalesCategory = QStatsSalesCategory.statsSalesCategory;

        List<StatsSalesCategoryResponse> results = queryFactory
                .select(Projections.constructor(StatsSalesCategoryResponse.class,
                        statsSalesCategory.category,
                        statsSalesCategory.soldOrderCount.sum(),
                        statsSalesCategory.canceledOrderCount.sum(),
                        statsSalesCategory.soldAmount.sum(),
                        statsSalesCategory.refundAmount.sum()))
                .from(statsSalesCategory)
                .where(statsSalesCategory.referenceDate.between(startDate, endDate))
                .groupBy(statsSalesCategory.category)
                .orderBy(statsSalesCategory.referenceDate.asc())
                .fetch();

        return results;
    }
}