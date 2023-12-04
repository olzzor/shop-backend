package com.bridgeshop.module.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -2073361069L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.bridgeshop.module.entity.QBaseTimeEntity _super = new com.bridgeshop.module.entity.QBaseTimeEntity(this);

    public final BooleanPath activateFlag = createBoolean("activateFlag");

    public final ListPath<com.bridgeshop.module.address.entity.Address, com.bridgeshop.module.address.entity.QAddress> addresses = this.<com.bridgeshop.module.address.entity.Address, com.bridgeshop.module.address.entity.QAddress>createList("addresses", com.bridgeshop.module.address.entity.Address.class, com.bridgeshop.module.address.entity.QAddress.class, PathInits.DIRECT2);

    public final BooleanPath adminFlag = createBoolean("adminFlag");

    public final EnumPath<AuthProvider> authProvider = createEnum("authProvider", AuthProvider.class);

    public final com.bridgeshop.module.cart.entity.QCart cart;

    public final ListPath<com.bridgeshop.module.contact.entity.Contact, com.bridgeshop.module.contact.entity.QContact> contacts = this.<com.bridgeshop.module.contact.entity.Contact, com.bridgeshop.module.contact.entity.QContact>createList("contacts", com.bridgeshop.module.contact.entity.Contact.class, com.bridgeshop.module.contact.entity.QContact.class, PathInits.DIRECT2);

    public final ListPath<com.bridgeshop.module.coupon.entity.CouponUser, com.bridgeshop.module.coupon.entity.QCouponUser> couponUsers = this.<com.bridgeshop.module.coupon.entity.CouponUser, com.bridgeshop.module.coupon.entity.QCouponUser>createList("couponUsers", com.bridgeshop.module.coupon.entity.CouponUser.class, com.bridgeshop.module.coupon.entity.QCouponUser.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final ListPath<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite> favorites = this.<com.bridgeshop.module.favorite.entity.Favorite, com.bridgeshop.module.favorite.entity.QFavorite>createList("favorites", com.bridgeshop.module.favorite.entity.Favorite.class, com.bridgeshop.module.favorite.entity.QFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final ListPath<com.bridgeshop.module.order.entity.Order, com.bridgeshop.module.order.entity.QOrder> orders = this.<com.bridgeshop.module.order.entity.Order, com.bridgeshop.module.order.entity.QOrder>createList("orders", com.bridgeshop.module.order.entity.Order.class, com.bridgeshop.module.order.entity.QOrder.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct> recentlyViewedProducts = this.<com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct>createList("recentlyViewedProducts", com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct.class, com.bridgeshop.module.recentlyviewedproduct.entity.QRecentlyViewedProduct.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final ListPath<com.bridgeshop.module.review.dto.Review, com.bridgeshop.module.review.dto.QReview> reviews = this.<com.bridgeshop.module.review.dto.Review, com.bridgeshop.module.review.dto.QReview>createList("reviews", com.bridgeshop.module.review.dto.Review.class, com.bridgeshop.module.review.dto.QReview.class, PathInits.DIRECT2);

    public final StringPath socialId = createString("socialId");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new com.bridgeshop.module.cart.entity.QCart(forProperty("cart"), inits.get("cart")) : null;
    }

}

