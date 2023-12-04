package com.bridgeshop.module.coupon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1401809097L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final StringPath code = createString("code");

    public final ListPath<CouponCategory, QCouponCategory> couponCategories = this.<CouponCategory, QCouponCategory>createList("couponCategories", CouponCategory.class, QCouponCategory.class, PathInits.DIRECT2);

    public final ListPath<CouponProduct, QCouponProduct> couponProducts = this.<CouponProduct, QCouponProduct>createList("couponProducts", CouponProduct.class, QCouponProduct.class, PathInits.DIRECT2);

    public final ListPath<CouponUser, QCouponUser> couponUsers = this.<CouponUser, QCouponUser>createList("couponUsers", CouponUser.class, QCouponUser.class, PathInits.DIRECT2);

    public final StringPath detail = createString("detail");

    public final EnumPath<CouponDiscountType> discountType = createEnum("discountType", CouponDiscountType.class);

    public final NumberPath<Integer> discountValue = createNumber("discountValue", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endValidDate = createDateTime("endValidDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> minAmount = createNumber("minAmount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final ListPath<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail> orderDetails = this.<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail>createList("orderDetails", com.bridgeshop.module.order.entity.OrderDetail.class, com.bridgeshop.module.order.entity.QOrderDetail.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final DateTimePath<java.time.LocalDateTime> startValidDate = createDateTime("startValidDate", java.time.LocalDateTime.class);

    public final EnumPath<CouponStatus> status = createEnum("status", CouponStatus.class);

    public final EnumPath<CouponType> type = createEnum("type", CouponType.class);

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

