package com.bridgeshop.module.coupon.service;

import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponCategory;
import com.bridgeshop.module.coupon.repository.CouponCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponCategoryService {

    private final CouponCategoryRepository couponCategoryRepository;

    @Transactional
    public void insertCouponCategory(Coupon coupon, List<Long> categoryIds) {

        for (Long categoryId : categoryIds) {
            CouponCategory couponCategory = new CouponCategory();

            Category category = new Category();
            category.setId(categoryId);

            couponCategory.setCoupon(coupon);
            couponCategory.setCategory(category);

            couponCategoryRepository.save(couponCategory);
        }
    }

    @Transactional
    public void updateCouponCategory(Coupon coupon, List<Long> categoryIds) {
        couponCategoryRepository.deleteAllByCoupon(coupon);
        this.insertCouponCategory(coupon, categoryIds);
    }
}