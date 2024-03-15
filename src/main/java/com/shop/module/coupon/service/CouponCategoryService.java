package com.shop.module.coupon.service;

import com.shop.common.exception.NotFoundException;
import com.shop.module.category.entity.Category;
import com.shop.module.category.repository.CategoryRepository;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponCategory;
import com.shop.module.coupon.repository.CouponCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponCategoryService {

    private final CouponCategoryRepository couponCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void insertCouponCategory(Coupon coupon, List<String> categoryCodes) {

        for (String categoryCode : categoryCodes) {
            Category category = categoryRepository.findByCode(categoryCode)
                    .orElseThrow(() -> new NotFoundException("categoryNotFound", "카테고리 정보를 찾을 수 없습니다."));

            CouponCategory couponCategory = CouponCategory.builder()
                    .coupon(coupon)
                    .category(category)
                    .build();

            couponCategoryRepository.save(couponCategory);
        }
    }

    @Transactional
    public void updateCouponCategory(Coupon coupon, List<String> categoryCodes) {
        couponCategoryRepository.deleteAllByCoupon(coupon);
        this.insertCouponCategory(coupon, categoryCodes);
    }
}