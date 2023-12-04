package com.bridgeshop.module.cart.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartProduct is a Querydsl query type for CartProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartProduct extends EntityPathBase<CartProduct> {

    private static final long serialVersionUID = -1711341134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartProduct cartProduct = new QCartProduct("cartProduct");

    public final QCart cart;

    public final com.bridgeshop.module.coupon.entity.QCoupon coupon;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.bridgeshop.module.product.entity.QProduct product;

    public final com.bridgeshop.module.product.entity.QProductSize productSize;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QCartProduct(String variable) {
        this(CartProduct.class, forVariable(variable), INITS);
    }

    public QCartProduct(Path<? extends CartProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartProduct(PathMetadata metadata, PathInits inits) {
        this(CartProduct.class, metadata, inits);
    }

    public QCartProduct(Class<? extends CartProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
        this.coupon = inits.isInitialized("coupon") ? new com.bridgeshop.module.coupon.entity.QCoupon(forProperty("coupon")) : null;
        this.product = inits.isInitialized("product") ? new com.bridgeshop.module.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.productSize = inits.isInitialized("productSize") ? new com.bridgeshop.module.product.entity.QProductSize(forProperty("productSize"), inits.get("productSize")) : null;
    }

}

