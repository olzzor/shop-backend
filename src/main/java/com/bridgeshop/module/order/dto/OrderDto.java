package com.bridgeshop.module.order.dto;

import com.bridgeshop.module.order.entity.OrderStatus;
import com.bridgeshop.module.review.dto.ReviewDto;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderDto(Long id, String orderNumber, String buyerEmail, String paymentMethod, int paymentAmount,
                    String cardNumber, OrderStatus status, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.buyerEmail = buyerEmail;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.cardNumber = cardNumber;
        this.status = status;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto userDto) {
        this.user = userDto;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetailDtoList) {
        this.orderDetails = orderDetailDtoList;
    }

    public void setReview(ReviewDto reviewDto) {
        this.review = reviewDto;
    }
}
