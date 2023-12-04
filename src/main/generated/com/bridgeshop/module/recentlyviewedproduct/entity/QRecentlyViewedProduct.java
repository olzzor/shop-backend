package com.bridgeshop.module.recentlyviewedproduct.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecentlyViewedProduct is a Querydsl query type for RecentlyViewedProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecentlyViewedProduct extends EntityPathBase<RecentlyViewedProduct> {

    private static final long serialVersionUID = 110688707L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecentlyViewedProduct recentlyViewedProduct = new QRecentlyViewedProduct("recentlyViewedProduct");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final com.bridgeshop.module.product.entity.QProduct product;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final com.bridgeshop.module.user.entity.QUser user;

    public final DateTimePath<java.time.LocalDateTime> viewedAt = createDateTime("viewedAt", java.time.LocalDateTime.class);

    public QRecentlyViewedProduct(String variable) {
        this(RecentlyViewedProduct.class, forVariable(variable), INITS);
    }

    public QRecentlyViewedProduct(Path<? extends RecentlyViewedProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecentlyViewedProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecentlyViewedProduct(PathMetadata metadata, PathInits inits) {
        this(RecentlyViewedProduct.class, metadata, inits);
    }

    public QRecentlyViewedProduct(Class<? extends RecentlyViewedProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.bridgeshop.module.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.bridgeshop.module.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

