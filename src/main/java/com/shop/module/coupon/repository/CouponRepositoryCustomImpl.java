package com.shop.module.coupon.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.common.util.QueryDslUtils;
import com.shop.module.coupon.dto.CouponListSearchRequest;
import com.shop.module.coupon.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Coupon> findByCondition(CouponListSearchRequest couponListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCoupon qCoupon = QCoupon.coupon;

        JPAQuery<Coupon> query = queryFactory
                .selectFrom(qCoupon)
                .where(
                        QueryDslUtils.eqCouponType(qCoupon.type, couponListSearchRequest.getType()),
                        QueryDslUtils.likeString(qCoupon.code, couponListSearchRequest.getCode()),
                        QueryDslUtils.likeString(qCoupon.name, couponListSearchRequest.getName()),
                        QueryDslUtils.eqInteger(qCoupon.minAmount, couponListSearchRequest.getMinAmount()),
                        QueryDslUtils.eqCouponDiscountType(qCoupon.discountType, couponListSearchRequest.getDiscountType()),
                        QueryDslUtils.eqInteger(qCoupon.discountValue, couponListSearchRequest.getDiscountValue()),
                        QueryDslUtils.betweenDate(qCoupon.startValidDate, couponListSearchRequest.getStartStartValidDate(), couponListSearchRequest.getEndStartValidDate()),
                        QueryDslUtils.betweenDate(qCoupon.endValidDate, couponListSearchRequest.getStartEndValidDate(), couponListSearchRequest.getEndEndValidDate()),
                        QueryDslUtils.eqCouponStatus(qCoupon.status, couponListSearchRequest.getStatus())
                )
                .orderBy(qCoupon.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Coupon> couponList = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(couponList, pageable, totalCount);
    }

    @Override
    public List<Coupon> findApplicableCouponsForCart(Long userId, List<Long> categoryIds, List<Long> productIds) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCoupon qCoupon = QCoupon.coupon;
        QCouponCategory qCouponCategory = QCouponCategory.couponCategory;
        QCouponProduct qCouponProduct = QCouponProduct.couponProduct;
        QCouponUser qCouponUser = QCouponUser.couponUser;

        JPAQuery<Coupon> query = queryFactory
                .selectFrom(qCoupon)
                .distinct()
                .leftJoin(qCoupon.couponCategories, qCouponCategory)
                .leftJoin(qCoupon.couponProducts, qCouponProduct)
                .leftJoin(qCoupon.couponUsers, qCouponUser)
                .where(
                        qCoupon.status.eq(CouponStatus.ACTIVE),
                        qCoupon.startValidDate.loe(LocalDateTime.now()),
                        qCoupon.endValidDate.goe(LocalDateTime.now()),
                        qCouponUser.user.id.eq(userId).or(qCouponUser.isNull()),
                        qCouponCategory.category.id.in(categoryIds).or(qCouponProduct.product.id.in(productIds))
                );

        List<Coupon> couponList = query.fetch();

        return couponList;
    }
}