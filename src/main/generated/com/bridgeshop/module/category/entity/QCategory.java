package com.bridgeshop.module.category.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 227339705L;

    public static final QCategory category = new QCategory("category");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final StringPath code = createString("code");

    public final ListPath<com.bridgeshop.module.coupon.entity.CouponCategory, com.bridgeshop.module.coupon.entity.QCouponCategory> couponCategories = this.<com.bridgeshop.module.coupon.entity.CouponCategory, com.bridgeshop.module.coupon.entity.QCouponCategory>createList("couponCategories", com.bridgeshop.module.coupon.entity.CouponCategory.class, com.bridgeshop.module.coupon.entity.QCouponCategory.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final ListPath<com.bridgeshop.module.product.entity.Product, com.bridgeshop.module.product.entity.QProduct> products = this.<com.bridgeshop.module.product.entity.Product, com.bridgeshop.module.product.entity.QProduct>createList("products", com.bridgeshop.module.product.entity.Product.class, com.bridgeshop.module.product.entity.QProduct.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final ListPath<com.bridgeshop.module.stats.entity.StatsSalesCategory, com.bridgeshop.module.stats.entity.QStatsSalesCategory> statsSalesCategories = this.<com.bridgeshop.module.stats.entity.StatsSalesCategory, com.bridgeshop.module.stats.entity.QStatsSalesCategory>createList("statsSalesCategories", com.bridgeshop.module.stats.entity.StatsSalesCategory.class, com.bridgeshop.module.stats.entity.QStatsSalesCategory.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

