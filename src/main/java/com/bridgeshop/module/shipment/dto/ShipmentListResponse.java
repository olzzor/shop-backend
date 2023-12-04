package com.bridgeshop.module.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentListResponse {
    private List<ShipmentDto> shipments;
    private int totalPages;
}
