package com.shop.module.shipment.repository;

import com.shop.module.shipment.dto.ShipmentListSearchRequest;
import com.shop.module.shipment.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentRepositoryCustom {
    Page<Shipment> findByCondition(ShipmentListSearchRequest productListSearchRequest, Pageable pageable);
}