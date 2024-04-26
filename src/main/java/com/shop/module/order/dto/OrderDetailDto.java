package com.shop.module.order.dto;

import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.review.dto.ReviewDto;
import com.shop.module.shipment.dto.ShipmentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private ReviewDto review;       // 20240416 추가
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderDetailDto(Long id, int quantity, int unitPrice, int discountPer, int finalPrice,
                          LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPer = discountPer;
        this.finalPrice = finalPrice;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(OrderDto orderDto) {
        this.order = orderDto;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setProductSize(ProductSizeDto productSizeDto) {
        this.productSize = productSizeDto;
    }

    public void setShipment(ShipmentDto shipmentDto) {
        this.shipment = shipmentDto;
    }

    public void setCoupon(CouponDto couponDto) {
        this.coupon = couponDto;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setReview(ReviewDto reviewDto) {
        this.review = reviewDto;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }
}
