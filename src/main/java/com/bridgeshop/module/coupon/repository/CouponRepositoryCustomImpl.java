package com.bridgeshop.module.coupon.repository;

import com.bridgeshop.module.coupon.dto.CouponListSearchRequest;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.QCoupon;
import com.bridgeshop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Coupon> findByCondition(CouponListSearchRequest couponListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
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

        List<Coupon> reviewList = query.fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(reviewList, pageable, totalCount);
    }
}