package com.shop.module.shipment.repository;

import com.shop.module.shipment.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShipmentRepository extends JpaRepository<Shipment, Long>, ShipmentRepositoryCustom {
        @Query("select distinct s from Shipment s join s.orderDetails od order by od.order.orderNumber desc, s.id")
    Page<Shipment> findDistinctShipmentByOrderDetailsOrderOrderNumberDescIdAsc(Pageable pageable);
}
