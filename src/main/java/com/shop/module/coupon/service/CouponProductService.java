package com.shop.module.coupon.service;

import com.shop.common.exception.NotFoundException;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponProduct;
import com.shop.module.coupon.repository.CouponProductRepository;
import com.shop.module.product.entity.Product;
import com.shop.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponProductService {

    private final CouponProductRepository couponProductRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void insertCouponProduct(Coupon coupon, List<Long> productIds) {

        for (Long productId : productIds) {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundException("productNotFound", "상품 정보를 찾을 수 없습니다."));

            CouponProduct couponProduct = CouponProduct.builder()
                    .coupon(coupon)
                    .product(product)
                    .build();

            couponProductRepository.save(couponProduct);
        }
    }

    @Transactional
    public void updateCouponProduct(Coupon coupon, List<Long> productIds) {
        couponProductRepository.deleteAllByCoupon(coupon);
        this.insertCouponProduct(coupon, productIds);
    }
}