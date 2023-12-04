package com.bridgeshop.module.coupon.service;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponUser;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.coupon.repository.CouponUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponUserService {

    private final CouponUserRepository couponUserRepository;

    @Transactional
    public void insertCouponUser(Coupon coupon, List<Long> userIds) {

        for (Long userId : userIds) {
            CouponUser couponUser = new CouponUser();

            User user = new User();
            user.setId(userId);

            couponUser.setCoupon(coupon);
            couponUser.setUser(user);

            couponUserRepository.save(couponUser);
        }
    }

    @Transactional
    public void updateCouponUser(Coupon coupon, List<Long> userIds) {

        couponUserRepository.deleteAllByCoupon(coupon);
        this.insertCouponUser(coupon, userIds);
    }
}