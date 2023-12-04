package com.bridgeshop.module.shipment.repository;

import com.bridgeshop.module.shipment.dto.ShipmentListSearchRequest;
import com.bridgeshop.module.shipment.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentRepositoryCustom {
    Page<Shipment> findByCondition(ShipmentListSearchRequest productListSearchRequest, Pageable pageable);
}