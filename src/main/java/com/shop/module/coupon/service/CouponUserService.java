package com.shop.module.coupon.service;

import com.shop.common.exception.NotFoundException;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponUser;
import com.shop.module.coupon.repository.CouponUserRepository;
import com.shop.module.user.entity.User;
import com.shop.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponUserService {

    private final CouponUserRepository couponUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public void insertCouponUser(Coupon coupon, List<Long> userIds) {

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

            CouponUser couponUser = CouponUser.builder()
                    .coupon(coupon)
                    .user(user)
                    .build();

            couponUserRepository.save(couponUser);
        }
    }

    @Transactional
    public void updateCouponUser(Coupon coupon, List<Long> userIds) {

        couponUserRepository.deleteAllByCoupon(coupon);
        this.insertCouponUser(coupon, userIds);
    }
}