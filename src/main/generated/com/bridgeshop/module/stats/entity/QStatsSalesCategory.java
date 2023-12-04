package com.bridgeshop.module.stats.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStatsSalesCategory is a Querydsl query type for StatsSalesCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatsSalesCategory extends EntityPathBase<StatsSalesCategory> {

    private static final long serialVersionUID = 1682617191L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStatsSalesCategory statsSalesCategory = new QStatsSalesCategory("statsSalesCategory");

    public final NumberPath<Integer> canceledOrderCount = createNumber("canceledOrderCount", Integer.class);

    public final com.bridgeshop.module.category.entity.QCategory category;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> referenceDate = createDate("referenceDate", java.time.LocalDate.class);

    public final NumberPath<Integer> refundAmount = createNumber("refundAmount", Integer.class);

    public final NumberPath<Integer> soldAmount = createNumber("soldAmount", Integer.class);

    public final NumberPath<Integer> soldOrderCount = createNumber("soldOrderCount", Integer.class);

    public QStatsSalesCategory(String variable) {
        this(StatsSalesCategory.class, forVariable(variable), INITS);
    }

    public QStatsSalesCategory(Path<? extends StatsSalesCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStatsSalesCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStatsSalesCategory(PathMetadata metadata, PathInits inits) {
        this(StatsSalesCategory.class, metadata, inits);
    }

    public QStatsSalesCategory(Class<? extends StatsSalesCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.bridgeshop.module.category.entity.QCategory(forProperty("category")) : null;
    }

}

