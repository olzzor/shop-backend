package com.shop.module.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderListResponse {
    private List<OrderDto> orders;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderListResponse(List<OrderDto> orders, int totalPages) {
        this.orders = orders;
        this.totalPages = totalPages;
    }
}
