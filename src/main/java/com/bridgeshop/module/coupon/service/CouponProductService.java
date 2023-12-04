package com.bridgeshop.module.coupon.service;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponProduct;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.coupon.repository.CouponProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponProductService {

    private final CouponProductRepository couponProductRepository;

    @Transactional
    public void insertCouponProduct(Coupon coupon, List<Long> productIds) {

        for (Long productId : productIds) {
            CouponProduct couponProduct = new CouponProduct();

            Product product = new Product();
            product.setId(productId);

            couponProduct.setCoupon(coupon);
            couponProduct.setProduct(product);

            couponProductRepository.save(couponProduct);
        }
    }

    @Transactional
    public void updateCouponProduct(Coupon coupon, List<Long> productIds) {
        couponProductRepository.deleteAllByCoupon(coupon);
        this.insertCouponProduct(coupon, productIds);
    }
}