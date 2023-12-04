package com.bridgeshop.module.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 862215299L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final ListPath<com.bridgeshop.module.cart.entity.CartProduct, com.bridgeshop.module.cart.entity.QCartProduct> cartProducts = this.<com.bridgeshop.module.cart.entity.CartProduct, com.bridgeshop.module.cart.entity.QCartProduct>createList("cartProducts", com.bridgeshop.module.cart.entity.CartProduct.class, com.bridgeshop.module.cart.entity.QCartProduct.class, PathInits.DIRECT2);

    public final com.bridgeshop.module.category.entity.QCategory category;

    public final StringPath code = createString("code");

    public final ListPath<com.bridgeshop.module.coupon.entity.CouponProduct, com.bridgeshop.module.coupon.entity.QCouponProduct> couponProducts = this.<com.bridgeshop.module.coupon.entity.CouponProduct, com.bridgeshop.module.coupon.entity.QCouponProduct>createList("couponProducts", com.bridgeshop.module.coupon.entity.CouponProduct.class, com.bridgeshop.module.coupon.entity.QCouponProduct.class, PathInits.DIRECT2);

    public final StringPath detail = createString("detail");

    public final NumberPath<Integer> discountPer = createNumber("discountPer", Integer.class);

    public final ListPath<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite> favorites = this.<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite>createList("favorites", com.bridgeshop.module.favorite.entity.Favorite.class, com.bridgeshop.module.favorite.entity.QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final ListPath<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail> orderDetails = this.<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail>createList("orderDetails", com.bridgeshop.module.order.entity.OrderDetail.class, com.bridgeshop.module.order.entity.QOrderDetail.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<ProductImage, QProductImage> productImages = this.<ProductImage, QProductImage>createList("productImages", ProductImage.class, QProductImage.class, PathInits.DIRECT2);

    public final ListPath<ProductSize, QProductSize> productSizes = this.<ProductSize, QProductSize>createList("productSizes", ProductSize.class, QProductSize.class, PathInits.DIRECT2);

    public final ListPath<com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct> recentlyViewedProducts = this.<com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct>createList("recentlyViewedProducts", com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct.class, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<ProductStatus> status = createEnum("status", ProductStatus.class);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.bridgeshop.module.category.entity.QCategory(forProperty("category")) : null;
    }

}

