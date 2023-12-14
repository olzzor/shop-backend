package com.bridgeshop.module.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderListSearchRequest {
    private String orderNumber;
    private String buyerEmail;
    private String paymentMethod;
    private String paymentAmount;
    private String orderProduct;
    private String orderStatus;
    private String startOrderDate;
    private String endOrderDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderListSearchRequest(String orderNumber, String buyerEmail, String paymentMethod, String paymentAmount,
                                  String orderProduct, String orderStatus, String startOrderDate, String endOrderDate) {
        this.orderNumber = orderNumber;
        this.buyerEmail = buyerEmail;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.orderProduct = orderProduct;
        this.orderStatus = orderStatus;
        this.startOrderDate = startOrderDate;
        this.endOrderDate = endOrderDate;
    }
}
