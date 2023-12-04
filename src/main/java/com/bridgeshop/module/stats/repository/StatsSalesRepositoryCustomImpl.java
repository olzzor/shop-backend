package com.bridgeshop.module.stats.repository;

import com.bridgeshop.module.stats.dto.StatsSalesResponse;
import com.bridgeshop.module.stats.entity.QStatsSales;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;


public class StatsSalesRepositoryCustomImpl implements StatsSalesRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<StatsSalesResponse> findYearlySales(LocalDate startDate, LocalDate endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStatsSales statsSales = QStatsSales.statsSales;

        List<StatsSalesResponse> results = queryFactory
                .select(Projections.constructor(StatsSalesResponse.class,
                        statsSales.referenceDate.min(),
                        statsSales.referenceDate.max(),
                        statsSales.soldOrderCount.sum(),
                        statsSales.canceledOrderCount.sum(),
                        statsSales.soldAmount.sum(),
                        statsSales.refundAmount.sum()))
                .from(statsSales)
                .where(statsSales.referenceDate.between(startDate, endDate))
                .groupBy(statsSales.referenceDate.year())
                .orderBy(statsSales.referenceDate.year().asc())
                .fetch();

        return results;
    }

    @Override
    public List<StatsSalesResponse> findMonthlySales(LocalDate startDate, LocalDate endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStatsSales statsSales = QStatsSales.statsSales;

        List<StatsSalesResponse> results = queryFactory
                .select(Projections.constructor(StatsSalesResponse.class,
                        statsSales.referenceDate.min(),
                        statsSales.referenceDate.max(),
                        statsSales.soldOrderCount.sum(),
                        statsSales.canceledOrderCount.sum(),
                        statsSales.soldAmount.sum(),
                        statsSales.refundAmount.sum()))
                .from(statsSales)
                .where(statsSales.referenceDate.between(startDate, endDate))
                .groupBy(statsSales.referenceDate.year(), statsSales.referenceDate.month())
                .orderBy(statsSales.referenceDate.year().asc(), statsSales.referenceDate.month().asc())
                .fetch();

        return results;
    }

    @Override
    public List<StatsSalesResponse> findWeeklySales(LocalDate startDate, LocalDate endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStatsSales statsSales = QStatsSales.statsSales;

        List<StatsSalesResponse> results = queryFactory
                .select(Projections.constructor(StatsSalesResponse.class,
                        statsSales.referenceDate.min(),
                        statsSales.referenceDate.max(),
                        statsSales.soldOrderCount.sum(),
                        statsSales.canceledOrderCount.sum(),
                        statsSales.soldAmount.sum(),
                        statsSales.refundAmount.sum()))
                .from(statsSales)
                .where(statsSales.referenceDate.between(startDate, endDate))
                .groupBy(statsSales.referenceDate.yearWeek())
                .orderBy(statsSales.referenceDate.yearWeek().asc())
                .fetch();

        return results;
    }

    @Override
    public List<StatsSalesResponse> findDailySales(LocalDate startDate, LocalDate endDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStatsSales statsSales = QStatsSales.statsSales;

        List<StatsSalesResponse> results = queryFactory
                .select(Projections.constructor(StatsSalesResponse.class,
                        statsSales.referenceDate,
                        statsSales.referenceDate,
                        statsSales.soldOrderCount,
                        statsSales.canceledOrderCount,
                        statsSales.soldAmount,
                        statsSales.refundAmount))
                .from(statsSales)
                .where(statsSales.referenceDate.between(startDate, endDate))
                .orderBy(statsSales.referenceDate.asc())
                .fetch();

        return results;
    }
}