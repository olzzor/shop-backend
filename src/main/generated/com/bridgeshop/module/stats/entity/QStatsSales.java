package com.bridgeshop.module.stats.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatsSales is a Querydsl query type for StatsSales
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatsSales extends EntityPathBase<StatsSales> {

    private static final long serialVersionUID = -2012943543L;

    public static final QStatsSales statsSales = new QStatsSales("statsSales");

    public final NumberPath<Integer> canceledOrderCount = createNumber("canceledOrderCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> referenceDate = createDate("referenceDate", java.time.LocalDate.class);

    public final NumberPath<Integer> refundAmount = createNumber("refundAmount", Integer.class);

    public final NumberPath<Integer> soldAmount = createNumber("soldAmount", Integer.class);

    public final NumberPath<Integer> soldOrderCount = createNumber("soldOrderCount", Integer.class);

    public QStatsSales(String variable) {
        super(StatsSales.class, forVariable(variable));
    }

    public QStatsSales(Path<? extends StatsSales> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatsSales(PathMetadata metadata) {
        super(StatsSales.class, metadata);
    }

}

