package com.shop.module.coupon.repository;

import com.shop.module.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {

    @Query("SELECT c FROM Coupon c "
            + "WHERE (c.id IN (SELECT cu.coupon.id FROM CouponUser cu WHERE cu.user.id = :userId) "
            + "OR c.id NOT IN (SELECT cu.coupon.id FROM CouponUser cu)) "
            + "AND c.startValidDate <= CURRENT_DATE AND c.endValidDate >= CURRENT_DATE "
            + "AND c.status = 'ACTIVE'")
    List<Coupon> findAllAvailableCouponsByUser(@Param("userId") Long userId);

    @Query("SELECT c FROM Coupon c "
            + "WHERE (c.id IN (SELECT cu.coupon.id FROM CouponUser cu WHERE cu.user.id = :userId) "
            + "OR c.id IN (SELECT cc.coupon.id FROM CouponCategory cc WHERE cc.category.id = :categoryId) "
            + "OR c.id IN (SELECT cp.coupon.id FROM CouponProduct cp WHERE cp.product.id = :productId)) "
            + "AND c.startValidDate <= CURRENT_DATE AND c.endValidDate >= CURRENT_DATE "
            + "AND c.status = 'ACTIVE'")
    List<Coupon> retrieveApplicableCoupons(@Param("userId") Long userId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("productId") Long productId);
}