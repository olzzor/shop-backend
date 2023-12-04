package com.bridgeshop.module.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListSearchRequest {
    private String orderNumber;
    private String buyerEmail;
    private String paymentMethod;
    private String paymentAmount;
    private String orderProduct;
    private String orderStatus;
    private String startOrderDate;
    private String endOrderDate;
}
