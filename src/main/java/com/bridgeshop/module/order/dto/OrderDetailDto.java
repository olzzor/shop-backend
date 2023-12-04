package com.bridgeshop.module.order.dto;

import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.dto.ProductSizeDto;
import com.bridgeshop.module.shipment.dto.ShipmentDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderDetailDto {
    private Long id;
    private OrderDto order;
    private ProductDto product;
    private ProductSizeDto productSize;
    private ShipmentDto shipment;
    private CouponDto coupon;       // 추가
    private int quantity;
    private int unitPrice;
    private int discountPer;        // 추가
    private int finalPrice;         // 추가
    //    private ReviewDto review;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
//    private boolean hasReview;

    public OrderDetailDto includeOrder(OrderDto orderDto) {
        this.order = orderDto;
        return this;
    }

    public OrderDetailDto includeProduct(ProductDto productDto) {
        this.product = productDto;
        return this;
    }

    public OrderDetailDto includeProductSize(ProductSizeDto productSizeDto) {
        this.productSize = productSizeDto;
        return this;
    }

    public OrderDetailDto includeCoupon(CouponDto couponDto) {
        this.coupon = couponDto;
        return this;
    }

    public OrderDetailDto includeShipment(ShipmentDto shipmentDto) {
        this.shipment = shipmentDto;
        return this;
    }

//    public OrderDetailDto includeReview(ReviewDto reviewDto) {
//        this.review = reviewDto;
//        return this;
//    }
}
