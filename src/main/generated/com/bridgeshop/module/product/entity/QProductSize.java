package com.bridgeshop.module.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductSize is a Querydsl query type for ProductSize
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSize extends EntityPathBase<ProductSize> {

    private static final long serialVersionUID = -114051292L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductSize productSize = new QProductSize("productSize");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final ListPath<com.bridgeshop.module.cart.entity.CartProduct, com.bridgeshop.module.cart.entity.QCartProduct> cartProducts = this.<com.bridgeshop.module.cart.entity.CartProduct, com.bridgeshop.module.cart.entity.QCartProduct>createList("cartProducts", com.bridgeshop.module.cart.entity.CartProduct.class, com.bridgeshop.module.cart.entity.QCartProduct.class, PathInits.DIRECT2);

    public final ListPath<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite> favorites = this.<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite>createList("favorites", com.bridgeshop.module.favorite.entity.Favorite.class, com.bridgeshop.module.favorite.entity.QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final ListPath<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail> orderDetails = this.<com.bridgeshop.module.order.entity.OrderDetail, com.bridgeshop.module.order.entity.QOrderDetail>createList("orderDetails", com.bridgeshop.module.order.entity.OrderDetail.class, com.bridgeshop.module.order.entity.QOrderDetail.class, PathInits.DIRECT2);

    public final QProduct product;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath size = createString("size");

    public QProductSize(String variable) {
        this(ProductSize.class, forVariable(variable), INITS);
    }

    public QProductSize(Path<? extends ProductSize> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductSize(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductSize(PathMetadata metadata, PathInits inits) {
        this(ProductSize.class, metadata, inits);
    }

    public QProductSize(Class<? extends ProductSize> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

