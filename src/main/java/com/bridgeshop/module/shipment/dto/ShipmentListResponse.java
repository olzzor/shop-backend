package com.bridgeshop.module.shipment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShipmentListResponse {
    private List<ShipmentDto> shipments;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ShipmentListResponse(List<ShipmentDto> shipments, int totalPages) {
        this.shipments = shipments;
        this.totalPages = totalPages;
    }
}
