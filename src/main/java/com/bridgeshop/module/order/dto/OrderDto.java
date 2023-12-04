package com.bridgeshop.module.order.dto;

import com.bridgeshop.module.order.entity.OrderStatus;
import com.bridgeshop.module.review.dto.ReviewDto;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OrderDto {
    private Long id;
    private UserDto user;
    private String orderNumber;
    private String buyerEmail;
    private String paymentMethod;
    private int paymentAmount;
    private String cardNumber;
    private OrderStatus status;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private List<OrderDetailDto> orderDetails;
    private ReviewDto review;

    public OrderDto includeReview(ReviewDto reviewDto) {
        this.review = reviewDto;
        return this;
    }
}
