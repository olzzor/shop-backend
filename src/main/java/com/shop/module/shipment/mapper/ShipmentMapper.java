package com.shop.module.shipment.mapper;

import com.shop.module.shipment.dto.ShipmentDto;
import com.shop.module.shipment.entity.Shipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShipmentMapper {

    public ShipmentDto mapToDto(Shipment shipment) {

        return ShipmentDto.builder()
                .id(shipment.getId())
                .recipientName(shipment.getRecipientName())
                .recipientPhone(shipment.getRecipientPhone())
                .shippingAddress(shipment.getShippingAddress())
                .courierCompany(shipment.getCourierCompany())
                .trackingNumber(shipment.getTrackingNumber())
                .status(shipment.getStatus())
                .regDate(shipment.getRegDate())
                .modDate(shipment.getModDate())
                .build();
    }

    public List<ShipmentDto> mapToDtoList(List<Shipment> shipmentList) {
        return shipmentList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}