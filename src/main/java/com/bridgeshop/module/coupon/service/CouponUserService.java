package com.bridgeshop.module.coupon.service;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponUser;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.coupon.repository.CouponUserRepository;
import com.bridgeshop.module.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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